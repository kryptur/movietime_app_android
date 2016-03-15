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

public class BaseObjectHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView titleView;
    private Context context;
    private CardView cardView;

    public BaseObjectHolder(View view) {
        super(view);
        context = view.getContext();
        cardView = (CardView) view;
        imageView = (ImageView)view.findViewById(R.id.baseobject_img);
        titleView = (TextView) view.findViewById(R.id.baseobject_title);
    }

    public void update(BaseObject baseObject) {
        Picasso
            .with(context)
            .load(baseObject.getPosterUri())
            .fit()
            .centerInside()
            .into(imageView);
        titleView.setText(baseObject.getName());
    }

    public void setOnClickListener(CardView.OnClickListener listener) {
        cardView.setOnClickListener(listener);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTitleView() {
        return titleView;
    }

    public CardView getCardView() {
        return cardView;
    }
}
