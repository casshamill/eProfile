package com.example.cassie_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    private FirebaseAuth mAuth;

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
        final String name = nameView.getText().toString();
        final String email = emailView.getText().toString();
        String pw = pwordView.getText().toString();
        String pw_conf = pwordConfView.getText().toString();
        mAuth = FirebaseAuth.getInstance();

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

        mAuth.createUserWithEmailAndPassword(email, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            System.out.println("User " + user.getUid() + "Registered");
                            if (radioGroupView.getCheckedRadioButtonId() == R.id.reg_parent) {
                                Intent i = new Intent(RegActivity.this, ParentRegActivity.class);
                                i.putExtra("EMAILVALUE",email);
                                i.putExtra("NAMEVALUE",name);
                                RegActivity.this.startActivity(i);
                            }
                            else if (radioGroupView.getCheckedRadioButtonId() == R.id.reg_teacher){
                                Intent i = new Intent(RegActivity.this, TeacherRegActivity.class);
                                i.putExtra("EMAILVALUE",email);
                                i.putExtra("NAMEVALUE",name);
                                RegActivity.this.startActivity(i);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            System.out.println("Regestration failed");
                            System.out.println("Failure Reason:" + task.getException());
                        }

                        // ...
                    }
                });
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
