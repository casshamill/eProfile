package com.example.cassie_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddPupilPopup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pupil_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.7), (int)(height*0.5));

        System.out.println("Starting Intent");

        final EditText nameView = (EditText) findViewById(R.id.pupilName);
        nameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameView.setText("");
            }
        });

        final EditText dobView = (EditText) findViewById(R.id.pupilDOB);
        dobView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dobView.setText("");
            }
        });

        Button addPupil_btn = (Button) findViewById(R.id.addPupil_btn);
        addPupil_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameView.getText().toString();
                String dob = dobView.getText().toString();
                if (name.equals("") || name.equals("Pupil Name")){
                    return;
                }
                if (dob.equals("") || name.equals("Pupil DOB")){
                    return;
                }
                Intent i = new Intent();
                i.putExtra("NAMEVALUE", name);
                i.putExtra("DOBVALUE", dob);
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

}
