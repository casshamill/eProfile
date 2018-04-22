package com.example.cassie_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ContentViewHolder> {

    List<Post> posts;
    List<Post> postsCopy;
    Bitmap decodedByte;
    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView description;
        TextView content;
        TextView date;
        ImageButton thumb;
        Context context;
        CheckBox golden;
        TextView area;

        public ContentViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            cv = (CardView)itemView.findViewById(R.id.card_view);
            description = (TextView)itemView.findViewById(R.id.description);
            content = (TextView)itemView.findViewById(R.id.feed_content);
            date = (TextView)itemView.findViewById(R.id.date);
            golden = (CheckBox)itemView.findViewById(R.id.golden);
            thumb = (ImageButton)itemView.findViewById(R.id.feed_thumb);
            area = (TextView)itemView.findViewById(R.id.area_indc);
        }
    }

    public RVAdapter(List<Post> posts) {
        this.posts = posts;
        Collections.reverse(this.posts);
        this.postsCopy = new ArrayList<Post>();
        this.postsCopy.addAll(posts);
    }

    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_main_parent, parent, false);
        ContentViewHolder cvh = new ContentViewHolder(v);
        //addCardView(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(final ContentViewHolder holder, int position) {
        holder.description.setText(posts.get(position).author);
        holder.date.setText(posts.get(position).date);
        if (posts.get(position).image != null) {
            try {
                final byte[] decodedString = Base64.decode(posts.get(position).image.getBytes(), Base64.DEFAULT);
                decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.thumb.setImageBitmap(decodedByte);
                holder.thumb.setVisibility(View.VISIBLE);
                holder.thumb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImageButton btn = (ImageButton)view.findViewById(R.id.feed_thumb);
                        Bitmap b = ((BitmapDrawable)btn.getDrawable()).getBitmap();
                        enlargeImage(holder, b);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        holder.content.setText(posts.get(position).content);
        String background = null;
        switch (posts.get(position).area) {
            case "Math":
                holder.area.setBackgroundResource(R.drawable.area_indicator_math);
                holder.area.setText("Math");
                break;
            case "Science":
                holder.area.setBackgroundResource(R.drawable.area_indicator_sci);
                holder.area.setText("Science");
                break;
            case "Geography":
                holder.area.setBackgroundResource(R.drawable.area_indicator_geo);
                holder.area.setText("Geography");
                break;
        }
        if (posts.get(position).golden){
            System.out.println("star at pos : " + position);
            holder.golden.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private void enlargeImage(ContentViewHolder holder, Bitmap bitmap){
        //Change this to popup activity like add pupil
        System.out.println("Enlarging image");
        String filePath= tempFileImage(holder.context,bitmap,"name");
        Intent i = new Intent(holder.context, ImageActivity.class);
        i.putExtra("path", filePath);
        holder.context.startActivity(i);
    }

    public static String tempFileImage(Context context, Bitmap bitmap, String name) {

        File outputDir = context.getCacheDir();
        File imageFile = new File(outputDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(context.getClass().getSimpleName(), "Error writing file", e);
        }

        return imageFile.getAbsolutePath();
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
