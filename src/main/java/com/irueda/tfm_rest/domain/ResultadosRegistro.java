package com.irueda.tfm_rest.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class ResultadosRegistro {
    @NotNull(message = "El campo uso es obligatorio")
    @Schema(description = "Indica si el EPI está en uso", example = "true", required = true)
    private Boolean uso;

    @NotNull(message = "El campo movimiento es obligatorio")
    @Schema(description = "Indica si se detecta movimiento ", example = "false", required = true)
    private Boolean movimiento;

    @NotNull(message = "La temperatura es obligatoria")
    @Schema(description = "Temperatura reportada por el dispositivo", example = "25", required = true)
    private Integer temperatura;

    public ResultadosRegistro(Boolean uso, Boolean movimiento, Integer temperatura) {
        this.uso = uso;
        this.movimiento = movimiento;
        this.temperatura = temperatura;
    }

    public ResultadosRegistro() {
    }

    public Boolean getUso() {
        return uso;
    }

    public void setUso(Boolean uso) {
        this.uso = uso;
    }

    public Boolean getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Boolean movimiento) {
        this.movimiento = movimiento;
    }

    public Integer getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Integer temperatura) {
        this.temperatura = temperatura;
    }

}
