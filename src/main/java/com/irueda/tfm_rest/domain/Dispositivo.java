package com.irueda.tfm_rest.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;

@Document(collection = "dispositivos")
public class Dispositivo {
    @Id
    @Schema(required = true, description = "Mongo ObjectID Identificador único del dispositivo")
    private String id;

    @Schema(required = true, description = "Dirección MAC LoRa del dispositivo")
    private String mac;

    @Schema(required = true, description = "Fabricante del dispositivo")
    private String fabricante;

    @Schema(required = true, description = "Modelo del dispositivo")
    private String modelo;

    public Dispositivo(String mac, String fabricante, String modelo) {
        this.mac = mac;
        this.fabricante = fabricante;
        this.modelo = modelo;
    }

    public Dispositivo(String id, String mac, String fabricante, String modelo) {
        this.id = id;
        this.mac = mac;
        this.fabricante = fabricante;
        this.modelo = modelo;
    }

    public Dispositivo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

}
