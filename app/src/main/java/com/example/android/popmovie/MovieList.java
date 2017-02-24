package com.example.android.popmovie;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by user on 11/23/2016.
 */

public class MovieList implements Parcelable {
    public final String LOG_TAG = MovieList.class.getSimpleName();
    String mImageURl;
    String mtitle;
    String mrelease_date;
    String msynopsis;
    String mrating;
    int mid;
    String mbackUrl;
   // title,release_date,synopsis,rating

//    public MovieList(int image) {
//        this.image = image;
//    }
    public MovieList(String Imageurl,String Title,String Release_date,String Synopsis , String Rating, int Id,String PosterUrl ) {
        this.mImageURl = Imageurl;
        this.mtitle = Title;
        this.mrelease_date = Release_date;
        this.msynopsis = Synopsis;
        this.mrating = Rating;
        this.mid = Id;
        this.mbackUrl=PosterUrl;
    }
    private MovieList(Parcel in){
        mImageURl=in.readString();
        mtitle=in.readString();
        mrelease_date=in.readString();
        msynopsis=in.readString();
        mrating=in.readString();
        mid=in.readInt();
        mbackUrl=in.readString();
    }



    public String getImageurl() {
        Log.v(LOG_TAG, "image in list " + mImageURl);
        return mImageURl;
    }
    public String getBackPoster() {
        Log.v(LOG_TAG,"back poster "+ mbackUrl);
        return  mbackUrl;
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

    public int getId(){ return mid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mImageURl);
        parcel.writeString(mtitle);
        parcel.writeString(mrelease_date);
        parcel.writeString(msynopsis);
        parcel.writeString(mrating);
        parcel.writeInt(mid);
        parcel.writeString(mbackUrl);
    }
    public final static Parcelable.Creator<MovieList> CREATOR = new Parcelable.Creator<MovieList>() {
        @Override
        public MovieList createFromParcel(Parcel parcel) {
            return new MovieList(parcel);
        }

        @Override
        public MovieList[] newArray(int i) {
            return new MovieList[i];
        }
    };
}
