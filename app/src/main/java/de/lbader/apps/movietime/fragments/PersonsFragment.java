package de.lbader.apps.movietime.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.lbader.apps.movietime.R;
import de.lbader.apps.movietime.adapters.FragmentHolder;
import de.lbader.apps.movietime.adapters.ViewPagerAdapter;
import de.lbader.apps.movietime.api.objects.Person;
import de.lbader.apps.movietime.logic.DiscoverLogic;
import de.lbader.apps.movietime.toolbar.ToolbarManager;

public class PersonsFragment extends Fragment {
    private static DiscoverLogic<Person> logic;

    public PersonsFragment() {
        if (logic == null) {
            logic = new DiscoverLogic<>(Person.class, "person/popular", R.id.base_recylcerview);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ToolbarManager.setToolbar(R.layout.toolbar_start, getResources().getString(R.string.menutitle_persons));
        View view = inflater.inflate(R.layout.fragment_persons, container, false);
        logic.init(getContext(), view, new Bundle());
        return view;
    }
}
