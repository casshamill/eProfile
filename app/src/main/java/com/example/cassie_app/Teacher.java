package com.example.cassie_app;

public class Teacher {
    private String email;
    private String name;
    private String class_id;
    private String school_id;
    private boolean confirmed;

    public Teacher(String email, String name, String school_id){
        this.email = email;
        this.name = name;
        this.class_id = "-1";
        this.school_id = school_id;
        this.confirmed = false;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getConfirmed(){
        return this.confirmed;
    }

    public void setConfirmed(boolean conf){
        this.confirmed = conf;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

}
