package org.fbluemle.android.appmatrix.activities;

import org.fbluemle.android.appmatrix.R;
import org.fbluemle.android.appmatrix.adapters.AppAdapter;
import org.fbluemle.android.appmatrix.models.AppInfo;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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
                mList.setAdapter(new AppAdapter(MainActivity.this, getPackages()));
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mList = (ListView) findViewById(R.id.listView);

        mList.setTextFilterEnabled(true);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Toast.makeText(getApplicationContext(),
                        ((TextView) view.findViewById(R.id.title)).getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<AppInfo> getPackages() {
        List<AppInfo> packages = new ArrayList<>();

        PackageManager pm = getPackageManager();
        List<ApplicationInfo> appInfos = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo appInfo : appInfos) {
            Log.d(TAG, "Installed package: " + appInfo.packageName);
            Log.d(TAG, "Source dir: " + appInfo.sourceDir);
            Log.d(TAG, "Launch Activity: " + pm.getLaunchIntentForPackage(appInfo.packageName));
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
