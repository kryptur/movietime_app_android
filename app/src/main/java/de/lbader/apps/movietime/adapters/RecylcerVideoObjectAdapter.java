package de.lbader.apps.movietime.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import de.lbader.apps.movietime.R;
import de.lbader.apps.movietime.api.objects.Cast;
import de.lbader.apps.movietime.api.objects.Video;
import de.lbader.apps.movietime.viewfactories.CastObjectHolder;
import de.lbader.apps.movietime.viewfactories.VideoObjectHolder;

public class RecylcerVideoObjectAdapter extends RecyclerView.Adapter<VideoObjectHolder> {
    private Context context;
    private ArrayList<Video> elements = new ArrayList<>();
    private VideoObjectAdapterEvents events;

    private int endOffset;

    public RecylcerVideoObjectAdapter(Context c, int endOffset) {
        this.context = c;
        this.endOffset = endOffset;
    }

    public void setCastObjectAdapterEvents(VideoObjectAdapterEvents events) {
        this.events = events;
    }

    public void addItem(Video item) {
        elements.add(item);
    }

    public int getCount() {
        return elements.size();
    }

    @Override
    public VideoObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.cardview_video, null);
        VideoObjectHolder videoObjectHolder = new VideoObjectHolder(view);
        return videoObjectHolder;
    }

    @Override
    public void onBindViewHolder(VideoObjectHolder holder, int position) {
        if (position > elements.size() - endOffset && events != null) {
            events.onEndReached(elements);
        }
        holder.update(elements.get(position));
        final Video video = elements.get(position);
        holder.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(
                        new Intent(Intent.ACTION_VIEW,
                                Uri.parse(video.getUrl()))
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public interface VideoObjectAdapterEvents {
        void onEndReached(ArrayList<Video> elements);
    }
}
