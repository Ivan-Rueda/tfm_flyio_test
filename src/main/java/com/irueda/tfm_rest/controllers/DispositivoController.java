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

import com.irueda.tfm_rest.domain.Dispositivo;
import com.irueda.tfm_rest.services.DispositivoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/dispositivos/v1")
@Tag(name = "Dispositivo API", description = "API para la gestión de dispositivos LoRa 📟")
public class DispositivoController {

    @Autowired
    private DispositivoService dService;

    @Operation(summary = "Insertar datos de prueba semilla en la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Base de datos inicializada con éxito", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Error al inicializar la base de datos", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("seed")
    public ResponseEntity<?> seedDatabase() {
        try {
            dService.seedDatabase();
            return new ResponseEntity<>("Datos semilla de dispositivos completada!", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error al cargar datos semilla: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    @Operation(summary = "Listar todos los dispositivos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de todos los dispositivos LoRa", content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<?> listAll() {
        List<Dispositivo> dispositivos = dService.getAll();
        return new ResponseEntity<List<Dispositivo>>(dispositivos, HttpStatus.OK);
    }

    @Operation(summary = "Obtener un dispositivo por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispositivo encontrado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Dispositivo no encontrado", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        Optional<Dispositivo> dispositivo = dService.findById(id);

        if (!dispositivo.isPresent()) {
            return new ResponseEntity<String>("No se encontró un dispositivo con id: " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Dispositivo>(dispositivo.get(), HttpStatus.OK);
    }

    @Operation(summary = "Crear un nuevo dispositivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dispositivo creado con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "text/plain"))
    })
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Dispositivo dispositivo) {

        if (dispositivo.getMac() == null || dispositivo.getMac().isEmpty()) {
            return new ResponseEntity<String>("La dirección MAC del dispositivo es obligatoria",
                    HttpStatus.BAD_REQUEST);
        }

        if (dispositivo.getFabricante() == null || dispositivo.getFabricante().isEmpty()) {
            return new ResponseEntity<String>("El fabricante del dispositivo es obligatorio", HttpStatus.BAD_REQUEST);
        }

        if (dispositivo.getModelo() == null || dispositivo.getModelo().isEmpty()) {
            return new ResponseEntity<String>("El modelo del dispositivo es obligatorio", HttpStatus.BAD_REQUEST);
        }

        Dispositivo d = dService.create(dispositivo);

        return new ResponseEntity<Dispositivo>(d, HttpStatus.CREATED);

    }

    @Operation(summary = "Actualizar un dispositivo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispositivo actualizado con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Dispositivo no encontrado", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "text/plain"))
    })
    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Dispositivo dispositivo) {

        Optional<Dispositivo> dispositivoToUpdate = dService.findById(id);

        if (!dispositivoToUpdate.isPresent()) {
            return new ResponseEntity<String>("No se encontró un dispositivo con id: " + id, HttpStatus.NOT_FOUND);
        }

        if (dispositivo.getMac() == null || dispositivo.getMac().isEmpty()) {
            return new ResponseEntity<String>("La dirección MAC del dispositivo es obligatoria",
                    HttpStatus.BAD_REQUEST);
        }

        if (dispositivo.getFabricante() == null || dispositivo.getFabricante().isEmpty()) {
            return new ResponseEntity<String>("El fabricante del dispositivo es obligatorio", HttpStatus.BAD_REQUEST);
        }

        if (dispositivo.getModelo() == null || dispositivo.getModelo().isEmpty()) {
            return new ResponseEntity<String>("El modelo del dispositivo es obligatorio", HttpStatus.BAD_REQUEST);
        }

        dispositivoToUpdate.get().setFabricante(dispositivo.getFabricante());
        dispositivoToUpdate.get().setMac(dispositivo.getMac());
        dispositivoToUpdate.get().setModelo(dispositivo.getModelo());

        Dispositivo d = dService.create(dispositivoToUpdate.get());

        return new ResponseEntity<Dispositivo>(d, HttpStatus.OK);

    }

    @Operation(summary = "Eliminar un dispositivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispositivo eliminado con éxito", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "Dispositivo no encontrado", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "text/plain"))
    })
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {

        Optional<Dispositivo> dispositivoToDelete = dService.findById(id);

        if (!dispositivoToDelete.isPresent()) {
            return new ResponseEntity<String>("No se encontró un dispositivo con id: " + id, HttpStatus.NOT_FOUND);
        }

        boolean isDeleted = dService.delete(dispositivoToDelete.get());

        if (!isDeleted) {
            return new ResponseEntity<String>("Hubo un error al intentar borrar el dispositivo, intente más tarde",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>("Dispositivo eliminado con éxito", HttpStatus.OK);
    }

}
