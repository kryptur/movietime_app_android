package de.lbader.apps.movietime.logic;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.lbader.apps.movietime.R;
import de.lbader.apps.movietime.api.SimpleCallback;
import de.lbader.apps.movietime.api.objects.Person;
import de.lbader.apps.movietime.api.objects.WatchableObject;
import de.lbader.apps.movietime.fragments.FragmentHolderLogic;
import de.lbader.apps.movietime.toolbar.ToolbarManager;

public class DetailPersonLogic implements FragmentHolderLogic {
    private Person person;
    private View detailView;

    public DetailPersonLogic(Person person) {
        this.person = person;
    }

    @Override
    public void init(Context context, View detailView, Bundle args) {
        this.detailView = detailView;
        TextView title = (TextView) detailView.findViewById(R.id.titleTextView);
        ImageView poster = (ImageView) detailView.findViewById(R.id.posterImageView);

        Picasso.with(context)
                .load(person.getPosterUri())
                .fit()
                .centerInside()
                .into(poster);

        title.setText(person.getName());

        person.loadAll(new SimpleCallback() {
            @Override
            public void callback() {
                complete();
            }
        });
    }

    private void complete() {
        TextView overview = (TextView) detailView.findViewById(R.id.overviewTextView);
        overview.setText(person.getBiography());
        TextView birthdate = (TextView) detailView.findViewById(R.id.birthdate);
        TextView birthloc = (TextView) detailView.findViewById(R.id.birthlocation);
        birthdate.setText(person.getBirthday());
        birthloc.setText(person.getBirth());
    }
}
