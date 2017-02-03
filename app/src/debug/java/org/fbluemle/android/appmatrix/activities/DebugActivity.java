package org.fbluemle.android.appmatrix.activities;

import org.fbluemle.android.appmatrix.BuildConfig;
import org.fbluemle.android.appmatrix.R;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DebugActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        findViewById(R.id.debugKillAppProcess).setOnClickListener(this);
        findViewById(R.id.debugCrashApp).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUi();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.debugKillAppProcess:
                Toast.makeText(this, R.string.debug_killing_app_in_3_seconds, Toast.LENGTH_SHORT)
                        .show();
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.exit(0);
                    }
                }, 3000);
                break;
            case R.id.debugCrashApp:
                throw new RuntimeException("Manually induced app crash");
        }
    }

    private void refreshUi() {
        ((TextView) findViewById(R.id.debugAppId)).setText(
                String.format("%s\n%s (%s)\n%s",
                        BuildConfig.APPLICATION_ID,
                        BuildConfig.VERSION_NAME,
                        BuildConfig.VERSION_CODE,
                        BuildConfig.BUILD_TYPE));
    }
}
