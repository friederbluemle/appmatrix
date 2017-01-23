package org.fbluemle.android.appmatrix.adapters;

import org.fbluemle.android.appmatrix.R;
import org.fbluemle.android.appmatrix.activities.MainActivity;

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

public class AppAdapter extends ArrayAdapter<MainActivity.Package> {
    private final Context mContext;
    private final List<MainActivity.Package> mPackages;

    public AppAdapter(Context context, List<MainActivity.Package> packages) {
        super(context, R.layout.list_app, packages);
        mContext = context;
        mPackages = packages;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        MainActivity.Package current = mPackages.get(position);

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_app, parent, false);
        }

        String versionInfo = "";
        if (!TextUtils.isEmpty(current.versionName) && current.versionCode != 0) {
            versionInfo = String.format(" %s (%s)", current.versionName, current.versionCode);
        }

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(String.format("%s%s", current.label, versionInfo));

        String targetInfo = "";
        if (current.targetSdkVersion != 0) {
            targetInfo = String.format(" (targetSdk: %s)", current.targetSdkVersion);
        }

        if (!TextUtils.isEmpty(current.packageName)) {
            TextView subTitle = (TextView) view.findViewById(R.id.subTitle);
            subTitle.setText(String.format("%s%s", current.packageName, targetInfo));
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
