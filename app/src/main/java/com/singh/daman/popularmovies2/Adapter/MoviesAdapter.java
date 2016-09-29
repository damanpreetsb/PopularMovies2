package com.singh.daman.popularmovies2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.singh.daman.popularmovies2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by daman on 11/9/16.
 */
public class MoviesAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> moviesposter, overview, date, title, vote;

    public MoviesAdapter(Context c, ArrayList<String> moviesposter, ArrayList<String> overview,
                         ArrayList<String> date, ArrayList<String> title, ArrayList<String> vote) {
        mContext = c;
        this.moviesposter = moviesposter;
        this.overview = overview;
        this.date = date;
        this.title = title;
        this.vote = vote;
    }

    @Override
    public int getCount() {
        return moviesposter.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class ViewHolder {
        public final ImageView imageView;

        public ViewHolder(View view){
            imageView = (ImageView) view.findViewById(R.id.grid_image);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.movie, null);

        } else {
            grid = (View) convertView;
        }
        ViewHolder holder = new ViewHolder(grid);
        Picasso.with(mContext).load(moviesposter.get(position)).placeholder(R.drawable.loading).fit().into(holder.imageView);

        return grid;
    }
}
