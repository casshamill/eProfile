package com.example.cassie_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TeacherRegActivity extends AppCompatActivity {

    private EditText classIdView;

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
    }

    private void AttemptTeacherRegistration() {
        //validate
        String classId = classIdView.getText().toString();

        if (TextUtils.isEmpty(classId)) {
            classIdView.setError(getString(R.string.error_field_required));
            classIdView.requestFocus();
            return;
        }
        else {
            Intent i = new Intent(TeacherRegActivity.this, MainTeacherActivity.class);
            TeacherRegActivity.this.startActivity(i);
        }
        //async task to register
        //TODO set up mysql db for this.
    }
}