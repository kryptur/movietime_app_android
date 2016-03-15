package de.lbader.apps.movietime.fragments;


import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
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
import de.lbader.apps.movietime.api.objects.WatchableObject;
import de.lbader.apps.movietime.logic.DetailCastLogic;
import de.lbader.apps.movietime.logic.DetailVideoLogic;
import de.lbader.apps.movietime.logic.DetailWatchLogic;
import de.lbader.apps.movietime.logic.DiscoverLogic;
import de.lbader.apps.movietime.toolbar.ToolbarManager;

public class DetailWatchableFragment extends Fragment {

    private ViewPager viewPager;
    private View detailView;
    private Toolbar toolbar;
    private TabLayout tabLayout;

    private FragmentHolderLogic mainLogic;
    private FragmentHolderLogic castLogic;
    private FragmentHolderLogic videoLogic;

    private WatchableObject watchableObject;

    private static DetailWatchableFragment instance;

    public DetailWatchableFragment() {
        // Required empty public constructor
    }

    public static DetailWatchableFragment newInstance(WatchableObject baseObject) {
        DetailWatchableFragment fragment = new DetailWatchableFragment();
        fragment.watchableObject = baseObject;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ToolbarManager.setToolbar(R.layout.toolbar_detail_m_tv, watchableObject.getName(), true);
        CollapsingToolbarLayout collapsingToolbarLayout
                = (CollapsingToolbarLayout) ToolbarManager.coordinatorLayout().
                findViewById(R.id.collapsingToolbarLayout);

        collapsingToolbarLayout.setTitle(watchableObject.getName());
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.white));
        collapsingToolbarLayout.setExpandedTitleMarginBottom(
                collapsingToolbarLayout.getExpandedTitleMarginBottom() + 20
        );


        detailView = inflater.inflate(R.layout.fragment_discover, container, false);

        createLogics();

        return detailView;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager = (ViewPager) detailView.findViewById(R.id.discover_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this.getChildFragmentManager());

        viewPagerAdapter.addFragment(
                FragmentHolder.newInstance(
                        R.layout.fragment_detail_watchable_main,
                        mainLogic,
                        getArguments()
                ),
                getResources().getString(R.string.detail_title_main)
        );
        viewPagerAdapter.addFragment(
                FragmentHolder.newInstance(
                        R.layout.fragment_detail_watchable_cast,
                        castLogic
                ),
                getResources().getString(R.string.detail_title_cast)
        );
        viewPagerAdapter.addFragment(
                FragmentHolder.newInstance(
                        R.layout.fragment_detail_watchable_videos,
                        videoLogic
                ),
                getResources().getString(R.string.detail_title_videos)
        );
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout = (TabLayout) ToolbarManager.coordinatorLayout().findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void createLogics() {
        mainLogic = new DetailWatchLogic(watchableObject);
        ((DetailWatchLogic)mainLogic).setToolbarLayout(
                (CollapsingToolbarLayout)ToolbarManager.coordinatorLayout().
                        findViewById(R.id.collapsingToolbarLayout));
        castLogic = new DetailCastLogic(watchableObject);
        videoLogic = new DetailVideoLogic(watchableObject);
    }
}
