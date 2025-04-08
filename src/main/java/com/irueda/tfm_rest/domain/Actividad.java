package com.irueda.tfm_rest.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Document(collection = "actividades")
public class Actividad {
    @Id
    @Schema(description = "Mongo ObjectID único de la actividad", example = "1234", required = true)
    private String id;

    @NotNull(message = "El empleado de la actividad es obligatorio")
    @Schema(description = "Empleado asociado a la actividad", required = true)
    private EmpleadoActividad empleado;

    @NotNull(message = "El dispositivo de la actividad es obligatorio")
    @Schema(description = "Dispositivo asociado a la actividad", required = true)
    private DispositivoActividad dispositivo;

    @NotBlank(message = "La tarea de la actividad es obligatoria")
    @Schema(description = "Tarea que se realiza en la actividad", example = "Inspección de antenas", required = true)
    private String tarea;

    @Schema(description = "Recomendaciones adicionales para la actividad", example = "Usar casco de seguridad")
    private String recomendaciones;

    @CreatedDate
    @Schema(description = "Fecha y hora de creación de la actividad", example = "2024-08-28T10:11:54Z", required = true)
    private Instant createdAt;

    public Actividad(EmpleadoActividad empleado, DispositivoActividad dispositivo, String tarea,
            String recomendaciones) {
        this.empleado = empleado;
        this.dispositivo = dispositivo;
        this.tarea = tarea;
        this.recomendaciones = recomendaciones;
        this.createdAt = Instant.now();
    }

    public Actividad() {
        this.createdAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EmpleadoActividad getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmpleadoActividad empleado) {
        this.empleado = empleado;
    }

    public DispositivoActividad getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(DispositivoActividad dispositivo) {
        this.dispositivo = dispositivo;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public String getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendaciones(String recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
