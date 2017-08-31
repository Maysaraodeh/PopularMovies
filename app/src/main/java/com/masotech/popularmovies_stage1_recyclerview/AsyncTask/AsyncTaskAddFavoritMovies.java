package com.masotech.popularmovies_stage1_recyclerview.AsyncTask;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.masotech.popularmovies_stage1_recyclerview.Fragment.DetailsActivityFragment;
import com.masotech.popularmovies_stage1_recyclerview.R;
import com.masotech.popularmovies_stage1_recyclerview.Utility.Utility;
import com.masotech.popularmovies_stage1_recyclerview.db.MovieContract;
import com.masotech.popularmovies_stage1_recyclerview.models.Movie;

/**
 * Created by maysaraodeh on 28/08/2017.
 */

public class AsyncTaskAddFavoritMovies extends AsyncTask<Void , Void , Integer> {

    private Movie mMovie;
    private Context mContext;
    private Toast mToast;

    public AsyncTaskAddFavoritMovies(Context context , Movie movie){
        mContext = context;
        mMovie = movie;
    }
    @Override
    protected Integer doInBackground(Void... params) {
        return Utility.isInFavorits(mContext , mMovie.getMovieId());
    }

    @Override
    protected void onPostExecute(Integer isFavorit) {

        if(isFavorit >= 1){
           // DetailsActivityFragment.favorit.setIcon(R.drawable.test);

            new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... params) {
                    return mContext.getContentResolver().delete(
                            MovieContract.MovieEntry.CONTENT_URI,
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                            new String[]{Integer.toString(mMovie.getMovieId())}
                    );
                }

                @Override
                protected void onPostExecute(Integer rowsDeleted) {
                    //item.setIcon();
                    if (mToast != null) {
                        mToast.cancel();
                    }
                    mToast = Toast.makeText(mContext, "Removed from your Favorit", Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }.execute();

        }else {

            new AsyncTask<Void, Void, Uri>() {
                @Override
                protected Uri doInBackground(Void... params) {
                    ContentValues values = new ContentValues();

                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovie.getMovieId());
                    values.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovie.getOriginalTitle());
                    values.put(MovieContract.MovieEntry.COLUMN_IMAGE, mMovie.getMoviePoster());
                    values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mMovie.getOverview());
                    values.put(MovieContract.MovieEntry.COLUMN_RATING, mMovie.getVoteAverage());
                    values.put(MovieContract.MovieEntry.COLUMN_DATE, mMovie.getReleaseDate());

                    return mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,
                            values);
                }

                @Override
                protected void onPostExecute(Uri returnUri) {
                    if (mToast != null) {
                        mToast.cancel();
                    }
                    mToast = Toast.makeText(mContext, "Added to Favorits", Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }.execute();

        }

    }
}
