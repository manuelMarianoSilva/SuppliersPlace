package com.pullapps.suppapp.View.model.pojo;

public class Compulsa {

    private String id;
    private String title;
    private String description;
    private String fechaCierre;
    private String pliego;
    private String urlImagen;

    public Compulsa() {

    }

    public Compulsa(String id, String title, String description, String fechaCierre, String pliego, String urlImagen) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.fechaCierre = fechaCierre;
        this.pliego = pliego;
        this.urlImagen = urlImagen;
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

    public String getPliego(){
        return pliego;
    }

    public String getUrlImagen() {
        return urlImagen;
    }
}
