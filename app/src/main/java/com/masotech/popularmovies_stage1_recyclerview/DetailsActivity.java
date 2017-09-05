package com.masotech.popularmovies_stage1_recyclerview;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ScrollView;

import com.masotech.popularmovies_stage1_recyclerview.Fragment.DetailsActivityFragment;

public class DetailsActivity extends AppCompatActivity {




    DetailsActivityFragment detailsActivityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);





        FragmentManager fragmentManager = getSupportFragmentManager();

        detailsActivityFragment = new DetailsActivityFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.details_activity , detailsActivityFragment)
                .commit();




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }




}
