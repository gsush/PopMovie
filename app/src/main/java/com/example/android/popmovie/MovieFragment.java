package com.example.android.popmovie;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 11/20/2016.
 */

public class MovieFragment extends Fragment {
    public MovieFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // View rootview = inflater.inflate(R.layout.movie_item,container,false);
        return super.onCreateView(inflater, container, savedInstanceState);


    }

    @Override
    public void onStart() {
        // this method is called by default on the start of the app .
        super.onStart();
        updateMovies();
    }

    private void updateMovies() {
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        moviesTask.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.most_popular) {
            //mostPopularMovies();
            FetchMoviesTask moviesTask = new FetchMoviesTask();
            moviesTask.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public class FetchMoviesTask extends AsyncTask<Void,Void,String>{
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * <p>
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private String[] getMoviesDataFromJson(String moviesJsonStr , int numMov) throws JSONException{

            //These are the names of the json Objects that we need to be extracted
            final String OWN_RESULT = "result"; // contains arrays of objects
            final String MOVIE_POSTER = "poster_path";
            final String MOVIE_TITLE = "title";
            final String MOVIE_VOTE = "vote_average";
            final String MOVIE_SYNOPSIS = "overview";
            final String MOVIE_RELEASE_DATE = "release_date";

            // this will call the data that is being in the result of the json
            JSONObject movieJson = new JSONObject(moviesJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(OWN_RESULT);
            String[] resultStrs = new String[numMov];

            for (int i = 0; i < movieArray.length();i++){
                // the format is poster,title,release data,synopsis and rating
                String poster;
                String title;
                String release_date;
                String synopsis;
                String rating;

                //get the JSON object representing the movies
                JSONObject movies  = movieArray.getJSONObject(i);

               // JSONObject movieObject = movies.getJSONObject(OWN_RESULT);


                JSONObject movieObject = movies.getJSONObject(OWN_RESULT);
                poster = movieObject.getString(MOVIE_POSTER);

                title = movieObject.getString(MOVIE_TITLE);
                release_date = movieObject.getString(MOVIE_RELEASE_DATE);
                synopsis = movieObject.getString(MOVIE_SYNOPSIS);
                rating = movieObject.getString(MOVIE_VOTE);
                title = movieObject.getString(MOVIE_TITLE);

                resultStrs[i]=poster;
                String imageURl ;
                imageURl = "https://image.tmdb.org/t/p/w185"+poster;


            }
            for (String s : resultStrs) {
                Log.v(LOG_TAG, "Result entry: " + s);
            }
            return resultStrs;
        }
        @Override
        protected String doInBackground(Void... voids) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            // define the values of the constants
            String popular = "popular";
            String language = "en-US";
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                //to build the uri
                final String MOVIE_BASE_URL =
                        "https://api.themoviedb.org/3/movie/popular?";
                final String APPID_PARAM = "api_key";
                final String LANGUAGE_PARAM = "language";

                // now how to use it in the uri.builder class
                Uri builtUri = Uri.parse(MOVIE_BASE_URL)
                        .buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .appendQueryParameter(LANGUAGE_PARAM, "en-US")
                        .build();

                //now to link it with a url object
                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built uri" + builtUri.toString());

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
            }
            return null;
        }
    }
}
