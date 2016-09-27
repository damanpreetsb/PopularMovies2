package com.singh.daman.popularmovies2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by daman on 28/9/16.
 */
public class TrailerAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<String> key;
    Context context;

    public TrailerAdapter(Context context, ArrayList<String> key) {
        this.context = context;
        this.key = key;
    }

    @Override
    public int getCount() {
        return key.size();
    }

    @Override
    public Object getItem(int location) {
        return key.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.trailer_layout, null);

        TextView title = (TextView) convertView.findViewById(R.id.trailer_text);
        String str = "Trailer " + (position+1);
        title.setText(str);

        return convertView;
    }

}
