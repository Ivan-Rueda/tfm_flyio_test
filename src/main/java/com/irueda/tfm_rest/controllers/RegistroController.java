package com.irueda.tfm_rest.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.irueda.tfm_rest.domain.Registro;
import com.irueda.tfm_rest.services.RegistroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/registros/v1")
@Tag(name = "Registro API", description = "API para la gestión de registros enviados desde dispositivos LoRa 📡 🔢")
public class RegistroController {

    @Autowired
    private RegistroService rService;

    @Operation(summary = "Insertar datos de prueba semilla en la base de datos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Base de datos inicializada con éxito", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "500", description = "Error al inicializar la base de datos", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("seed")
    public ResponseEntity<?> seedDatabase() {
        try {
            rService.seedDatabase();
            return new ResponseEntity<>("Datos semilla de registros (enviados desde dispositivos LoRa) completada!", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error al cargar datos semilla: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Listar todos los registros")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de todos los registros", content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<?> listAll() {
        List<Registro> registros = rService.getAll();
        return new ResponseEntity<List<Registro>>(registros, HttpStatus.OK);
    }

    @Operation(summary = "Listar los registros de la última hora")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de registros de la última hora", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("lastHour")
    public ResponseEntity<?> listRegisterLastHour() {
        List<Registro> registros = rService.getRegistrosLastHour();
        return new ResponseEntity<List<Registro>>(registros, HttpStatus.OK);
    }

    @Operation(summary = "Crear un nuevo registro")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Registro creado con éxito", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "text/plain"))
    })
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Registro registro) {

        if (registro.getLatitud() == 0.0f) {
            return new ResponseEntity<String>("La latitud es obligatoria", HttpStatus.BAD_REQUEST);
        }

        if (registro.getLongitud() == 0.0f) {
            return new ResponseEntity<String>("La longitud es obligatoria", HttpStatus.BAD_REQUEST);
        }

        if (registro.getMacDispositivo() == null || registro.getMacDispositivo().isEmpty()) {
            return new ResponseEntity<String>("La dirección MAC del dispositivo es obligatoria",
                    HttpStatus.BAD_REQUEST);
        }

        if (registro.getRssi() == 0.0f) {
            return new ResponseEntity<String>("El rssi es obligatorio", HttpStatus.BAD_REQUEST);
        }

        registro.setCreatedAt(new Date().toInstant());

        Registro r = rService.create(registro);

        return new ResponseEntity<Registro>(r, HttpStatus.CREATED);

    }

}
