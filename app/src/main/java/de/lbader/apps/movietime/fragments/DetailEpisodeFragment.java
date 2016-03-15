package de.lbader.apps.movietime.fragments;


import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
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
import de.lbader.apps.movietime.api.objects.Episode;
import de.lbader.apps.movietime.api.objects.Season;
import de.lbader.apps.movietime.logic.EpisodeLogic;
import de.lbader.apps.movietime.logic.SeasonEpisodesLogic;
import de.lbader.apps.movietime.logic.SeasonLogic;
import de.lbader.apps.movietime.toolbar.ToolbarManager;

public class DetailEpisodeFragment extends Fragment {

    private ViewPager viewPager;
    private View detailView;
    private Toolbar toolbar;
    private TabLayout tabLayout;

    private Episode episode;

    private FragmentHolderLogic mainLogic;
    private FragmentHolderLogic episodeLogic;

    private static DetailEpisodeFragment instance;

    public DetailEpisodeFragment() {
        // Required empty public constructor
    }

    public static DetailEpisodeFragment newInstance(Episode episode) {
        DetailEpisodeFragment fragment = new DetailEpisodeFragment();
        fragment.episode = episode;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ToolbarManager.setToolbar(R.layout.toolbar_detail_m_tv,"");


        CollapsingToolbarLayout collapsingToolbarLayout
                = (CollapsingToolbarLayout) ToolbarManager.coordinatorLayout().
                findViewById(R.id.collapsingToolbarLayout);

        collapsingToolbarLayout.setTitle(episode.getName());
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.white));
        collapsingToolbarLayout.setExpandedTitleMarginBottom(
                collapsingToolbarLayout.getExpandedTitleMarginBottom() + 20
        );

        detailView = inflater.inflate(R.layout.fragment_discover, container, false);

        if (getArguments().getString("frame") != null) {
            ViewCompat.setTransitionName(detailView, getArguments().getString("frame"));
        }

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
                        (Bundle)getArguments().clone()
                ),
                getResources().getString(R.string.detail_title_main)
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
        mainLogic = new EpisodeLogic(episode);
        //episodeLogic = new SeasonEpisodesLogic(season);
    }
}
