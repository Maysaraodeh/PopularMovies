package com.masotech.popularmovies_stage1_recyclerview.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.masotech.popularmovies_stage1_recyclerview.DetailsActivity;
import com.masotech.popularmovies_stage1_recyclerview.MainActivity;
import com.masotech.popularmovies_stage1_recyclerview.R;
import com.masotech.popularmovies_stage1_recyclerview.models.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by maysaraodeh on 21/08/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    public Movie[] mMovies;
    private Context mContext;
    public static int mPosition;

    public ImageAdapter(Context context , Movie [] movies  ){
        mContext = context;
        mMovies = movies;
    }

    public interface Callback {
        void onItemSelected(Movie movie);
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutIdForImages = R.layout.grid_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean attachImmediately = false;

        View view = inflater.inflate(layoutIdForImages , parent , attachImmediately);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view);

        return  imageViewHolder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {

        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie movie = mMovies[position];

                if(!MainActivity.mTwoPane) {
                    Intent intent = new Intent(mContext.getApplicationContext(), DetailsActivity.class);
                    intent.putExtra(mContext.getResources().getString(R.string.parcel_key), movie);
                    mContext.startActivity(intent);
                }else {
                    ((Callback) mContext).onItemSelected(movie);
                }
                mPosition = position;
            }
        });
        Picasso.with(mContext)
                .load(mMovies[position].getMoviePoster())
                .resize(mContext.getResources().getInteger(R.integer.poster_width) , mContext.getResources().getInteger(R.integer.poster_height))
                .into(holder.movieImage);

    }

    @Override
    public int getItemCount() {
        if(mMovies == null){
            return -1;
        } else{
            return mMovies.length;
        }
    }






    class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView movieImage;

        public ImageViewHolder(View itemView) {
            super(itemView);
            movieImage = (ImageView) itemView.findViewById(R.id.movie_image);
            movieImage.setAdjustViewBounds(true);
        }
    }


}
