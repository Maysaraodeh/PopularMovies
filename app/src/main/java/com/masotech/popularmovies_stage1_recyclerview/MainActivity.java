package com.masotech.popularmovies_stage1_recyclerview;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.masotech.popularmovies_stage1_recyclerview.Adapter.ImageAdapter;
import com.masotech.popularmovies_stage1_recyclerview.Fragment.DetailsActivityFragment;
import com.masotech.popularmovies_stage1_recyclerview.Fragment.MainActivityFragment;
import com.masotech.popularmovies_stage1_recyclerview.models.Movie;

public class MainActivity extends AppCompatActivity implements ImageAdapter.Callback{



    public static boolean mTwoPane = false;
    private static final String FRAGMENT_KEY = "MY_FRAGMENT";


    MainActivityFragment mainActivityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout mDetailsPlaceholder = (FrameLayout) findViewById(R.id.movie_detail_container);



        mTwoPane = (mDetailsPlaceholder != null);
        FragmentManager fragmentManager = getSupportFragmentManager();

        if(savedInstanceState != null){
            mainActivityFragment = (MainActivityFragment) getSupportFragmentManager().getFragment(savedInstanceState , FRAGMENT_KEY);
        }else{
            mainActivityFragment = new MainActivityFragment();
        }


            fragmentManager.beginTransaction()
                    .replace(R.id.main_activity, mainActivityFragment)
                    .commit();






    }

    @Override
    public void onItemSelected(Movie movie) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailsActivityFragment.DETAIL_MOVIE, movie);

            DetailsActivityFragment fragment = new DetailsActivityFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DetailsActivityFragment.TAG)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, FRAGMENT_KEY, mainActivityFragment);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mainActivityFragment.onResume();
    }
}


