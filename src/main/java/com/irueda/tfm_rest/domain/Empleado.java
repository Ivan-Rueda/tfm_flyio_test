package com.irueda.tfm_rest.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Document(collection = "empleados")
public class Empleado {
    @Id
    @Schema(required = true, description = "Mongo ObjectID Identificador único del empleado")
    private String id;

    @Schema(required = true, description = "Nombres del empleado")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombres;

    @Schema(required = true, description = "Apellidos del empleado")
    @NotBlank(message = "El nombre es obligatorio")
    private String apellidos;

    @Schema(required = true, description = "DNI del empleado")
    @NotBlank(message = "El DNI es obligatorio")
    private String dni;

    @Schema(required = true, description = "Número de teléfono del empleado")
    private String n_telefono;

    @Schema(required = true, description = "Correo electrónico del empleado")
    private String email;

    @Schema(description = "ID de Moodle asignado al empleado al crearlo en la Moodle API")
    private String id_moodle;

    @Schema(required = true, description = "Lista de cursos asociados al empleado")
    private List<Curso> cursos;

    public Empleado(String id, String nombres, String apellidos, String dni, String n_telefono, String email, String id_moodle,
            List<Curso> cursos) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.n_telefono = n_telefono;
        this.email = email;
        this.id_moodle = id_moodle;
        this.cursos = cursos;
    }

    public Empleado(String nombres, String apellidos, String dni, String n_telefono, String email, String id_moodle, List<Curso> cursos) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.n_telefono = n_telefono;
        this.email = email;
        this.id_moodle = id_moodle;
        this.cursos = cursos;
    }

    public Empleado() {
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
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

    public String getId_moodle() {
        return id_moodle;
    }

    public void setId_moodle(String id_moodle) {
        this.id_moodle = id_moodle;
    }
    
    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }

}
