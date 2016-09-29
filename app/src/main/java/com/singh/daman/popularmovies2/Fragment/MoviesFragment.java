package com.singh.daman.popularmovies2.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.singh.daman.popularmovies2.Activity.DetailActivity;
import com.singh.daman.popularmovies2.Database.DatabaseHandler;
import com.singh.daman.popularmovies2.Model.Movies;
import com.singh.daman.popularmovies2.Adapter.MoviesAdapter;
import com.singh.daman.popularmovies2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by daman on 11/9/16.
 */
public class MoviesFragment extends Fragment {

    private MoviesAdapter mMoviesAdapter;
    ArrayList<String> moviesposter = new ArrayList<String>();
    ArrayList<String> overview = new ArrayList<String>();
    ArrayList<String> date = new ArrayList<String>();
    ArrayList<String> title = new ArrayList<String>();
    ArrayList<String> vote = new ArrayList<String>();
    ArrayList<String> id = new ArrayList<String>();
    private DatabaseHandler handler;

    public MoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviesfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            Data();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        handler = new DatabaseHandler(getContext());

        mMoviesAdapter = new MoviesAdapter(getActivity(), moviesposter, overview, date, title, vote);
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setAdapter(mMoviesAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Bundle extras = new Bundle();
                extras.putString("EXTRA_IMAGE", moviesposter.get(i));
                extras.putString("EXTRA_OVERVIEW", overview.get(i));
                extras.putString("EXTRA_DATE", date.get(i));
                extras.putString("EXTRA_TITLE", title.get(i));
                extras.putString("EXTRA_VOTE", vote.get(i));
                extras.putString("EXTRA_ID", id.get(i));
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        Data();

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        Data();
    }

    public void PopulateList(){
        id.clear();
        title.clear();
        moviesposter.clear();
        vote.clear();
        date.clear();
        overview.clear();
        ArrayList<Movies> moviesArrayList = handler.getAllMovies();
        for (int i = 0; i < moviesArrayList.size(); i++){
            Movies movies = moviesArrayList.get(i);
                id.add(movies.getId());
                title.add(movies.getTitle());
                moviesposter.add(movies.getImage());
                vote.add(movies.getVote());
                date.add(movies.getDate());
                overview.add(movies.getOverview());
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String order = prefs.getString(getString(R.string.pref_order_key),
                getString(R.string.pref_order_asc));
        if (order.equals("asc")) {
            Collections.reverse(moviesposter);
            Collections.reverse(overview);
            Collections.reverse(title);
            Collections.reverse(vote);
            Collections.reverse(date);
            Collections.reverse(id);
        }
        mMoviesAdapter.notifyDataSetChanged();
    }

    public void Data() {
        try {
            final String BASE_URL = "https://api.themoviedb.org/3/movie/";
            final String API_KEY_URL = "api_key=";
            final String API_KEY = "78152e1f5dc1e0ca19063a06ea342fae";
            final String IMAGE_URL = "http://image.tmdb.org/t/p/w500";
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sort = prefs.getString(getString(R.string.pref_sort_key),
                    getString(R.string.pref_sort_popularity));
            final String order = prefs.getString(getString(R.string.pref_order_key),
                    getString(R.string.pref_order_asc));

            String url = BASE_URL + sort + "?" + API_KEY_URL + API_KEY;
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                handler.dropTable();
                                JSONObject object = new JSONObject(response);
                                String syncresponse = object.getString("results");
                                JSONArray a1obj = new JSONArray(syncresponse);
                                for (int j = 0; j < a1obj.length(); j++) {
                                    JSONObject obj = a1obj.getJSONObject(j);
                                    String image = IMAGE_URL + obj.getString("poster_path");
                                    Movies movies = new Movies();
                                    movies.setId(obj.getString("id"));
                                    movies.setTitle(obj.getString("title"));
                                    movies.setImage(image);
                                    movies.setVote(obj.getString("vote_average"));
                                    movies.setDate(obj.getString("release_date"));
                                    movies.setOverview(obj.getString("overview"));
                                    handler.addMovies(movies);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            PopulateList();
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
                    HashMap<String, String> headers = new HashMap<>();
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
