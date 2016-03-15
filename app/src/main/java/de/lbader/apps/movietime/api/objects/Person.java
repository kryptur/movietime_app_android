package de.lbader.apps.movietime.api.objects;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.concurrent.Exchanger;

import de.lbader.apps.movietime.api.ApiCallback;
import de.lbader.apps.movietime.api.ApiParams;
import de.lbader.apps.movietime.api.SimpleCallback;
import de.lbader.apps.movietime.api.TmdbApi;

public class Person extends BaseObject {
    private String birthday;
    private int id;
    private String biography;
    private ArrayList<Cast> casts = new ArrayList<>();
    private String birth;

    private boolean loaded = false;

    @Override
    public Uri getPosterUri() {
        return super.getPosterUri("w185");
    }

    @Override
    public void load(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            name = jsonObject.getString("name");
            poster_path = jsonObject.getString("profile_path");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public void manual(int id, String name, String poster) {
        this.id = id;
        this.name = name;
        this.poster_path = poster;
    }

    public void loadAll(final SimpleCallback callback) {
        if (loaded) {
            callback.callback();
        } else {
            TmdbApi.call("person/" + id, new ApiParams("append_to_response", "combined_credits").addLanguage(), new ApiCallback() {
                @Override
                public void callback(JSONObject result, int responseCode) {
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        try {
                            biography = result.optString("biography", "");
                            birthday = result.optString("birthday", "");
                            birth = result.optString("place_of_birth", "");
                            JSONArray jsonCasts = result.getJSONObject("combined_credits").getJSONArray("cast");
                            for (int i = 0; i < jsonCasts.length(); ++i) {
                                Cast cast = new Cast();
                                cast.load(jsonCasts.getJSONObject(i));
                                casts.add(cast);
                            }
                            callback.callback();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public String getBiography() {
        return biography.equals("null") ? "" : biography;
    }

    public ArrayList<Cast> getCasts() {
        return casts;
    }

    public String getBirth() {
        return birth.equals("null") ? "" : birth;
    }

    public String getBirthday() {
        return birthday.equals("null") ? "" : birthday;
    }
}
