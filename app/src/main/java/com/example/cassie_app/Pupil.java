package com.example.cassie_app;

import com.google.firebase.database.DatabaseReference;
public class Pupil {
    private String name;
    private DatabaseReference feed;

    public Pupil(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DatabaseReference getFeed() {
        return feed;
    }

    public void setFeed(DatabaseReference feed) {
        this.feed = feed;
    }
}
