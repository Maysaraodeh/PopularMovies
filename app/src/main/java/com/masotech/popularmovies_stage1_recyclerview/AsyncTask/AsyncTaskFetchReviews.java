package com.masotech.popularmovies_stage1_recyclerview.AsyncTask;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.masotech.popularmovies_stage1_recyclerview.MainActivity;
import com.masotech.popularmovies_stage1_recyclerview.TaskCompleted.TaskCompleted;
import com.masotech.popularmovies_stage1_recyclerview.models.Review;

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

/**
 * Created by maysaraodeh on 25/08/2017.
 */

public class AsyncTaskFetchReviews extends AsyncTask<String , Void , Review[]> {

    private final static String TAG = "THE URL IS";
    Class<MainActivity> context = MainActivity.class;

    final String TMDB_BASE_URL = "http://api.themoviedb.org/3/movie/";
    final String API_KEY_PARAM = "api_key";


    private final String mApiKey;
    private TaskCompleted mlistener;

    public AsyncTaskFetchReviews(String apiKey , TaskCompleted listener ){
        super();
        mApiKey = apiKey;
        mlistener = listener;
    }



    @Override
    protected Review[] doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        String reviewString = null;

        try {
            URL url = buildUrl(params);
            urlConnection = (HttpURLConnection) url.openConnection();
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

            reviewString = builder.toString();

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
            return getTrailerDataFromJson(reviewString);

        }catch (JSONException e){
            e.printStackTrace();

        }

        return null;
    }



    private URL buildUrl (String[] parameters){

        //Log.d("parameter [0]" , parameters[0]);

        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath( parameters[0])
                .appendPath("reviews")
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

    private Review[] getTrailerDataFromJson(String reviewString) throws JSONException {

        final String TAG_RESULTS = "results";
        final String TAG_ID = "id";
        final String TAG_AUTHOR = "author";
        final String TAG_CONTENT = "content";


        JSONObject reviewJson = new JSONObject(reviewString);
        JSONArray data = reviewJson.getJSONArray(TAG_RESULTS);
        Review[] reviews = new Review[data.length()];

        for(int i=0 ; i< data.length() ; i++){
            reviews[i] = new Review();
            JSONObject reviewInfo = data.getJSONObject(i);
            reviews[i].setId(reviewInfo.getString(TAG_ID));
            reviews[i].setAuthor(reviewInfo.getString(TAG_AUTHOR));
            reviews[i].setContent(reviewInfo.getString(TAG_CONTENT));


        }
        return reviews;
    }

    @Override
    protected void onPostExecute(Review[] reviews) {
        super.onPostExecute(reviews);
        mlistener.fetchReviewTaskCompleted(reviews);
    }
}

