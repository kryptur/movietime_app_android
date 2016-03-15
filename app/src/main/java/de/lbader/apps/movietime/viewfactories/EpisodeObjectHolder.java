package de.lbader.apps.movietime.viewfactories;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.lbader.apps.movietime.R;
import de.lbader.apps.movietime.api.objects.Episode;
import de.lbader.apps.movietime.api.objects.Season;

public class EpisodeObjectHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView episodenum;
    private TextView release;
    private TextView name;
    private Context context;
    private CardView cardView;
    private RatingBar ratingBar;

    public EpisodeObjectHolder(View view) {
        super(view);
        context = view.getContext();
        cardView = (CardView) view;
        imageView = (ImageView)view.findViewById(R.id.imageView);
        name = (TextView) view.findViewById(R.id.name);
        episodenum = (TextView) view.findViewById(R.id.episodenum);
        release = (TextView) view.findViewById(R.id.release);
        ratingBar = (RatingBar) view.findViewById(R.id.rating);
    }


    public void update(Episode episode) {
        Picasso
                .with(context)
                .load(episode.getPosterUri())
                .fit()
                .centerInside()
                .into(imageView);

        name.setText(episode.getName());
        episodenum.setText("#" + episode.getId());
        release.setText(episode.getReleaseDate());
        ratingBar.setStepSize(0.25f);
        ratingBar.setNumStars(5);
        ratingBar.setRating((float)episode.getVote_average() / 2);
    }

    public void setOnClickListener(CardView.OnClickListener listener) {
        cardView.setOnClickListener(listener);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTextView() {
        return name;
    }

    public CardView getCardView() {
        return cardView;
    }
}
