package com.example.cassie_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;


public class TeacherSettingsFragment extends Fragment {

    private String email;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_teacher_settings, container, false);
        Button edit_pupil_btn = (Button) view.findViewById(R.id.button_editpupil);
        Button add_pupil_btn = (Button) view.findViewById(R.id.button_addpupil);
        Button delete_pupil_btn = (Button) view.findViewById(R.id.button_deletepupil);
        Button delete_class_btn = (Button) view.findViewById(R.id.button_deleteclass);
        Button edit_pword_btn = (Button) view.findViewById(R.id.button_changepword);

        add_pupil_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CreateClassActivity.class);
                TeacherSettingsFragment.this.startActivity(i);
            }
        });

        edit_pupil_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        delete_pupil_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        delete_class_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        edit_pword_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = email;
                FirebaseAuth auth = FirebaseAuth.getInstance();


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String email = user.getEmail();
                    System.out.println(email);
                }

                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");
                                    Toast.makeText(getActivity(), "A Password Reset Email has been Sent", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        return view;

    }

}
