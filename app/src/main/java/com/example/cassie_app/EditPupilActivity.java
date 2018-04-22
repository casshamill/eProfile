package com.example.cassie_app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditPupilActivity extends AppCompatActivity {

    private String classUID;
    private String pupilUID;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    EditText editName;
    TextView pupilName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pupil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab_done = (FloatingActionButton) findViewById(R.id.fab);
        fab_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditPupil();
            }
        });

        editName = (EditText) findViewById(R.id.editPupilName);

        pupilName = (TextView) findViewById(R.id.pupil_name);

        Bundle bundle = getIntent().getExtras();
        String uids = bundle.getString("uids");
        pupilUID = uids.split(",")[0];


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        load_data();

    }

    private void EditPupil(){
        String newName = editName.getText().toString();

        //edit in pupils list
        DatabaseReference pupilRef = mDatabase.child("pupils").child(pupilUID).getRef();
        DatabaseReference editRef = pupilRef.child("name").getRef();
        editRef.setValue(newName);

        //edit in class list
        DatabaseReference classRef = mDatabase.child("classes").child(classUID).getRef();
        editRef = classRef.child(pupilUID).getRef();
        editRef.setValue(newName);

        finish();
    }

    private void load_data(){
        DatabaseReference pupil = mDatabase.child("pupils").child(pupilUID);
        pupil.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("name").getValue();
                System.out.println("Name is : " + name);
                pupilName.setText(name);
                //WILL WORK WHEN CLASS ID SET UP PROPERLY
                classUID = (String) dataSnapshot.child("classID").getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
