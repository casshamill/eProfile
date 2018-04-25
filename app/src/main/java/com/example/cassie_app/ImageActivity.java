package com.example.cassie_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class ImageActivity extends AppCompatActivity {

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Bundle bundle = getIntent().getExtras();
        String path = bundle.getString("path");
        File file = new File(path);
        ImageView image = (ImageView) findViewById(R.id.enlarged_image);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        try{
            int scale = Math.abs(Math.min((width / bitmap.getWidth()) - 1 , (height / bitmap.getHeight()) - 1));
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()*scale, bitmap.getHeight()*scale, false);
            image.setImageBitmap(scaledBitmap);
        } catch (IllegalArgumentException e){
            Toast.makeText(getApplicationContext(), "Something went wrong, Image may be corrupted.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

}
