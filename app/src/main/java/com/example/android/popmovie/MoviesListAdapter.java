package com.example.android.popmovie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by user on 11/21/2016.
 */


public class MoviesListAdapter extends ArrayAdapter<MovieList> {

    public MoviesListAdapter(Context context, List<MovieList> movieList) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, movieList);
    }
    // private String[] imageUrls;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieList moviesList = getItem(position);


        if ( convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item, parent, false);

        }
        ImageView posterView = (ImageView) convertView.findViewById(R.id.flavor_image);
        Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w185/zSouWWrySXshPCT4t3UKCQGayyo.jpg").into(posterView);
        //Picasso.with(getContext()).load(MovieFragment.movieList).into(posterView);
       // posterView.setImageResource(MoviesListAdapter.imageUrls);


//        Picasso.with(context)
//                .load(imageUrls[position])
//                .fit() // will explain later
//                .into((ImageView) convertView);

        return convertView;
    }
}
