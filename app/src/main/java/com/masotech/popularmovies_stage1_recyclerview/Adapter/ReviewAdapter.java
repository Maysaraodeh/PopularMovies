package com.masotech.popularmovies_stage1_recyclerview.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.masotech.popularmovies_stage1_recyclerview.R;
import com.masotech.popularmovies_stage1_recyclerview.models.Review;

/**
 * Created by maysaraodeh on 25/08/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context mContext;
    private Review[] mReviews;

    public ReviewAdapter(Context context , Review[] reviews){
        mContext = context;
        mReviews = reviews;
    }


    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutIdForImages = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean attachImmediately = false;

        View view = inflater.inflate(layoutIdForImages , parent , attachImmediately);
        ReviewAdapter.ReviewViewHolder reviewViewHolder = new ReviewAdapter.ReviewViewHolder(view);

        return  reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        if(mReviews[position].getAuthor().equals("")){
            holder.reviewAuthor.setText("No reviews Available");
        }
        holder.reviewAuthor.setText(mReviews[position].getAuthor());
        holder.reviewContent.setText(mReviews[position].getContent());
    }

    @Override
    public int getItemCount() {
        if(mReviews == null)
            return -1;
        return mReviews.length;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder{

        TextView reviewAuthor;
        TextView reviewContent;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            reviewAuthor = (TextView) itemView.findViewById(R.id.author_name);
            reviewContent = (TextView) itemView.findViewById(R.id.review_content);
        }
    }
}
