package com.singh.daman.popularmovies2.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.singh.daman.popularmovies2.R;

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

    static class CardViewHolder {
        TextView title;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.trailer_layout, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.title = (TextView) row.findViewById(R.id.trailer_text);
            row.setTag(viewHolder);
        }else{
            viewHolder = (CardViewHolder)row.getTag();
        }

        String str = "Trailer " + (position+1);
        viewHolder.title.setText(str);

//        CardView cardView = (CardView) convertView.findViewById(R.id.trailer_card);
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key.get(position))));
//            }
//        });

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key.get(position))));
            }
        });

        return row;
    }

}
