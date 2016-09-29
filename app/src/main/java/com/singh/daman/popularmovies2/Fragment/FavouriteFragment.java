package com.singh.daman.popularmovies2.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.singh.daman.popularmovies2.Adapter.FavouriteAdapter;
import com.singh.daman.popularmovies2.Database.DatabaseHandler;
import com.singh.daman.popularmovies2.Model.Movies;
import com.singh.daman.popularmovies2.R;

import java.util.ArrayList;

/**
 * Created by daman on 29/9/16.
 */

public class FavouriteFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private FavouriteAdapter mfavouriteAdapter;
    private DatabaseHandler handler;
    ArrayList<String> moviesposter = new ArrayList<String>();
    ArrayList<String> overview = new ArrayList<String>();
    ArrayList<String> date = new ArrayList<String>();
    ArrayList<String> title = new ArrayList<String>();
    ArrayList<String> vote = new ArrayList<String>();
    ArrayList<String> id = new ArrayList<String>();
    ArrayList<String> favourite = new ArrayList<>();

    public FavouriteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // inflater.inflate(R.menu.moviesfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favourite, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.favrecyclerview);
        mRecyclerView.setHasFixedSize(true);
        handler = new DatabaseHandler(getContext());
        mfavouriteAdapter = new FavouriteAdapter(getActivity(), id, moviesposter, overview, date, title, vote, favourite);
        mRecyclerView.setAdapter(mfavouriteAdapter);
        GridLayoutManager llm = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(llm);
        PopulateList();
        return rootView;
    }

    public void PopulateList() {
        ArrayList<Movies> moviesArrayList = handler.getAllMovies();
        for (int i = 0; i < moviesArrayList.size(); i++){
            Movies movies = moviesArrayList.get(i);
            if(movies.getFavourite().equals("YES")) {
                id.add(movies.getId());
                title.add(movies.getTitle());
                moviesposter.add(movies.getImage());
                vote.add(movies.getVote());
                date.add(movies.getDate());
                overview.add(movies.getOverview());
                favourite.add(movies.getFavourite());
            }
        }
        mfavouriteAdapter.notifyDataSetChanged();
    }
}
