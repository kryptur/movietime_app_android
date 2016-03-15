package de.lbader.apps.movietime.api.objects;

import org.json.JSONException;
import org.json.JSONObject;

import de.lbader.apps.movietime.api.TmdbApi;

public class Cast extends ApiObject {
    private WatchableObject watchableObject;
    private Person person;
    private String character;
    private String name;
    private String profile_path;
    private String poster_path;
    private String type;

    @Override
    public void load(JSONObject jsonObject) {
        try {
            character = jsonObject.optString("character", "");
            if (jsonObject.has("name")) {
                name = jsonObject.getString("name");
                int id = jsonObject.optInt("id");
                profile_path = jsonObject.optString("profile_path", "");
                person = new Person();
                person.manual(id, name, profile_path);
            } else {
                name = jsonObject.optString("title", "");
                type = jsonObject.optString("media_type", "");
                poster_path = jsonObject.optString("poster_path", "");
                int id = jsonObject.optInt("id", 0);
                switch (type) {
                    case "movie":
                        watchableObject = new Movie();
                        watchableObject.setId(id);
                        break;
                    case "tv":
                        watchableObject = new TvShow();
                        watchableObject.setId(id);
                        break;
                    default:
                        break;
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public String getCharacter() {
        return character;
    }

    public String getName() {
        return name;
    }

    public String getPosterPath() {
        return getPosterPath("w185");
    }

    public String getPosterPath(String width) {
        return TmdbApi.config.getImgBaseUri(width) + poster_path;
    }

    public String getProfilePath() {
        return getProfilePath("w185");
    }

    public String getProfilePath(String width) {
        return TmdbApi.config.getImgBaseUri(width) + profile_path;
    }

    public WatchableObject getWatchableObject() {
        return watchableObject;
    }

    public Person getPerson() {
        return person;
    }


}
