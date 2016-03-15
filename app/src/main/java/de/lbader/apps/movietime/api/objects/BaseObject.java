package de.lbader.apps.movietime.api.objects;

import android.net.Uri;

import org.json.JSONObject;

import java.net.HttpURLConnection;

import de.lbader.apps.movietime.api.ApiCallback;
import de.lbader.apps.movietime.api.ApiParams;
import de.lbader.apps.movietime.api.SimpleCallback;
import de.lbader.apps.movietime.api.TmdbApi;

/**
 * Movies, Series, Persons
 */
public abstract class BaseObject extends ApiObject {
    protected String name;
    protected String poster_path;
    protected String overview;
    protected double vote_average;
    protected boolean loaded = false;
    protected int id;

    protected String api_base = "";

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

    public void load(final SimpleCallback callback) {
        if (loaded) {
            callback.callback();
        } else {
            TmdbApi.call(api_base + id, new ApiParams(true), new ApiCallback() {
                @Override
                public void callback(JSONObject result, int responseCode) {
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        load(result);
                        loaded = true;
                        callback.callback();
                    }
                }
            });
        }
    }
}
