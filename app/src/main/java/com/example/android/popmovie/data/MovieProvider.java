package com.example.android.popmovie.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by user on 1/31/2017.
 */

public class MovieProvider extends ContentProvider {
    /*
     * this urimatcher used by the content provider
     * The "s" in this variable name signigies that this
     * Urimatcher is a static member variable of MovieProvder
     */
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;
    private static final String TAG = MovieProvider.class.getSimpleName();

    // step 1 of creating a content provider is determining the uri
    // Each types of uri are used for different types of operations against
    // the underlying database.
    static final int FAV_MOVIES = 100;
    static final int FAV_MOVIES_ITEMS = 200;

    static UriMatcher buildUriMatcher(){
        // urimatcher have a corresponding code to return when a metch is
        // found.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        //Use the addUri function to match each of the types.
        // for each type of Uri you want to add, create a corresponding code.
        matcher.addURI(authority,MovieContract.PATH_MOVIE,FAV_MOVIES);
        matcher.addURI(authority,MovieContract.PATH_MOVIE+"/*",FAV_MOVIES_ITEMS);
        return matcher;
    }
    /*
    *onCreate is run on the main thread, so performing any lengthy operations
    * will cause lag in our app. Since MovieDbHelper's constructor is
    * very lightweight, we are safe to perform that initialization here.
     */
    @Override
    public boolean onCreate() {
        mOpenHelper =  new MovieDbHelper(getContext());
        return false;
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                   String sortOrder)     {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            // "favourite_movie"
            case FAV_MOVIES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        // use the Uri matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match){
            // Get all favourite movies records.
            case FAV_MOVIES:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            /**
             * Get a movie record
             */
            case FAV_MOVIES_ITEMS:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case FAV_MOVIES: {
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME,null,contentValues);
                if(_id > 0)
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into "+uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        // A null value deletes all rows.  In my implementation of this, I only notified
        // the uri listeners (using the content resolver) if the rowsDeleted != 0 or the selection
        // is null.
        //this makes delete all rows return the no of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case FAV_MOVIES:
                rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }
        //notify the listeners here
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        //return the actual rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case FAV_MOVIES:
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }
        // notify the listeners here.
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // return the number of rows impacted by the update.
        return rowsUpdated;
    }
}
