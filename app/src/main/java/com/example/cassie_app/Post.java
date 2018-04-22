package com.example.cassie_app;


public class Post {
    String uid;
    String description;
    String author;
    String pupil;
    String date;
    String content;
    String image;
    String area;
    boolean golden;
    public Post (String uid, String description, String author, String pupil, String date, String content, String image, String area, boolean golden){
        this.description = description;
        this.author = author;
        this.golden = golden;
        this.pupil = pupil;
        this.date = date;
        this.content = content;
        this.uid = uid;
        this.image = image;
        this.area = area;
    }

    //constructor for recyled card view
    public Post (String author, String content, String date, String image, String area, boolean golden){
        this.author = author;
        this.golden = golden;
        this.content = content;
        this.date = date;
        this.area = area;
        this.pupil = "";
        this.description = "";
        this.uid = "";
        this.image = image;
    }


}
