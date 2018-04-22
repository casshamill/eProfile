package com.example.cassie_app;

import com.google.firebase.database.DatabaseReference;

import java.util.Date;

/**
 * Created by Cassie on 24/03/2018.
 */

public class Pupil {
    private String name;
    private long dob;
    private String classID;
    private DatabaseReference feed;

    public Pupil(String name, long dob, String classID){
        this.name = name;
        this.classID = classID;
        this.dob = dob;
    }

    public Pupil(String name){
        this.name = name;
        this.classID = "Dummy for now";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public long getDOB() {
        return dob;
    }

    public void setDOB(long dob) {
        this.dob = dob;
    }

    public DatabaseReference getFeed() {
        return feed;
    }

    public void setFeed(DatabaseReference feed) {
        this.feed = feed;
    }
}
