package com.example.cassie_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class TeacherSettingsFragment extends Fragment {

    final int ADD_REQUEST_CODE = 0;
    final int DELETE_REQUEST_CODE = 1;
    final int EDIT_REQUEST_CODE = 2;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String classUID;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_teacher_settings, container, false);
        Button edit_pupil_btn = (Button) view.findViewById(R.id.button_editpupil);
        final Button add_pupil_btn = (Button)view.findViewById(R.id.button_addpupil);
        Button delete_pupil_btn = (Button) view.findViewById(R.id.button_deletepupil);
        Button delete_class_btn = (Button) view.findViewById(R.id.button_deleteclass);
        Button edit_pword_btn = (Button) view.findViewById(R.id.button_changepword);

        add_pupil_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddPupilPopup.class);
                TeacherSettingsFragment.this.startActivityForResult(i, ADD_REQUEST_CODE);
            }
        });
        edit_pupil_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPupil();
            }
        });
        delete_pupil_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePupil();
            }
        });
        delete_class_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete Class")
                        .setMessage("Are you sure you want to delete your class?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                deleteClass();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
        edit_pword_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPassword();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        getClassId();
        return view;

    }

    private void addPupil(String classUID, String name, long dob){
        //add to pupils list
        DatabaseReference pupilsRef = mDatabase.child("pupils").getRef();
        DatabaseReference newRef = pupilsRef.push();
        String uid = newRef.getKey();
        newRef.setValue(new Pupil(name, dob, classUID));

        //add to class list
        DatabaseReference classRef = mDatabase.child("classes").child(classUID).getRef();
        classRef.child(uid).setValue(name);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final String name = data.getStringExtra("NAMEVALUE");
        long dob = data.getLongExtra("DOBVALUE", -1);
        if (requestCode == ADD_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                addPupil(classUID, name, dob);
            }
        }
    }

    private void getClassId(){
        String teacherUID = mAuth.getCurrentUser().getUid();
        DatabaseReference teacher = mDatabase.child("teachers").child(teacherUID).getRef();
        teacher.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                classUID = (String) dataSnapshot.child("class_id").getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void deletePupil(){
        Intent i = new Intent(getActivity(), PupilListActivity.class);
        i.putExtra("parent", "delete");
        TeacherSettingsFragment.this.startActivity(i);
    }

    private void deleteClass(){
        DatabaseReference classRef = mDatabase.child("classes").child(classUID);
        classRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(), "Your Class has been removed", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "An error occurred, please try again.", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Class failed to delete." + task.getException());
                }
            }
        });
    }

    private void editPupil(){
        Intent i = new Intent(getActivity(), PupilListActivity.class);
        i.putExtra("parent", "edit");
        TeacherSettingsFragment.this.startActivity(i);
    }

    private void editPassword(){
        String emailAddress = "";

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            emailAddress = user.getEmail();
            mAuth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                                Toast.makeText(getActivity(), "A Password Reset Email has been Sent", Toast.LENGTH_LONG).show();
                            } else {
                                    Log.d(TAG, "Email failed to send.");
                                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}

