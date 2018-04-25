package com.example.cassie_app;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class TeacherSettingsFragmentTest {

    TeacherSettingsFragment test;
    LoginActivity login;
    ;
    @Before
    public void setup(){
        test = new TeacherSettingsFragment();
            //test.setClassUID("test");
            login = new LoginActivity();

    }

    @Test
    public void onCreateView() throws Exception {
    }

    @Test
    public void onActivityResult() throws Exception {
    }

    @Test
    public void addPupil() throws Exception{
       // Method method = login.getDe();
    }

    @Test
    public void DeletePupil() throws Exception{
    }

    @Test
    public void EditPupil() throws Exception{
    }

    @Test
    public void editPassword() throws Exception{
    }


}