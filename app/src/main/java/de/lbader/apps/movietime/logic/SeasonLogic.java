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

import de.lbader.apps.movietime.R;
import de.lbader.apps.movietime.activities.MainActivity;
import de.lbader.apps.movietime.api.SimpleCallback;
import de.lbader.apps.movietime.api.objects.Movie;
import de.lbader.apps.movietime.api.objects.Season;
import de.lbader.apps.movietime.api.objects.TvShow;
import de.lbader.apps.movietime.api.objects.WatchableObject;
import de.lbader.apps.movietime.fragments.FragmentHolderLogic;
import de.lbader.apps.movietime.toolbar.ToolbarManager;

public class SeasonLogic implements FragmentHolderLogic {
    private Season season;
    private View detailView;
    private Context context;
    private TextView title;
    private ImageView poster;
    private TextView taglineHeader;
    private TextView tagline;

    public SeasonLogic(Season season) {
        this.season = season;
    }

    @Override
    public void init(Context context, View detailView, Bundle args) {
        this.detailView = detailView;
        this.context = context;
        title = (TextView) detailView.findViewById(R.id.titleTextView);
        poster = (ImageView) detailView.findViewById(R.id.posterImageView);
        tagline = (TextView) detailView.findViewById(R.id.tagline);
        taglineHeader = (TextView) detailView.findViewById(R.id.taglineHeader);
        tagline.setVisibility(View.GONE);
        taglineHeader.setVisibility(View.GONE);
        detailView.findViewById(R.id.rating).setVisibility(View.GONE);
        detailView.findViewById(R.id.ratingHeader).setVisibility(View.GONE);



        ViewCompat.setTransitionName(title, args.getString("title"));
        ViewCompat.setTransitionName(poster, args.getString("poster"));

        season.load(new SimpleCallback() {
                @Override
                public void callback() {
                    setup();
                }
            });
    }

    private void setup() {
        TextView overview = (TextView) detailView.findViewById(R.id.overviewTextView);

        if (poster.getDrawable() == null) {
            Picasso.with(context)
                    .load(season.getPosterUri())
                    .noFade()
                    .fit()
                    .centerInside()
                    .into(poster);
        }


        title.setText(season.getName());

        ((TextView)detailView.findViewById(R.id.statusHeader)).setText(
                MainActivity.context.getResources().getString(R.string.episodecount));


        ((TextView)detailView.findViewById(R.id.status)).setText("" + season.getEpisodeCount());
        ((TextView)detailView.findViewById(R.id.releasedate)).setText(season.getReleaseDate());

        overview.setText(season.getOverview());
    }
}
