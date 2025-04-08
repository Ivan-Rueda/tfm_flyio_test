package com.irueda.tfm_rest.domain;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Document(collection = "climas")
public class Clima {
    @Id
    @Schema(description = "Mongo ObjectID único del registro de clima ", example = "603d2e16e7179a20d8f3d547")
    private String id;

    @NotNull(message = "La latitud es obligatoria")
    @Schema(description = "Latitud reportada por el dispositivo LoRa", example = "-34.60", required = true)
    private float latitud;

    @NotNull(message = "La longitud es obligatoria")
    @Schema(description = "Longitud reportada por el dispositivo LoRa", example = "-58.38", required = true)
    private float longitud;

    @NotNull(message = "La fecha de creación es obligatoria")
    @Schema(description = "Fecha y hora en que se registró el clima", example = "2023-08-01T12:34:56.789Z", required = true)
    private Instant createdAt;

    @NotNull(message = "La temperatura es obligatoria")
    @Schema(description = "Temperatura registrada en grados Celsius (extraído del API externa). Será tenido en cuenta por 'Alerta' si es menor a 14 o mayor a 27.", example = "25", required = true)
    private int temperatura;

    @NotNull(message = "La humedad es obligatoria")
    @Schema(description = "Humedad relativa en porcentaje. Será tenido en cuenta por 'Alerta' si es mayor a 70.", example = "37", required = true)
    private int humedad;

    @NotNull(message = "La velocidad del viento es obligatoria")
    @Schema(description = "Velocidad del viento en kilómetros por hora.  Será tenido en cuenta por 'Alerta' si es mayor a 15.", example = "15", required = true)
    private int viento;

    @NotNull(message = "El índice UV es obligatorio.")
    @Schema(description = "Índice UV registrado. Será tenido en cuenta por 'Alerta' si es mayor a 5.", example = "4", required = true)
    private int uv;

    public Clima(float latitud, float longitud, Instant createdAt, int temperatura, int humedad, int viento, int uv) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.createdAt = createdAt;
        this.temperatura = temperatura;
        this.humedad = humedad;
        this.viento = viento;
        this.uv = uv;
    }

    public Clima() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public int getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(int temperatura) {
        this.temperatura = temperatura;
    }

    public int getHumedad() {
        return humedad;
    }

    public void setHumedad(int humedad) {
        this.humedad = humedad;
    }

    public int getViento() {
        return viento;
    }

    public void setViento(int viento) {
        this.viento = viento;
    }

    public int getUv() {
        return uv;
    }

    public void setUv(int uv) {
        this.uv = uv;
    }

}
