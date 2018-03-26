package com.example.cassie_app;

public class Parent {

    private String email;
    private String name;
    private String pupil_id;

    public Parent(String email, String name, String pupil_id){
        this.email = email;
        this.name = name;
        this.pupil_id = pupil_id;
    }

    public String getPupil_id() {
        return pupil_id;
    }

    public void setPupil_id(String pupil_id) {
        this.pupil_id = pupil_id;
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


}
