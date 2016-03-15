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
import de.lbader.apps.movietime.api.objects.TvShow;
import de.lbader.apps.movietime.logic.DiscoverLogic;
import de.lbader.apps.movietime.toolbar.ToolbarManager;

public class TvShowsFragment extends Fragment {

    private ViewPager viewPager;
    private View discoverView;
    private Toolbar toolbar;
    private TabLayout tabLayout;

    private DiscoverLogic<TvShow> airingLogic;
    private DiscoverLogic<TvShow> popularLogic;
    private DiscoverLogic<TvShow> topratedLogic;
    private DiscoverLogic<TvShow> todayLogic;

    private static TvShowsFragment instance;

    public TvShowsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ToolbarManager.setToolbar(R.layout.toolbar_discover,
                getResources().getString(R.string.menutitle_series));

        discoverView = inflater.inflate(R.layout.fragment_discover, container, false);

        createLogics();

        return discoverView;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager = (ViewPager) discoverView.findViewById(R.id.discover_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this.getChildFragmentManager());

        viewPagerAdapter.addFragment(

                FragmentHolder.newInstance(
                        R.layout.fragment_base_list,
                        popularLogic
                ),
                getResources().getString(R.string.popular)
        );

        viewPagerAdapter.addFragment(

                FragmentHolder.newInstance(
                        R.layout.fragment_base_list,
                        topratedLogic
                ),
                getResources().getString(R.string.topRated)
        );


        viewPagerAdapter.addFragment(

                FragmentHolder.newInstance(
                        R.layout.fragment_base_list,
                        todayLogic
                ),
                getResources().getString(R.string.today)
        );

        viewPagerAdapter.addFragment(

                FragmentHolder.newInstance(
                        R.layout.fragment_base_list,
                        airingLogic
                ),
                getResources().getString(R.string.upcoming)
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
        todayLogic = new DiscoverLogic<>(TvShow.class,
                "tv/airing_today",
                R.id.base_recylcerview
        );
        topratedLogic = new DiscoverLogic<>(TvShow.class,
                "tv/top_rated",
                R.id.base_recylcerview
        );
        airingLogic = new DiscoverLogic<>(TvShow.class,
                "tv/on_the_air",
                R.id.base_recylcerview
        );
        popularLogic = new DiscoverLogic<>(TvShow.class,
                "tv/popular",
                R.id.base_recylcerview
        );
    }
}
