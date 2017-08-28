package com.project1.popularmovie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.project1.popularmovie.data.Movie;
import com.project1.popularmovie.data.MovieNetworkUtility;
import com.project1.popularmovie.data.MovieTrailerAdapter;
import com.project1.popularmovie.data.MovieTrailerJSONUtility;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

public class MovieDetailActivity extends AppCompatActivity implements
        MovieTrailerAdapter.MovieTrailerOnClickHandler, LoaderManager.LoaderCallbacks<String[]> {

    private static final String TITLE_KEY = "title";
    private static final String RELEASE_DATE_KEY = "releaseDate";
    private static final String PLAYTIME_KEY = "playTime";
    private static final String USER_RATING_KEY = "userRating";
    private static final String POSTER_KEY = "poster";

    private final static String TAG = MovieDetailActivity.class.getSimpleName();
    private final static int LOADER_ID = 100;
    Movie mSelectedMovie;
    // the Detail view items
    TextView mOriginalTitle = null;
    TextView mReleaseDate = null;
    //TextView mMoviePlayTime = null; // for eg. 120 mins
    ScrollView mScrollView = null;
    TextView mMoviePlot = null;
    TextView mUserRating = null;
    ImageView mMoviePoster= null;
    String posterBaseURL = Movie.getPosterURL("w185");
    String moviePosterURL = null;
    TextView mTrailerHeader = null;
    RecyclerView mMovieVideoList = null;
    MovieTrailerAdapter mTrailerAdapter = null;
    Button mButtonAddFavorite = null;

    TextView mErrorMessage = null;
    ProgressBar mProgressBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        RelativeLayout relativeLayout =  (RelativeLayout) findViewById(R.id.relative_details_view);
        mSelectedMovie =  getIntent().getParcelableExtra("movies");
        Log.e(TAG, "mSelectedMovie" + mSelectedMovie.toString());
        mOriginalTitle = (TextView) findViewById(R.id.text_movie_title);
        mMoviePoster = (ImageView) findViewById(R.id.image_movie_poster);
        mReleaseDate = (TextView)findViewById(R.id.text_movie_release_year);
        mUserRating = (TextView) findViewById(R.id.text_movie_rating);
        mScrollView = (ScrollView) findViewById(R.id.scrollview_plot);
        mMoviePlot = (TextView)findViewById(R.id.text_movie_plot);
        // TODO: add a recycler view to show the movie video
        mTrailerHeader = (TextView) findViewById(R.id.text_trailers_header);
        mMovieVideoList = (RecyclerView) findViewById(R.id.movie_videos_list);
        //mMoviePlayTime = (TextView)findViewById(R.id.text_movie_playtime);
        mButtonAddFavorite = (Button)findViewById(R.id.add_favorite_button);
        mMovieVideoList = (RecyclerView)findViewById(R.id.movie_videos_list);
        mErrorMessage = (TextView) findViewById(R.id.text_detail_error_message);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_detail_loading_indicator);

        // Setup Recycler View 's layout to use LinearLayout
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        mMovieVideoList.setLayoutManager(layoutManager);
        mMovieVideoList.setHasFixedSize(true);
        // Create the MovieTrailerAdapter
        mTrailerAdapter = new MovieTrailerAdapter(this);
        // Set Adapter for the RecyclerView
        mMovieVideoList.setAdapter(mTrailerAdapter);

        if(mSelectedMovie !=null) {

            mOriginalTitle.setText(mSelectedMovie.getMovieTitle());
            mUserRating.setText(mSelectedMovie.getUserRating() + "/10");
            mReleaseDate.setText(mSelectedMovie.getReleaseYear());
            mMoviePlot.setText(mSelectedMovie.getMoviePlot());
            // compose Thumbnail movie poster's URL
            moviePosterURL = posterBaseURL + mSelectedMovie.getPosterPath();
            Log.d(TAG, "Poster URL:" + moviePosterURL);
            // Picasso will handle loading the images on a background thread, image decompression and caching the images.
            Picasso.with(this).load(moviePosterURL).into(mMoviePoster);

        }

        // TODO : add back the call to get Video Trailer URL
        // TODO: create the AsycnTask - the new one in Lesson6
        Bundle args = new Bundle();
        getSupportLoaderManager().initLoader(LOADER_ID, args, this);

    }

    private void showErrorMessage(){

        //TODO: Error message visible
        mErrorMessage.setVisibility(View.VISIBLE);
        mMovieVideoList.setVisibility(View.INVISIBLE);
    }

    private void showTrailers(){

        //TODO: set Error message Invisible
        mErrorMessage.setVisibility(View.INVISIBLE);
        mMovieVideoList.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TITLE_KEY, mOriginalTitle.getText().toString());
        outState.putString(RELEASE_DATE_KEY, mReleaseDate.getText().toString());
        outState.putString(USER_RATING_KEY, mUserRating.getText().toString());
        outState.putString(POSTER_KEY, moviePosterURL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO: add back the setting to get review of the movies
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_reviews) {
            loadMovieReviews();
        }
        return super.onOptionsItemSelected(item);
    }

    // Method to handle when recycler's row is being clicked
    @Override
    public void onClickToPlayVideo(String trailerID) {

        // TODO: start intent to play the youtube video
        //String trailerPath = "http://www.youtube.com/watch?v=" + trailerID;

        String trailerPath = "http://www.youtube.com/watch?v=opZ69P-0Jbc";
        Uri uri = Uri.parse(trailerPath);

        Intent trailerIndent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(trailerIndent);


    }





    /***
     * loadMovieReviews - get call when click on the Menu option - reviews
     */
    private void loadMovieReviews() {

        // call the background task to get results from URL by AsyncLoaderTask
    }

    // TODO: get trailers

    /**
     * loadMovieTrailer - get call when click on the RecyclerView's row
     */
    private void loadMovieTrailers(){
        // TODO : use the AsyncLoaderTask to get the result and then set the data to the Adapter


    }

    @Override
    public Loader<String[]> onCreateLoader(int id, final Bundle args) {
        // cache the result

        return new AsyncTaskLoader<String[]>(this) {
            String[] trailersData = null;
            @Override
            public String[] loadInBackground() {

                String movieID = mSelectedMovie.getMovieId();
                URL trailerURL = MovieNetworkUtility.getMovieURL(MovieNetworkUtility.VIDEOS,movieID);
                try {
                    String response = MovieNetworkUtility.getResponseFromHttp(trailerURL);
                    trailersData = MovieTrailerJSONUtility.parseTrailerData(getApplicationContext(),response);
                } catch (Exception e) {

                    return null;
                }

                return new String[0];
            }

            @Override
            protected void onStartLoading() {

                super.onStartLoading();
                if (trailersData != null) {

                   deliverResult(trailersData);

                }
                else {
                    Log.d(TAG,"forceLoad");
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(String[] data)
            {
                // cache the Data
                if (data != null) {
                    trailersData = data;
                }
                super.deliverResult(data);
            }
        };
       // return null;
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {

        Log.d(TAG,"onLoadFinished");
        // set the Progress bar to invisible
        mProgressBar.setVisibility(View.INVISIBLE);

        if (data == null) {
            showErrorMessage();
            //isErrorOccurs = true;
            Log.e(TAG,"Unable to get movie trailer data, please check the URL!");
        } else {
            // TODO: update the Adapter's data
            Log.d(TAG,"onLoadFinished- set adapter data");
            mTrailerAdapter.setTrailersData(data);
            showTrailers();
        }

    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

    }
}
