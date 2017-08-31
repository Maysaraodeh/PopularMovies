package com.masotech.popularmovies_stage1_recyclerview.models;

/**
 * Created by maysaraodeh on 21/08/2017.
 */

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.masotech.popularmovies_stage1_recyclerview.Fragment.MainActivityFragment;

/**
 * Created by maysaraodeh on 12/08/2017.
 */

public class Movie implements Parcelable {

    private Integer movieId;
    private String originalTitle;
    private String moviePoster;
    private String overview;
    private String releaseDate;
    private Double voteAverage;

    public Movie(){}
    public Movie(Cursor cursor){
        this.movieId = cursor.getInt(MainActivityFragment.COL_MOVIE_ID);
        this.originalTitle = cursor.getString(MainActivityFragment.COL_TITLE);
        this.overview = cursor.getString(MainActivityFragment.COL_OVERVIEW);
        this.releaseDate = cursor.getString(MainActivityFragment.COL_RELEASE_DATE);
        this.voteAverage = cursor.getDouble(MainActivityFragment.COL_RATING);
        this.moviePoster = cursor.getString(MainActivityFragment.COL_POSTER_PATH);
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }


    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getMoviePoster() {
        final String TMDB_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";
        if(moviePoster.contains(TMDB_POSTER_BASE_URL)){
            return moviePoster;

        }else{
            return TMDB_POSTER_BASE_URL + moviePoster;

        }



    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        if(!(overview.equals("null")))
            this.overview = overview;

    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // store the data in parcel
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(originalTitle);
        parcel.writeString(moviePoster);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeValue(voteAverage);
        parcel.writeLong(movieId);
    }

    //retrive data from parcel
    protected Movie(Parcel in) {
        originalTitle = in.readString();
        moviePoster = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        voteAverage = (Double) in.readValue(Double.class.getClassLoader());
        movieId = (Integer) in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


}
