package de.lbader.apps.movietime.api.objects;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import de.lbader.apps.movietime.activities.MainActivity;
import de.lbader.apps.movietime.api.ApiCallback;
import de.lbader.apps.movietime.api.ApiParams;
import de.lbader.apps.movietime.api.SimpleCallback;
import de.lbader.apps.movietime.api.TmdbApi;

public class Movie extends WatchableObject {
    private int imdb_id;
    private boolean adult;
    private int budget;
    private List<Genre> genres;
    private String homepage;
    private String orig_lang;
    private String orig_title;
    private double popularity;
    private List<Company> production_companies;
    private List<Country> production_countries;
    private int revenue;
    private int runtime;
    private String tagline;

    public Movie() {
        api_base = "movie/";
    }

    public void load(JSONObject jsonObject) {
        try {
            this.id         = jsonObject.getInt("id");
            this.adult      = jsonObject.getBoolean("adult");
            this.overview   = jsonObject.getString("overview");
            this.name      = jsonObject.getString("title");
            this.poster_path = jsonObject.getString("poster_path");
            this.backdrop_path = jsonObject.getString("backdrop_path");
            this.vote_average = jsonObject.getDouble("vote_average");
            this.voteCount  = jsonObject.optInt("vote_count", 0);
            this.tagline = jsonObject.optString("tagline", "");
            this.status = jsonObject.optString("status", "");
            this.releaseDate = jsonObject.optString("release_date", "");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public void load(int dbId, ApiCallback callback) {
    }

    @Override
    public void loadCast(final SimpleCallback callback) {
        if (cast_loaded) {
            callback.callback();
        } else {
            TmdbApi.call("movie/" + this.id + "/credits", new ApiParams(true), new ApiCallback() {
                @Override
                public void callback(JSONObject result, int responseCode) {
                    try {
                        JSONArray res = result.getJSONArray("cast");
                        for (int i = 0; i < res.length(); ++i) {
                            Cast cast = new Cast();
                            cast.load(res.getJSONObject(i));
                            casts.add(cast);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    cast_loaded = true;
                    callback.callback();
                }
            });
        }
    }

    public String getTagline() {
        return tagline;
    }
}
