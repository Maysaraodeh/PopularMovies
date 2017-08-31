package com.masotech.popularmovies_stage1_recyclerview.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.masotech.popularmovies_stage1_recyclerview.AsyncTask.AsyncTaskAddFavoritMovies;
import com.masotech.popularmovies_stage1_recyclerview.AsyncTask.AsyncTaskFetchReviews;
import com.masotech.popularmovies_stage1_recyclerview.AsyncTask.AsyncTaskFetchTrailers;
import com.masotech.popularmovies_stage1_recyclerview.MainActivity;
import com.masotech.popularmovies_stage1_recyclerview.R;
import com.masotech.popularmovies_stage1_recyclerview.Adapter.ReviewAdapter;
import com.masotech.popularmovies_stage1_recyclerview.TaskCompleted.TaskCompleted;
import com.masotech.popularmovies_stage1_recyclerview.Adapter.TrailerAdapter;
import com.masotech.popularmovies_stage1_recyclerview.models.Movie;
import com.masotech.popularmovies_stage1_recyclerview.models.Review;
import com.masotech.popularmovies_stage1_recyclerview.models.Trailer;
import com.squareup.picasso.Picasso;

/**
 * Created by maysaraodeh on 28/08/2017.
 */

public class DetailsActivityFragment extends Fragment {

    public static final String TAG = DetailsActivityFragment.class.getSimpleName();

    private TextView mTitle;
    private TextView mRating;
    private TextView mReleaseDate;
    private ImageView mPoster;
    private RatingBar mRatingBar ;
    private TextView mOverview;
    public static final String DETAIL_MOVIE = "DETAIL_MOVIE";
    public static final String SAVED_LAYOUT_MANAGER = "SAVED_LAYOUT_MANAGER";
    private static final String LIST_STATE_KEY = "SAVE_SCROLL";
    private Parcelable mListState;
    public static MenuItem favorit;

    ScrollView mScrollView;

    private Movie movie;
    private RecyclerView mReviewsRecyclerView;
    private ReviewAdapter mReviewAdapter;
    private RecyclerView.LayoutManager mReviewsLayoutManager;
    private RecyclerView mTrailersRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private RecyclerView.LayoutManager mTrailersLayoutManager;

    public DetailsActivityFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ActionBar actionBar;
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);


    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.details_activity_fragment , container , false);
        mTitle = (TextView) view.findViewById(R.id.titleTextView);
        mRating = (TextView) view.findViewById(R.id.ratingTextView);
        mReleaseDate = (TextView)view. findViewById(R.id.releaseDate);
        mPoster = (ImageView) view.findViewById(R.id.posterImageView);
        mRatingBar = (RatingBar) view.findViewById(R.id.rating);
        mOverview = (TextView) view.findViewById(R.id.overviewTextView);

        if(!MainActivity.mTwoPane) {
            Intent intent = getActivity().getIntent();
            movie = intent.getParcelableExtra(getString(R.string.parcel_key));
        }else {
            Bundle arguments = getArguments();
            if (arguments != null) {
                movie = arguments.getParcelable(DetailsActivityFragment.DETAIL_MOVIE);
            }
        }

        Picasso.with(getActivity())
                .load(movie.getMoviePoster())
                .resize(getResources().getInteger(R.integer.poster_details_width),
                        getResources().getInteger(R.integer.poster_details_height))

                .into(mPoster);

        mRating.setText(movie.getVoteAverage().toString() + "/10");
        mRatingBar.setRating(( movie.getVoteAverage().floatValue() / 2f));
        mOverview.setText(movie.getOverview());
        mReleaseDate.setText(movie.getReleaseDate());
        mTitle.setText(movie.getOriginalTitle());

        String movieId = (movie.getMovieId()).toString();
        mTrailersRecyclerView = (RecyclerView) view.findViewById(R.id.rv_trailers);
        mReviewsRecyclerView = (RecyclerView) view.findViewById(R.id.rv_reviews);

        loadTrailers(movieId);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().onBackPressed();
        }
        if(id == R.id.action_favorite){
            AsyncTaskAddFavoritMovies asyncTaskFetchFavoritMovies = new AsyncTaskAddFavoritMovies(getActivity() , movie);
            asyncTaskFetchFavoritMovies.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_details_fragment , menu);
        favorit = menu.findItem(R.id.action_favorite);


    }

    public void loadTrailers(String movieId) {
        String apiKey = getResources().getString(R.string.api_key);
        try {
            TaskCompleted taskCompleted = new TaskCompleted() {
                @Override
                public void fetchMovieTaskCompleted(Movie[] movies) {

                }

                @Override
                public void fetchTrailerTaskCompleted(Trailer[] trailers) {
                    mTrailerAdapter = new TrailerAdapter(getActivity() , trailers);
                    mTrailersRecyclerView.setAdapter(mTrailerAdapter);
                    if(mTrailerAdapter.getItemCount() == 0){

                    }else {
                        mTrailersLayoutManager = new GridLayoutManager(getActivity() , mTrailerAdapter.getItemCount());
                        mTrailersRecyclerView.setLayoutManager(mTrailersLayoutManager);
                    }

                }

                @Override
                public void fetchReviewTaskCompleted(Review[] reviews) {
                    mReviewAdapter = new ReviewAdapter(getActivity() , reviews);
                    mReviewsRecyclerView.setAdapter(mReviewAdapter);
                    mReviewsLayoutManager = new LinearLayoutManager(getActivity());
                    mReviewsRecyclerView.setLayoutManager(mReviewsLayoutManager);
                }

                @Override
                public void fetchFavoriteMoviesTaskCompleted(Movie[] favoriteMovies) {

                }

            };
            AsyncTaskFetchTrailers asyncTaskFetchTrailers = new AsyncTaskFetchTrailers(apiKey, taskCompleted);
            asyncTaskFetchTrailers.execute(movieId);
            AsyncTaskFetchReviews asyncTaskFetchReviews = new AsyncTaskFetchReviews(apiKey , taskCompleted);
            asyncTaskFetchReviews.execute(movieId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }






}
