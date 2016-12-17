package com.example.android.popmovie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.List;

/**
 * Created by user on 11/20/2016.
 */

public class MovieFragment extends Fragment {
    public static final String EXTRA_MOVIE_DATA = "EXTRA_MOVIE_DATA";
    private ArrayAdapter<MovieList> mAdapter;
    public MovieFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);// this will give the permission for the refresh button to inflate.
    }
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.moviefragment,menu);
//    }
    @Override
    public void onStart() {
        // this method is called by default on the start of the app .
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute();
        super.onStart();
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_refresh) {
//            FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
//            fetchMoviesTask.execute();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//
//    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //This is the view of the view which is located within the fragment so
        // wherever one start a activity actually a fragment is started and the
        // the view of the fragment is decided by this method.
        View rootview = inflater.inflate(R.layout.fragment_main,container,false);
        mAdapter = new MoviesListAdapter(getContext(),R.layout.movie_item);
         final GridView gridView = (GridView) rootview.findViewById(R.id.flavors_grid);
        gridView.setAdapter(mAdapter);
        /**
         * This is the method when you click on any images there is the onitemclicklistener
         * and wherever it is clicked an action is defined within the click .
         * this method handles the click on the images.
         */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MovieList movie = mAdapter.getItem(position);
                Intent intent = new Intent(getContext(),  DetailActivity.class);
                intent.putExtra(MovieFragment.EXTRA_MOVIE_DATA,movie);
                startActivity(intent);
            }
        });
        return rootview;
    }

    /**
     * This is the method that has like four methods within like doinbackground(),
     * onpreExecute(),onPostExecute(),onProgressUpdate. It has the threads
     * so all the networking code is running on the background thread
     * and after that it is executed by the onPostExecute() method.
     */
    public class FetchMoviesTask extends AsyncTask<Object, Void, List<MovieList>> {
      private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
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
            final String OWN_RESULT = "results"; // contains arrays of objects
            final String MOVIE_POSTER = "poster_path";
            final String MOVIE_TITLE = "title";
            final String MOVIE_VOTE = "vote_average";
            final String MOVIE_SYNOPSIS = "overview";
            final String MOVIE_RELEASE_DATE = "release_date";

            //String[] resultStr = new String[20];
            List<MovieList> moviesdata = new ArrayList<>();

            // this will call the data that is being in the result of the json
            JSONObject movieJson = new JSONObject(moviesJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(OWN_RESULT);
            String imageURl;



            for (int i = 0; i < movieArray.length();i++){

                //get the JSON object representing the movies of the single movie
                JSONObject movies  = movieArray.getJSONObject(i);

                String poster = movies.getString(MOVIE_POSTER);
                String title = movies.getString(MOVIE_TITLE);
                String release_date = movies.getString(MOVIE_RELEASE_DATE);
                String synopsis = movies.getString(MOVIE_SYNOPSIS);
                String rating = movies.getString(MOVIE_VOTE);


               imageURl = "https://image.tmdb.org/t/p/w185"+poster;
//                Log.v(LOG_TAG, "url for image: " + imageURl);
//                Log.v(LOG_TAG, "Title: " + title);
//                Log.v(LOG_TAG, "release date: " + release_date);
//                Log.v(LOG_TAG, "rating: " + rating);
//                Log.v(LOG_TAG, "synopsis: " + synopsis);

                //movie= new Movie(imageURl,title,release_date,synopsis,rating,imageURl);
              // movieArrayList.add(imageURl);
                //movieArrayList.add(title);
               // return imageURl;
                MovieList movieList = new MovieList(imageURl,title,release_date,synopsis,rating);
                moviesdata.add(movieList);
                //resultStr[i]= imageURl;

            }
            return moviesdata;
        }



        @Override
        protected List<MovieList> doInBackground(Object... params) {
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
               // getting the saved value in the shared Preference and changing the preference according
                // to the user .
                SharedPreferences preference= PreferenceManager.getDefaultSharedPreferences(getContext());
                String sortOrder = preference.getString(getString(R.string.pref_sort_key),
                        getResources().getString(R.string.pref_order_pop));

                switch(sortOrder)
                {   case "0":   sortOrder="popular";
                    break;

                    case "1":   sortOrder="top_rated";
                        break;
                }
                //Log.v(LOG_TAG,"lkjk"+sortOrder);

                // Construct the URL for the themoviedb query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://api.themoviedb.org/3/movie
                // to build the uri
                    final String MOVIE_BASE_URL =
                            "https://api.themoviedb.org/3/movie";

                //final String SHOW_VIEW = "top_rated";
                final String APPID_PARAM = "api_key";
                final String LANGUAGE_PARAM = "language";
                //final String SORT_BY ="sort_by";

                // now how to use it in the uri.builder class
                Uri builtUri = Uri.parse(MOVIE_BASE_URL)
                        .buildUpon()
                        .appendPath(sortOrder)
                        .appendQueryParameter(APPID_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .appendQueryParameter(LANGUAGE_PARAM, "en-US")
                        //.appendQueryParameter(SORT_BY,"popularity.desc")// by popularity and another is vote_count.desc
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
}
