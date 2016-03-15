package de.lbader.apps.movietime.logic;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import de.lbader.apps.movietime.R;
import de.lbader.apps.movietime.adapters.RecylcerBaseObjectAdapter;
import de.lbader.apps.movietime.api.ApiCallback;
import de.lbader.apps.movietime.api.ApiParams;
import de.lbader.apps.movietime.api.TmdbApi;
import de.lbader.apps.movietime.api.objects.BaseObject;
import de.lbader.apps.movietime.api.objects.Movie;
import de.lbader.apps.movietime.api.objects.Person;
import de.lbader.apps.movietime.api.objects.TvShow;
import de.lbader.apps.movietime.fragments.FragmentHolderLogic;
import de.lbader.apps.movietime.viewfactories.ItemOffsetDecoration;

public class SearchLogic implements FragmentHolderLogic {
    private Context context;
    private RecyclerView recyclerView;
    private int page = 1;
    private int elementCount = 0;
    private String path;
    private int recyclerId;
    private RecylcerBaseObjectAdapter adapter;

    private String search;

    public SearchLogic(String path, int recyclerId) {
        this.path = path;
        this.recyclerId = recyclerId;
        this.search = "";
    }

    public void clearSearch() {
        this.search = "";
        page = 1;
        adapter = null;
        elementCount = 0;
        recyclerView.setAdapter(new RecylcerBaseObjectAdapter(context, 4));
    }

    public void updateSearch(String search) {
        clearSearch();
        this.search = search;
        load();
    }

    @Override
    public void init(Context context, View view, Bundle args) {
        this.context = context;

        recyclerView = (RecyclerView) view.findViewById(recyclerId);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new ItemOffsetDecoration(context, R.dimen.cardview_spacing));

        load();
    }

    private void load() {
        if (search.length() == 0) {
            return;
        }

        if (adapter == null) {
            ApiParams params = new ApiParams(true);
            params.addParam("page", "" + page);
            params.addParam("query", search);
            TmdbApi.call(path, params, new ApiCallback() {
                @Override
                public void callback(JSONObject result, int status) {
                    setup(result, status);
                }
            });
        } else {
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public String getSearch() {
        return search;
    }

    private void setup(JSONObject jsonObject, int responseCode) {
        adapter = new RecylcerBaseObjectAdapter(context, 4);

        adapter.setBaseObjectAdapterEvents(new RecylcerBaseObjectAdapter.BaseObjectAdapterEvents() {
            @Override
            public void onEndReached(ArrayList<BaseObject> elements) {
                if (elementCount < elements.size()) {
                    // Try loading more content
                    elementCount = elements.size();
                    page++;
                    ApiParams params = new ApiParams(true);
                    params.addParam("page", "" + page);
                    params.addParam("query", search);
                    TmdbApi.call(path, params, new ApiCallback() {
                        @Override
                        public void callback(JSONObject result, int status) {
                            addResults(result, status);
                        }
                    });
                }
            }
        });
        recyclerView.setAdapter(adapter);

        addResults(jsonObject, responseCode);
    }

    private void addResults(JSONObject jsonObject, int responseCode) {
        RecylcerBaseObjectAdapter adapter = (RecylcerBaseObjectAdapter) recyclerView.getAdapter();
        try {
            if (responseCode == HttpURLConnection.HTTP_OK) {
                JSONArray elems = jsonObject.getJSONArray("results");
                for (int i = 0; i < elems.length(); ++i) {
                    JSONObject elem = elems.getJSONObject(i);
                    BaseObject baseObject;
                    switch (elem.optString("media_type")) {
                        default:
                        case "movie":
                            baseObject = new Movie();
                            break;
                        case "tv":
                            baseObject = new TvShow();
                            break;
                        case "person":
                            baseObject = new Person();
                            break;
                    }
                    baseObject.load(elem);
                    adapter.addItem(baseObject);
                }
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
