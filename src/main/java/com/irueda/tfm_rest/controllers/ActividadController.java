package com.irueda.tfm_rest.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.irueda.tfm_rest.domain.Actividad;
import com.irueda.tfm_rest.services.ActividadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/actividades/v1")
@Tag(name = "Actividad API", description = "API para la gestión de actividades 📋")
public class ActividadController {

    @Autowired
    private ActividadService aService;

    @Operation(summary = "Insertar datos de prueba semilla en la base de datos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Base de datos inicializada con éxito", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "500", description = "Error al inicializar la base de datos", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("seed")
    public ResponseEntity<?> seedDatabase() {
        try {
            aService.seedDatabase();
            return new ResponseEntity<>("Datos semilla de actividades completada!", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error al cargar datos semilla: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Listar todas las actividades")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de todas las actividades", content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<?> listAll() {
        List<Actividad> actividades = aService.getAll();
        return new ResponseEntity<List<Actividad>>(actividades, HttpStatus.OK);
    }

    @Operation(summary = "Obtener una actividad por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Actividad encontrada", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Actividad no encontrada", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        Optional<Actividad> actividad = aService.findById(id);

        if (!actividad.isPresent()) {
            return new ResponseEntity<String>("No se encontró una actividad con id: " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Actividad>(actividad.get(), HttpStatus.OK);
    }

    @Operation(summary = "Obtener la actividad más reciente por MAC del dispositivo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Actividad encontrada"),
        @ApiResponse(responseCode = "404", description = "No se encontró ninguna actividad para esta MAC")
    })
    @GetMapping("/last/{mac}")
    public ResponseEntity<?> getLatestByMac(@PathVariable String mac) {
        Optional<Actividad> actividad = aService.findLatestByMac(mac);

        if (!actividad.isPresent()) {
            return new ResponseEntity<>("No se encontró ninguna actividad para el dispositivo con MAC: " + mac, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(actividad.get(), HttpStatus.OK);
    }

    @Operation(summary = "Crear una nueva actividad")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Actividad creada con éxito", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "text/plain"))
    })
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Actividad actividad) {

        if (actividad.getEmpleado() == null) {
            return new ResponseEntity<String>("El empleado de la actividad es obligatorio", HttpStatus.BAD_REQUEST);
        }

        if (actividad.getDispositivo() == null) {
            return new ResponseEntity<String>("El dispositivo de la actividad es obligatorio", HttpStatus.BAD_REQUEST);
        }

        if (actividad.getTarea() == null || actividad.getTarea().isEmpty()) {
            return new ResponseEntity<String>("La tarea de la actividad es obligatoria", HttpStatus.BAD_REQUEST);
        }

        if (actividad.getRecomendaciones() == null || actividad.getRecomendaciones().isEmpty()) {
            actividad.setRecomendaciones("");
        }

        Actividad a = aService.create(actividad);

        return new ResponseEntity<Actividad>(a, HttpStatus.CREATED);

    }

    @Operation(summary = "Actualizar una actividad existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Actividad actualizada con éxito", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Actividad no encontrada", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "text/plain"))
    })
    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Actividad actividad) {
        Optional<Actividad> actividadToUpdate = aService.findById(id);

        if (!actividadToUpdate.isPresent()) {
            return new ResponseEntity<String>("No se encontró una actividad con id: " + id, HttpStatus.NOT_FOUND);
        }

        if (actividad.getEmpleado() == null) {
            return new ResponseEntity<String>("El empleado de la actividad es obligatorio", HttpStatus.BAD_REQUEST);
        }

        if (actividad.getDispositivo() == null) {
            return new ResponseEntity<String>("El dispositivo de la actividad es obligatorio", HttpStatus.BAD_REQUEST);
        }

        if (actividad.getTarea() == null || actividad.getTarea().isEmpty()) {
            return new ResponseEntity<String>("La tarea de la actividad es obligatoria", HttpStatus.BAD_REQUEST);
        }

        if (actividad.getRecomendaciones() == null || actividad.getRecomendaciones().isEmpty()) {
            actividad.setRecomendaciones("");
        }

        actividadToUpdate.get().setDispositivo(actividad.getDispositivo());
        actividadToUpdate.get().setEmpleado(actividad.getEmpleado());
        actividadToUpdate.get().setTarea(actividad.getTarea());
        actividadToUpdate.get().setRecomendaciones(actividad.getRecomendaciones());

        Actividad a = aService.create(actividadToUpdate.get());

        return new ResponseEntity<Actividad>(a, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar una actividad")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Actividad eliminada con éxito", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "404", description = "Actividad no encontrada", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "text/plain"))
    })
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {

        Optional<Actividad> actividadToDelete = aService.findById(id);

        if (!actividadToDelete.isPresent()) {
            return new ResponseEntity<String>("No se encontró una actividad con id: " + id, HttpStatus.NOT_FOUND);
        }

        boolean isDeleted = aService.delete(actividadToDelete.get());

        if (!isDeleted) {
            return new ResponseEntity<String>("Hubo un error al intentar borrar la actividad, intente más tarde",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>("Actividad eliminada con éxito", HttpStatus.OK);
    }

}
