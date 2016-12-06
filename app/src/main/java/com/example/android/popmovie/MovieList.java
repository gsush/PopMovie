package com.example.android.popmovie;

import android.util.Log;

/**
 * Created by user on 11/23/2016.
 */

public class MovieList {
    private final String LOG_TAG = MovieList.class.getSimpleName();
    String mImageURl;
    String mtitle;
    String mrelease_date;
    String msynopsis;
    String mrating;
   // title,release_date,synopsis,rating

//    public MovieList(int image) {
//        this.image = image;
//    }
    public MovieList(String Imageurl/*,String Title,String Release_date,String Synopsis , String Rating*/ ) {
        this.mImageURl = Imageurl;
//        this.mtitle = Title;
//        this.mrelease_date = Release_date;
//        this.msynopsis = Synopsis;
//        this.mrating = Rating;
    }
    public String getImageurl() {
        Log.v(LOG_TAG, "image in list " + mImageURl)    ;
        return mImageURl;
    }
    public String getTitle() {
        return mtitle;
    }
    public String getRelease_date(){
        return mrelease_date;
    }
    public String getSynopsis(){
        return msynopsis;
    }
    public String getRating () {
        return mrating;
    }
}
