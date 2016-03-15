package de.lbader.apps.movietime.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.lbader.apps.movietime.R;
import de.lbader.apps.movietime.api.objects.Movie;
import de.lbader.apps.movietime.logic.DiscoverLogic;
import de.lbader.apps.movietime.logic.SearchLogic;
import de.lbader.apps.movietime.navigation.Navigation;
import de.lbader.apps.movietime.toolbar.ToolbarManager;

public class SearchFragment extends Fragment {
    private static SearchLogic logic;

    public SearchFragment() {
        if (logic == null) {
            logic = new SearchLogic("search/multi", R.id.base_recylcerview);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (logic == null) {
            logic = new SearchLogic("search/multi", R.id.base_recylcerview);
        }

        ToolbarManager.setToolbar(R.layout.toolbar_search, "");
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        final SearchView searchView = (SearchView) ToolbarManager.coordinatorLayout().findViewById(R.id.searchview);
        searchView.setQuery(logic.getSearch(), false);
        searchView.setQueryHint(getString(R.string.menutitle_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Navigation.instance.hideKeyboard();
                logic.updateSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    logic.clearSearch();
                    return true;
                } else {
                    return false;
                }
            }
        });
        logic.init(getContext(), view, new Bundle());
        return view;
    }
}
