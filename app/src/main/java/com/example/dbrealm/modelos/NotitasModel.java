package com.example.dbrealm.modelos;

import io.realm.RealmObject;

public class NotitasModel extends RealmObject {

    private int id;
    private String titulo;
    private String descripcion;

    public NotitasModel(){

    }

    public NotitasModel(String nombre, String descripcion){

        this.titulo = nombre;
        this.descripcion = descripcion;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
