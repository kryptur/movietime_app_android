package de.lbader.apps.movietime.fragments;


import android.os.Bundle;
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
import de.lbader.apps.movietime.api.objects.Season;
import de.lbader.apps.movietime.logic.DetailCastLogic;
import de.lbader.apps.movietime.logic.DetailPersonLogic;
import de.lbader.apps.movietime.logic.SeasonEpisodesLogic;
import de.lbader.apps.movietime.logic.SeasonLogic;
import de.lbader.apps.movietime.toolbar.ToolbarManager;

public class DetailSeasonFragment extends Fragment {

    private ViewPager viewPager;
    private View detailView;
    private Toolbar toolbar;
    private TabLayout tabLayout;

    private Season season;

    private FragmentHolderLogic mainLogic;
    private FragmentHolderLogic episodeLogic;

    private static DetailSeasonFragment instance;

    public DetailSeasonFragment() {
        // Required empty public constructor
    }

    public static DetailSeasonFragment newInstance(Season season) {
        DetailSeasonFragment fragment = new DetailSeasonFragment();
        fragment.season = season;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ToolbarManager.setToolbar(R.layout.toolbar_discover,
                season.getName());

        detailView = inflater.inflate(R.layout.fragment_detail_person, container, false);if (getArguments().getString("frame") != null) {
            ViewCompat.setTransitionName(detailView, getArguments().getString("frame"));
        }

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
                        R.layout.fragment_detail_watchable_main,
                        mainLogic,
                        (Bundle)getArguments().clone()
                ),
                getResources().getString(R.string.detail_title_main)
        );
        viewPagerAdapter.addFragment(
                FragmentHolder.newInstance(
                        R.layout.fragment_detail_person_cast,
                        episodeLogic,
                        (Bundle)getArguments().clone()
                ),
                getResources().getString(R.string.episodes)
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
        mainLogic = new SeasonLogic(season);
        episodeLogic = new SeasonEpisodesLogic(season);
    }
}
