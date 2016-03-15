package de.lbader.apps.movietime.viewfactories;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.lbader.apps.movietime.R;
import de.lbader.apps.movietime.api.objects.Cast;
import de.lbader.apps.movietime.api.objects.Season;

public class SeasonObjectHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView episodes;
    private TextView release;
    private TextView name;
    private Context context;
    private CardView cardView;

    public SeasonObjectHolder(View view) {
        super(view);
        context = view.getContext();
        cardView = (CardView) view;
        imageView = (ImageView)view.findViewById(R.id.imageView);
        name = (TextView) view.findViewById(R.id.name);
        episodes = (TextView) view.findViewById(R.id.episodes);
        release = (TextView) view.findViewById(R.id.release);
    }


    public void update(Season season) {
        Picasso
                .with(context)
                .load(season.getPosterUri())
                .fit()
                .centerInside()
                .into(imageView);

        name.setText(season.getName());
        episodes.setText("" + season.getEpisodeCount());
        release.setText(season.getReleaseDate());
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
