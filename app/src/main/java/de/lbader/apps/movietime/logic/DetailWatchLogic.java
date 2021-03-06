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

import org.w3c.dom.Text;

import de.lbader.apps.movietime.R;
import de.lbader.apps.movietime.api.SimpleCallback;
import de.lbader.apps.movietime.api.objects.Movie;
import de.lbader.apps.movietime.api.objects.TvShow;
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
    private TextView taglineHeader;
    private TextView tagline;

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
        tagline = (TextView) detailView.findViewById(R.id.tagline);
        taglineHeader = (TextView) detailView.findViewById(R.id.taglineHeader);
        if (watchableObject instanceof TvShow) {
            tagline.setVisibility(View.GONE);
            taglineHeader.setVisibility(View.GONE);
        }

        ViewCompat.setTransitionName(title, args.getString("title"));
        ViewCompat.setTransitionName(poster, args.getString("poster"));

        watchableObject.load(new SimpleCallback() {
                @Override
                public void callback() {
                    setup();
                }
            });
    }

    private void setup() {
        TextView overview = (TextView) detailView.findViewById(R.id.overviewTextView);
        RatingBar rating = (RatingBar) detailView.findViewById(R.id.rating);

        toolbarLayout.setTitle(watchableObject.getName());

        if (poster.getDrawable() == null) {
            Picasso.with(context)
                    .load(watchableObject.getPosterUri())
                    .noFade()
                    .fit()
                    .centerInside()
                    .into(poster);
        }


        title.setText(watchableObject.getName());
        if (watchableObject instanceof Movie) {
            Log.d("Tagline", ((Movie) watchableObject).getTagline());
            tagline.setText(((Movie) watchableObject).getTagline());
        }

        ((TextView)detailView.findViewById(R.id.votecount)).setText(watchableObject.getVoteCount() + " ");
        ((TextView)detailView.findViewById(R.id.status)).setText(watchableObject.getStatus());
        ((TextView)detailView.findViewById(R.id.releasedate)).setText(watchableObject.getReleaseDate());

        overview.setText(watchableObject.getOverview());

        rating.setNumStars(5);
        rating.setStepSize(0.25f);
        rating.setRating((float)watchableObject.getVote_average() / 2);
    }
}
