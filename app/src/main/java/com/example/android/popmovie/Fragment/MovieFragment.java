package com.example.android.popmovie.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.example.android.popmovie.Activity.DetailActivity;
import com.example.android.popmovie.FetchMoviesTask;
import com.example.android.popmovie.MovieList;
import com.example.android.popmovie.MoviesListAdapter;
import com.example.android.popmovie.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

/**
 * Created by user on 11/20/2016.
 */

public class MovieFragment extends Fragment {
    public static final String EXTRA_MOVIE_DATA = "EXTRA_MOVIE_DATA";
    private ArrayAdapter<MovieList> mAdapter;
    private FloatingActionMenu menuRed;


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
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);

        mAdapter = new MoviesListAdapter(getContext(), R.layout.movie_item);
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
//            mColumnNumber = 2;
//        else
//            mColumnNumber = 3;
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
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(MovieFragment.EXTRA_MOVIE_DATA, movie);
                startActivity(intent);
            }
        });
        // Configure RecyclerView
//        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
//            mColumnNumber = 2;
//        else
//            mColumnNumber = 3;
//        GridLayoutManager layoutManager = new GridLayoutManager(this, mColumnNumber);
//        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.setAdapter(mMoviesAdapter);
//        mRecyclerView.setHasFixedSize(true);
       return rootview;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        menuRed = (FloatingActionMenu) view.findViewById(R.id.menu_red);

       // For Floating action with menu.
        final FloatingActionButton programFab1 = new FloatingActionButton(getActivity());
        programFab1.setButtonSize(FloatingActionButton.SIZE_MINI);
        programFab1.setLabelText("Highest Rated");
        programFab1.setImageResource(R.drawable.ic_star);
        final FloatingActionButton programFab2 = new FloatingActionButton((getActivity()));
        programFab2.setButtonSize(FloatingActionButton.SIZE_MINI);
        programFab2.setLabelText("Most Popular");
        programFab2.setImageResource(R.drawable.ic_heart);
        menuRed.addMenuButton(programFab1);
        menuRed.addMenuButton(programFab2);
        //fab1.setLabelText("Most Popular");
        programFab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                programFab1.setLabelColors(ContextCompat.getColor(getActivity(), R.color.grey),
                        ContextCompat.getColor(getActivity(), R.color.light_grey),
                        ContextCompat.getColor(getActivity(), R.color.white_transparent));
                programFab1.setLabelTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            }
        });
        menuRed.setClosedOnTouchOutside(true);
    }

}
