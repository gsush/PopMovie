package com.example.android.popmovie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.android.popmovie.data.MovieContract.MovieEntry;

/**
 * Created by user on 1/31/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate (SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_MOVIE_TABLE =
                // MovieEntry did not explicitly declare a column called "_ID".However,
                // we use it here to designate our table's primary key. and this field
                // can be found in baseColumns interface.
                "CREATE TABLE" + MovieContract.MovieEntry.TABLE_NAME + "(" +
                        MovieEntry._ID + "INTEGER PRIMARY KEY AUTOINCREMENT," +
                        MovieEntry.COLUMN_MOVIE_ID + "TEXT NOT NULL," +
                        MovieEntry.COLUMN_TITLE + "TEXT NOT NULL," +
                        MovieEntry.COLUMN_RELEASE_DATE + "TEXT NOT NULL," +
                        MovieEntry.COLUMN_POSTER_IMAGE + "TEXT NOT NULL," +
                        MovieEntry.COLUMN_OVERVIEW + "TEXT NOT NULL," +
                        MovieEntry.COLUMN_AVERAGE_RATING + "REAL NOT NULL,"+
                        MovieEntry.COLUMN_BACK_POSTER + "REAL NOT NULL )";
        /**
         * After we've spelled out our SQLite table creation statement above, we actually execute
         * that SQL with the execSQL method of our SQLite database object.
         */
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    /**
     * This database should use method to drop tables, add tables, or do anything else it needs to
     * upgrade to new schema version.
     * @param sqLiteDatabase that is being upgraded
     * @param oldVersion the old database version
     * @param newVersion the new database version
     */
    @Override
    public void onUpgrade (SQLiteDatabase sqLiteDatabase,int oldVersion, int newVersion){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
