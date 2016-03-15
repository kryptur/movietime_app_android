package de.lbader.apps.movietime.logic;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import de.lbader.apps.movietime.R;
import de.lbader.apps.movietime.adapters.RecylcerCastObjectAdapter;
import de.lbader.apps.movietime.adapters.RecylcerSeasonObjectAdapter;
import de.lbader.apps.movietime.api.SimpleCallback;
import de.lbader.apps.movietime.api.objects.Cast;
import de.lbader.apps.movietime.api.objects.Person;
import de.lbader.apps.movietime.api.objects.Season;
import de.lbader.apps.movietime.api.objects.TvShow;
import de.lbader.apps.movietime.api.objects.WatchableObject;
import de.lbader.apps.movietime.fragments.FragmentHolderLogic;
import de.lbader.apps.movietime.viewfactories.ItemOffsetDecoration;

public class DetailSeasonLogic implements FragmentHolderLogic {
    private TvShow tvShow;
    RecyclerView recyclerView;
    RecylcerSeasonObjectAdapter adapter;

    public DetailSeasonLogic(TvShow tvShow) {
        this.tvShow = tvShow;
    }

    @Override
    public void init(Context context, View view, Bundle args) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new ItemOffsetDecoration(context, R.dimen.cardview_spacing));

        if (adapter == null) {
            adapter = new RecylcerSeasonObjectAdapter(context, 4);
            tvShow.load(new SimpleCallback() {
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
        for (Season season : tvShow.getSeasons()) {
            adapter.addItem(season);
        }
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
