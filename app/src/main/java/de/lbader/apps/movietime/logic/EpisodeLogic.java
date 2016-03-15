package de.lbader.apps.movietime.logic;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.lbader.apps.movietime.R;
import de.lbader.apps.movietime.activities.MainActivity;
import de.lbader.apps.movietime.api.SimpleCallback;
import de.lbader.apps.movietime.api.objects.Episode;
import de.lbader.apps.movietime.api.objects.Season;
import de.lbader.apps.movietime.fragments.FragmentHolderLogic;
import de.lbader.apps.movietime.toolbar.ToolbarManager;

public class EpisodeLogic implements FragmentHolderLogic {
    private Episode episode;
    private View detailView;
    private Context context;
    private TextView title;
    private ImageView poster;
    private TextView taglineHeader;
    private TextView tagline;
    private RatingBar rating;
    private TextView voteCount;
    private ImageView headerImg;

    public EpisodeLogic(Episode episode) {
        this.episode = episode;
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
        rating = (RatingBar) detailView.findViewById(R.id.rating);
        voteCount = (TextView) detailView.findViewById(R.id.votecount);
        detailView.findViewById(R.id.statusHeader).setVisibility(View.GONE);

        ViewCompat.setTransitionName(title, args.getString("title"));
        ViewCompat.setTransitionName(poster, args.getString("poster"));

        episode.load(new SimpleCallback() {
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
                    .load(episode.getPosterUri())
                    .noFade()
                    .fit()
                    .centerInside()
                    .into(poster);
        }


        title.setText(episode.getName());


        headerImg = (ImageView) ToolbarManager.coordinatorLayout().findViewById(R.id.banner_image);
        Picasso.with(context)
                .load(episode.getPosterUri("original"))
                .noFade()
                .fit()
                .centerInside()
                .into(headerImg);


        ((TextView)detailView.findViewById(R.id.statusHeader)).setText(
                MainActivity.context.getResources().getString(R.string.episodecount));


        (detailView.findViewById(R.id.status)).setVisibility(View.GONE);
        ((TextView)detailView.findViewById(R.id.releasedate)).setText(episode.getReleaseDate());

        rating.setStepSize(0.25f);
        rating.setNumStars(5);
        rating.setRating((float)episode.getVote_average() / 2);

        voteCount.setText(episode.getVoteCount() + " ");

        overview.setText(episode.getOverview());
    }
}
