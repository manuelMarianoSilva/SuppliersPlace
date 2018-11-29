package com.pullapps.suppapp.View.model.pojo;

public class Compulsa {

    private String id;
    private String title;
    private String description;
    private String fechaCierre;

    public Compulsa() {

    }

    public Compulsa(String id, String title, String description, String fechaCierre) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.fechaCierre = fechaCierre;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getFechaCierre() {
        return fechaCierre;
    }

    public String getId() {
        return id;
    }


}
