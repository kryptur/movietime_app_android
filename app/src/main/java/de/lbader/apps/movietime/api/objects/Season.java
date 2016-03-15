package de.lbader.apps.movietime.api.objects;

import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.lbader.apps.movietime.R;
import de.lbader.apps.movietime.activities.MainActivity;

public class Season extends BaseObject {
    TvShow tvshow;
    int episodeCount;
    String releaseDate;
    ArrayList<Episode> episodes;

    public Season(TvShow tvShow) {
        this.tvshow = tvShow;
        api_base = "tv/" + tvShow.getId() + "/season/";
    }

    @Override
    public void load(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getInt("season_number");
            this.poster_path = jsonObject.optString("poster_path", "");
            this.episodeCount = jsonObject.optInt("episode_count");
            this.releaseDate = jsonObject.optString("air_date", "");
            this.overview = jsonObject.optString("overview", "");
            this.name = MainActivity.context.getResources().getString(R.string.season) + " " + id;
            if (id == 0) {
                name = MainActivity.context.getResources().getString(R.string.seasonspecials);
            }
            if (jsonObject.has("episodes")) {
                JSONArray eps = jsonObject.getJSONArray("episodes");
                this.episodes = new ArrayList<>();
                for (int i = 0; i < eps.length(); ++i) {
                    Episode episode = new Episode(this);
                    episode.load(eps.getJSONObject(i));
                    episodes.add(episode);
                }
                episodeCount = episodes.size();
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }


    public int getTvshowid() {
        return this.tvshow.getId();
    }

    public int getEpisodeCount() {
        return episodeCount;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public ArrayList<Episode> getEpisodes() {
        return episodes;
    }
}
