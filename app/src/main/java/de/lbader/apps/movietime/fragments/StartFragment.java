package de.lbader.apps.movietime.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.lbader.apps.movietime.R;
import de.lbader.apps.movietime.api.objects.Movie;
import de.lbader.apps.movietime.logic.DiscoverLogic;
import de.lbader.apps.movietime.toolbar.ToolbarManager;

public class StartFragment extends Fragment {
    private static DiscoverLogic<Movie> logic;

    public StartFragment() {
        if (logic == null) {
            logic = new DiscoverLogic<>(Movie.class, "movie/upcoming", R.id.base_recylcerview);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ToolbarManager.setToolbar(R.layout.toolbar_start, getResources().getString(R.string.menutitle_start));
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        logic.init(getContext(), view, new Bundle());
        return view;
    }
}
