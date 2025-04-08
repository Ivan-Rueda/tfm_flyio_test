package com.irueda.tfm_rest.domain;

public class DispositivoActividad {
    private String id;
    private String mac;
    private String modelo;

    public DispositivoActividad(String id, String mac, String modelo) {
        this.id = id;
        this.mac = mac;
        this.modelo = modelo;
    }

    public DispositivoActividad() {
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

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

}
