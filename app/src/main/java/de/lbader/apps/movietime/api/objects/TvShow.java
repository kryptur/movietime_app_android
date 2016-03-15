package de.lbader.apps.movietime.api.objects;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.lbader.apps.movietime.api.ApiCallback;
import de.lbader.apps.movietime.api.ApiParams;
import de.lbader.apps.movietime.api.SimpleCallback;
import de.lbader.apps.movietime.api.TmdbApi;

public class TvShow extends WatchableObject {
    private ArrayList<Season> seasons;

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
            this.status = jsonObject.optString("status", "");
            this.releaseDate = jsonObject.optString("first_air_date", "");
            if (jsonObject.has("seasons")) {
                JSONArray seas = jsonObject.getJSONArray("seasons");
                seasons = new ArrayList<>();
                for (int i = 0; i < seas.length(); ++i) {
                    Season season = new Season(this);
                    season.load(seas.getJSONObject(i));
                    seasons.add(season);
                }
            }
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

    public ArrayList<Season> getSeasons() {
        return seasons;
    }
}
