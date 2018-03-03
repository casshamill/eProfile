package com.example.cassie_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class RegActivity extends AppCompatActivity {

    private EditText schoolIdView;
    private EditText nameView;
    private EditText emailView;
    private RadioButton teacherbtnView;
    private RadioButton parentbtnView;
    private EditText pwordView;
    private EditText pwordConfView;
    private RadioGroup radioGroupView;
    private View mRegFormView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        schoolIdView = (EditText) findViewById(R.id.reg_school_id);
        nameView = (EditText) findViewById(R.id.reg_name);
        emailView = (EditText) findViewById(R.id.reg_email);
        pwordView = (EditText) findViewById(R.id.reg_pw);
        pwordConfView = (EditText) findViewById(R.id.reg_pw_conf);
        teacherbtnView = (RadioButton) findViewById(R.id.reg_teacher);
        parentbtnView = (RadioButton) findViewById(R.id.reg_parent);
        radioGroupView = (RadioGroup) findViewById(R.id.reg_radio);
        radioGroupView.check(R.id.reg_teacher);

        Button RegButton = (Button) findViewById(R.id.reg_submit);
        RegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttemptRegistration();
            }
        });

        mRegFormView = (View) findViewById(R.id.reg_content);
    }

    private void AttemptRegistration(){
        //validate
        String schoolId =schoolIdView.getText().toString();
        String name = nameView.getText().toString();
        String email = emailView.getText().toString();
        String pw = pwordView.getText().toString();
        String pw_conf = pwordConfView.getText().toString();
        if (TextUtils.isEmpty(schoolId)){
            schoolIdView.setError(getString(R.string.error_field_required));
            schoolIdView.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(name)){
            nameView.setError(getString(R.string.error_field_required));
            nameView.requestFocus();
            return;
        }
        if (!emailValid(email)){
            emailView.setError(getString(R.string.error_invalid_email));
            emailView.requestFocus();
            return;
        }
        if (!(passwordsValid(pw, pw_conf))) {
            return;
        }

        if (radioGroupView.getCheckedRadioButtonId() == R.id.reg_parent) {
            Intent i = new Intent(RegActivity.this, ParentRegActivity.class);
            RegActivity.this.startActivity(i);
        }
        else if (radioGroupView.getCheckedRadioButtonId() == R.id.reg_teacher){
            Intent i = new Intent(RegActivity.this, TeacherRegActivity.class);
            RegActivity.this.startActivity(i);
        }

        //async task to register
        //TODO set up mysql db for this.
    }

    private boolean passwordsValid(String pw, String pw_conf){
        if (pw.length() < 4 || pw_conf.length() < 4 ){
            pwordView.setError(getString(R.string.error_invalid_password));
            pwordView.requestFocus();
            return false;
        } else if (!pw.equals(pw_conf)){
            pwordConfView.setError(getString(R.string.error_password_not_match));
            pwordView.requestFocus();
            return false;
        }
        return true;
    }

    //TODO add more validation
    private boolean emailValid(String email){
        if (!email.contains("@")) return false;
        return true;
    }



}
