package com.example.cassie_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class AddPupilPopup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pupil_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.6), (int)(height*0.3));

        System.out.println("Starting Intent");

        final EditText nameView = (EditText) findViewById(R.id.pupilName);
        final EditText dobView = (EditText) findViewById(R.id.pupilDOB);

        Button addPupil_btn = (Button) findViewById(R.id.addPupil_btn);
        addPupil_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameView.getText().toString();
                if (name.equals("") || name.equals("Pupil Name")) {
                    return;
                }
                Date dob = (Date) dobView.getText();
                Calendar cal = Calendar.getInstance();
                cal.setLenient(false);
                cal.setTime(dob);
                try {
                    cal.getTime();
                }
                catch (Exception e) {
                    System.out.println("Invalid date");
                    Toast.makeText(AddPupilPopup.this, "You have entered an invalid date, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent i = new Intent();
                i.putExtra("NAMEVALUE", name);
                i.putExtra("DOBVALUE", dob.getTime());
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

}
