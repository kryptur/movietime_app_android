package de.lbader.apps.movietime.api.objects;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import de.lbader.apps.movietime.api.ApiCallback;
import de.lbader.apps.movietime.api.ApiParams;
import de.lbader.apps.movietime.api.SimpleCallback;
import de.lbader.apps.movietime.api.TmdbApi;

/**
 * Movies, TV Shows
 */
public abstract class WatchableObject extends BaseObject {
    protected String backdrop_path;
    protected boolean cast_loaded;
    protected boolean video_loaded;
    protected ArrayList<Cast> casts = new ArrayList<>();
    protected ArrayList<Video> videos = new ArrayList<>();
    protected long voteCount;
    protected String status;
    protected String releaseDate;

    public void setId(int id) {
        this.id = id;
        loaded = false;
    }

    public Uri getBackdropUri() {
        return getBackdropUri("w500");
    }

    public Uri getBackdropUri(String width) {
        return Uri.parse(
                TmdbApi.config.getImgBaseUri(width) + this.backdrop_path);
    }

    public abstract void loadCast(SimpleCallback callback);

    public void loadVideos(final SimpleCallback callback) {
        if (video_loaded) {
            callback.callback();
        } else {
            TmdbApi.call(api_base + id + "/videos", new ApiParams(true), new ApiCallback() {
                @Override
                public void callback(JSONObject result, int responseCode) {
                    try {
                        JSONArray res = result.getJSONArray("results");
                        for (int i = 0; i < res.length(); ++i) {
                            Video video = new Video();
                            video.load(res.getJSONObject(i));
                            videos.add(video);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    video_loaded = true;
                    callback.callback();
                }
            });
        }
    }

    public boolean isCastLoaded() {
        return cast_loaded;
    }

    public boolean isVideoLoaded() {
        return video_loaded;
    }

    public ArrayList<Cast> getCasts() {
        return casts;
    }

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public String getStatus() {
        return status;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
