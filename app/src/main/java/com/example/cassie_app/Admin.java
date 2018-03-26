package com.example.cassie_app;

/**
 * Created by Tiarnan on 13/03/2018.
 */

public class Admin {
    private String school_id;
    private String email;

    public Admin(String email, String school_id){
        this.email = email;
        this.school_id = school_id;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
