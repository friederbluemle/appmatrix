package org.fbluemle.android.appmatrix.activities;

import org.fbluemle.android.appmatrix.BuildConfig;
import org.fbluemle.android.appmatrix.R;
import org.fbluemle.android.appmatrix.adapters.AppAdapter;
import org.fbluemle.android.appmatrix.models.AppInfo;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    protected ListView mList;
    private boolean mIncludeSystemApps;

    private FirebaseAnalytics mFirebaseAnalytics;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

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
                AppInfo app = (AppInfo) parent.getItemAtPosition(position);
                Intent intent = getPackageManager().getLaunchIntentForPackage(app.info.packageName);
                if (intent != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "No launch intent for " + app.info.packageName, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshAppInfoList();
    }

    private void refreshAppInfoList() {
        new LoadAppInfoTask().execute(PackageManager.GET_META_DATA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        if (BuildConfig.DEBUG) {
            // Add Debug entry on all activities except DebugActivity itself
            if (!"DebugActivity".equals(getClass().getSimpleName())) {
                menu.add(0, menu.size(), 999, "Debug");
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            refreshAppInfoList();
        } else if (id == R.id.action_show_system_apps) {
            item.setChecked(!item.isChecked());
            mIncludeSystemApps = item.isChecked();
            refreshAppInfoList();
        } else if (id == R.id.action_about) {
            showAboutDialog();
        }

        if (BuildConfig.DEBUG) {
            if ("Debug".equals(item.getTitle())) {
                startActivity(new Intent(this, DebugActivity.class));
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAboutDialog() {
        View view = View.inflate(this, R.layout.about_dialog, null);
        TextView text = (TextView) view.findViewById(R.id.aboutText);
        text.setText(new SpannableString(getString(R.string.about_text, BuildConfig.VERSION_NAME)));
        Linkify.addLinks(text, Linkify.ALL);
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.about_title, getString(R.string.app_name)))
                .setCancelable(true)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton(android.R.string.ok, null)
                .setView(view)
                .show();
    }

    private class LoadAppInfoTask extends AsyncTask<Integer, Integer, List<AppInfo>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected List<AppInfo> doInBackground(Integer... params) {
            List<AppInfo> apps = new ArrayList<>();

            PackageManager pm = getPackageManager();
            List<ApplicationInfo> infos = pm.getInstalledApplications(params[0]);

            for (ApplicationInfo info : infos) {
                if (!mIncludeSystemApps && (info.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                    continue;
                }
                AppInfo app = new AppInfo();
                app.info = info;
                app.label = (String) info.loadLabel(pm);
                apps.add(app);
            }
            Collections.sort(apps, new DisplayNameComparator());
            return apps;
        }

        @Override
        protected void onPostExecute(List<AppInfo> appInfos) {
            super.onPostExecute(appInfos);
            mList.setAdapter(new AppAdapter(MainActivity.this, appInfos));
            mSwipeRefreshLayout.setRefreshing(false);
            Snackbar.make(mList, appInfos.size() + " applications loaded", Snackbar.LENGTH_SHORT)
                    .show();
        }

        class DisplayNameComparator implements Comparator<AppInfo> {
            public final int compare(AppInfo aa, AppInfo ab) {
                CharSequence sa = aa.label;
                if (sa == null) {
                    sa = aa.info.packageName;
                }
                CharSequence sb = ab.label;
                if (sb == null) {
                    sb = ab.info.packageName;
                }

                return Collator.getInstance().compare(sa.toString(), sb.toString());
            }
        }
    }
}
