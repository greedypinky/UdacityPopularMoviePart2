package com.project1.popularmovie.data;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project1.popularmovie.R;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.VideoViewHolder> {

    private static final String TAG = MovieTrailerAdapter.class.getSimpleName();
    String[] mVideos = null;
    public final MovieTrailerOnClickHandler mClickHandler;


    /**
     * Constructer
     * @param handler
     */
    public MovieTrailerAdapter(MovieTrailerOnClickHandler handler) {

        mClickHandler = handler;
    }

    // TODO: DetailActivity will implement this onClickHandler to start Intent to show the video
    public interface MovieTrailerOnClickHandler {
        public void onClickToPlayVideo(String trailer);
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View videoLayoutList = inflater.inflate(R.layout.activity_movie_video_adapter, parent, shouldAttachToParentImmediately);
        return new VideoViewHolder(videoLayoutList);

    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        String video = mVideos[position];
        int index = position + 1;
        holder.mTrailerText.setText("Trailer " + index);
    }

    @Override
    public int getItemCount() {
        if (mVideos==null) {
            return 0;
        }
        else {
            return mVideos.length;
        }
    }


    // TODO: when we get the videos array from URL
    /**
     * Call by activity to update the data getting from the URL
     * setTrailersData
     * @param videos
     */
    public void setTrailersData(String[] videos) {
        if(videos != null) {
            // create new array again with new length
            mVideos = new String[videos.length];
            mVideos = videos;
            notifyDataSetChanged(); // this will trigger the update of the adapter
        } else {
            Log.d(TAG, "set no data");
        }
    }

    //TODO: we need to create a inner class VideoViewHolder
    /**
     * VideoViewHolder - View Holder for Movie Trailer
     */
    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTrailerText;

        public VideoViewHolder(View layoutView) {
            super(layoutView);
            mTrailerText = (TextView)layoutView.findViewById(R.id.trailer_name);
            mTrailerText.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //TODO: need to handle when onClick to start an intent for the video
            // Do it from Detail Activity
            // Activity implements the onClickHandler
            int selectedTrailer = getAdapterPosition();
            Log.d(TAG, "onClick selectedTrailer position: " + selectedTrailer);
            String trailerKey = mVideos[selectedTrailer];
            // TODO: need to implement the onClickHander in the activity
            mClickHandler.onClickToPlayVideo(trailerKey);
        }


    }

}
