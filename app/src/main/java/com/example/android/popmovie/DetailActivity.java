package com.example.android.popmovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by user on 12/7/2016.
 */

public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.DetailAct, new DetailMovieFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailMovieFragment extends Fragment {
        private final String LOG_TAG = DetailMovieFragment.class.getSimpleName();
        private String mMovieStr;

        public  DetailMovieFragment() {
            setHasOptionsMenu(true);
        }

        
        @BindView(R.id.details) TextView mMovieTitle;
        @BindView(R.id.poster) ImageView mMoviePoster;
        @BindView(R.id.release_date) TextView mReleaseDate;
        @BindView(R.id.rating) TextView mRatingAverage;
        @BindView(R.id.synopsis) TextView mSynopsis;
        private Unbinder unbinder;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            MovieList movie = getActivity().getIntent().getParcelableExtra(MovieFragment.EXTRA_MOVIE_DATA);


            unbinder=ButterKnife.bind(this,rootView);


            mMovieTitle.setText(movie.getTitle());
            Picasso.with(getContext())
                    .load(movie.getImageurl())
                    .placeholder(R.drawable.image)
                    .error(R.drawable.image_error)
                    .into(mMoviePoster);
            mReleaseDate.setText("Released:\n"+movie.getRelease_date());
            mRatingAverage.setText(String.valueOf("Rating:\n"+movie.getRating()+"/10"));
            mSynopsis.setText("SYNOPSIS:\n"+movie.getSynopsis());
            return rootView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }
    }
}