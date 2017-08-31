package com.masotech.popularmovies_stage1_recyclerview.models;

/**
 * Created by maysaraodeh on 25/08/2017.
 */

public class Review {

    private String id;
    private String author;
    private String content;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
