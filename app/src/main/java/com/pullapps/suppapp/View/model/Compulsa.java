package com.pullapps.suppapp.View.model;

public class Compulsa {

    private String title;
    private String description;

    public Compulsa(){

    }

    public Compulsa(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
