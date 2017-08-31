package com.masotech.popularmovies_stage1_recyclerview.TaskCompleted;

import com.masotech.popularmovies_stage1_recyclerview.models.Movie;
import com.masotech.popularmovies_stage1_recyclerview.models.Review;
import com.masotech.popularmovies_stage1_recyclerview.models.Trailer;

/**
 * Created by maysaraodeh on 23/08/2017.
 */

public interface TaskCompleted {
     void fetchMovieTaskCompleted(Movie[] movies);
     void fetchTrailerTaskCompleted(Trailer[] trailers);
     void fetchReviewTaskCompleted(Review[] reviews);
     void fetchFavoriteMoviesTaskCompleted(Movie [] favoriteMovies);
}
