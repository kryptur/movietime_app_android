package de.lbader.apps.movietime.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ProgressBar;

import de.lbader.apps.movietime.R;
import de.lbader.apps.movietime.api.TmdbApi;
import de.lbader.apps.movietime.fragments.DiscoverFragment;
import de.lbader.apps.movietime.fragments.PersonsFragment;
import de.lbader.apps.movietime.fragments.StartFragment;
import de.lbader.apps.movietime.navigation.Navigation;
import de.lbader.apps.movietime.toolbar.ToolbarManager;

public class MainActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Navigation navi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TmdbApi.init((ProgressBar) findViewById(R.id.loadingBar));

        ToolbarManager.init(this, drawerLayout);

        navi = new Navigation(this);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ToolbarManager.init(this, drawerLayout);

        navigationView.inflateMenu(R.menu.navigation_menu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    default:
                        return false;
                    case R.id.mainmenu_start:
                        navi.clear();
                        navi.navigate(new StartFragment(), R.id.fragment_container);
                        return true;
                    case R.id.mainmenu_discover:
                        navi.clear();
                        navi.navigate(new DiscoverFragment(), R.id.fragment_container);
                        return true;
                    case R.id.mainmenu_persons:
                        navi.clear();
                        navi.navigate(new PersonsFragment(), R.id.fragment_container);
                        return true;
                }
            }
        });

        Fragment fragment = new StartFragment();
        if (savedInstanceState != null) {
            fragment = getSupportFragmentManager().getFragment(savedInstanceState,
                    "fragmentInstanceSaved");
        } else {
            fragment = new StartFragment();
        }
        navi.navigate(fragment, R.id.fragment_container);
    }


    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    @Override
    public void onBackPressed() {
        navi.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState,
                "fragmentInstanceSaved",
                getSupportFragmentManager().findFragmentById(R.id.fragment_container));
    }
}
