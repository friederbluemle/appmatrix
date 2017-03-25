package org.fbluemle.android.appmatrix.adapters;

import org.fbluemle.android.appmatrix.R;
import org.fbluemle.android.appmatrix.models.AppInfo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AppAdapter extends ArrayAdapter<AppInfo> {
    private final LayoutInflater mLayoutInflater;
    private final PackageManager mPackageManager;
    private final List<AppInfo> mApps;

    public AppAdapter(Context context, List<AppInfo> apps) {
        super(context, R.layout.app_list_item, apps);
        mLayoutInflater = LayoutInflater.from(context);
        mPackageManager = context.getPackageManager();
        mApps = apps;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        AppInfo current = mApps.get(position);

        View view = convertView;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.app_list_item, parent, false);
        }

        ((TextView) view.findViewById(R.id.title)).setText(current.label);

        try {
            PackageInfo pi = mPackageManager.getPackageInfo(current.info.packageName, 0);
            if (!TextUtils.isEmpty(pi.versionName)) {
                String versionInfo = String.format("%s", pi.versionName);
                ((TextView) view.findViewById(R.id.appVersion)).setText(versionInfo);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        if (!TextUtils.isEmpty(current.info.packageName)) {
            TextView subTitle = (TextView) view.findViewById(R.id.subTitle);
            subTitle.setText(current.info.packageName);
        }

        ImageView logo = (ImageView) view.findViewById(R.id.icon);
        Drawable background = current.info.loadIcon(mPackageManager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            logo.setBackground(background);
        } else {
            //noinspection deprecation
            logo.setBackgroundDrawable(background);
        }

        return view;
    }
}
