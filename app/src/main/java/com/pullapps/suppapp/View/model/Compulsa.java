package com.pullapps.suppapp.View.model;

public class Compulsa {

    private String id;
    private String title;

    public Compulsa(){

    }

    public Compulsa(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

}
