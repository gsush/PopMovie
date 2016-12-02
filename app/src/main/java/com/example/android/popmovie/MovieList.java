package com.example.android.popmovie;

/**
 * Created by user on 11/23/2016.
 */

public class MovieList {
    String mImageURl;
    String mtitle;
    long mrelease_date;
    String msynopsis;
    double mrating;
   // title,release_date,synopsis,rating

//    public MovieList(int image) {
//        this.image = image;
//    }
    public MovieList(String Imageurl,String Title,long Release_date,String Synopsis , double Rating ) {
        this.mImageURl = Imageurl;
        this.mtitle = Title;
        this.mrelease_date = Release_date;
        this.msynopsis = Synopsis;
        this.mrating = Rating;
    }
    public String getImageurl() {
        return mImageURl;
    }
    public String getTitle() {
        return mtitle;
    }
    public long getRelease_date(){
        return mrelease_date;
    }
    public String getSynopsis(){
        return msynopsis;
    }
    public double getRating () {
        return mrating;
    }
}
