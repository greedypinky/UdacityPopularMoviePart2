package com.project1.popularmovie.data;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ritalaw on 2017-08-20.
 */

public class MovieTrailerJSONUtility {
    //http://www.youtube.com/watch?v=jc86EFjLFV4"

    //    {"id":211672,"results":[{"id":"571b76d8c3a36864e00025a0","iso_639_1":"en","iso_3166_1":"US","key":"jc86EFjLFV4","name":"Official Trailer 2","site":"YouTube","size":1080,"type":"Trailer"}
    private static final String RESULTS = "results";
    private static final String ID = "id";
    private static final String ISO_639_1 = "iso_639_1";
    private static final String ISO_3166_1 = "iso_3166_1";
    private static final String KEY = "key";
    private static final String TRAILER_NAME = "Official Trailer 2";
    private static final String TRAILER_SITE = "site"; //YouTube
    private static final String TRAILER_SIZE = "size"; // 1080
    private static final String TYPE = "type"; //Trailer
    private static final List<String> trailerKeys = new ArrayList<String>();

    public static String[] parseTrailerData(Context context, String jsonData) throws JSONException{

        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray resultsArray = jsonObject.getJSONArray(RESULTS);

        for(int i = 0; i < resultsArray.length(); i++){

            JSONObject result = (JSONObject) resultsArray.get(i);
            if(result!=null) {
                String id = result.getString(ID);
                String trailerKey = result.getString(KEY);
                String trailerName = result.getString(TRAILER_NAME);
                String site = result.getString(TRAILER_SITE);
                String size = result.getString(TRAILER_SIZE);
                String type = result.getString(TYPE);
                trailerKeys.add(trailerKey);
            }


        }

        return trailerKeys.toArray(new String[resultsArray.length()]);
    }


}
