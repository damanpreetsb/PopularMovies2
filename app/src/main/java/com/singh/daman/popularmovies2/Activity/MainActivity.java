package com.singh.daman.popularmovies2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.singh.daman.popularmovies2.Fragment.MoviesFragment;
import com.singh.daman.popularmovies2.R;

public class MainActivity extends AppCompatActivity{

    private boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movies_detail_container) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean("ISTAB", mTwoPane);
        if (savedInstanceState == null) {
            MoviesFragment moviesFragment = new MoviesFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, moviesFragment)
                    .commit();
            moviesFragment.setArguments(bundle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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

//    public void onItemSelected(Uri contentUri) {
//
//        if (mTwoPane) {
//            Bundle args = new Bundle();
//            args.putParcelable(DetailFragment.DETAIL_URI, contentUri);
//            DetailFragment fragment = new DetailFragment();
//            fragment.setArguments(args);
//            getSupportFragmentManager().beginTransaction().replace(R.id.movies_detail_container, fragment, DETAILFRAGMENT_TAG).commit();
//        } else {
//            Intent intent = new Intent(this, DetailActivity.class).setData(contentUri);
//            startActivity(intent);
//        }
//    }
}
