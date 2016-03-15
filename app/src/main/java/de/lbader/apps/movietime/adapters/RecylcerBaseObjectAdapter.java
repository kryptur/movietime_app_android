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
import java.util.UUID;

import de.lbader.apps.movietime.R;
import de.lbader.apps.movietime.api.objects.BaseObject;
import de.lbader.apps.movietime.api.objects.Movie;
import de.lbader.apps.movietime.api.objects.Person;
import de.lbader.apps.movietime.api.objects.TvShow;
import de.lbader.apps.movietime.api.objects.WatchableObject;
import de.lbader.apps.movietime.fragments.DetailPersonFragment;
import de.lbader.apps.movietime.fragments.DetailWatchableFragment;
import de.lbader.apps.movietime.navigation.Navigation;
import de.lbader.apps.movietime.viewfactories.BaseObjectHolder;

public class RecylcerBaseObjectAdapter extends RecyclerView.Adapter<BaseObjectHolder> {
    private Context context;
    private ArrayList<BaseObject> elements = new ArrayList<>();
    private BaseObjectAdapterEvents events;

    private String unique;

    private int endOffset;

    public RecylcerBaseObjectAdapter(Context c, int endOffset) {
        this.context = c;
        this.endOffset = endOffset;
        unique = UUID.randomUUID().toString();
    }

    public void setBaseObjectAdapterEvents(BaseObjectAdapterEvents events) {
        this.events = events;
    }

    public void addItem(BaseObject item) {
        elements.add(item);
    }

    public int getCount() {
        return elements.size();
    }

    @Override
    public BaseObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.cardview_baseobject, null);
        BaseObjectHolder baseObjectHolder = new BaseObjectHolder(view);
        return baseObjectHolder;
    }

    @Override
    public void onBindViewHolder(final BaseObjectHolder holder, int position) {
        if (position > elements.size() - endOffset && events != null) {
            events.onEndReached(elements);
        }
        holder.update(elements.get(position));
        final ImageView poster = holder.getImageView();
        final TextView title = holder.getTitleView();
        final CardView card = holder.getCardView();
        final BaseObject elem = elements.get(position);

        final String uniqueTitle = "detailTitle_" + position + "_" + unique;
        final String uniquePoster = "detailPoster_" + position + "_" + unique;
        final String uniqueCard = "detailCard_" + position + "_" + unique;

        ViewCompat.setTransitionName(poster, uniquePoster);
        ViewCompat.setTransitionName(title, uniqueTitle);
        ViewCompat.setTransitionName(card, uniqueCard);


        holder.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Pair<String, View>> sharedElements = new HashMap<>();
                sharedElements.put("poster", new Pair(uniquePoster, poster));
                sharedElements.put("title", new Pair(uniqueTitle, title));
                sharedElements.put("frame", new Pair(uniqueCard, card));

                Fragment newFragment;
                if (elem instanceof Movie || elem instanceof TvShow) {
                    newFragment = DetailWatchableFragment.newInstance((WatchableObject) elem);
                } else {
                    newFragment = DetailPersonFragment.newInstance((Person) elem);
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

    public interface BaseObjectAdapterEvents {
        void onEndReached(ArrayList<BaseObject> elements);
    }
}
