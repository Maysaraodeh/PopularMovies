package com.masotech.popularmovies_stage1_recyclerview.Utility;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.masotech.popularmovies_stage1_recyclerview.db.MovieContract;

/**
 * Created by maysaraodeh on 28/08/2017.
 */

public class Utility {


    private static int mNumRows;
    public static int isInFavorits(Context context, int id) {
        Cursor cursor = context.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[] { Integer.toString(id) },
                null
        );
        try{
            mNumRows = cursor.getCount();
            cursor.close();

        }catch (NullPointerException e){
            Log.d("No Entries" , mNumRows+"");
        }
        return mNumRows;

    }
}
