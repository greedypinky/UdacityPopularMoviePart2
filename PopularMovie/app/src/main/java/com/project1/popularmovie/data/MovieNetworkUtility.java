package com.project1.popularmovie.data;
import android.net.Uri;
import android.util.Log;

import com.project1.popularmovie.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


/**
 * MovieNetworkUtility
 */
public class MovieNetworkUtility {
   // TODO: Please replace your own MOVIEDB's API KEY
   private final static String V3_API_KEY="a232d2749d97ef5370730e47205b19cf";
   private final static String TAG = MovieNetworkUtility.class.getSimpleName();
  //TODO: To fetch trailers you will want to make a request to the /movie/{id}/videos endpoint.
  //TODO:  To fetch reviews you will want to make a request to the /movie/{id}/reviews endpoint
   private final static String MOVIE_VIDEOS_URL = "http://api.themoviedb.org/3/movie/%ID%/videos";
   private final static String MOVIE_REVIEWS_URL = "http://api.themoviedb.org/3/movie/%ID%/reviews";
   private final static String POPULAR_MOVIE_BASE_URI = "http://api.themoviedb.org/3/movie/popular";
   private final static String TOP_RATED_MOVIE_BASE_URI = "http://api.themoviedb.org/3/movie/top_rated";
   private final static String API_KEY_PARAM = "api_key"; // API_KEY parameter
   private final static String DELIMITER = "//A";
   public final static String POPULAR = "popular";
   public final static String TOP_RATED = "top_rated";
    public final static String VIDEOS = "videos";
    public final static String REVIEWS = "review";

   /**
    * getMovieVideosURL
    * @return URL URL for movie videos
    */
   public static URL getMovieVideosURL(String movieID,String type){
       // http://api.themoviedb.org/3/211672/a232d2749d97ef5370730e47205b19cf/videos?api_key=a232d2749d97ef5370730e47205b19cf
      Uri.Builder builder = Uri.parse(MOVIE_VIDEOS_URL.replace("%ID%", movieID)).buildUpon();
      URL moviesURL = null;

      try {
         moviesURL = new URL(builder.build().toString());
      } catch(MalformedURLException e) {
        e.printStackTrace();
        Log.e(TAG,e.toString());
      }
      return moviesURL;
   }

   /**
    * getMovieReviewsURL
    * @return URL URL for movie reviews
    */
   //http://api.themoviedb.org/3/movie/popular?api_key=a232d2749d97ef5370730e47205b19cf
   // for eg: Minion - 211672
   // http://api.themoviedb.org/3/movie/211672/videos?api_key=a232d2749d97ef5370730e47205b19cf
   // http://api.themoviedb.org/3/movie/211672/reviews?api_key=a232d2749d97ef5370730e47205b19cf
   public static URL getMovieReviewsURL(String movieID,String type) {

       Uri.Builder builder = null;
       URL moviesURL = null;
       switch(type) {

           case VIDEOS:
               builder = Uri.parse(MOVIE_VIDEOS_URL.replace("%ID%", movieID)).buildUpon();
                   break;
           case REVIEWS:
               builder = Uri.parse(MOVIE_REVIEWS_URL.replace("%ID%", movieID)).buildUpon();
               break;
       }

       Uri movieUri = builder.appendQueryParameter(API_KEY_PARAM, V3_API_KEY).build();

      try {
         moviesURL = new URL(movieUri.toString());
      } catch(MalformedURLException e) {
         e.printStackTrace();
         Log.e(TAG,e.toString());
      }
      return moviesURL;

   }


   /**
    * getMovieURL - build the URL by Uri.Builder
    * @param sortType
    * @return URL URL of Popular movies or Top-rated movies
    */
   public static URL getMovieURL(String sortType,String movieID) {
      Uri.Builder builder = null;
      Uri movieUri = null;
      URL movieURL = null;

         switch(sortType) {
             case POPULAR:
               builder = Uri.parse(POPULAR_MOVIE_BASE_URI).buildUpon();
               Log.d(TAG,"Popular URL:" + builder.toString());
               break;

            case TOP_RATED:
               builder = Uri.parse(TOP_RATED_MOVIE_BASE_URI).buildUpon();
               Log.d(TAG,"Top-Rated URL:" + builder.toString());
               break;

             case VIDEOS:
                 builder = Uri.parse(MOVIE_VIDEOS_URL.replace("%ID%", movieID)).buildUpon();
                 break;
             case REVIEWS:
                 builder = Uri.parse(MOVIE_REVIEWS_URL.replace("%ID%", movieID)).buildUpon();
                 break;

            default:
               break;
         }

         movieUri = builder.appendQueryParameter(API_KEY_PARAM, V3_API_KEY).build();

      try {
         movieURL = new URL(movieUri.toString());
      }catch (MalformedURLException e) {
          e.printStackTrace();
          Log.e(TAG, e.toString());
      }
      return movieURL;
   }

   /**
    * gerRespsonseFromHttp
    * @param url
    * @return response
    * @throws IOException
    */
   public static String getResponseFromHttp(URL url) throws IOException{

      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      InputStream responseInputStream = conn.getInputStream();
      String response = null;

      try {

         Scanner scanner = new Scanner(responseInputStream);
         scanner.useDelimiter(DELIMITER);
         if(scanner.hasNext()) {
            response = scanner.next();
            return response;
         } else {
            return null;
         }

      } finally {
         conn.disconnect();
      }

   }


}
