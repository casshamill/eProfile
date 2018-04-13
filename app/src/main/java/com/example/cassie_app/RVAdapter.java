package com.example.cassie_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tiarnan on 11/03/2018.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ContentViewHolder> {

    List<Post> posts;
    List<Post> postsCopy;
    static ImageView photo;
    Bitmap decodedByte;

    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView description;
        TextView content;
        TextView date;
        ImageButton thumb;

        public ContentViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            description = (TextView)itemView.findViewById(R.id.description);
            content = (TextView)itemView.findViewById(R.id.feed_content);
            photo = (ImageView)itemView.findViewById(R.id.feed_photo);
            date = (TextView)itemView.findViewById(R.id.date);
            thumb = (ImageButton)itemView.findViewById(R.id.feed_thumb);
        }
    }

    public RVAdapter(List<Post> posts) {
        this.posts = posts;
        this.postsCopy = new ArrayList<Post>();
        this.postsCopy.addAll(posts);
    }

    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_main_parent, parent, false);
        ContentViewHolder cvh = new ContentViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(final ContentViewHolder holder, int position) {
        holder.description.setText(posts.get(position).author);
        holder.date.setText(posts.get(position).date);
        if (posts.get(position).image != null) {
            try {
                byte[] decodedString = Base64.decode(posts.get(position).image.getBytes(), Base64.DEFAULT);
                decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.thumb.setImageBitmap(decodedByte);
                holder.thumb.setVisibility(View.VISIBLE);
                holder.thumb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        enlargeImage(holder);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    public void enlargeImage(ContentViewHolder holder){
        System.out.println("Enlarging image");
        photo.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, (decodedByte.getWidth())*5, (decodedByte.getHeight())*5, false));
        photo.setVisibility(View.VISIBLE);
    }

    public void filter(String text) {
        posts.clear();
        if(text.isEmpty()){
            posts.addAll(postsCopy);
        } else{
            text = text.toLowerCase();
            for(Post post: postsCopy){
                if(post.content.toLowerCase().contains(text) || post.author.toLowerCase().contains(text)){
                    posts.add(post);
                }
            }
        }
        notifyDataSetChanged();
    }
}
