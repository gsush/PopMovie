package com.example.android.popmovie.data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by user on 1/31/2017.
 */

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.popmovie";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";


    // inner class that defines the table contents of the weather table.
    public static final class MovieEntry implements BaseColumns {
        /**
         * The base CONTENT_URI used to query the movie table from the content provider.
         */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        // used internally as the name of our movie fav table;
        public static final String TABLE_NAME = "movies";

        //creating column for the database
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POSTER_IMAGE = "poster_image";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_AVERAGE_RATING = "average_rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_TITLE = "title";

        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

    }


}
