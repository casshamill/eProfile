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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditPupilActivity extends AppCompatActivity {

    private String classUID;
    private String pupilUID;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    EditText editName;
    EditText editDOB;
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
        editDOB = (EditText) findViewById(R.id.editPupilDOB);
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

        String dob_str = editDOB.getText().toString();
        String[] split = dob_str.split("/");
        if(split[split.length-1].length() < 4 ){
            editDOB.setError("Please use the format DD/MM/YYYY");
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        Date dob = null;
        try {
            dob = sdf.parse(dob_str);
            Date now = new Date();
            if (now.getTime() - dob.getTime() < 0) {
                Toast.makeText(EditPupilActivity.this, "You have entered an invalid date, please try again.", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(EditPupilActivity.this, "You have entered an invalid date, please try again.", Toast.LENGTH_LONG).show();
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.setLenient(false);
        cal.setTime(dob);
        try {
            cal.getTime();

        }
        catch (Exception e) {
            System.out.println("Invalid date");
            Toast.makeText(EditPupilActivity.this, "You have entered an invalid date, please try again.", Toast.LENGTH_LONG).show();
            return;
        }

        //edit in pupils list
        DatabaseReference pupilRef = mDatabase.child("pupils").child(pupilUID).getRef();
        DatabaseReference editRef = pupilRef.child("name").getRef();
        editRef.setValue(newName);
        editRef = pupilRef.child("dob").getRef();
        editRef.setValue(dob.getTime());

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
                Long dob_l = (Long) dataSnapshot.child("dob").getValue();
                Date dob = new Date(dob_l);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String dob_str =  sdf.format(dob);
                System.out.println("Name is : " + name);
                pupilName.setText(name);
                editName.setText(name);
                editDOB.setText(dob_str);

                //WILL WORK WHEN CLASS ID SET UP PROPERLY
                classUID = (String) dataSnapshot.child("classID").getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
