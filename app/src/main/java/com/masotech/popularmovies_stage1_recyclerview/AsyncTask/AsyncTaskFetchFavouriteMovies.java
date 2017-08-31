package com.masotech.popularmovies_stage1_recyclerview.AsyncTask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.masotech.popularmovies_stage1_recyclerview.TaskCompleted.TaskCompleted;
import com.masotech.popularmovies_stage1_recyclerview.db.MovieContract;
import com.masotech.popularmovies_stage1_recyclerview.models.Movie;

/**
 * Created by maysaraodeh on 28/08/2017.
 */

public class AsyncTaskFetchFavouriteMovies extends AsyncTask<Void , Void , Movie[]> {

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_IMAGE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_DATE
    };



    private Context mContext;
    private TaskCompleted mListener;
    public AsyncTaskFetchFavouriteMovies(Context context , TaskCompleted listener){
        mContext = context;
        mListener = listener;
    }


    @Override
    protected Movie[] doInBackground(Void... params) {
        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                MOVIE_COLUMNS,
                null,
                null,
                null
        );
        return getFavoriteMoviesDataFromCursor(cursor);
    }

    private Movie[] getFavoriteMoviesDataFromCursor(Cursor cursor) {
        Movie[] results = new Movie[cursor.getCount()];
        int i=0;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Movie movie = new Movie(cursor);
                Log.d("movie cursor" , movie.getMoviePoster());
                results[i]=movie;
                i++;
            } while (cursor.moveToNext());
            cursor.close();
        }
        return results;
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        super.onPostExecute(movies);
        mListener.fetchFavoriteMoviesTaskCompleted(movies);

    }
}
