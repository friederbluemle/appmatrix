package org.fbluemle.android.appmatrix;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.util.StringBuilderPrinter;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class App extends Application {
    public static final String TAG = "AppMatrix";

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, new String(getDebugInfo(BuildConfig.DEBUG).toByteArray()));

        Fabric.with(this, new Crashlytics());
    }

    public ByteArrayOutputStream getDebugInfo(boolean verbose) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bos);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        ps.println("****************************************************************************");
        ps.println("* ApplicationId:   " + BuildConfig.APPLICATION_ID);
        ps.println("* VersionName:     " + BuildConfig.VERSION_NAME);
        ps.println("* VersionCode:     " + BuildConfig.VERSION_CODE);
        ps.println("* LaunchTime:      " + df.format(Calendar.getInstance().getTime()));
        ps.println("* Debug:           " + BuildConfig.DEBUG);
        ps.println("* BuildType:       " + BuildConfig.BUILD_TYPE);
        if (!TextUtils.isEmpty(BuildConfig.FLAVOR)) {
            ps.println("* Flavor:          " + BuildConfig.FLAVOR);
        }
        ps.println("* MaxHeapSize:     " + (Runtime.getRuntime().maxMemory() / (1048576)) + " MB");
        ps.println("* CacheDir:        " + getCacheDir());
        ps.println("* PackagePath:     " + getPackageCodePath());
        if (verbose) {
            ps.println("* ApplicationInfo:");
            StringBuilder appInfoDump = new StringBuilder(4096);
            getApplicationInfo().dump(new StringBuilderPrinter(appInfoDump), "*        ");
            ps.print(appInfoDump.toString());
        }
        ps.println("****************************************************************************");
        return bos;
    }
}
