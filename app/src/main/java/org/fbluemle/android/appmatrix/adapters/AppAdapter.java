package org.fbluemle.android.appmatrix.adapters;

import org.fbluemle.android.appmatrix.R;
import org.fbluemle.android.appmatrix.activities.MainActivity;

import android.content.Context;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        MainActivity.Package current = mPackages.get(position);

        View rowView = inflater.inflate(R.layout.list_app, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.packageName);
        textView.setText(String.format("%s%s",
                current.packageName,
                current.targetSdkVersion != 0 ? (" (" + current.targetSdkVersion + ")") : ""));

        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
        imageView.setImageResource(R.mipmap.ic_launcher);

        return rowView;
    }
}
