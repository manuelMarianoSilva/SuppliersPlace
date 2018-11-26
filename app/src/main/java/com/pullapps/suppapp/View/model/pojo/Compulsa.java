package com.pullapps.suppapp.View.model.pojo;

public class Compulsa {

    private String id;
    private String title;
    private String description;

    public Compulsa() {

    }

    public Compulsa(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }
}
