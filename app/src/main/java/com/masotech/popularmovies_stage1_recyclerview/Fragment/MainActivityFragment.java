package com.masotech.popularmovies_stage1_recyclerview.Fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.masotech.popularmovies_stage1_recyclerview.AsyncTask.AsyncTaskFetchFavouriteMovies;
import com.masotech.popularmovies_stage1_recyclerview.AsyncTask.AsyncTaskFetchMovies;
import com.masotech.popularmovies_stage1_recyclerview.Adapter.ImageAdapter;
import com.masotech.popularmovies_stage1_recyclerview.R;
import com.masotech.popularmovies_stage1_recyclerview.TaskCompleted.TaskCompleted;
import com.masotech.popularmovies_stage1_recyclerview.models.Movie;
import com.masotech.popularmovies_stage1_recyclerview.models.Review;
import com.masotech.popularmovies_stage1_recyclerview.models.Trailer;

import static com.masotech.popularmovies_stage1_recyclerview.R.id.action_sort_by_rating;

/**
 * Created by maysaraodeh on 27/08/2017.
 */

public class MainActivityFragment extends Fragment {
    RecyclerView recyclerView;
    ImageAdapter imageAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    public static String  sortBy = "popular";
    private static final String SORT_SETTING_KEY = "sort_setting";
    boolean once = false;


    public static final int COL_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_TITLE = 2;
    public static final int COL_POSTER_PATH = 3;
    public static final int COL_OVERVIEW = 4;
    public static final int COL_RATING = 5;
    public static final int COL_RELEASE_DATE = 6;
    private static final String FAVORITE = "favorite";

    public MainActivityFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu , menu);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_activity_fragment , container , false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_movies);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerViewLayoutManager = new GridLayoutManager(getActivity(), 4);
        } else {
            recyclerViewLayoutManager = new GridLayoutManager(getActivity(), 2);
        }
        recyclerView.setLayoutManager(recyclerViewLayoutManager);



        if (savedInstanceState == null) {
            loadMovies(sortBy);

        } else {
            Parcelable[] parcelables = savedInstanceState.getParcelableArray(getString(R.string.parcel_key));
            if (savedInstanceState.containsKey(SORT_SETTING_KEY)) {
                sortBy = savedInstanceState.getString(SORT_SETTING_KEY);
                if(sortBy == getResources().getString(R.string.sort_by_pop)){
                    if (parcelables != null) {
                        int numMovieObjects = parcelables.length;
                        Movie[] moviesOnSave = new Movie[numMovieObjects];
                        for (int i = 0; i < numMovieObjects; i++) {
                            moviesOnSave[i] = (Movie) parcelables[i];
                        }

                        imageAdapter = new ImageAdapter(getContext(), moviesOnSave);
                        recyclerView.setAdapter(imageAdapter);

                    }
                }else {
                    loadMovies(sortBy);
                }
                }
            }
        return view;
    }



    @Override
    public void onResume() {

        if(sortBy == FAVORITE){
            loadMovies(sortBy);
        }
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_sort_by_popularity) {
            sortBy = getString(R.string.sort_by_pop);
            loadMovies(sortBy);

        }
        if (id == action_sort_by_rating) {
            sortBy = getString(R.string.sort_by_top);
            loadMovies(sortBy);

        }
        if(id == R.id.action_sort_by_favorite){
            sortBy = FAVORITE;
            loadMovies(sortBy);
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean networkAvilable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public void loadMovies(final String sortBy)  {
        String apiKey = getResources().getString(R.string.api_key);
        if (networkAvilable()) {
            try {
                TaskCompleted taskCompleted = new TaskCompleted() {

                    @Override
                    public void fetchMovieTaskCompleted(final Movie[] moviesDone) {
                        imageAdapter = new ImageAdapter(getContext(), moviesDone);
                        recyclerView.setAdapter(imageAdapter);

                    }
                    @Override
                    public void fetchTrailerTaskCompleted(Trailer[] trailers) {}
                    @Override
                    public void fetchReviewTaskCompleted(Review[] reviews) {}
                    @Override
                    public void fetchFavoriteMoviesTaskCompleted(Movie[] favoriteMovies) {
                        imageAdapter = new ImageAdapter(getContext(), favoriteMovies);
                        recyclerView.setAdapter(imageAdapter);

                    }



                };


                if(!sortBy.equals(FAVORITE)) {
                    AsyncTaskFetchMovies asyncTaskMovie = new AsyncTaskFetchMovies(apiKey, taskCompleted);
                    asyncTaskMovie.execute(sortBy);
                }else {
                    AsyncTaskFetchFavouriteMovies asyncTaskFetchFavouriteMovies = new AsyncTaskFetchFavouriteMovies(getContext() , taskCompleted);
                    asyncTaskFetchFavouriteMovies.execute();
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            Toast.makeText(getActivity(), getResources().getString(R.string.internet_connection), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
            outState.putString(SORT_SETTING_KEY, sortBy);


        if (networkAvilable()) {
            int moviesCount = imageAdapter.getItemCount();
            if (moviesCount > 0) {
                Movie[] moviesSave = new Movie[moviesCount];

                for (int i = 0; i < moviesCount; i++) {
                    moviesSave[i] = imageAdapter.mMovies[i];

                }
                outState.putParcelableArray(getString(R.string.parcel_key), moviesSave);
            }
        }
    }
}
