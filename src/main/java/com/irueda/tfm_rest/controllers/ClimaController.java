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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.irueda.tfm_rest.domain.Clima;
import com.irueda.tfm_rest.services.ClimaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/climas/v1")
@Tag(name = "Clima API", description = "API para la gestión de datos climatológicos extraídos del API externa 🌦️")
public class ClimaController {

    @Autowired
    private ClimaService cService;

    @Operation(summary = "Insertar datos de prueba semilla en la base de datos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Base de datos inicializada con éxito", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "500", description = "Error al inicializar la base de datos", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("seed")
    public ResponseEntity<?> seedDatabase() {
        try {
            cService.seedDatabase();
            return new ResponseEntity<>("Datos semilla de clima completada!", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error al cargar datos semilla: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Listar todos los registros de clima")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de todos los registros de clima", content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<?> listAll() {
        List<Clima> climas = cService.getAll();
        return new ResponseEntity<List<Clima>>(climas, HttpStatus.OK);
    }

    @Operation(summary = "Obtener el último registro de clima para una ubicación específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Último registro de clima encontrado", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "No se encontró un registro de clima para la ubicación especificada", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("last")
    public ResponseEntity<?> getLastestClima(@RequestParam float latitud, @RequestParam float longitud) {

        Clima lastClima = cService.getLastestClimaByLatLong(latitud, longitud);

        if (lastClima.getId() == null) {
            return new ResponseEntity<String>("No se encontró un registro de clima para la ubicación especificada", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Clima>(lastClima, HttpStatus.OK);

    }

    @Operation(summary = "Crear un nuevo registro de clima")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Registro de clima creado con éxito", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "text/plain"))
    })
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Clima clima) {

        if (clima.getLatitud() == 0.0f) {
            return new ResponseEntity<String>("La latitud es obligatoria", HttpStatus.BAD_REQUEST);
        }

        if (clima.getLongitud() == 0.0f) {
            return new ResponseEntity<String>("La longitud es obligatoria", HttpStatus.BAD_REQUEST);
        }

        if (clima.getTemperatura() == 0) {
            return new ResponseEntity<String>("La temperatura es obligatoria", HttpStatus.BAD_REQUEST);
        }

        if (clima.getHumedad() == 0) {
            return new ResponseEntity<String>("La humedad es obligatoria", HttpStatus.BAD_REQUEST);
        }

        if (clima.getViento() == 0) {
            return new ResponseEntity<String>("El viento es obligatorio", HttpStatus.BAD_REQUEST);
        }

        if (clima.getUv() == 0) {
            return new ResponseEntity<String>("El índice UV es obligatorio", HttpStatus.BAD_REQUEST);
        }

        clima.setCreatedAt(new Date().toInstant());

        Clima c = cService.create(clima);

        return new ResponseEntity<Clima>(c, HttpStatus.CREATED);

    }

}
