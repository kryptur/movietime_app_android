package de.lbader.apps.movietime.api.objects;

import android.util.Log;

import org.json.JSONObject;

/**
 * Trailer...
 */
public class Video extends ApiObject {
    private String site;
    private String language;
    private String type;
    private String name;
    private String key;
    private int size;


    @Override
    public void load(JSONObject jsonObject) {
        try {
            site = jsonObject.getString("site");
            language = jsonObject.getString("iso_639_1");
            size = jsonObject.getInt("size");
            type = jsonObject.getString("type");
            name = jsonObject.getString("name");
            key = jsonObject.getString("key");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getSite() {
        return site;
    }

    public String getLanguage() {
        return language;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public int getSize() {
        return size;
    }

    public String getPreviewPath() {
        return "http://img.youtube.com/vi/" + key + "/sddefault.jpg";
    }

    public String getUrl() {
        return "http://youtube.com/watch?v=" + key;
    }
}

