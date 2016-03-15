package de.lbader.apps.movietime.navigation;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.HashMap;

import de.lbader.apps.movietime.R;
import de.lbader.apps.movietime.activities.MainActivity;
import de.lbader.apps.movietime.adapters.FragmentHolder;
import de.lbader.apps.movietime.tools.CopyDrawableImageTransform;

public class Navigation {
    Context context;
    public static Navigation instance;

    public Navigation(Context context) {
        this.context = context;
        instance = this;
    }


    public void navigate(Fragment fragment, int container, HashMap<String, Pair<String, View>> sharedElements) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionSet transitionSet = new TransitionSet();

            CopyDrawableImageTransform copyDrawableImageTransform = new CopyDrawableImageTransform();
            ChangeTransform changeTransform = new ChangeTransform();
            ChangeBounds changeBounds = new ChangeBounds();

            changeBounds.setDuration(500);
            copyDrawableImageTransform.setDuration(500);


            transitionSet.addTransition(copyDrawableImageTransform);
            transitionSet.addTransition(changeBounds);
            transitionSet.addTransition(changeTransform);
            transitionSet.setOrdering(TransitionSet.ORDERING_TOGETHER);

            fragment.setSharedElementEnterTransition(transitionSet);
            fragment.setSharedElementReturnTransition(transitionSet);
            fragment.setEnterTransition(new Fade());
            fragment.setExitTransition(new Fade());
        }


        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        long transId = System.currentTimeMillis();

        FragmentTransaction ft = ((MainActivity)context).getSupportFragmentManager().beginTransaction();

        for (String transitionName : sharedElements.keySet()) {
            String unique = sharedElements.get(transitionName).first;

            ViewCompat.setTransitionName(sharedElements.get(transitionName).second, unique);

            ft.addSharedElement(sharedElements.get(transitionName).second, unique);

            args.putString(transitionName, unique);
        }

        fragment.setArguments(args);

        ft.replace(container, fragment);
        ft.addToBackStack(null);
        ft.commit();
        ((MainActivity)context).getDrawerLayout().closeDrawers();
    }

    public void navigate(Fragment fragment, int container) {
        this.navigate(fragment, container, new HashMap<String, Pair<String, View>>());
    }

    public void onBackPressed() {
        FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        } else {
            ((MainActivity)context).finish();
        }
    }

    public void clear() {
        FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void hideKeyboard() {
        View view = ((MainActivity)context).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }
}
