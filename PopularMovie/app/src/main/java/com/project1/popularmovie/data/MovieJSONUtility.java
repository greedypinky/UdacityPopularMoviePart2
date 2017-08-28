package com.project1.popularmovie.data;

import android.content.Context;
import android.util.Log;

import com.project1.popularmovie.data.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//import static java.time.LocalDate.*;

/**
 * parse the information into a movie object and store it into an array
 */
public class MovieJSONUtility {

    private static final String TAG = MovieJSONUtility.class.getSimpleName();
    private static final String JSON_RESULTS = "results";
    private static final String STATUS_CODE = "status_code";
    private static final String STATUS_MESSAGE = "status_message";

    // Information that we need to retrieve from each movie
    private static String id_param = "id";
    private static String vote_average_param = "vote_average";
    private static String title_param = "title";
    private static String popularity_param = "popularity";
    private static String poster_path_param = "poster_path";
    private static String original_title_param = "original_title";
    private static String overview_param = "overview";
    private static String releaseDate_param = "release_date";

    public static Movie[] parseData(Context context, String movieJson) throws JSONException {

        JSONObject movieJSONObject =  new JSONObject(movieJson);

        if(movieJSONObject.has(STATUS_CODE) && movieJSONObject.has(STATUS_MESSAGE)){

            String statusCode = (String) movieJSONObject.getString(STATUS_CODE);
            String statusMessage = (String) movieJSONObject.getString(STATUS_MESSAGE);
            Log.d(TAG,"StatusCode:" + statusCode );
            Log.d(TAG,"StatusMessage:" + statusMessage);
            return null;
        }

        JSONArray movieResults = movieJSONObject.getJSONArray(JSON_RESULTS);
        Movie[] mMovies = null;
        mMovies = new Movie[movieResults.length()];

        for (int i=0; i < movieResults.length() ; i++) {
            String id;
            String title;
            String overview;
            String original_title;
            String vote_average;
            String poster_path;
            String releaseDate;
            String releaseYear = "";

            // what kind of parameter do we need to get?
            /* get the parameter and store into the movie object
            id	:	19404

            video	:		false

            vote_average	:	9

            title	:	Dilwale Dulhania Le Jayenge

            popularity	:	4.562736

            poster_path	:	/2gvbZMtV1Zsl7FedJa5ysbpBx2G.jpg

            original_language	:	hi

            original_title	:	Dilwale Dulhania Le Jayenge
            overview	:	Raj is a rich, carefree, happy-go-lucky second generation NRI. Simran is the daughter of Chaudhary Baldev Singh, who in spite of being an NRI is very strict about adherence to Indian values. Simran has left for India to be married to her childhood fiancé. Raj leaves for India with a mission at his hands, to claim his lady love under the noses of her whole family. Thus begins a saga.
		    release_date	:	1995-10-20

            */

            JSONObject movieResult = (JSONObject) movieResults.get(i);
            id = movieResult.getString(id_param);
            title = movieResult.getString(title_param);
            original_title = movieResult.getString(original_title_param);
            overview = movieResult.getString(overview_param);
            vote_average = movieResult.getString(vote_average_param);
            poster_path = movieResult.getString(poster_path_param);
            releaseDate = movieResult.getString(releaseDate_param);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");

            //int releaseYear = LocalTime.parse(dateFormat.format(new Date(releaseDate)));
            // dateFormat.parse(movieResult.getString(releaseDate_param));
            Movie theMovie;
            if(original_title!=null) {
                theMovie = new Movie(id, original_title, overview, vote_average, releaseDate, poster_path);
            } else {
                theMovie = new Movie(id, title, overview, vote_average, releaseDate, poster_path);
            }
            mMovies[i] = theMovie;
        }

        return mMovies;
    }



}
