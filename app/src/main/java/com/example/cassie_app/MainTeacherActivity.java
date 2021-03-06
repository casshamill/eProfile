package com.example.cassie_app;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainTeacherActivity extends AppCompatActivity implements TeacherUploadFragment.OnFragmentInteractionListener{

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profiles:
                   setTitle("Profiles");
                    TeacherProfilesFragment fragment1 = new TeacherProfilesFragment();
                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.replace(R.id.teacherContainer, fragment1, "FragmentName");
                    fragmentTransaction1.commit();
                    return true;
                case R.id.navigation_upload:
                    setTitle("Upload");
                    TeacherUploadFragment fragment2 = new TeacherUploadFragment();
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.teacherContainer, fragment2, "FragmentName");
                    fragmentTransaction2.commit();
                    return true;
                case R.id.navigation_notifications:
                    setTitle("Settings");
                    TeacherSettingsFragment fragment3 = new TeacherSettingsFragment();
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.teacherContainer, fragment3, "FragmentName");
                    fragmentTransaction3.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_teacher);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //when app starts uploadfragment will be displayed
        setTitle("Upload");
        TeacherUploadFragment fragment2 = new TeacherUploadFragment();
        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction2.replace(R.id.teacherContainer, fragment2, "FragmentName");
        fragmentTransaction2.commit();
    }

    public void onFragmentInteractionListener(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
