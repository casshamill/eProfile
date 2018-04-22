package com.example.cassie_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UploadPhoto extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private String encodedImage = "";
    private String selected;
    private String uids;
    private String file;
    String area;
    private DatabaseReference mDatabase;
    CheckBox isGolden;
    private FirebaseAuth mAuth;
    private EditText photo_comment;
    private ImageView imageView;
    private Spinner spinner;
    private static final String[]areas = {"Math", "Science", "Geography"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadData();
            }
        });

        isGolden = findViewById(R.id.goldenBox);

        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UploadPhoto.this,
                android.R.layout.simple_spinner_item,areas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                area = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Bundle bundle = getIntent().getExtras();
        selected = bundle.getString("selected");
        uids = bundle.getString("uids");
        file = bundle.getString("file");
        imageView = findViewById(R.id.photo_show);
        if (!file.equals("camera")) {
            System.out.print("Loading from file");
            byte[] decodedString = Base64.decode(file.getBytes(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            //byte[] decodedString = Base64.decode(file, Base64.DEFAULT);
            //Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);
            encodedImage = file;
        } else {
            Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
            if (i.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(i, CAMERA_REQUEST);
            }
        }
        ConstraintLayout inner = (ConstraintLayout) findViewById(R.id.content_include);
        System.out.println(inner);
        photo_comment = inner.findViewById(R.id.photo_comment);
        photo_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photo_comment.setText("");
            }
        });

        TextView pupilsView = findViewById(R.id.pupil_names);
        pupilsView.setText(selected);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView imageView = findViewById(R.id.photo_show);
        System.out.println("Tiarnan 1");
        System.out.println("error: " + resultCode);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            System.out.println("Tiarnan 2");
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteFormat = stream.toByteArray();
            encodedImage = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        } else {
            finish();
        }
    }

    public void uploadData(){
        if (encodedImage.length() == 0){
            System.out.println("Returning");
            return;
        }

        DatabaseReference teacher = mDatabase.child("teachers").child(mAuth.getCurrentUser().getUid());
        teacher.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                writeToDb(name);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    public void writeToDb(String name){
        String content = photo_comment.getText().toString();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        boolean golden = isGolden.isChecked();
        Post post = new Post(name, content, formattedDate, encodedImage, area, golden);
        String[] pupils = uids.split(",");
        for(String pupil: pupils){
            DatabaseReference newRef = mDatabase.child("pupils").child(pupil).child("feed").getRef();
            newRef.push().setValue(post);
        }

        Intent output = new Intent(UploadPhoto.this, successPost.class);
        UploadPhoto.this.startActivity(output);
    }

}
