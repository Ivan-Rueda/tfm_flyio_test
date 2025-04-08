package com.irueda.tfm_rest.domain;

import io.swagger.v3.oas.annotations.media.Schema;

public class Curso {
    @Schema(required = true, description = "Identificador del curso que se ha asignado en Moodle. Se utiliza para la automatriculación de los empleados") 
    private String id;

    @Schema(required = true, description = "Nombre descriptivo del curso.")
    private String nombre;

    @Schema(description = "Indica si el curso ha sido aprobado por el empleado.")
    private boolean aprobado;

    public Curso(String id, String nombre, boolean aprobado) {
        this.id = id;
        this.nombre = nombre;
        this.aprobado = aprobado;
    }

    public Curso() {
    }

    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isAprobado() {
        return aprobado;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }
}
