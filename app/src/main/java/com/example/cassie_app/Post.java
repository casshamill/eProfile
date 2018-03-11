package com.example.cassie_app;

public class Post {
    String uid;
    String description;
    String author;
    String pupil;
    String date;
    String content;

    public Post (String uid, String description, String author, String pupil, String date, String content ){
        this.description = description;
        this.author = author;
        this.pupil = pupil;
        this.date = date;
        this.content = content;
        this.uid = uid;
    }

    //constructor for recyled card view
    public Post (String author, String content, String date){
        this.author = author;
        this.content = content;
        this.date = date;
        this.pupil = "";
        this.description = "";
        this.uid = "";
    }
}