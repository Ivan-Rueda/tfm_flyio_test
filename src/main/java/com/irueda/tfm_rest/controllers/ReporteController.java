package com.irueda.tfm_rest.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.irueda.tfm_rest.domain.Actividad;
import com.irueda.tfm_rest.domain.Alerta;
import com.irueda.tfm_rest.domain.Dispositivo;
import com.irueda.tfm_rest.domain.Registro;
import com.irueda.tfm_rest.domain.Reporte;
import com.irueda.tfm_rest.domain.ReporteLatLog;
import com.irueda.tfm_rest.services.ActividadService;
import com.irueda.tfm_rest.services.AlertaService;
import com.irueda.tfm_rest.services.DispositivoService;
import com.irueda.tfm_rest.services.RegistroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/reportes/v1")
@Tag(name = "Reporte API", description = "API para la generación de datos para reportes 📅 📊")
public class ReporteController {

    @Autowired
    private ActividadService actividadService;

    @Autowired
    private RegistroService registroService;

    @Autowired
    private DispositivoService dispositivoService;

    @Autowired
    private AlertaService alertaService;

    @Operation(summary = "Generar un reporte basado en una fecha específica [Mes/día/año]")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reporte generado con éxito", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida, se requiere una fecha en el formato Mes/día/año", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping
    public ResponseEntity<?> buildReport(@RequestParam(required = false) Optional<String> date) {

        if (!date.isPresent() || date.get().isEmpty()) {
            return new ResponseEntity<String>("La fecha es requerida. [Mes/día/año]", HttpStatus.BAD_REQUEST);
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("M/d/uuuu", Locale.US);
        LocalDate localDate = LocalDate.parse(date.get(), dateTimeFormatter);

        // ? Obtener las alertas de hoy
        List<Alerta> alertas_today = alertaService.getAlertsByDay(localDate);

        // ? Obtener las alertas del mes
        List<Alerta> alertas_mes = alertaService.getAlertsByMonth(localDate.getYear(), localDate.getMonth().getValue());

        List<ReporteLatLog> ubicaciones = new ArrayList<>();

        // ? Obtener todos los dispositivos
        List<Dispositivo> dispositivos = dispositivoService.getAll();

        // ? Extraer las direcciones MACs de los dispositivos
        List<String> macs = dispositivos.stream().filter(dispositivo -> dispositivo.getMac().length() > 0)
                .collect(Collectors.mapping(x -> x.getMac(), Collectors.toList()));

        // ? Obtener todos los registros
        List<Registro> registros = registroService.getAll();

        // ? Recorrer cada dirección MAC
        for (String mac : macs) {
            // ? Extraer la latitud y longitud del registro según la dirección MAC
            for (Registro registro : registros) {
                if (registro.getMacDispositivo().equals(mac)) {
                    ubicaciones.add(new ReporteLatLog(registro.getLatitud(), registro.getLongitud()));
                }
            }
        }

        // ? Obtener todas las actividades
        List<Actividad> actividades = actividadService.getAll();

        // ? Obtener las alertas de hoy
        List<Actividad> actividades_today = actividadService.getActividadByDay(localDate);

        // ? Construir el objeto "Reporte"
        Reporte reporte = new Reporte(
                alertas_mes.size(),
                alertas_today.size(),
                actividades_today.size(),
                ubicaciones,
                actividades);

        return new ResponseEntity<Reporte>(reporte, HttpStatus.OK);
    }

}
