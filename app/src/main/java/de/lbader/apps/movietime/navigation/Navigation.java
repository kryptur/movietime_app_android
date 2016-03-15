package de.lbader.apps.movietime.navigation;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import de.lbader.apps.movietime.R;
import de.lbader.apps.movietime.activities.MainActivity;

public class Navigation {
    Context context;
    public static Navigation instance;

    public Navigation(Context context) {
        this.context = context;
        instance = this;
    }


    public void navigate(Fragment fragment, int container, HashMap<String, View> sharedElements) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition detail = TransitionInflater.from(context).inflateTransition(
                    R.transition.change_image_transform
            );
            fragment.setSharedElementEnterTransition(detail);
            fragment.setSharedElementReturnTransition(detail);
        }

        FragmentTransaction ft = ((MainActivity)context).getSupportFragmentManager().beginTransaction();

        for (String transitionName : sharedElements.keySet()) {
            ViewCompat.setTransitionName(sharedElements.get(transitionName), transitionName);
            ft.addSharedElement(sharedElements.get(transitionName), transitionName);
        }
        ft.replace(container, fragment);
        ft.addToBackStack(null);
        ft.commit();
        ((MainActivity)context).getDrawerLayout().closeDrawers();
    }

    public void navigate(Fragment fragment, int container) {
        this.navigate(fragment, container, new HashMap<String, View>());
    }

    public void onBackPressed() {
        FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            ((MainActivity)context).finish();
        }
    }
}
