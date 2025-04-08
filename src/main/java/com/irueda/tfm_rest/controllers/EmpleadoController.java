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

import com.irueda.tfm_rest.domain.Empleado;
import com.irueda.tfm_rest.services.EmpleadoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/empleados/v1")
@Tag(name = "Empleado API", description = "API para la gestión de empleados 👷‍♂️")
public class EmpleadoController {

    @Autowired
    private EmpleadoService eService;

    @Operation(summary = "Insertar datos de prueba semilla en la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Base de datos inicializada con éxito", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Error al inicializar la base de datos", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("seed")
    public ResponseEntity<?> seedDatabase() {
        try {
            eService.seedDatabase();
            return new ResponseEntity<>("Datos semilla de empleado completada!", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error al cargar datos semilla: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "Listar todos los empleados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de todos los empleados", content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<?> listAll() {
        List<Empleado> empleados = eService.getAll();
        return new ResponseEntity<List<Empleado>>(empleados, HttpStatus.OK);
    }

    @Operation(summary = "Obtener un empleado por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado encontrado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        Optional<Empleado> empleado = eService.findById(id);

        if (!empleado.isPresent()) {
            return new ResponseEntity<String>("No se encontró un empleado con id: " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Empleado>(empleado.get(), HttpStatus.OK);
    }

    @Operation(summary = "Crear un nuevo empleado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empleado creado con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "text/plain"))
    })
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Empleado empleado) {

        if (empleado.getNombres() == null || empleado.getNombres().isEmpty()) {
            return new ResponseEntity<String>("Los nombres del empleado son obligatorios", HttpStatus.BAD_REQUEST);
        }

        if (empleado.getApellidos() == null || empleado.getApellidos().isEmpty()) {
            return new ResponseEntity<String>("Los apellidos del empleado son obligatorios", HttpStatus.BAD_REQUEST);
        }

        if (empleado.getDni() == null || empleado.getDni().isEmpty()) {
            return new ResponseEntity<String>("El DNI del empleado es obligatorio", HttpStatus.BAD_REQUEST);
        }

        if (empleado.getN_telefono() == null || empleado.getN_telefono().isEmpty()) {
            return new ResponseEntity<String>("El número de teléfono del empleado es obligatorio", HttpStatus.BAD_REQUEST);
        }

        if (empleado.getEmail() == null || empleado.getEmail().isEmpty()) {
            return new ResponseEntity<String>("El correo del empleado es obligatorio", HttpStatus.BAD_REQUEST);
        }

        if (empleado.getCursos() == null) {
            empleado.setCursos(List.of());
            
        // El ID de Moodle será generado después de la creación del usuario en Moodle
        try {
            Empleado e = eService.createEmpleadoWithMoodle(empleado);
            return new ResponseEntity<Empleado>(e, HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            return new ResponseEntity<String>("Error al crear empleado: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        
        }

        Empleado e = eService.create(empleado);

        return new ResponseEntity<Empleado>(e, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un empleado existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado actualizado con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "text/plain"))
    })
    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Empleado empleado) {

        Optional<Empleado> empleadoToUpdate = eService.findById(id);

        if (!empleadoToUpdate.isPresent()) {
            return new ResponseEntity<String>("No se encontró un empleado con id: " + id, HttpStatus.NOT_FOUND);
        }

        if (empleado.getNombres() == null || empleado.getNombres().isEmpty()) {
            return new ResponseEntity<String>("Los nombres del empleado son obligatorios", HttpStatus.BAD_REQUEST);
        }

        if (empleado.getApellidos() == null || empleado.getApellidos().isEmpty()) {
            return new ResponseEntity<String>("Los apellidos del empleado son obligatorios", HttpStatus.BAD_REQUEST);
        }

        if (empleado.getDni() == null || empleado.getDni().isEmpty()) {
            return new ResponseEntity<String>("El DNI del empleado es obligatorio", HttpStatus.BAD_REQUEST);
        }

        if (empleado.getN_telefono() == null || empleado.getN_telefono().isEmpty()) {
            return new ResponseEntity<String>("El número de teléfono del empleado es obligatorio", HttpStatus.BAD_REQUEST);
        }

        if (empleado.getEmail() == null || empleado.getEmail().isEmpty()) {
            return new ResponseEntity<String>("El correo del empleado es obligatorio", HttpStatus.BAD_REQUEST);
        }
        
        if (empleado.getId_moodle() == null || empleado.getId_moodle().isEmpty()) {
            return new ResponseEntity<String>("El ID de Moodle es obligatorio", HttpStatus.BAD_REQUEST);
        }

        if (empleado.getCursos() == null) {
            empleado.setCursos(List.of());
        }

        empleadoToUpdate.get().setNombres(empleado.getNombres());
        empleadoToUpdate.get().setApellidos(empleado.getApellidos());
        empleadoToUpdate.get().setDni(empleado.getDni());
        empleadoToUpdate.get().setN_telefono(empleado.getN_telefono());
        empleadoToUpdate.get().setEmail(empleado.getEmail());
        empleadoToUpdate.get().setId_moodle(empleado.getId_moodle());
        empleadoToUpdate.get().setCursos(empleado.getCursos());

        Empleado e = eService.create(empleadoToUpdate.get());

        return new ResponseEntity<Empleado>(e, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar un empleado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado eliminado con éxito", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "text/plain"))
    })
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {

        Optional<Empleado> empleadoToDelete = eService.findById(id);

        if (!empleadoToDelete.isPresent()) {
            return new ResponseEntity<String>("No se encontró un empleado con id: " + id, HttpStatus.NOT_FOUND);
        }

        boolean isDeleted = eService.delete(empleadoToDelete.get());

        if (!isDeleted) {
            return new ResponseEntity<String>("Hubo un error al intentar borrar el empleado, intente más tarde", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>("Empleado eliminado con éxito", HttpStatus.OK);
    }
}
