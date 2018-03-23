package com.example.cassie_app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UploadText extends AppCompatActivity {

    EditText contentView;
    String selected;
    String uids;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPost();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        selected = bundle.getString("selected");
        uids = bundle.getString("uids");
        TextView pupilsView = findViewById(R.id.pupil_name);
        pupilsView.setText(selected);
        contentView = findViewById(R.id.content_text);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contentView.setText("");
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    public void uploadPost(){
        String content = contentView.getText().toString();
        if (content.equals("Content Here")){
            contentView.setError(getString(R.string.error_content_required));
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
        String content = contentView.getText().toString();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        Post post = new Post(name, content, formattedDate, null);
        String[] pupils = uids.split(",");
        for(String pupil: pupils){
            DatabaseReference newRef = mDatabase.child("pupils").child(pupil).child("feed").getRef();
            newRef.push().setValue(post);
        }
    }
}
