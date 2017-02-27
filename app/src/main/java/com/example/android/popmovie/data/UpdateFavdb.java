package com.example.android.popmovie.data;

/**
 * Created by user on 2/15/2017.
 */

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popmovie.MovieList;
import com.example.android.popmovie.data.MovieContract.MovieEntry;

/** This class is created so that we can call the addMovie method and
 * also it should be on the background thread hence we have define a new method
 * i can do it in the FetchMoviesTask doinbackground method but it fails to
 * do that whenever it call the doinbackground method the addMovie method is
 * also called which i don't want i only want it to be called when i click on
 * the button. So that's why i have to create another class where i can call
 * it seperately
 */

public class UpdateFavdb extends AsyncTask<Void, Void, Void> {

    private static final String LOG_TAG = UpdateFavdb.class.getSimpleName();

    private Context mContext;
    private MovieList mMovie;
    private DBUpdateListener mDBUpdateListener;

    public interface DBUpdateListener {
        void onSuccess(int operationType);

        void onFailure();
    }

    public static final int ADDED_TO_FAVORITE = 1;
    public static final int REMOVED_FROM_FAVORITE = 2;

    public UpdateFavdb(Context context, MovieList movie, DBUpdateListener updateListener) {
        mDBUpdateListener = updateListener;
        mContext = context;
        mMovie = movie;
    }

    @Override
    protected Void doInBackground(Void... params) {
        addMovie();
        return null;
    }

    public void addMovie() {
        //Check if the movie with this movie_id  exists in the db

        Log.d(LOG_TAG, MovieContract.MovieEntry.CONTENT_URI.getAuthority());

        Cursor favMovieCursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{String.valueOf(mMovie.getId())},
                null);

        // If it exists, delete the movie with that movie id
        if (favMovieCursor.moveToFirst()) {
            int rowDeleted = mContext.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{String.valueOf(mMovie.getId())});

            if (rowDeleted > 0) {
                mDBUpdateListener.onSuccess(REMOVED_FROM_FAVORITE);
            } else {
                mDBUpdateListener.onFailure();
            }

        } else {

            // Otherwise, insert it using the content resolver and the base URI
            ContentValues values = new ContentValues();

            //Then add the data, along with the corresponding name of the data type,
            //so the content provider knows what kind of value is being inserted.
            values.put(MovieEntry.COLUMN_MOVIE_ID, mMovie.getId());
            values.put(MovieEntry.COLUMN_TITLE, mMovie.getTitle());
            values.put(MovieEntry.COLUMN_POSTER_IMAGE, mMovie.getImageurl());
            values.put(MovieEntry.COLUMN_OVERVIEW, mMovie.getSynopsis());
            values.put(MovieEntry.COLUMN_AVERAGE_RATING, mMovie.getRating());
            values.put(MovieEntry.COLUMN_RELEASE_DATE, mMovie.getRelease_date());
            values.put(MovieEntry.COLUMN_BACK_POSTER, mMovie.getBackPoster());


            // Finally, insert movie data into the database.
            Uri insertedUri = mContext.getContentResolver().insert(
                    MovieContract.MovieEntry.CONTENT_URI,
                    values);

            // The resulting URI contains the ID for the row.  Extract the movie rowId from the Uri.
            long movieRowId = ContentUris.parseId(insertedUri);

            if (movieRowId > 0) {
                mDBUpdateListener.onSuccess(ADDED_TO_FAVORITE);
            } else {
                mDBUpdateListener.onFailure();
            }
        }
        favMovieCursor.close();
    }

    }

