package com.project1.popularmovie.data;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.project1.popularmovie.R;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * MovieAdapter
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    private Context mContext; // Context of MainActivity
    private List<Movie> mMovies; // Movie list
    private String currentPosterSize ="w185"; // Currently we will use poster size w185

    public static final String TAG = MovieAdapter.class.getSimpleName();


    /**
     * MovieAdapter
     * @param context
     * @param movies
     */
    public MovieAdapter(Activity context, List<Movie> movies) {
        super(context,0,movies);
        mContext = context;
        mMovies = movies;
        Log.d(TAG,"movies " + mMovies );
    }



    @Override
    public Movie getItem(int position) {
        if(mMovies != null) {
            return mMovies.get(position);
        } else {
            return null;
        }
    }

    /**
     * getCount
     * @return return movie count
     */
    @Override
    public int getCount() {

        if(mMovies != null) {
            return mMovies.size();
        } else {
            return 0;
        }
    }

    /**
     * getItemId
     * @param position
     * @return item's position
     */
    @Override
    public long getItemId(int position) {

        return position;
    }

    /**
     * getView
     * @param position
     * @param convertView
     * @param parent
     * @return View View for movie item
     */
    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {

        Log.d(TAG,"getView is called at position " + position);
        // super.getView(position, convertView, parent);

        // Get the movie by position
        Movie movieItem = getItem(position);
        //ImageView posterImageView = null;
        // Inflat the view that hold the image view
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item, parent, false);
        }

        ImageView posterImageView = (ImageView) convertView.findViewById(R.id.movie_thumbnail_image);
        //posterImageView.setLayoutParams(new GridView.LayoutParams(85,85));
        // Set the image by this position
        String posterSize = currentPosterSize;
        String posterBaseURL = Movie.getPosterURL(posterSize);

        String moviePosterURL = posterBaseURL + movieItem.getPosterPath();
        Log.d(TAG,"Load image from poster URL : " + moviePosterURL);
        // Picasso will handle loading the images on a background thread, image decompression and caching the images.
        Picasso.with(getContext()).load(moviePosterURL).into(posterImageView);
        // populate the view item in the AdapterView
        return convertView;
    }

    /**
     * call from the Async Task to set the latest movie list
     * updateMovieList
     * @param theMovies movie list
     */
    public void updateMovieList(List<Movie> theMovies) {
        mMovies = theMovies;
        Log.d(TAG,"is the movie list empty?" + mMovies.size());
        // need to call notifyDataSetChanged otherwise Gridview will not be updated.
        notifyDataSetChanged();
    }

    /**
     * getMovieList
     * @return List<Movie> movie list
     */
    public List<Movie> getMovieList() {

       return mMovies;

    }

    /**
     * updatePosterSize
     * @param size set Poster'size to a new size
     */
    public void updatePosterSize(String size) {

        currentPosterSize = size;

    }

}
