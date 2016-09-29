package com.singh.daman.popularmovies2.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.singh.daman.popularmovies2.R;

import java.util.ArrayList;

/**
 * Created by daman on 29/9/16.
 */

public class ReviewsAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<String> reviewlist;
    Context context;

    public ReviewsAdapter(Context context, ArrayList<String> reviewlist) {
        this.context = context;
        this.reviewlist = reviewlist;
    }

    @Override
    public int getCount() {
        return reviewlist.size();
    }

    @Override
    public Object getItem(int location) {
        return reviewlist.get(location);
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
        title.setText(reviewlist.get(position));

        return convertView;
    }

}
