package de.lbader.apps.movietime.logic;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import junit.framework.Assert;

import de.lbader.apps.movietime.R;
import de.lbader.apps.movietime.api.SimpleCallback;
import de.lbader.apps.movietime.api.objects.WatchableObject;
import de.lbader.apps.movietime.fragments.FragmentHolderLogic;
import de.lbader.apps.movietime.toolbar.ToolbarManager;

public class DetailWatchLogic implements FragmentHolderLogic {
    private WatchableObject watchableObject;
    private View detailView;
    private Context context;
    private CollapsingToolbarLayout toolbarLayout;
    private TextView title;
    private ImageView poster;

    public DetailWatchLogic(WatchableObject watchableObject) {
        this.watchableObject = watchableObject;
    }

    public void setToolbarLayout(CollapsingToolbarLayout toolbarLayout) {
        this.toolbarLayout = toolbarLayout;
    }

    @Override
    public void init(Context context, View detailView, Bundle args) {
        this.detailView = detailView;
        this.context = context;
        title = (TextView) detailView.findViewById(R.id.titleTextView);
        poster = (ImageView) detailView.findViewById(R.id.posterImageView);

        ViewCompat.setTransitionName(title, args.getString("title"));
        ViewCompat.setTransitionName(poster, args.getString("poster"));


        if (watchableObject.isLoaded()) {
            setup();
        } else {
            watchableObject.load(new SimpleCallback() {
                @Override
                public void callback() {
                    setup();
                }
            });
        }
    }

    private void setup() {
        TextView overview = (TextView) detailView.findViewById(R.id.overviewTextView);
        RatingBar rating = (RatingBar) detailView.findViewById(R.id.rating);

        toolbarLayout.setTitle(watchableObject.getName());

        Picasso.with(context)
                .load(watchableObject.getPosterUri())
                .fit()
                .centerInside()
                .into(poster);

        title.setText(watchableObject.getName());

        ImageView header = (ImageView) ToolbarManager.coordinatorLayout().findViewById(R.id.banner_image);
        Picasso.with(context)
                .load(watchableObject.getBackdropUri("original"))
                .fit()
                .centerCrop()
                .into(header);

        overview.setText(watchableObject.getOverview());

        rating.setNumStars(5);
        rating.setRating((float)watchableObject.getVote_average() / 2);
    }
}
