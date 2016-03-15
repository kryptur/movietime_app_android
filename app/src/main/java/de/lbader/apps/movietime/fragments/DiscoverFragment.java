package de.lbader.apps.movietime.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public class DiscoverFragment extends Fragment {

    private ViewPager viewPager;
    private View discoverView;
    private Toolbar toolbar;
    private TabLayout tabLayout;

    private DiscoverLogic<Movie> movieLogic;
    private DiscoverLogic<TvShow> tvLogic;

    private static DiscoverFragment instance;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ToolbarManager.setToolbar(R.layout.toolbar_discover,
                getResources().getString(R.string.menutitle_discover));

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
                        movieLogic
                ),
                getResources().getString(R.string.menutitle_movies)
        );
        viewPagerAdapter.addFragment(
                FragmentHolder.newInstance(
                        R.layout.fragment_base_list,
                        tvLogic
                ),
                getResources().getString(R.string.menutitle_series)
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
        movieLogic = new DiscoverLogic<>(Movie.class,
                "discover/movie",
                R.id.base_recylcerview
        );
        tvLogic = new DiscoverLogic<>(TvShow.class,
                "discover/tv",
                R.id.base_recylcerview
        );
    }
}
