package de.lbader.apps.movietime.api;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.BuildConfig;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Map;

public class TmdbApi {
    private static String apiKey = de.lbader.apps.movietime.BuildConfig.API_KEY;
    public static ApiConfig config;
    private static ProgressBar loadingBar;

    public static void init(ProgressBar progressBar) {
        loadingBar = progressBar;
        loadingBar.setVisibility(View.GONE);
        config = new ApiConfig();
        call("configuration", new ApiCallback() {
            @Override
            public void callback(JSONObject result, int status) {
                if (status == HttpURLConnection.HTTP_OK) {
                    config.loadConfig(result);
                }
            }
        });
    }

    public static void call(String apiFunction, ApiParams params, ApiCallback callback) {
        if (params == null) {
            callback.callback(null, -1);
        } else {
            params.addParam("api_key", apiKey);
            new TmdbApi.RequestTask(apiFunction, params, callback).execute("");
        }
    }

    public static void call(String apiFunction, ApiCallback callback) {
        TmdbApi.call(apiFunction, new ApiParams(), callback);
    }


    private static class RequestTask extends AsyncTask<String, String, String> {
        private String path;
        private ApiParams args;
        private ApiCallback callback;
        private static int activeCalls = 0;

        private String baseUrl = "http://api.themoviedb.org/3/";

        private int responseCode = -1;

        private RequestTask(String path, ApiParams params, ApiCallback callback) {
            this.path = path;
            this.args = params;
            this.callback = callback;
            loadingBar.setVisibility(View.VISIBLE);
        }

        private String getPostDataString() throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> entry : args.getMap().entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
        }

        @Override
        protected String doInBackground(String... useless) {
            activeCalls++;
            try {
                URL url = new URL(baseUrl + path + "?" + getPostDataString());
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setReadTimeout(5000);
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("GET");
                responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String response = "";
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                    return response;
                } else {
                    return null;
                }

            } catch (MalformedURLException ex) {
                return null;
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            activeCalls--;
            if (activeCalls <= 0) {
                loadingBar.setVisibility(View.GONE);
            }

            if (result == null) {
                try {
                    JSONObject obj = new JSONObject("");
                    callback.callback(obj, responseCode);
                } catch (Exception ex) {
                    callback.callback(null, responseCode);
                }
            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    callback.callback(obj, responseCode);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    callback.callback(null, responseCode);
                }
            }
        }
    }
}
