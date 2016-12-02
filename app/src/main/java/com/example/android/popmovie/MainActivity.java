package com.example.android.popmovie;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.fragment, new MovieFragment())
//                    .commit();
//        }
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction() // removed the support from the getFragmentManager
                    .add(R.id.fragment, new MovieFragment())
                    .commit();
        }



       // ListView.setAdapter(new MoviesListAdapter(MainActivity.this, moviesImages));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
         // as you specify a parent activity in AndroidManifest.xml.
               int id = item.getItemId();

                       //noinspection SimplifiableIfStatement
        if (id == R.id.most_popular) {
            // when it is clicked. The app shows the most_popular movies
            //openMostPopularMovies()

            return true;
        }
        if (id == R.id.top_rated) {
            // This should open the top_rated movies.
            //openTopRatedMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openTopRatedMovies() {

    }

    private void openMostPopularMovies() {

    }
}
