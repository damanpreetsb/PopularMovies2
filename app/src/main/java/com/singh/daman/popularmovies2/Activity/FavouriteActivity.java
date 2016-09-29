package com.singh.daman.popularmovies2.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.singh.daman.popularmovies2.Fragment.FavouriteFragment;
import com.singh.daman.popularmovies2.R;

/**
 * Created by daman on 29/9/16.
 */

public class FavouriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerfav, new FavouriteFragment())
                    .commit();
        }
    }
}

