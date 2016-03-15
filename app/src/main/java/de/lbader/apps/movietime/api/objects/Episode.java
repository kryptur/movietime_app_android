package de.lbader.apps.movietime.api.objects;

import org.json.JSONException;
import org.json.JSONObject;

public class Episode extends BaseObject {
    private String releaseDate;
    private int voteCount;
    private Season season;

    public Episode(Season season) {
        this.season = season;
        loaded = true;
        api_base = "/tv/" + season.getTvshowid() + "/season/" + season.getId() + "/episode/";
    }

    @Override
    public void load(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("episode_number");
            name = jsonObject.getString("name");
            overview = jsonObject.getString("overview");
            poster_path = jsonObject.getString("still_path");
            vote_average = jsonObject.optDouble("vote_average");
            voteCount = jsonObject.optInt("vote_count");
            releaseDate = jsonObject.optString("air_date");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getVoteCount() {
        return voteCount;
    }
}
