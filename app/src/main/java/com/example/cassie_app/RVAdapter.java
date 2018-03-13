package com.example.cassie_app;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ContentViewHolder> {

    List<Post> posts;
    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView description;
        TextView content;
        TextView date;

        public ContentViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            description = (TextView)itemView.findViewById(R.id.description);
            content = (TextView)itemView.findViewById(R.id.feed_content);
            date = (TextView)itemView.findViewById(R.id.date);
        }
    }

    public RVAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_main_parent, parent, false);
        ContentViewHolder cvh = new ContentViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(ContentViewHolder holder, int position) {
        holder.description.setText(posts.get(position).author);
        holder.date.setText(posts.get(position).date);
        holder.content.setText(posts.get(position).content);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

}
