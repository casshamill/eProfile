package com.example.cassie_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class TeacherUploadFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_teacher_upload, container, false);
        Button TextUpButton = (Button) view.findViewById(R.id.buttontext);
        Button CameraButton = (Button) view.findViewById(R.id.buttoncamera);
        CameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });
        TextUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadText();
            }
        });
        return view;
    }

    public void openCamera(){
        Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(i);
    }

    public void uploadText(){
        UploadTextFragment f = new UploadTextFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.teacherContainer, f, "FragmentName");
        fragmentTransaction.commit();
    }

}
