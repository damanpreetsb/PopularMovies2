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
    private ArrayList<String> reviewauthor;
    Context context;

    public ReviewsAdapter(Context context, ArrayList<String> reviewlist, ArrayList<String> reviewauthor) {
        this.context = context;
        this.reviewlist = reviewlist;
        this.reviewauthor = reviewauthor;
    }

    @Override
    public int getCount() {
        if (reviewlist.size() == 0) {
            return 1;
        } else
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
            convertView = inflater.inflate(R.layout.review_layout, null);

        TextView title = (TextView) convertView.findViewById(R.id.review_author);
        TextView content = (TextView) convertView.findViewById(R.id.review_content);
        if (reviewlist.size() != 0) {
            content.setVisibility(View.VISIBLE);
            String author = reviewauthor.get(position) + " said: ";
            title.setText(author);
            content.setText(reviewlist.get(position));
        } else {
            content.setVisibility(View.GONE);
        }

        return convertView;
    }

}
