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


        TextView mMovieTitle, mReleaseDate, mSynopsis, mRatingAverage;
        ImageView mMoviePoster;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            MovieList movie = getActivity().getIntent().getParcelableExtra(MovieFragment.EXTRA_MOVIE_DATA);

            mMovieTitle = (TextView) rootView.findViewById(R.id.details);
            mMoviePoster = (ImageView) rootView.findViewById(R.id.poster);
            mReleaseDate = (TextView) rootView.findViewById(R.id.release_date);
            mRatingAverage = (TextView) rootView.findViewById(R.id.rating);
            mSynopsis = (TextView) rootView.findViewById(R.id.synopsis);


            mMovieTitle.setText(movie.getTitle());
            Picasso.with(getContext()).load(movie.getImageurl()).into(mMoviePoster);
            mReleaseDate.setText(movie.getRelease_date());
            mRatingAverage.setText(String.valueOf(movie.getRating()+"/10"));
            mSynopsis.setText(movie.getSynopsis());
            return rootView;
        }
    }
}