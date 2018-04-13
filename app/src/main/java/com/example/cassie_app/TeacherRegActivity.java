package com.example.cassie_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TeacherRegActivity extends AppCompatActivity {

    private EditText classIdView;
    private Button RegButton;
    private Button exit_btn;
    private TextView reg_complete;

    private String name;
    private String email;
    private String schoolId;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_reg);

        classIdView = (EditText) findViewById(R.id.class_id);
        RegButton = (Button) findViewById(R.id.reg_submit2);

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
        schoolId = getIntent().getStringExtra("SCHOOLVALUE");
        Teacher t = new Teacher(email, name, schoolId);
        DatabaseReference newRef = mDatabase.child("teachers").getRef();
        newRef.child(uid).setValue(t);
        newRef = mDatabase.child("schools").child(schoolId);
        newRef.child(uid).setValue(t.getConfirmed());
        Intent i = new Intent(TeacherRegActivity.this, WaitForConfirmedActivity.class);
        TeacherRegActivity.this.startActivity(i);
    }

}