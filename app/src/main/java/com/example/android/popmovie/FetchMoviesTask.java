package com.example.android.popmovie;

/**
 * Created by user on 2/10/2017.
 */


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.android.popmovie.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * This is the method that has like four methods within like doinbackground(),
 * onpreExecute(),onPostExecute(),onProgressUpdate. It has the threads
 * so all the networking code is running on the background thread
 * and after that it is executed by the onPostExecute() method.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, List<MovieList>> {
    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    private ArrayAdapter<MovieList> mAdapter;
    public static Context mcontext;
    private MovieList mMovie;
    public FetchMoviesTask(Context context,ArrayAdapter<MovieList> MovieAdapter){
        mcontext = context;
        mAdapter = MovieAdapter;
    }

    /**
     * Helper method to help insertion of a new movie in the db.
     * This is the method which gets called when we clink on the save button and then
     * it checks with the database and the data that is the parceable. it first query and
     * then if it exist then we show a toast about it else we insert it .
     */
    public void addMovie(){
        //First check if the movie with the movieid already exists in the database
        Cursor movieCursor = mcontext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,new String[]{MovieContract.MovieEntry._ID},
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",new String[]{String.valueOf(mMovie.getId())},null);
        Log.v(LOG_TAG,movieCursor+"cursor");
        // if it exist then show a toast about it else insert it into the database
        if (movieCursor.moveToFirst()) {
            int locationIdIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry._ID);
            Log.v(LOG_TAG,locationIdIndex+"loation");
            Toast.makeText(mcontext,"movie exists",Toast.LENGTH_LONG).show();
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
            Toast.makeText(mcontext,"Movie added to Fav",Toast.LENGTH_LONG).show();
            Log.v(LOG_TAG,insertedUri+"uri");
            // The resulting URI contains the ID for the row.  Extract the movieId from the Uri.
            long movieRowId = ContentUris.parseId(insertedUri);
        }
        movieCursor.close();

    }



    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     * <p>
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    public List<MovieList> getMoviesDataFromJson(String moviesJsonStr)
            throws JSONException {

        //These are the names of the json Objects that we need to be extracted
        final String MOVIE_ID = "id";
        final String OWN_RESULT = "results"; // contains arrays of objects
        final String MOVIE_POSTER = "poster_path";
        final String MOVIE_TITLE = "title";
        final String MOVIE_VOTE = "vote_average";
        final String MOVIE_SYNOPSIS = "overview";
        final String MOVIE_RELEASE_DATE = "release_date";
       // try {

            //String[] resultStr = new String[20];
            List<MovieList> moviesdata = new ArrayList<>();


            // this will call the data that is being in the result of the json
            JSONObject movieJson = new JSONObject(moviesJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(OWN_RESULT);
            Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());
            String imageURl;


            for (int i = 0; i < movieArray.length(); i++) {

                //get the JSON object representing the movies of the single movie
                JSONObject movies = movieArray.getJSONObject(i);

                String poster = movies.getString(MOVIE_POSTER);
                String title = movies.getString(MOVIE_TITLE);
                String release_date = movies.getString(MOVIE_RELEASE_DATE);
                String synopsis = movies.getString(MOVIE_SYNOPSIS);
                String rating = movies.getString(MOVIE_VOTE);
                String id = movies.getString(MOVIE_ID);


                imageURl = "https://image.tmdb.org/t/p/w185" + poster;
//                Log.v(LOG_TAG, "url for image: " + imageURl);
//                Log.v(LOG_TAG, "Title: " + title);
//                Log.v(LOG_TAG, "release date: " + release_date);
//                Log.v(LOG_TAG, "rating: " + rating);
//                Log.v(LOG_TAG, "synopsis: " + synopsis);
                Log.v(LOG_TAG, "movieid: " + id);

                //movie= new Movie(imageURl,title,release_date,synopsis,rating,imageURl);
                // movieArrayList.add(imageURl);
                //movieArrayList.add(title);
                // return imageURl;
                MovieList movieList = new MovieList(imageURl, title, release_date, synopsis, rating, id);
                moviesdata.add(movieList);
                //resultStr[i]= imageURl;

            }
        return moviesdata;
    }



    @Override
    protected List<MovieList> doInBackground(String... params) {
        String sortQuery = params[0];
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        // define the values of the constants
        // String popular = "popular";
        //String language = "en-US";
        int numMov = 0;
        try {
//            // getting the saved value in the shared Preference and changing the preference according
//            // to the user .
//            SharedPreferences preference= PreferenceManager.getDefaultSharedPreferences(getContext());
//            String sortOrder = preference.getString(getString(R.string.pref_sort_key), (R.string.pref_order_pop));
            switch(sortQuery)
                {   case "0":   sortQuery="popular";
                    break;

                    case "1":   sortQuery="top_rated";
                        break;
                }
//
            //Log.v(LOG_TAG,"lkjk"+sortOrder);

            // Construct the URL for the themoviedb query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://api.themoviedb.org/3/movie
            // to build the uri
            final String MOVIE_BASE_URL =
                    "https://api.themoviedb.org/3/movie";


            final String APPID_PARAM = "api_key";
            final String LANGUAGE_PARAM = "language";

            // now how to use it in the uri.builder class
            Uri builtUri = Uri.parse(MOVIE_BASE_URL)
                    .buildUpon()
                    .appendPath(sortQuery)
                    .appendQueryParameter(APPID_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .appendQueryParameter(LANGUAGE_PARAM, "en-US")
                    .build();

            //now to link it with a url object
            URL url = new URL(builtUri.toString());
            Log.v(LOG_TAG, "Built uri" + builtUri.toString());
            //here we are going to get the trailer of the particular movie
           // FetchMoviesTask mfetchmovie = new FetchMoviesTask().addMovie();



            //https://www.youtube.com/watch?v=wRaV4SIQY8A
            //https://api.themoviedb.org/3/movie/245891/reviews?api_key=4066178db02b89ef245ae29365f3f7ac&language=en-US
            //https://api.themoviedb.org/3/movie/245891/videos?api_key=4066178db02b89ef245ae29365f3f7ac&language=en-US


            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();
            Log.v(LOG_TAG, "Forecast json string: " + movieJsonStr);
        } catch (IOException e) {
            Log.e("ForecastFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("ForecastFragment", "Error closing stream", e);
                }
            }
        }try {
            return getMoviesDataFromJson(movieJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(List<MovieList> movies) {
        if (movies != null) {
            mAdapter.clear(); // first we clear all the previous entry
            for(MovieList movieForecastStr : movies) { // then we add each forecast entery one by one
                mAdapter.add(movieForecastStr); // from the server to the adapter.
            }
        }
    }

}