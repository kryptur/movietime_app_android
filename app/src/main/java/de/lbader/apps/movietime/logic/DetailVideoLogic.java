package de.lbader.apps.movietime.logic;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import de.lbader.apps.movietime.R;
import de.lbader.apps.movietime.adapters.RecylcerCastObjectAdapter;
import de.lbader.apps.movietime.adapters.RecylcerVideoObjectAdapter;
import de.lbader.apps.movietime.api.SimpleCallback;
import de.lbader.apps.movietime.api.objects.Cast;
import de.lbader.apps.movietime.api.objects.Video;
import de.lbader.apps.movietime.api.objects.WatchableObject;
import de.lbader.apps.movietime.fragments.FragmentHolderLogic;
import de.lbader.apps.movietime.viewfactories.ItemOffsetDecoration;

public class DetailVideoLogic implements FragmentHolderLogic {
    private WatchableObject watchableObject;
    RecyclerView recyclerView;
    RecylcerVideoObjectAdapter adapter;

    public DetailVideoLogic(WatchableObject watchableObject) {
        this.watchableObject = watchableObject;
    }

    @Override
    public void init(Context context, View view, Bundle args) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new ItemOffsetDecoration(context, R.dimen.cardview_spacing));

        if (adapter == null) {
            adapter = new RecylcerVideoObjectAdapter(context, R.dimen.cardview_spacing);
            watchableObject.loadVideos(new SimpleCallback() {
                @Override
                public void callback() {
                    initAdapter();
                }
            });
        } else {
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void initAdapter() {
        for (Video video : watchableObject.getVideos()) {
            adapter.addItem(video);
        }
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
