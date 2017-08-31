package com.masotech.popularmovies_stage1_recyclerview.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.masotech.popularmovies_stage1_recyclerview.R;
import com.masotech.popularmovies_stage1_recyclerview.models.Trailer;
import com.squareup.picasso.Picasso;

/**
 * Created by maysaraodeh on 25/08/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private Trailer[] mTrailer;
    private Context mContext;

    public TrailerAdapter(Context context , Trailer [] trailers){
        mContext = context;
        mTrailer = trailers;

    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        int layoutIdForImages = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean attachImmediately = false;

        View view = inflater.inflate(layoutIdForImages , parent , attachImmediately);
        TrailerViewHolder trailerViewHolder = new TrailerViewHolder(view);

        return  trailerViewHolder;

    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + mTrailer[position].getKey()));
                mContext.startActivity(intent);
            }
        });
        String yt_thumbnail_url = "http://img.youtube.com/vi/" + mTrailer[position].getKey() + "/0.jpg";
        Picasso.with(mContext)
                .load(yt_thumbnail_url)
                .resize(mContext.getResources().getInteger(R.integer.poster_trailer_width) , mContext.getResources().getInteger(R.integer.poster_trailer_height))
                .into(holder.trailerImage);
    }

    @Override
    public int getItemCount() {
        if(mTrailer == null)
            return 2;
        return mTrailer.length;
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder{

        ImageView trailerImage;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            trailerImage = (ImageView) itemView.findViewById(R.id.trailer_image);
            trailerImage.setAdjustViewBounds(true);
        }
    }
}
