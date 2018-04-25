package com.example.cassie_app;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.widget.Button;
import android.widget.EditText;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class AddPupilPopupTest {

    AddPupilPopup add;
    @Before
    public void setup(){
        add = new AddPupilPopup();
    }

    @Rule
    public ActivityTestRule<AddPupilPopup> mActivityRule = new ActivityTestRule<>(
            AddPupilPopup.class);
    @Test
    public void addPupil() throws Exception {
//        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(NextActivity.class.getName(), null, false);
        final EditText nameView = (EditText) add.findViewById(R.id.pupilName);
        final EditText dobView = (EditText) add.findViewById(R.id.pupilDOB);
        final Button add_btn = (Button) add.findViewById(R.id.addPupil_btn);
        dobView.setText("");
        nameView.setText("");
        add_btn.performClick();
    }



}