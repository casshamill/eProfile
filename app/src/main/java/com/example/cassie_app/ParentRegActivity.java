package com.example.cassie_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParentRegActivity extends AppCompatActivity {

    private EditText childNameView;
    private EditText childDobView;
    private EditText classNameView;

    private String uid;
    private String email;
    private String name;
    private String pupil_id;
    private boolean pupil_found;

    private Pattern pattern;
    private Matcher matcher;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_reg);

        childNameView = (EditText) findViewById(R.id.child_name);
        childDobView = (EditText) findViewById(R.id.child_dob);
        classNameView = (EditText) findViewById(R.id.class_name);

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
        String childDob = childDobView.getText().toString();
        String className = classNameView.getText().toString();

        if (TextUtils.isEmpty(childName)) {
            childNameView.setError(getString(R.string.error_field_required));
            childNameView.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(childDob)) {
            childDobView.setError(getString(R.string.error_field_required));
            childDobView.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(className)) {
            classNameView.setError(getString(R.string.error_field_required));
            classNameView.requestFocus();
            return;
        }
        /*if (!(DateValidator(childDob))) {
            childDobView.setError(getString(R.string.error_date));
            childDobView.requestFocus();
            return; }*/

        pupil_found = false;
        //find child uid
        DatabaseReference childRef = mDatabase.child("pupils");
        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Finding child");
                for (DataSnapshot d : dataSnapshot.getChildren()){
                    String currname = (String) d.child("name").getValue();
                    if (currname.equals(childName)){
                        pupil_id = d.getKey();
                        pupil_found = true;
                        register();
                        break;
                    }
                }
                if (pupil_found == false){
                    System.out.println("Pupil " + childName + "not found, Registreation unsucessful");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
