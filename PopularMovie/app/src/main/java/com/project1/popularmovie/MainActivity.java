package com.project1.popularmovie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project1.popularmovie.data.Movie;
import com.project1.popularmovie.data.MovieAdapter;
import com.project1.popularmovie.data.MovieJSONUtility;
import com.project1.popularmovie.data.MovieNetworkUtility;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * MainActivity
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie[]> {

    String TAG = MainActivity.class.getSimpleName();
    private String defaultSortingMethod = MovieNetworkUtility.POPULAR;
    private GridView mMovieGridView = null;
    private TextView mErrorMessage = null;
    private ProgressBar mLoadingIndicator = null;
    private MovieAdapter mMovieAdapter = null;
    private Movie[] mMovieArray = {};
    private static final String MOVIE_KEY = "movies";
    private static final String ERROR_KEY = "error";
    private static final String SORT_POPULAR_KEY = "sort_popular";
    private static final String SORT_TOP_RATED_KEY = "sort_top_rated";
    private static final String SHOW_FAVORITE_KEY = "show_favorite_movies";

    private boolean isSortPopular = true;
    private boolean isSortTopRated = false;
    private boolean isFavorite = false;
    private boolean isErrorOccurs = false;
    ArrayList<Movie> mMovieList;
    private static final int MOVIES_LOADER_ID = 88;

    public enum SortMethod {
        POPULAR("popular"),
        TOP_RATED("top_rated"),
        FAVORITE("favorite");

        private String mSortDesc;
        SortMethod(String sort){
            mSortDesc = sort;
        }

        String getDesc(){
            return mSortDesc;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int loaderID = MOVIES_LOADER_ID;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean hasSavedInstanceState = false;

        // Retrieve store state
        if(savedInstanceState!= null && savedInstanceState.containsKey(MOVIE_KEY)) {
            if(savedInstanceState.containsKey(MOVIE_KEY)) {
                // Retrieve the data from the saveInstanceState
                mMovieList = savedInstanceState.getParcelableArrayList(MOVIE_KEY);
                Log.d(TAG, "restore the movie list");
            }
            if(savedInstanceState.containsKey(ERROR_KEY)) {
               if( savedInstanceState.getBoolean(ERROR_KEY)) {
                   showErrorMessage();
                   Log.d(TAG, "restore the state of error!");
               }
            }
        }

        if(savedInstanceState!= null && savedInstanceState.containsKey(SORT_POPULAR_KEY)) {
            isSortPopular = savedInstanceState.getBoolean(SORT_POPULAR_KEY);
            if(isSortPopular) {
                getSupportActionBar().setTitle(R.string.app_pop_title);
                defaultSortingMethod = SortMethod.POPULAR.name();
            }
        } else if (savedInstanceState!= null && savedInstanceState.containsKey(SORT_TOP_RATED_KEY)) {
            isSortTopRated = savedInstanceState.getBoolean(SORT_TOP_RATED_KEY);
            if(isSortTopRated) {
                getSupportActionBar().setTitle(R.string.app_top_rated_title);
                defaultSortingMethod = SortMethod.POPULAR.name();
            }

        } else if(savedInstanceState!= null && savedInstanceState.containsKey(SHOW_FAVORITE_KEY)){
            isFavorite = savedInstanceState.getBoolean(SHOW_FAVORITE_KEY);
            if(isFavorite) {
                getSupportActionBar().setTitle(R.string.app_favorites_title);
                defaultSortingMethod = SortMethod.FAVORITE.name();
            }
        } else {

            // by default, set App title as Popular movie
            getSupportActionBar().setTitle(R.string.app_pop_title);
        }

        Log.d(TAG, "ENUM Popular:" + SortMethod.POPULAR);
        Log.d(TAG, "ENUM TOPRATED" + SortMethod.TOP_RATED);
        Log.d(TAG, "ENUM FAVORITE:" + SortMethod.FAVORITE);

        Log.d(TAG, "ENUM Popular n:" + SortMethod.POPULAR.getDesc());
        Log.d(TAG, "ENUM TOPRATED n:" + SortMethod.TOP_RATED.getDesc());
        Log.d(TAG, "ENUM FAVORITE n:" + SortMethod.FAVORITE.getDesc());

        Log.d(TAG, "Action Bar Title:" + getSupportActionBar().getTitle());

        mMovieGridView = (GridView) findViewById(R.id.movie_grid);
        mErrorMessage = (TextView) findViewById(R.id.error_message);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        // add listener when clicking on the item
        mMovieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Movie selectedMovie = mMovieAdapter.getItem(i) ;
                Log.d(TAG, "Selected movie to string:" + selectedMovie.toString());
                Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);
                intent.putExtra(MOVIE_KEY, selectedMovie);
                startActivity(intent);

            }
        });

        // init the movie list only when it is null
        if(mMovieList == null) {
            mMovieList = new ArrayList<>();
        }
        mMovieAdapter = new MovieAdapter(this, mMovieList);
        mMovieGridView.setAdapter(mMovieAdapter);
        // If no savedInstanceState, we need to get data from the API
        //if(savedInstanceState == null) {
        //    loadMoviesData(defaultSortingMethod);
        //}
        // TODO: add back load Favorite

        LoaderManager.LoaderCallbacks<Movie[]> loaderCallbacks = MainActivity.this;
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.sort_key), defaultSortingMethod);
        getSupportLoaderManager().initLoader(loaderID,bundle,loaderCallbacks);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(isErrorOccurs) {
            outState.putBoolean(ERROR_KEY, isErrorOccurs);
        } else {
            ArrayList<Movie> movieList = new ArrayList<>(mMovieAdapter.getMovieList());
            outState.putParcelableArrayList(MOVIE_KEY, movieList);
            Log.d(TAG, "onSaveInstanceState");
            if(isSortPopular) {
                outState.putBoolean(SORT_POPULAR_KEY, isSortPopular);
                Log.d(TAG, "onSaveInstanceState-Popular");
            } else if (isSortTopRated) {
                outState.putBoolean(SORT_TOP_RATED_KEY, isSortTopRated);
                Log.d(TAG, "onSaveInstanceState-TopRated");
            }
            else {
                // get the Favorite movie list
                /*
                ArrayList<Movie> movieList = new ArrayList<>();
                outState.putParcelableArrayList(MOVIE_KEY, movieList);
                outState.putBoolean(SHOW_FAVORITE_KEY, isFavorite);
                */
            }

        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = new Bundle();
        switch (item.getItemId()) {
            case R.id.action_sort_most_popular:
                //loadMoviesData(MovieNetworkUtility.POPULAR);
                bundle.putString(getString(R.string.sort_key), SortMethod.POPULAR.getDesc());
                getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID,bundle,this);
                getSupportActionBar().setTitle(R.string.app_pop_title);
                isSortPopular = true;
                break;
            case R.id.action_sort_top_rated:
                //loadMoviesData(MovieNetworkUtility.TOP_RATED);
                bundle.putString(getString(R.string.sort_key), SortMethod.TOP_RATED.getDesc());
                getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID,bundle,this);
                getSupportActionBar().setTitle((R.string.app_top_rated_title));
                isSortTopRated = true;
                break;

            case R.id.action_sort_favorite:
                // TODO: get the favorite list of movies
                // this List should save it in the SharedPreference?
                // load the Favorite movies
                loadFavoriteMovies();
                // set the App Title
                getSupportActionBar().setTitle(getString(R.string.app_favorites_title));
                // set the isFavorite flag to true
                isFavorite = true;
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showPosterGrid(){
        mMovieGridView.setVisibility(GridView.VISIBLE);
        mErrorMessage.setVisibility(TextView.INVISIBLE);
    }

    private void showErrorMessage(){
        mMovieGridView.setVisibility(GridView.INVISIBLE);
        mErrorMessage.setVisibility(TextView.VISIBLE);
    }

    /**
     * onCreateLoader - loader's callback method
     * @param id
     * @param args
     * @return
     */
    @Override
    public Loader onCreateLoader(int id, final Bundle args) {

        return new AsyncTaskLoader(this) {
            Movie[] theMovies;
            @Override
            public Object loadInBackground() {
                // TODO: get the Sorting Method from the Bundle
                String sortMethod = args.getString(getString(R.string.sort_key));
                Log.d(TAG, "loadInBackground:Movie sortby " + sortMethod);
                Log.d(TAG, "loadInBackground:Popular String " + getString(R.string.most_popular));
               // if (sortMethod.equals(getString(R.string.most_popular)) || sortMethod.equals(getString(R.string.top_rated)) )
               // {
                    URL sortMovieURL = MovieNetworkUtility.getMovieURL(sortMethod, null);
                    Log.d(TAG, "Movie URL is " + sortMovieURL);
                    try {
                        String response = MovieNetworkUtility.getResponseFromHttp(sortMovieURL);
                        theMovies = MovieJSONUtility.parseData(getApplicationContext(), response);

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
//                } else {
//                    // Load favorite movies
//                    Log.d(TAG, "Load FavoriteMovies");
//                   theMovies = loadFavoriteMovies();
//
//                }
                return theMovies;
            }

            @Override
            protected void onStartLoading() {

                super.onStartLoading();
                if (theMovies!=null) {

                    deliverResult(theMovies);

                } else {

                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad(); // loadInBackground
                }
            }

            @Override
            public void deliverResult(Object data) {

                Log.d(TAG,"AsyncTaskLoader:deliverResult");
                // caching the result, so that everytime we do not need to forceload
                if(data != null) {
                    theMovies = (Movie[]) data;
                }

                super.deliverResult(data);

            }

            //            @Override
//            private void deliverResult(Movie[] data) {
//                Log.d(TAG,"AsyncTaskLoader:deliverResult");
//                // cache the result
//                theMovies = data;
//                super.deliverResult(data);
//            }
        };
    }

    /**
     * onLoadFinished - loader's callback method
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(Loader<Movie[]> loader, Movie[] data) {
        Log.d(TAG,"AsyncTaskLoader:onLoadFinished");
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data == null) {
            // show error
            showErrorMessage();
            isErrorOccurs = true;
            Log.e(TAG,"Unable to get Movies data, please check the API KEY!");
        } else {
            // update Adapter's data
            Log.d(TAG,"Update the movie list!");
            mMovieAdapter.updateMovieList(Arrays.asList(data));
            // show the grid
            Log.d(TAG,"Show the poster grid");
            showPosterGrid();
        }

    }


    /**
     * onLoaderReset - loader's callback method
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader loader) {

    }

    /**
     * AsyncTask to get back poster in the other thread
     */
    /*
    private class GetMovieAsyncTask extends AsyncTask<String , Void , Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(ProgressBar.VISIBLE);

        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            // dismiss the progressbar
            mLoadingIndicator.setVisibility(ProgressBar.INVISIBLE);
            if(movies != null) {
                // Show Grid view
                showPosterGrid();
                // update the adapter's movie list
                mMovieAdapter.updateMovieList(Arrays.asList(movies));


            } else {
                // show error message when unable to get the result from the request
                showErrorMessage();
                isErrorOccurs = true;
                Log.e(TAG,"Unable to get Movies data, please check the API KEY!");
            }

        }

        @Override
        protected Movie[] doInBackground(String...params) {
            if(params.length == 0) {
                return null;
            }

            String sortMethod = params[0];
            URL sortMovieURL = MovieNetworkUtility.getMovieURL(sortMethod,null);
            Log.d(TAG,"Movie URL is " + sortMovieURL);
            try {
                String response = MovieNetworkUtility.getResponseFromHttp(sortMovieURL);
                Movie[] theMovies = MovieJSONUtility.parseData(getApplicationContext(),response);
                return theMovies;
            }catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    */

    /**
     * loadMoviesData
     * @param sortType sort by popular or top-rated
     */
    /*
    private void loadMoviesData(String sortType){
        showPosterGrid();
        new GetMovieAsyncTask().execute(sortType);
    }*/

    /**
     * loadFavoriteMovies
     */
    private Movie[] loadFavoriteMovies() {
        showPosterGrid();
        // get Favorite movies from SharedPreference ?
        return null;
    }


    /**
     * loadFavoritesFromSharePreference
     * @param sharedPreferences
     */
    private void loadFavoritesFromSharePreference(SharedPreferences sharedPreferences){


    }
}
