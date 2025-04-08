package com.irueda.tfm_rest.domain;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Document(collection = "registros")
public class Registro {
   @Schema(description = "Mongo ObjectID único del registro ", example = "66c5e4712a327515dc761cdf", required = true)
    @Id
    private String id;

    @Schema(description = "Fecha y hora de creación del registro (de datos enviados por el dispositivo LoRa)", example = "2024-08-22T14:30:00Z", required = true)
    private Instant createdAt;

    @NotNull(message = "La latitud es obligatoria")
    @Schema(description = "Latitud de la ubicación GPS reportada por el dispositivo LoRa. Positivio o Negativo (-)", example = "35.68", required = true)
    private float latitud;

    @NotNull(message = "La longitud es obligatoria")
    @Schema(description = "Longitud de la ubicación GPS reportada por el dispositivo LoRa. Positivio o Negativo (-)", example = "139.69", required = true)
    private float longitud;

    @NotBlank(message = "La dirección MAC del dispositivo LoRa es obligatoria")
    @Schema(description = "Dirección MAC del dispositivo LoRa que envió el registro", example = "94F0641C5210", required = true)
    private String macDispositivo;

    @Schema(description = "Resultados del registro", required = false)
    private ResultadosRegistro resultados;

    @NotNull(message = "El valor de RSSI es obligatorio")
    @Schema(description = "Intensidad de la señal recibida (RSSI)", example = "-70", required = true)
    private float rssi;

    public Registro(Instant createdAt, float latitud, float longitud, String macDispositivo,
            ResultadosRegistro resultados, float rssi) {
        this.createdAt = createdAt;
        this.latitud = latitud;
        this.longitud = longitud;
        this.macDispositivo = macDispositivo;
        this.resultados = resultados;
        this.rssi = rssi;
    }

    public Registro() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ResultadosRegistro getResultados() {
        return resultados;
    }

    public void setResultados(ResultadosRegistro resultados) {
        this.resultados = resultados;
    }

    public float getRssi() {
        return rssi;
    }

    public void setRssi(float rssi) {
        this.rssi = rssi;
    }

}
