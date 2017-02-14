package com.example.android.popmovie;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.example.android.popmovie.Activity.DetailActivity;

/**
 * Created by user on 11/20/2016.
 */

public class MovieFragment extends Fragment {
    public static final String EXTRA_MOVIE_DATA = "EXTRA_MOVIE_DATA";
    private ArrayAdapter<MovieList> mAdapter;
    private Activity mactivity;
    private MovieList mMovieList;

    public MovieFragment() {
        // need to create empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);// this will give the permission for the refresh button to inflate.
    }

    @Override
    public void onStart() {
        // this method is called by default on the start of the app .
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(getActivity(),mAdapter);
        SharedPreferences preference= PreferenceManager.getDefaultSharedPreferences(getActivity());
                String sortOrder = preference.getString(getString(R.string.pref_sort_key),
                        getResources().getString(R.string.pref_order_pop));


        fetchMoviesTask.execute(sortOrder);
        super.onStart();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //This is the view of the view which is located within the fragment so
        // wherever one start a activity actually a fragment is started and the
        // the view of the fragment is decided by this method.
        View rootview = inflater.inflate(R.layout.fragment_main,container,false);
        mAdapter = new MoviesListAdapter(getContext(),R.layout.movie_item);
        final GridView gridView = (GridView) rootview.findViewById(R.id.flavors_grid);
        gridView.setAdapter(mAdapter);
        /**
         * This is the method when you click on any images there is the onitemclicklistener
         * and wherever it is clicked an action is defined within the click .
         * this method handles the click on the images.
         */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MovieList movie = mAdapter.getItem(position);
                Intent intent = new Intent(getContext(),  DetailActivity.class);
                intent.putExtra(MovieFragment.EXTRA_MOVIE_DATA,movie);
                startActivity(intent);
            }
        });
        return rootview;

    }


}
