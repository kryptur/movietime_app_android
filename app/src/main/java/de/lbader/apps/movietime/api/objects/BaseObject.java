package de.lbader.apps.movietime.api.objects;

import android.net.Uri;

import org.json.JSONObject;

import de.lbader.apps.movietime.api.TmdbApi;

/**
 * Movies, Series, Persons
 */
public abstract class BaseObject extends ApiObject {
    protected String name;
    protected String poster_path;
    protected String overview;
    protected double vote_average;
    protected int id;

    public BaseObject() {

    }

    public Uri getPosterUri() {
        return getPosterUri("w185");
    }

    public Uri getPosterUri(String width) {
        return Uri.parse(
                TmdbApi.config.getImgBaseUri(width) + this.poster_path);
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public double getVote_average() {
        return vote_average;
    }

    public int getId() {
        return id;
    }
}
