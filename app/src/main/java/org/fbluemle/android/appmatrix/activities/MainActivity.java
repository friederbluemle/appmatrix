package org.fbluemle.android.appmatrix.activities;

import org.fbluemle.android.appmatrix.R;
import org.fbluemle.android.appmatrix.adapters.AppAdapter;
import org.fbluemle.android.appmatrix.models.AppInfo;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    protected ListView mList;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshAppInfoList();
            }
        });

        mList = (ListView) findViewById(R.id.listView);
        mList.setTextFilterEnabled(true);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AppInfo appInfo = (AppInfo) parent.getItemAtPosition(position);
                Intent intent = getPackageManager().getLaunchIntentForPackage(appInfo.packageName);
                if (intent != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "No launch intent for " + appInfo.packageName, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshAppInfoList();
    }

    private void refreshAppInfoList() {
        new LoadAppInfoTask().execute(PackageManager.GET_META_DATA);
    }

    private class LoadAppInfoTask extends AsyncTask<Integer, Integer, List<AppInfo>> {
        @Override
        protected List<AppInfo> doInBackground(Integer... params) {
            List<AppInfo> packages = new ArrayList<>();

            PackageManager pm = getPackageManager();
            List<ApplicationInfo> appInfos = pm.getInstalledApplications(params[0]);

            for (ApplicationInfo appInfo : appInfos) {
                AppInfo p = new AppInfo((String) appInfo.loadLabel(pm));
                p.packageName = appInfo.packageName;
                p.targetSdkVersion = appInfo.targetSdkVersion;
                p.icon = appInfo.loadIcon(pm);
                try {
                    PackageInfo pi = pm.getPackageInfo(appInfo.packageName, 0);
                    p.versionCode = pi.versionCode;
                    p.versionName = pi.versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(TAG, "Unable to get packageInfo for " + appInfo.packageName);
                }
                packages.add(p);
            }
            return packages;
        }

        @Override
        protected void onPostExecute(List<AppInfo> appInfos) {
            super.onPostExecute(appInfos);
            mList.setAdapter(new AppAdapter(MainActivity.this, appInfos));
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
