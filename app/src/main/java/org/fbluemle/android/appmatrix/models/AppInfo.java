package org.fbluemle.android.appmatrix.models;

import android.graphics.drawable.Drawable;

public class AppInfo {
    public String label;
    public String packageName;
    public String versionName;
    public int versionCode;
    public int targetSdkVersion;
    public Drawable icon;

    public AppInfo(String label) {
        this.label = label;
    }
}
