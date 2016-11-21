package com.example.android.popmovie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by user on 11/21/2016.
 */


public class MoviesListAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String[] imageUrls;

    public MoviesListAdapter(Context context, String[] imageUrls) {
        super(context, R.layout.movie_item, imageUrls);

        this.context = context;
        this.imageUrls = imageUrls;

        inflater = LayoutInflater.from(context);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.movie_item, parent, false);
        }

        Picasso.with(context)
                .load(imageUrls[position])
                .fit() // will explain later
                .into((ImageView) convertView);

        return convertView;
    }
}
