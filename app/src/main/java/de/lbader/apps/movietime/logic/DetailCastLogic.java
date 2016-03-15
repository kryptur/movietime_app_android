package de.lbader.apps.movietime.logic;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONObject;

import de.lbader.apps.movietime.R;
import de.lbader.apps.movietime.adapters.RecylcerCastObjectAdapter;
import de.lbader.apps.movietime.api.ApiCallback;
import de.lbader.apps.movietime.api.ApiParams;
import de.lbader.apps.movietime.api.SimpleCallback;
import de.lbader.apps.movietime.api.TmdbApi;
import de.lbader.apps.movietime.api.objects.Cast;
import de.lbader.apps.movietime.api.objects.Person;
import de.lbader.apps.movietime.api.objects.WatchableObject;
import de.lbader.apps.movietime.fragments.FragmentHolderLogic;
import de.lbader.apps.movietime.viewfactories.ItemOffsetDecoration;

public class DetailCastLogic implements FragmentHolderLogic {
    private WatchableObject watchableObject;
    private Person person;
    RecyclerView recyclerView;
    RecylcerCastObjectAdapter adapter;
    private boolean isPersonCast;

    public DetailCastLogic(WatchableObject watchableObject) {
        this.watchableObject = watchableObject;
        this.isPersonCast = false;
    }

    public DetailCastLogic(Person person) {
        this.person = person;
        this.isPersonCast = true;
    }

    @Override
    public void init(Context context, View view, Bundle args) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new ItemOffsetDecoration(context, R.dimen.cardview_spacing));

        if (adapter == null) {
            adapter = new RecylcerCastObjectAdapter(context, R.dimen.cardview_spacing, isPersonCast);
            if (isPersonCast) {
                person.loadAll(new SimpleCallback() {
                    @Override
                    public void callback() {
                        initAdapter();
                    }
                });
            } else {
                watchableObject.loadCast(new SimpleCallback() {
                    @Override
                    public void callback() {
                        initAdapter();
                    }
                });
            }
        } else {
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void initAdapter() {
        if (isPersonCast) {
            for (Cast cast : person.getCasts()) {
                adapter.addItem(cast);
            }
        } else {
            for (Cast cast : watchableObject.getCasts()) {
                adapter.addItem(cast);
            }
        }
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
