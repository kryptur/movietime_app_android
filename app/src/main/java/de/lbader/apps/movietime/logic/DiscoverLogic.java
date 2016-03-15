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
import de.lbader.apps.movietime.fragments.FragmentHolderLogic;
import de.lbader.apps.movietime.viewfactories.ItemOffsetDecoration;

public class DiscoverLogic<E extends BaseObject> implements FragmentHolderLogic {
    private Context context;
    private RecyclerView recyclerView;
    private int page = 1;
    private int elementCount = 0;
    private String path;
    private int recyclerId;
    private RecylcerBaseObjectAdapter adapter;

    private Class<E> mClass;

    public DiscoverLogic(Class<E> cls, String path, int recyclerId) {
        this.path = path;
        this.recyclerId = recyclerId;
        mClass = cls;
    }

    @Override
    public void init(Context context, View view, Bundle args) {
        this.context = context;

        recyclerView = (RecyclerView) view.findViewById(recyclerId);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new ItemOffsetDecoration(context, R.dimen.cardview_spacing));


        if (adapter == null) {
            ApiParams params = new ApiParams(true);
            params.addParam("page", "" + page);
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
                    E discover = mClass.newInstance();
                    discover.load(elem);
                    adapter.addItem(discover);
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
