package com.example.cassie_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText currPasswordView;
    private EditText newPasswordView;
    private EditText matchPasswordView;
    private Button change_pword_btn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        change_pword_btn = findViewById(R.id.btn_submit_new_pword);
        change_pword_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptChange();
            }
        });
        mAuth = FirebaseAuth.getInstance();
    }

    public void attemptChange(){
        String curr_password = currPasswordView.getText().toString();
        String new_password = newPasswordView.getText().toString();
        String match_password = matchPasswordView.getText().toString();
        AuthCredential credential = EmailAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(), curr_password);
        mAuth.getCurrentUser().reauthenticate(credential).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                failed();
                
            }
        });



    }

    public void failed(){

    }

}


