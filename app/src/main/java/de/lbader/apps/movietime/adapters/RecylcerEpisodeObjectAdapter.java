package de.lbader.apps.movietime.adapters;

import android.content.Context;
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
import de.lbader.apps.movietime.api.objects.Episode;
import de.lbader.apps.movietime.api.objects.Season;
import de.lbader.apps.movietime.fragments.DetailEpisodeFragment;
import de.lbader.apps.movietime.fragments.DetailSeasonFragment;
import de.lbader.apps.movietime.navigation.Navigation;
import de.lbader.apps.movietime.viewfactories.EpisodeObjectHolder;
import de.lbader.apps.movietime.viewfactories.SeasonObjectHolder;

public class RecylcerEpisodeObjectAdapter extends RecyclerView.Adapter<EpisodeObjectHolder> {
    private Context context;
    private ArrayList<Episode> elements = new ArrayList<>();
    private EpisodeObjectAdapterEvents events;

    private int endOffset;

    private String unique;

    public RecylcerEpisodeObjectAdapter(Context c, int endOffset) {
        this.context = c;
        this.endOffset = endOffset;
        unique = UUID.randomUUID().toString();
    }

    public void setEpisodeObjectAdapterEvents(EpisodeObjectAdapterEvents events) {
        this.events = events;
    }

    public void addItem(Episode item) {
        elements.add(item);
    }

    public int getCount() {
        return elements.size();
    }

    @Override
    public EpisodeObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.cardview_episode, null);
        EpisodeObjectHolder objectHolder = new EpisodeObjectHolder(view);
        return objectHolder;
    }

    @Override
    public void onBindViewHolder(final EpisodeObjectHolder holder, int position) {
        if (position > elements.size() - endOffset && events != null) {
            events.onEndReached(elements);
        }
        holder.update(elements.get(position));

        final ImageView poster = holder.getImageView();
        final TextView title = holder.getTextView();
        final CardView card = holder.getCardView();

        final String uniqueTitle = "episodeTitle_" + position + "_" + unique;
        final String uniquePoster = "episodePoster_" + position + "_" + unique;
        final String uniqueCard = "episodeFrame_" + position + "_" + unique;

        ViewCompat.setTransitionName(poster, uniquePoster);
        ViewCompat.setTransitionName(title, uniqueTitle);
        ViewCompat.setTransitionName(card, uniqueCard);

        final Episode elem = elements.get(position);
        holder.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Pair<String, View>> sharedElements = new HashMap<>();
                sharedElements.put("poster", new Pair(uniquePoster, poster));
                sharedElements.put("title", new Pair(uniqueTitle, title));
                sharedElements.put("frame", new Pair(uniqueCard, card));

                Fragment newFragment;
                newFragment = DetailEpisodeFragment.newInstance(elem);

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

    public interface EpisodeObjectAdapterEvents {
        void onEndReached(ArrayList<Episode> elements);
    }
}
