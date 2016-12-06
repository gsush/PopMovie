package com.example.android.popmovie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by user on 11/21/2016.
 */


public class MoviesListAdapter extends ArrayAdapter<MovieList> {
    private final String LOG_TAG = MoviesListAdapter.class.getSimpleName();

    public MoviesListAdapter(Context context, int resources) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, resources);
    }
    // private String[] imageUrls;


    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        MovieList moviesList = getItem(position);


        if ( convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item, parent, false);

        }
        ImageView posterView = (ImageView) convertView.findViewById(R.id.flavor_image);
       // Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w185/zSouWWrySXshPCT4t3UKCQGayyo.jpg").into(posterView);
        Picasso.with(getContext()).load(moviesList.getImageurl()).fit().into(posterView);
        Log.v(LOG_TAG, "Setting image " + moviesList.getImageurl());
       // posterView.setImageResource(MoviesListAdapter.imageUrls);


//        Picasso.with(context)
//                .load(imageUrls[position])
//                .fit() // will explain later
//                .into((ImageView) convertView);

        return convertView;
    }
}
