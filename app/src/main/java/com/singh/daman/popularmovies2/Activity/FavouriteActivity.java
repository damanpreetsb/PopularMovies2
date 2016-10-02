package com.singh.daman.popularmovies2.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.singh.daman.popularmovies2.Fragment.FavouriteFragment;
import com.singh.daman.popularmovies2.R;

/**
 * Created by daman on 29/9/16.
 */

public class FavouriteActivity extends AppCompatActivity {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        if (findViewById(R.id.fav_movies_detail_container) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean("ISTAB", mTwoPane);
        if (savedInstanceState == null) {
            FavouriteFragment favFragment = new FavouriteFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerfav, favFragment)
                    .commit();
            favFragment.setArguments(bundle);
        }
    }
}

