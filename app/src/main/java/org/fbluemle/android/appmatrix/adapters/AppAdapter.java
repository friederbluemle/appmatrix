package org.fbluemle.android.appmatrix.adapters;

import org.fbluemle.android.appmatrix.R;
import org.fbluemle.android.appmatrix.models.AppInfo;

import android.content.Context;
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
    private final Context mContext;
    private final List<AppInfo> mApps;

    public AppAdapter(Context context, List<AppInfo> apps) {
        super(context, R.layout.list_app, apps);
        mContext = context;
        mApps = apps;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        AppInfo current = mApps.get(position);

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_app, parent, false);
        }

        if (!TextUtils.isEmpty(current.versionName) && current.versionCode != 0) {
            String versionInfo = String.format("%s (%s)", current.versionName, current.versionCode);
            ((TextView) view.findViewById(R.id.appVersion)).setText(versionInfo);
        }

        ((TextView) view.findViewById(R.id.title)).setText(current.label);

        if (!TextUtils.isEmpty(current.packageName)) {
            TextView subTitle = (TextView) view.findViewById(R.id.subTitle);
            subTitle.setText(current.packageName);
        }

        ImageView logo = (ImageView) view.findViewById(R.id.icon);
        if (current.icon != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                logo.setBackground(current.icon);
            } else {
                //noinspection deprecation
                logo.setBackgroundDrawable(current.icon);
            }
        } else {
            logo.setImageResource(R.mipmap.ic_launcher);
        }

        return view;
    }
}
