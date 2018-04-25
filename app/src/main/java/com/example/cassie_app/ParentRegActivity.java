package com.example.cassie_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private EditText teacherNameView;

    private String childName;
    private String teacherName = null;
    private String teacherUID;
    private Date childDob;
    private String schoolUID;
    private Spinner teachers_dropdown;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private boolean registered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_reg);

        childNameView = (EditText) findViewById(R.id.child_name);
        childDobView = (EditText) findViewById(R.id.child_dob);
        teachers_dropdown = (Spinner) findViewById(R.id.teachers);

        Button RegButton = (Button) findViewById(R.id.reg_submit3);
        RegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttemptParentRegistration();
            }
        });

        schoolUID = getIntent().getExtras().getString("SCHOOLVALUE");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        DatabaseReference schoolRef = mDatabase.child("schools").child(schoolUID);
        final ArrayList<String> teachers = new ArrayList<String>();
        final ArrayList<String> teachernames = new ArrayList<String>();
        schoolRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()){
                    try {
                        boolean conf = (boolean)d.getValue();
                        continue;
                    } catch (ClassCastException e ){
                        teachers.add(d.getKey());
                        teachernames.add((String) d.getValue());
                    }
                }
                //findPupil(teachers, childName, childDob);
                populate_teacher_dropdown(teachers, teachernames);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDestroy() {
        if (isFinishing() && !registered){
            removeUser();
        }
        super.onDestroy();
    }

    private void AttemptParentRegistration() {
        //validate
        childName = childNameView.getText().toString();
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

        if (teacherName == null){
            Toast.makeText(ParentRegActivity.this, "Please choose a teacher from the drop down", Toast.LENGTH_LONG).show();

        }
        /*if (TextUtils.isEmpty(teacher)) {
            teacherNameView.setError(getString(R.string.error_field_required));
            teacherNameView.requestFocus();
            return;
        }*/
        String dob_str = childDobView.getText().toString();
        String[] split = dob_str.split("/");
        if(split[split.length-1].length() < 4 ){
            childDobView.setError("Use format DD/MM/YYYY");
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        Date dob = null;
        try {
            dob = sdf.parse(dob_str);
            Date now = new Date();
            if (now.getTime() - dob.getTime() < 0) {
                Toast.makeText(ParentRegActivity.this, "You have entered an invalid date, please try again.", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(ParentRegActivity.this, "You have entered an invalid date, please try again.", Toast.LENGTH_LONG).show();
            return;
        }
        //Date dob = (Date) dobView.getText();
        Calendar cal = Calendar.getInstance();
        cal.setLenient(false);
        cal.setTime(dob);
        try {
            cal.getTime();
        }
        catch (Exception e) {
            System.out.println("Invalid date");
            Toast.makeText(ParentRegActivity.this, "You have entered an invalid date, please try again.", Toast.LENGTH_LONG).show();
            return;
        }
        childDob = dob;
        findPupil();
        //find child uid
        /*DatabaseReference classRef = mDatabase.child("classes").child(teacher);
        if (classRef == null) {
            Toast.makeText(ParentRegActivity.this, "You have entered an invalid class ID, please try again.", Toast.LENGTH_LONG).show();
            return;
        }*/
    }

    private void populate_teacher_dropdown(final ArrayList<String> teachers, ArrayList<String> teachernames){
        System.out.println("Teachers: " + teachernames);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ParentRegActivity.this,
                android.R.layout.simple_spinner_item,teachernames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teachers_dropdown.setAdapter(adapter);
        teachers_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                teacherName = (String) adapterView.getItemAtPosition(i);
                teacherUID = teachers.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void findPupil() {
        //Get class from teacher UID
        DatabaseReference teacherRef = mDatabase.child("teachers").child(teacherUID);
        teacherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String classUID = (String) dataSnapshot.child("class_id").getValue();
                isPupilInClass(classUID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void isPupilInClass(String classUID){
        DatabaseReference classRef = mDatabase.child("classes").child(classUID);
        classRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean pupil_found = false;
                for (DataSnapshot d : dataSnapshot.getChildren()){
                    if (((String) d.getValue()).equals(childName)){
                        pupil_found = true;
                        checkPupilDetails(d.getKey());
                    }
                }
                if (pupil_found == false) {
                    System.out.println("Pupil " + childName + "not found, Registration unsuccessful");
                    Toast.makeText(ParentRegActivity.this, "Pupil " + childName + " not found, Registration unsuccessful", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkPupilDetails(final String pupilUID){
        DatabaseReference pupilRef = mDatabase.child("pupils").child(pupilUID);
        pupilRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long dob_l = (Long) dataSnapshot.child("dob").getValue();
                if (dob_l == childDob.getTime()) {
                    register(pupilUID);
                }else {
                    System.out.println("Pupil " + childName + "not found, Registration unsuccessful");
                    Toast.makeText(ParentRegActivity.this, "Pupil " + childName + " Date of Birth Incorrect, Registration unsuccessful", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void register(String pupilUID){
        //add parent to db
        String email = getIntent().getStringExtra("EMAILVALUE");
        String name = getIntent().getStringExtra("NAMEVALUE");
        Parent p = new Parent(email, name, pupilUID);
        DatabaseReference newRef = mDatabase.child("parents").getRef();
        newRef.child(mAuth.getCurrentUser().getUid()).setValue(p);
        registered = true;
        Intent i = new Intent(ParentRegActivity.this, MainParentActivity.class);
        ParentRegActivity.this.startActivity(i);
    }

    private void removeUser(){
        FirebaseUser user = mAuth.getCurrentUser();
        user.delete();
    }
}
