package com.example.android.popmovie;

import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
import java.util.Arrays;

/**
 * Created by user on 11/20/2016.
 */

public class MovieFragment extends Fragment {
    private  MoviesListAdapter listmovies;
    ArrayList<String> movieArrayList = new ArrayList<String>();
    public MovieFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);// this will give the permission for the refresh button to inflate.
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviefragment,menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
            fetchMoviesTask.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
    public static MovieList[] movieList = {
                        new MovieList(R.drawable.cupcake),new MovieList(R.drawable.donut),
            new MovieList(R.drawable.eclair),new MovieList(R.drawable.froyo),
            new MovieList(R.drawable.gingerbread),new MovieList(R.drawable.honeycomb),
            new MovieList(R.drawable.icecream),new MovieList(R.drawable.jellybean),
            new MovieList(R.drawable.kitkat),new MovieList(R.drawable.lollipop),
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main,container,false);
        listmovies = new MoviesListAdapter(getActivity(), Arrays.asList(movieList));

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootview.findViewById(R.id.flavors_grid);
        gridView.setAdapter(listmovies);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MovieList forecast = listmovies.getItem(position);
               // Toast.makeText(getActivity(),forecast,Toast.LENGTH_SHORT).show();
            }
        });


        return rootview;


    }



    public class FetchMoviesTask extends AsyncTask<String,Void, ArrayList<Movie>> {
      private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * <p>
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        public ArrayList<String> getMoviesDataFromJson(String moviesJsonStr)
                throws JSONException {

            //These are the names of the json Objects that we need to be extracted
            final String OWN_RESULT = "results"; // contains arrays of objects
            final String MOVIE_POSTER = "poster_path";
            final String MOVIE_TITLE = "title";
            final String MOVIE_VOTE = "vote_average";
            final String MOVIE_SYNOPSIS = "overview";
            final String MOVIE_RELEASE_DATE = "release_date";

            // this will call the data that is being in the result of the json
            JSONObject movieJson = new JSONObject(moviesJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(OWN_RESULT);
            String imageURl = null;



            for (int i = 0; i < movieArray.length();i++){

                //get the JSON object representing the movies
                JSONObject movies  = movieArray.getJSONObject(i);

                String poster = movies.getString(MOVIE_POSTER);
                String title = movies.getString(MOVIE_TITLE);
                String release_date = movies.getString(MOVIE_RELEASE_DATE);
                String synopsis = movies.getString(MOVIE_SYNOPSIS);
                String rating = movies.getString(MOVIE_VOTE);


                imageURl = "https://image.tmdb.org/t/p/w185"+poster;
                Log.v(LOG_TAG, "url for image: " + imageURl);
                Log.v(LOG_TAG, "Title: " + title);
                Log.v(LOG_TAG, "release date: " + release_date);
                Log.v(LOG_TAG, "rating: " + rating);
                Log.v(LOG_TAG, "synopsis: " + synopsis);

                //movie= new Movie(poster,title,release_date,synopsis,rating,imageURl);
               movieArrayList.add(imageURl);
                //movieArrayList.add(title);
               // return imageURl;

            }
            return movieArrayList;
        }



        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            ArrayList<MovieList> movieJsonStr = null;

            // define the values of the constants
           // String popular = "popular";
            //String language = "en-US";
            int numMov = 0;
            try {
                // Construct the URL for the themoviedb query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://api.themoviedb.org/3/movie
                //to build the uri
                final String MOVIE_BASE_URL =
                        "https://api.themoviedb.org/3/discover/movie?";
               // final String SHOW_VIEW = "popular";
                final String APPID_PARAM = "api_key";
                final String LANGUAGE_PARAM = "language";
                final String SORT_BY ="sort_by";

                // now how to use it in the uri.builder class
                Uri builtUri = Uri.parse(MOVIE_BASE_URL)
                        .buildUpon()
                        //.appendPath(SHOW_VIEW)
                        .appendQueryParameter(APPID_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .appendQueryParameter(LANGUAGE_PARAM, "en-US")
                        .appendQueryParameter(SORT_BY,"popularity.desc")// by popularity and another is vote_count.desc
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
          }try {
                return getMoviesDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }
//        @Override
//        protected void onPostExecute(ArrayList<String> movies) {
//            if (movies != null) {
//                movieArrayList.clear(); // first we clear all the previous entry
//                for(String dayForecastStr : movieArrayList) { // then we add each forecast entery one by one
//                    movieArrayList.add(dayForecastStr); // from the server to the adapter.
//                }
//            }
//        }

    }
}
