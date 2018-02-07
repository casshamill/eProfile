package com.example.cassie_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParentRegActivity extends AppCompatActivity {

    private EditText childNameView;
    private EditText childDobView;
    private EditText classNameView;

    private Pattern pattern;
    private Matcher matcher;


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

    }

    private void AttemptParentRegistration() {
        //validate
        String childName = childNameView.getText().toString();
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

        else {
            Intent i = new Intent(ParentRegActivity.this, MainParentActivity.class);
            ParentRegActivity.this.startActivity(i);
        }

        //async task to register
        //TODO set up mysql db for this.
    }
}
