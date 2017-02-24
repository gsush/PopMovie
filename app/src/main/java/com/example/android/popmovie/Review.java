package com.example.android.popmovie;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2/22/2017.
 */

public class Review implements Parcelable {
    private String author;
    private String body;

    public Review(String author, String body){
        this.author = author;
        this.body = body;
    }

    public String getAuthor(){return author;}


    public String getBody(){return body;}

    @Override
    public int describeContents() {
        return 0;
    }
    private Review(Parcel in){
        author = in.readString();
        body = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(body);
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int i) {
            return new Review[i];
        }

    };
}
