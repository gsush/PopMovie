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
import android.widget.Toast;

import com.example.android.popmovie.MovieList;

import static com.example.android.popmovie.FetchMoviesTask.mcontext;

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


    @Override
    protected Void doInBackground(Void... params) {
        addMovie();
        return null;
    }

    public void addMovie() {
        //First check if the movie with the movieid already exists in the database
        Cursor movieCursor = mcontext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry._ID},
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                new String[]{mMovie.getId()}, null);
        Log.v(LOG_TAG, movieCursor + "cursor");
        // if it exist then show a toast about it else insert it into the database
        if (movieCursor.moveToFirst()) {
            int locationIdIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry._ID);
            Log.v(LOG_TAG, locationIdIndex + "loation");
            Toast.makeText(mcontext, "movie exists", Toast.LENGTH_LONG).show();
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues movieValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovie.getId());
            movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_IMAGE, mMovie.getImageurl());
            movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mMovie.getSynopsis());
            movieValues.put(MovieContract.MovieEntry.COLUMN_AVERAGE_RATING, mMovie.getRating());
            movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, mMovie.getRelease_date());
            movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovie.getTitle());

            // Finally, insert location data into the database.
            Uri insertedUri = mcontext.getContentResolver().insert(
                    MovieContract.MovieEntry.CONTENT_URI,
                    movieValues
            );
            Toast.makeText(mcontext, "Movie added to Fav", Toast.LENGTH_LONG).show();
            Log.v(LOG_TAG, insertedUri + "uri");
            // The resulting URI contains the ID for the row.  Extract the movieId from the Uri.
            long movieRowId = ContentUris.parseId(insertedUri);

        }
        movieCursor.close();

    }
}

