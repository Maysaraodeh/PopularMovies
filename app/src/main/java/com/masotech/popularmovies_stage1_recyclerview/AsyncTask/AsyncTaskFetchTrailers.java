package com.masotech.popularmovies_stage1_recyclerview.AsyncTask;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.masotech.popularmovies_stage1_recyclerview.MainActivity;
import com.masotech.popularmovies_stage1_recyclerview.TaskCompleted.TaskCompleted;
import com.masotech.popularmovies_stage1_recyclerview.models.Trailer;

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
 * Created by maysaraodeh on 23/08/2017.
 */

public class AsyncTaskFetchTrailers extends AsyncTask<String , Void , Trailer[]> {

    private final static String TAG = "THE URL IS";
    Class<MainActivity> context = MainActivity.class;

    final String TMDB_BASE_URL = "http://api.themoviedb.org/3/movie/";
    final String API_KEY_PARAM = "api_key";


    private final String mApiKey;
    private TaskCompleted mlistener;

    public AsyncTaskFetchTrailers(String apiKey , TaskCompleted listener ){
        super();
        mApiKey = apiKey;
       mlistener = listener;
    }



    @Override
    protected Trailer[] doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        String trailerString = null;

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

            trailerString = builder.toString();

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
            return getTrailerDataFromJson(trailerString);

        }catch (JSONException e){
            e.printStackTrace();

        }

        return null;
    }



    private URL buildUrl (String[] parameters){

        //Log.d("parameter [0]" , parameters[0]);

        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath( parameters[0])
                .appendPath("videos")
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

    private Trailer[] getTrailerDataFromJson(String trailerString) throws JSONException {

        final String TAG_RESULTS = "results";
        final String TAG_ID = "id";
        final String TAG_KEY = "key";
        final String TAG_NAME = "name";
        final String TAG_SITE = "site";
        final String TAG_TYPE = "type";

        JSONObject trailerJson = new JSONObject(trailerString);
        JSONArray data = trailerJson.getJSONArray(TAG_RESULTS);
        Trailer[] trailers = new Trailer[data.length()];

        for(int i=0 ; i< data.length() ; i++){
            trailers[i] = new Trailer();
            JSONObject trailerInfo = data.getJSONObject(i);
            trailers[i].setId(trailerInfo.getString(TAG_ID));
            trailers[i].setKey(trailerInfo.getString(TAG_KEY));
            trailers[i].setName(trailerInfo.getString(TAG_NAME));
            trailers[i].setSite(trailerInfo.getString(TAG_SITE));
            trailers[i].setType(trailerInfo.getString(TAG_TYPE));

        }
        return trailers;
    }

    @Override
    protected void onPostExecute(Trailer[] trailers) {
        super.onPostExecute(trailers);
        mlistener.fetchTrailerTaskCompleted(trailers);
    }
}

