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
import de.lbader.apps.movietime.api.objects.Video;

public class VideoObjectHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView site;
    private TextView name;
    private Context context;
    private CardView cardView;

    public VideoObjectHolder(View view) {
        super(view);
        context = view.getContext();
        cardView = (CardView) view;
        imageView = (ImageView)view.findViewById(R.id.imageView);
        name = (TextView) view.findViewById(R.id.name);
        site = (TextView) view.findViewById(R.id.site);
    }


    public void update(Video video) {
        Picasso
                .with(context)
                .load(video.getPreviewPath())
                .fit()
                .centerInside()
                .into(imageView);

        site.setText(video.getSite());
        name.setText(video.getName());
    }

    public void setOnClickListener(CardView.OnClickListener listener) {
        cardView.setOnClickListener(listener);
    }
}
