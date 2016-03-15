package de.lbader.apps.movietime.viewfactories;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.lbader.apps.movietime.R;
import de.lbader.apps.movietime.api.objects.BaseObject;
import de.lbader.apps.movietime.api.objects.Cast;

public class CastObjectHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView character;
    private TextView name;
    private Context context;
    private CardView cardView;
    private boolean isPersonCast;

    public CastObjectHolder(View view, boolean isPersonCast) {
        super(view);
        context = view.getContext();
        cardView = (CardView) view;
        imageView = (ImageView)view.findViewById(R.id.imageView);
        name = (TextView) view.findViewById(R.id.name);
        character = (TextView) view.findViewById(R.id.character);
        this.isPersonCast = isPersonCast;
    }


    public void update(Cast cast) {
        String img = isPersonCast ? cast.getPosterPath() : cast.getProfilePath();


        Picasso
                .with(context)
                .load(img)
                .fit()
                .centerInside()
                .into(imageView);

        character.setText(cast.getCharacter());
        name.setText(cast.getName());
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
