package org.fbluemle.android.appmatrix.adapters;

import org.fbluemle.android.appmatrix.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppAdapter extends ArrayAdapter<String> {
    private final Context mContext;
    private final String[] mValues;

    public AppAdapter(Context context, String[] values) {
        super(context, R.layout.list_app, values);
        mContext = context;
        mValues = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_app, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
        textView.setText(mValues[position]);

        // Change icon based on name
        String s = mValues[position];

        System.out.println(s);

        if (s.equals("WindowsMobile")) {
            imageView.setImageResource(R.mipmap.ic_launcher);
        } else if (s.equals("iOS")) {
            imageView.setImageResource(R.mipmap.ic_launcher);
        } else if (s.equals("Blackberry")) {
            imageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            imageView.setImageResource(R.mipmap.ic_launcher);
        }

        return rowView;
    }
}
