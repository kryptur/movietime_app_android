package de.lbader.apps.movietime.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import de.lbader.apps.movietime.R;
import de.lbader.apps.movietime.api.objects.BaseObject;
import de.lbader.apps.movietime.api.objects.Cast;
import de.lbader.apps.movietime.api.objects.Movie;
import de.lbader.apps.movietime.api.objects.Person;
import de.lbader.apps.movietime.api.objects.TvShow;
import de.lbader.apps.movietime.api.objects.WatchableObject;
import de.lbader.apps.movietime.fragments.DetailPersonFragment;
import de.lbader.apps.movietime.fragments.DetailWatchableFragment;
import de.lbader.apps.movietime.navigation.Navigation;
import de.lbader.apps.movietime.viewfactories.CastObjectHolder;

public class RecylcerCastObjectAdapter extends RecyclerView.Adapter<CastObjectHolder> {
    private Context context;
    private ArrayList<Cast> elements = new ArrayList<>();
    private CastObjectAdapterEvents events;
    private boolean isPersonCast = false;

    private int endOffset;

    private String unique;

    public RecylcerCastObjectAdapter(Context c, int endOffset, boolean isPersonCast) {
        this.context = c;
        this.endOffset = endOffset;
        this.isPersonCast = isPersonCast;
        unique = UUID.randomUUID().toString();
    }

    public void setCastObjectAdapterEvents(CastObjectAdapterEvents events) {
        this.events = events;
    }

    public void addItem(Cast item) {
        elements.add(item);
    }

    public int getCount() {
        return elements.size();
    }

    @Override
    public CastObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.cardview_cast, null);
        CastObjectHolder castObjectHolder = new CastObjectHolder(view, isPersonCast);
        return castObjectHolder;
    }

    @Override
    public void onBindViewHolder(final CastObjectHolder holder, int position) {
        if (position > elements.size() - endOffset && events != null) {
            events.onEndReached(elements);
        }
        holder.update(elements.get(position));

        final ImageView poster = holder.getImageView();
        final TextView title = holder.getTextView();
        final CardView card = holder.getCardView();

        final String uniqueTitle = "castTitle_" + position + "_" + unique;
        final String uniquePoster = "castPoster_" + position + "_" + unique;
        final String uniqueCard = "castFrame_" + position + "_" + unique;

        ViewCompat.setTransitionName(poster, uniquePoster);
        ViewCompat.setTransitionName(title, uniqueTitle);
        ViewCompat.setTransitionName(card, uniqueCard);

        final Cast elem = elements.get(position);
        holder.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Pair<String, View>> sharedElements = new HashMap<>();
                sharedElements.put("poster", new Pair(uniquePoster, poster));
                sharedElements.put("title", new Pair(uniqueTitle, title));
                sharedElements.put("frame", new Pair(uniqueCard, card));

                Fragment newFragment;
                if (isPersonCast) {
                    newFragment = DetailWatchableFragment.newInstance(elem.getWatchableObject());
                } else {
                    newFragment = DetailPersonFragment.newInstance(elem.getPerson());
                }

                Navigation.instance.navigate(
                        newFragment,
                        R.id.fragment_container,
                        sharedElements
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public interface CastObjectAdapterEvents {
        void onEndReached(ArrayList<Cast> elements);
    }
}
