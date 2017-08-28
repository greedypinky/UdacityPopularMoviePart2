package com.project1.popularmovie.data;

/**
 *
 */
public class MovieTrailer {

    private String id;
    private String trailerKey;
    private String trailerName;
    private String site;
    private String size;
    private String type;

    public MovieTrailer(String id, String trailerKey, String trailerName, String site, String size, String type) {
        this.id = id;
        this.trailerKey = trailerKey;
        this.trailerName = trailerName;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public String getSite() {
        return site;
    }

    public String getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
