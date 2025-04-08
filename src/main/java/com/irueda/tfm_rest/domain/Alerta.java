package com.irueda.tfm_rest.domain;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;

@Document(collection = "alertas")
public class Alerta {
    @Id
    @Schema(required = true, description = "Mongo ObjectID único de la alerta")
    private String id;
    
    @Schema(required = true, description = "Tipo de alerta")
    private String tipo;

    @Schema(required = true, description = "Descripción de la alerta")
    private String descripcion;

    @Schema(required = true, description = "Fecha de creación de la alerta")
    private Instant createdAt;

    @Schema(required = true, description = "Latitud de la ubicación de la alerta")
    private float latitud;

    @Schema(required = true, description = "Longitud de la ubicación de la alerta")
    private float longitud;

    @Schema(required = true, description = "Dirección MAC del dispositivo relacionado con la alerta")
    private String macDispositivo;

    @Schema(required = true, description = "Indica si la alerta ha sido atendida")
    private boolean atendido;

    @Schema(required = true, description = "Fecha de la última actualización de la alerta")
    private Instant updatedAt;

    public Alerta(String tipo, String descripcion, Instant createdAt, float latitud, float longitud,
            String macDispositivo, boolean atendido, Instant updatedAt) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.createdAt = createdAt;
        this.latitud = latitud;
        this.longitud = longitud;
        this.macDispositivo = macDispositivo;
        this.atendido = atendido;
        this.updatedAt = updatedAt;
    }

    public Alerta() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public String getMacDispositivo() {
        return macDispositivo;
    }

    public void setMacDispositivo(String macDispositivo) {
        this.macDispositivo = macDispositivo;
    }

    public boolean isAtendido() {
        return atendido;
    }

    public void setAtendido(boolean atendido) {
        this.atendido = atendido;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

}
