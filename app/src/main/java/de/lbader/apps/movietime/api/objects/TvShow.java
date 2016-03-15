package de.lbader.apps.movietime.api.objects;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.lbader.apps.movietime.api.ApiCallback;
import de.lbader.apps.movietime.api.ApiParams;
import de.lbader.apps.movietime.api.SimpleCallback;
import de.lbader.apps.movietime.api.TmdbApi;

public class TvShow extends WatchableObject {
    private List<Genre> genres;
    private String orig_lang;
    private String orig_name;
    private double popularity;
    private List<Country> origin_countries;
    private String air_date;
    private int vote_count;

    public TvShow() {
        api_base = "tv/";
    }

    public void load(JSONObject jsonObject) {
        try {
            this.id         = jsonObject.getInt("id");
            this.overview   = jsonObject.getString("overview");
            this.name       = jsonObject.getString("name");
            this.poster_path = jsonObject.getString("poster_path");
            this.backdrop_path = jsonObject.getString("backdrop_path");
            this.vote_average = jsonObject.getDouble("vote_average");
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
            TmdbApi.call("tv/" + this.id + "/credits", new ApiParams(true), new ApiCallback() {
                @Override
                public void callback(JSONObject result, int responseCode) {
                    try {
                        JSONArray res = result.getJSONArray("cast");
                        for (int i = 0; i < res.length(); ++i) {
                            Cast cast = new Cast();
                            cast.load(res.getJSONObject(i));
                            casts.add(cast);
                        }
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                    cast_loaded = true;
                    callback.callback();
                }
            });
        }
    }
}
