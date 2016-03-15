package de.lbader.apps.movietime.api.objects;

import org.json.JSONObject;

public abstract class ApiObject {
    public abstract void load(JSONObject jsonObject);
}
