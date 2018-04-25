package com.example.cassie_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
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
                String dob_str = dobView.getText().toString();
                String[] split = dob_str.split("/");
                if(split[split.length-1].length() < 4 ){
                    dobView.setError("Use format DD/MM/YYYY");
                    return;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                Date dob = null;
                try {
                    dob = sdf.parse(dob_str);
                    Date now = new Date();
                    if (now.getTime() - dob.getTime() < 0) {
                        Toast.makeText(AddPupilPopup.this, "You have entered an invalid date, please try again.", Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(AddPupilPopup.this, "You have entered an invalid date, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
                //Date dob = (Date) dobView.getText();
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        super.onBackPressed();
    }

}
