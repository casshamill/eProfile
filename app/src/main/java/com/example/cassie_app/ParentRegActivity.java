package com.example.cassie_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ParentRegActivity extends AppCompatActivity {

    private EditText childNameView;
    private EditText childDobView;
    private EditText classUIDView;

    private String email;
    private String name;
    private String pupil_id;
    private boolean pupil_found;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_reg);

        childNameView = (EditText) findViewById(R.id.child_name);
        childDobView = (EditText) findViewById(R.id.child_dob);
        classUIDView = (EditText) findViewById(R.id.class_uid);

        Button RegButton = (Button) findViewById(R.id.reg_submit3);
        RegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttemptParentRegistration();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    private void AttemptParentRegistration() {
        //validate
        final String childName = childNameView.getText().toString();
        final Date childDob = (Date) childDobView.getText();
        String classUID = classUIDView.getText().toString();

        if (TextUtils.isEmpty(childName)) {
            childNameView.setError(getString(R.string.error_field_required));
            childNameView.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(childDobView.getText().toString())) {
            childDobView.setError(getString(R.string.error_field_required));
            childDobView.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(classUID)) {
            classUIDView.setError(getString(R.string.error_field_required));
            classUIDView.requestFocus();
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.setLenient(false);
        cal.setTime(childDob);
        try {
            cal.getTime();
        }
        catch (Exception e) {
            System.out.println("Invalid date");
            Toast.makeText(ParentRegActivity.this, "You have entered an invalid date, please try again.", Toast.LENGTH_LONG).show();
            return;
        }
        pupil_found = false;
        //find child uid
        //TODO: Better to get class id from parents as part of registration and search from there using dob to validate.
        DatabaseReference classRef = mDatabase.child("classes").child(classUID);
        if (classRef == null) {
            Toast.makeText(ParentRegActivity.this, "You have entered an invalid class ID, please try again.", Toast.LENGTH_LONG).show();
            return;
        }
        //BUILD LIST OF PUPILS IN CLASS
        final ArrayList<String> pupils = new ArrayList<String>();
        classRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Finding child");
                for (DataSnapshot d : dataSnapshot.getChildren()){
                    pupils.add(d.getKey());
                }
                findPupil(pupils, childName, childDob);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void findPupil(ArrayList<String> pupils, final String childName, final Date dob) {
        for (String pupil : pupils) {
            DatabaseReference pupilRef = mDatabase.child("pupils").child(pupil);
            pupilRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String currname = (String) dataSnapshot.child("name").getValue();
                    if (currname.equals(childName)) {
                        //check dob
                        String currentdobstr = (String) dataSnapshot.child("dob").getValue();
                        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date currentdob = new Date();
                        try {
                            currentdob = sdf.parse(currentdobstr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (currentdob.equals(dob)) {
                            pupil_id = dataSnapshot.getKey();
                            pupil_found = true;
                            register();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        if (pupil_found == false) {
            System.out.println("Pupil " + childName + "not found, Registreation unsucessful");
        }
    }

    private void register(){
        //add parent to db
        email = getIntent().getStringExtra("EMAILVALUE");
        name = getIntent().getStringExtra("NAMEVALUE");
        Parent p = new Parent(email, name, pupil_id);
        DatabaseReference newRef = mDatabase.child("parents").getRef();
        newRef.child(mAuth.getCurrentUser().getUid()).setValue(p);
        Intent i = new Intent(ParentRegActivity.this, MainParentActivity.class);
        ParentRegActivity.this.startActivity(i);
    }
}
