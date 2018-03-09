package com.example.cassie_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class TeacherRegActivity extends AppCompatActivity {

    private EditText classIdView;
    private String name;
    private String email;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_reg);

        classIdView = (EditText) findViewById(R.id.class_id);
        Button RegButton = (Button) findViewById(R.id.reg_submit2);

        RegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttemptTeacherRegistration();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    private void AttemptTeacherRegistration() {
        //validate
        String classId = classIdView.getText().toString();

        if (TextUtils.isEmpty(classId)) {
            classIdView.setError(getString(R.string.error_field_required));
            classIdView.requestFocus();
            return;
        }
        String uid = mAuth.getCurrentUser().getUid();
        email = getIntent().getStringExtra("EMAILVALUE");
        name = getIntent().getStringExtra("NAMEVALUE");
        Teacher t = new Teacher(email, name, "1" );
        DatabaseReference newRef = mDatabase.child("teachers").getRef();
        newRef.child(uid).setValue(t);
        //needs changed to wait for confirmed activity
        //Intent i = new Intent(TeacherRegActivity.this, MainTeacherActivity.class);
        //TeacherRegActivity.this.startActivity(i);
    }

}