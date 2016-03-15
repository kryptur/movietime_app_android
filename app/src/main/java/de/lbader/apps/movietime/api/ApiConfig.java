package de.lbader.apps.movietime.api;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiConfig {
    private String imgBaseUri = "http://image.tmdb.org/t/p/";

    public String getImgBaseUri() {
        return getImgBaseUri("original");
    }

    public String getImgBaseUri(String width) {
        return imgBaseUri + width;
    }

    protected void loadConfig(JSONObject config) {
        try {
            imgBaseUri = config.getJSONObject("images").getString("base_url");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
}
