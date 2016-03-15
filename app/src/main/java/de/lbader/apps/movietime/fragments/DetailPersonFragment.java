package de.lbader.apps.movietime.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.lbader.apps.movietime.R;
import de.lbader.apps.movietime.adapters.FragmentHolder;
import de.lbader.apps.movietime.adapters.ViewPagerAdapter;
import de.lbader.apps.movietime.api.objects.Movie;
import de.lbader.apps.movietime.api.objects.Person;
import de.lbader.apps.movietime.api.objects.TvShow;
import de.lbader.apps.movietime.logic.DetailCastLogic;
import de.lbader.apps.movietime.logic.DetailPersonLogic;
import de.lbader.apps.movietime.logic.DiscoverLogic;
import de.lbader.apps.movietime.toolbar.ToolbarManager;

public class DetailPersonFragment extends Fragment {

    private ViewPager viewPager;
    private View detailView;
    private Toolbar toolbar;
    private TabLayout tabLayout;

    private Person person;

    private DetailPersonLogic mainLogic;
    private DetailCastLogic castLogic;

    private static DetailPersonFragment instance;

    public DetailPersonFragment() {
        // Required empty public constructor
    }

    public static DetailPersonFragment newInstance(Person person) {
        DetailPersonFragment fragment = new DetailPersonFragment();
        fragment.person = person;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ToolbarManager.setToolbar(R.layout.toolbar_discover,
                person.getName());

        detailView = inflater.inflate(R.layout.fragment_detail_person, container, false);

        createLogics();

        return detailView;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager = (ViewPager) detailView.findViewById(R.id.viewpager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this.getChildFragmentManager());

        viewPagerAdapter.addFragment(
                FragmentHolder.newInstance(
                        R.layout.fragment_detail_person_main,
                        mainLogic
                ),
                getResources().getString(R.string.detail_title_main)
        );
        viewPagerAdapter.addFragment(
                FragmentHolder.newInstance(
                        R.layout.fragment_detail_person_cast,
                        castLogic
                ),
                getResources().getString(R.string.detail_title_roles)
        );
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout = (TabLayout) ToolbarManager.coordinatorLayout().findViewById(R.id.tablayout_discover);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void createLogics() {
        mainLogic = new DetailPersonLogic(person);
        castLogic = new DetailCastLogic(person);
    }
}
