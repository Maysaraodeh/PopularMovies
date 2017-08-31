package com.masotech.popularmovies_stage1_recyclerview.AsyncTask;

/**
 * Created by maysaraodeh on 21/08/2017.
 */

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.masotech.popularmovies_stage1_recyclerview.MainActivity;
import com.masotech.popularmovies_stage1_recyclerview.TaskCompleted.TaskCompleted;
import com.masotech.popularmovies_stage1_recyclerview.models.Movie;

/**
 * Created by maysaraodeh on 21/08/2017.
 */

public class AsyncTaskFetchMovies extends AsyncTask<String , Void , Movie[]> {

    private final static String TAG = "THE URL IS";

    Class<MainActivity> context = MainActivity.class;

    final String TMDB_BASE_URL = "http://api.themoviedb.org/3/movie/";
    final String API_KEY_PARAM = "api_key";


    private final String mApiKey;
    private TaskCompleted mlistener;

    public AsyncTaskFetchMovies(String apiKey , TaskCompleted listener ){
        super();
        mApiKey = apiKey;
        mlistener = listener;

    }



    @Override
    protected Movie[] doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        String movieString = null;

        try {
            URL url = buildUrl(params);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream in = urlConnection.getInputStream();
            StringBuilder builder = new StringBuilder();

            if(in == null){
                return null;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(in));
            String lineByLine;

            while ((lineByLine = bufferedReader.readLine()) != null){
                builder.append(lineByLine).append("\n");
            }

            if(builder.length() == 0){
                return null;
            }

            movieString = builder.toString();

        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        finally {

            if(urlConnection !=null){
                urlConnection.disconnect();
            }
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                }catch (IOException e){
                    e.printStackTrace();
                    return null;
                }
            }
        }

        try {
            return getMovieDataFromJson(movieString);

        }catch (JSONException e){
            e.printStackTrace();

        }

        return null;
    }



    private URL buildUrl (String[] parameters){

        //Log.d("parameter [0]" , parameters[0]);

        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath( parameters[0])
                .appendQueryParameter(API_KEY_PARAM , mApiKey)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());

        }catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }
        Log.d(TAG , url.toString());
        return url;
    }

    private Movie[] getMovieDataFromJson(String movieString) throws JSONException {

        final String TAG_RESULTS = "results";
        final String TAG_ID = "id";
        final String TAG_ORIGINAL_TITLE = "original_title";
        final String TAG_POSTER_PATH = "poster_path";
        final String TAG_OVERVIEW = "overview";
        final String TAG_VOTE_AVERAGE = "vote_average";
        final String TAG_RELEASE_DATE = "release_date";

        JSONObject movieJson = new JSONObject(movieString);
        JSONArray data = movieJson.getJSONArray(TAG_RESULTS);
        Movie[] movies = new Movie[data.length()];

        for(int i=0 ; i< data.length() ; i++){
            movies[i] = new Movie();
            JSONObject movieInfo = data.getJSONObject(i);
            movies[i].setMovieId(movieInfo.getInt(TAG_ID));
            movies[i].setOriginalTitle(movieInfo.getString(TAG_ORIGINAL_TITLE));
            movies[i].setMoviePoster(movieInfo.getString(TAG_POSTER_PATH));
            movies[i].setOverview(movieInfo.getString(TAG_OVERVIEW));
            movies[i].setReleaseDate(movieInfo.getString(TAG_RELEASE_DATE));
            movies[i].setVoteAverage(movieInfo.getDouble(TAG_VOTE_AVERAGE));



        }
        return movies;
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        super.onPostExecute(movies);
        mlistener.fetchMovieTaskCompleted(movies);
    }
}

