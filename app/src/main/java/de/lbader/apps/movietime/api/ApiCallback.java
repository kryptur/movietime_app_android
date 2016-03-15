package de.lbader.apps.movietime.api;

import org.json.JSONObject;

public interface ApiCallback {
    void callback(JSONObject result, int responseCode);
}
