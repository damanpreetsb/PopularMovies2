package com.singh.daman.popularmovies2.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.like.IconType;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.singh.daman.popularmovies2.Adapter.ReviewsAdapter;
import com.singh.daman.popularmovies2.Adapter.TrailerAdapter;
import com.singh.daman.popularmovies2.Database.DatabaseHandler;
import com.singh.daman.popularmovies2.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by daman on 30/9/16.
 */

public class DetailFragment extends Fragment {

    String title;
    ArrayList<String> key = new ArrayList<String>();
    ArrayList<String> reviewtext = new ArrayList<String>();
    ArrayList<String> reviewauthor = new ArrayList<String>();
    ExpandableHeightListView listView;
    ExpandableHeightListView reviewlist;
    TrailerAdapter adapter;
    ReviewsAdapter reviewsAdapter;
    Bundle extras;
    LikeButton btnfav;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<small>"+title+"</small>"));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Bundle arguments = getArguments();
        if(arguments != null){
            extras = getArguments();
        }
        btnfav = (LikeButton) rootView.findViewById(R.id.fav_button);
        btnfav.setIcon(IconType.Star);
        btnfav.setLikeDrawableRes(R.drawable.star_like_detail);
        btnfav.setUnlikeDrawableRes(R.drawable.star_unlike_detail);
        btnfav.setExplodingDotColorsRes(R.color.colorPrimary,R.color.colorPrimaryDark);
        btnfav.setAnimationScaleFactor(2);
        btnfav.setIconSizeDp(40);
        listView = (ExpandableHeightListView) rootView.findViewById(R.id.trailerlist);
        adapter = new TrailerAdapter(getContext(), key);
        listView.setAdapter(adapter);
        listView.setExpanded(true);

        reviewlist = (ExpandableHeightListView) rootView.findViewById(R.id.reviewlist);
        reviewsAdapter = new ReviewsAdapter(getContext(), reviewtext, reviewauthor);
        reviewlist.setAdapter(reviewsAdapter);
        reviewlist.setExpanded(true);

        final String image = extras.getString("EXTRA_IMAGE");
        final String overview = extras.getString("EXTRA_OVERVIEW");
        final String date = "Release date:\n"+extras.getString("EXTRA_DATE");
        title = extras.getString("EXTRA_TITLE");
        final String vote = "Rating:\n"+extras.getString("EXTRA_VOTE") + "/10";
        final String id = extras.getString("EXTRA_ID");
        Trailer(id);
        Review(id);



        if (date.length() != 0 || overview.length() != 0) {

            ((TextView) rootView.findViewById(R.id.detail_name))
                    .setText(title);
            ((TextView) rootView.findViewById(R.id.detail_date))
                    .setText(date);
            ((TextView) rootView.findViewById(R.id.detail_info))
                    .setText(overview);
            ((TextView) rootView.findViewById(R.id.detail_vote))
                    .setText(vote);
        }
        ImageView imageView = (ImageView) rootView.findViewById(R.id.detail_image);
        Picasso.with(getContext()).load(image).placeholder(R.drawable.loading).fit().into(imageView);


//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String str = key.get(i);
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + str)));
//            }
//        });

        final DatabaseHandler handler = new DatabaseHandler(getContext());
        if (handler.CheckIsFAv(id)) {
            btnfav.setLiked(true);
        } else
            btnfav.setLiked(false);
        btnfav.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                handler.addFavs(id ,
                        title, image,
                        vote, date, overview);
                btnfav.setLiked(true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                handler.deleteFav(id);
                btnfav.setLiked(false);
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailfragment, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                title + "#PopularMovies");
        return shareIntent;
    }

    private void Trailer(final String idstr) {
        try {
            final String BASE_URL = "http://api.themoviedb.org/3/movie/" + idstr + "/videos?";
            final String API_KEY_URL = "api_key=";
            final String API_KEY = "78152e1f5dc1e0ca19063a06ea342fae";

            String url = BASE_URL + API_KEY_URL + API_KEY;
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                String syncresponse = object.getString("results");
                                JSONArray a1obj = new JSONArray(syncresponse);
                                for (int j = 0; j < a1obj.length(); j++) {
                                    JSONObject obj = a1obj.getJSONObject(j);
                                    key.add(obj.getString("key"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NoConnectionError) {
                        Toast.makeText(getContext(), "No internet connections!", Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Review(final String idstr) {
        try {
            final String BASE_URL = "http://api.themoviedb.org/3/movie/" + idstr + "/reviews?";
            final String API_KEY_URL = "api_key=";
            final String API_KEY = "78152e1f5dc1e0ca19063a06ea342fae";

            String url = BASE_URL + API_KEY_URL + API_KEY;
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                String syncresponse = object.getString("results");
                                JSONArray a1obj = new JSONArray(syncresponse);
                                for (int j = 0; j < a1obj.length(); j++) {
                                    JSONObject obj = a1obj.getJSONObject(j);
                                    reviewauthor.add(obj.getString("author"));
                                    reviewtext.add(obj.getString("content"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            reviewsAdapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NoConnectionError) {
                        Toast.makeText(getContext(), "No internet connections!", Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
