package com.singh.daman.popularmovies2.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.singh.daman.popularmovies2.Adapter.ReviewsAdapter;
import com.singh.daman.popularmovies2.R;
import com.singh.daman.popularmovies2.Adapter.TrailerAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerdetail, new DetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class DetailFragment extends Fragment {
        String title;
        ArrayList<String> key = new ArrayList<String>();
        ArrayList<String> reviewtext = new ArrayList<String>();
        ArrayList<String> reviewauthor = new ArrayList<String>();
        TextView trailer;
        ExpandableHeightListView listView;
        ExpandableHeightListView reviewlist;
        TrailerAdapter adapter;
        ReviewsAdapter reviewsAdapter;

        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            listView = (ExpandableHeightListView) rootView.findViewById(R.id.trailerlist);
            adapter = new TrailerAdapter(getContext(), key);
            listView.setAdapter(adapter);
            listView.setExpanded(true);

            reviewlist = (ExpandableHeightListView) rootView.findViewById(R.id.reviewlist);
            reviewsAdapter = new ReviewsAdapter(getContext(), reviewtext, reviewauthor);
            reviewlist.setAdapter(reviewsAdapter);
            reviewlist.setExpanded(true);

            Bundle extras = getActivity().getIntent().getExtras();

            String image = extras.getString("EXTRA_IMAGE");
            String overview = extras.getString("EXTRA_OVERVIEW");
            String date = extras.getString("EXTRA_DATE");
            title = extras.getString("EXTRA_TITLE");
            String vote = extras.getString("EXTRA_VOTE") + "/10";
            String id = extras.getString("EXTRA_ID");
            Trailer(id);
            Review(id);

            if (date.length() != 0 || overview.length() != 0) {

                String year = date.split("-")[0];

                ((TextView) rootView.findViewById(R.id.detail_year))
                        .setText(year);
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

            trailer = (TextView) rootView.findViewById(R.id.detail_trailer);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String str = key.get(i);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + str)));
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
            } else {
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
    }
}