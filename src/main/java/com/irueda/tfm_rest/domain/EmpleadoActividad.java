package com.irueda.tfm_rest.domain;

public class EmpleadoActividad {
    private String id;
    private String nombres;
    private String apellidos;
    private String n_telefono;
    private String email;

    public EmpleadoActividad(String id, String nombres, String apellidos, String n_telefono, String email) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.n_telefono = n_telefono;
        this.email = email;
    }

    public EmpleadoActividad() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getN_telefono() {
        return n_telefono;
    }

    public void setN_telefono(String n_telefono) {
        this.n_telefono = n_telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
