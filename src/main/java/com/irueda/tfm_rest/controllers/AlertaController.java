package com.irueda.tfm_rest.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.irueda.tfm_rest.domain.Alerta;
import com.irueda.tfm_rest.services.AlertaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/alertas/v1")
@Tag(name = "Alerta API", description = "API para la gestión de alertas 🚨")
public class AlertaController {

    @Autowired
    private AlertaService aService;

    @Operation(summary = "Insertar datos de prueba semilla en la base de datos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Base de datos inicializada con éxito", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "500", description = "Error al inicializar la base de datos", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("seed")
    public ResponseEntity<?> seedDatabase() {
        try {
            aService.seedDatabase();
            return new ResponseEntity<>("Datos semilla de alerta completada!", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error al cargar datos semilla: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Listar todas las alertas, con opción de filtrar por fecha [Mes/día/año]")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de todas las alertas", content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<?> listAll(@RequestParam(required = false) Optional<String> date) {

        if (date.isPresent() && !date.get().isEmpty()) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("M/d/uuuu", Locale.US);
            LocalDate localDate = LocalDate.parse(date.get(), dateTimeFormatter);
            List<Alerta> alertasHoy = aService.getAlertsByDay(localDate);
            return new ResponseEntity<List<Alerta>>(alertasHoy, HttpStatus.OK);
        }

        List<Alerta> alertas = aService.getAll();
        return new ResponseEntity<List<Alerta>>(alertas, HttpStatus.OK);
    }

    @Operation(summary = "Obtener una alerta por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Alerta encontrada", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Alerta no encontrada", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        Optional<Alerta> alerta = aService.findById(id);

        if (!alerta.isPresent()) {
            return new ResponseEntity<String>("No se encontró una alerta con id: " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Alerta>(alerta.get(), HttpStatus.OK);
    }

    @Operation(summary = "Crear una nueva alerta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Alerta creada con éxito", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "text/plain"))
    })
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Alerta alerta) {

        if (alerta.getTipo() == null || alerta.getTipo().isEmpty()) {
            return new ResponseEntity<String>("El tipo de alerta es obligatorio", HttpStatus.BAD_REQUEST);
        }

        if (alerta.getDescripcion() == null || alerta.getDescripcion().isEmpty()) {
            return new ResponseEntity<String>("La descripción de la alerta es obligatoria", HttpStatus.BAD_REQUEST);
        }

        if (alerta.getMacDispositivo() == null || alerta.getMacDispositivo().isEmpty()) {
            return new ResponseEntity<String>("La dirección MAC del dispositivo es obligatoria",
                    HttpStatus.BAD_REQUEST);
        }

        if (alerta.getLatitud() == 0.0f) {
            return new ResponseEntity<String>("La latitud es obligatoria", HttpStatus.BAD_REQUEST);
        }

        if (alerta.getLongitud() == 0.0f) {
            return new ResponseEntity<String>("La longitud es obligatoria", HttpStatus.BAD_REQUEST);
        }

        alerta.setAtendido(false);
        alerta.setCreatedAt(new Date().toInstant());
        alerta.setUpdatedAt(new Date().toInstant());

        Alerta a = aService.create(alerta);

        return new ResponseEntity<Alerta>(a, HttpStatus.CREATED);

    }

    @Operation(summary = "Actualizar una alerta existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Alerta actualizada con éxito", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Alerta no encontrada", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "text/plain"))
    })
    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Alerta alerta) {

        Optional<Alerta> alertaToUpdate = aService.findById(id);

        if (!alertaToUpdate.isPresent()) {
            return new ResponseEntity<String>("No se encontró una alerta con id: " + id, HttpStatus.NOT_FOUND);
        }

        if (alerta.getTipo() == null || alerta.getTipo().isEmpty()) {
            return new ResponseEntity<String>("El tipo de alerta es obligatorio", HttpStatus.BAD_REQUEST);
        }

        if (alerta.getDescripcion() == null || alerta.getDescripcion().isEmpty()) {
            return new ResponseEntity<String>("La descripción de la alerta es obligatoria", HttpStatus.BAD_REQUEST);
        }

        if (alerta.getMacDispositivo() == null || alerta.getMacDispositivo().isEmpty()) {
            return new ResponseEntity<String>("La dirección MAC del dispositivo es obligatoria",
                    HttpStatus.BAD_REQUEST);
        }

        if (alerta.getLatitud() == 0.0f) {
            return new ResponseEntity<String>("La latitud es obligatoria", HttpStatus.BAD_REQUEST);
        }

        if (alerta.getLongitud() == 0.0f) {
            return new ResponseEntity<String>("La longitud es obligatoria", HttpStatus.BAD_REQUEST);
        }

        alertaToUpdate.get().setTipo(alerta.getTipo());
        alertaToUpdate.get().setDescripcion(alerta.getDescripcion());
        alertaToUpdate.get().setLatitud(alerta.getLatitud());
        alertaToUpdate.get().setLongitud(alerta.getLongitud());
        alertaToUpdate.get().setMacDispositivo(alerta.getMacDispositivo());
        alertaToUpdate.get().setAtendido(alerta.isAtendido());
        alertaToUpdate.get().setUpdatedAt(new Date().toInstant());

        Alerta a = aService.create(alertaToUpdate.get());

        return new ResponseEntity<Alerta>(a, HttpStatus.OK);

    }

}
