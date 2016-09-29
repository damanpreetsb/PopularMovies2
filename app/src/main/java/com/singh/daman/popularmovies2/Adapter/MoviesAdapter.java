package com.singh.daman.popularmovies2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.singh.daman.popularmovies2.Database.DatabaseHandler;
import com.singh.daman.popularmovies2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by daman on 11/9/16.
 */
public class MoviesAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> id, moviesposter, overview, date, title, vote, favourite;

    public MoviesAdapter(Context c, ArrayList<String> id,
                         ArrayList<String> moviesposter, ArrayList<String> overview,
                         ArrayList<String> date, ArrayList<String> title, ArrayList<String> vote,
                         ArrayList<String> favourite) {
        mContext = c;
        this.id = id;
        this.moviesposter = moviesposter;
        this.overview = overview;
        this.date = date;
        this.title = title;
        this.vote = vote;
        this.favourite = favourite;
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
        public LikeButton btnfav;

        public ViewHolder(View view){
            imageView = (ImageView) view.findViewById(R.id.grid_image);
            btnfav = (LikeButton) view.findViewById(R.id.fav_button);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.movie, null);

        } else {
            grid = (View) convertView;
        }
        final ViewHolder holder = new ViewHolder(grid);
        Picasso.with(mContext).load(moviesposter.get(position)).placeholder(R.drawable.loading).fit().into(holder.imageView);
        if(favourite.get(position).equals("YES")){
            holder.btnfav.setLiked(true);
        }
        else
            holder.btnfav.setLiked(false);
        holder.btnfav.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                holder.btnfav.setLiked(true);
                DatabaseHandler handler = new DatabaseHandler(mContext);
                handler.favUpdate("YES", id.get(position));
                favourite.set(position, "YES");
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                DatabaseHandler handler = new DatabaseHandler(mContext);
                handler.favUpdate("NO", id.get(position));
                favourite.set(position, "NO");
                holder.btnfav.setLiked(false);
            }
        });
        return grid;
    }
}
