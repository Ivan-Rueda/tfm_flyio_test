package com.irueda.tfm_rest.services;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.irueda.tfm_rest.domain.Alerta;
import com.irueda.tfm_rest.repositories.AlertaRepository;

@Service
public class AlertaService {
    @Autowired
    AlertaRepository aRepository;

    public List<Alerta> getAll() {
        return aRepository.findAll();
    }

    public List<Alerta> getAlertsByDay(LocalDate date) {
        // Calcular el inicio del día (00:00:00)
        Instant startOfDay = date.atStartOfDay(ZoneOffset.UTC).toInstant();

        // Calcular el final del día (23:59:59.999999999)
        Instant endOfDay = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().minusNanos(1);

        return aRepository.findByCreatedAtBetween(startOfDay, endOfDay);
    }

    public List<Alerta> getAlertsByMonth(int year, int month) {
        // Crear un YearMonth para el año y mes especificados
        YearMonth yearMonth = YearMonth.of(year, month);

        // Calcular el inicio del mes (primer día del mes a las 00:00:00)
        Instant startOfMonth = yearMonth.atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        // Calcular el final del mes (último día del mes a las 23:59:59.999999999)
        Instant endOfMonth = yearMonth.atEndOfMonth().atStartOfDay(ZoneOffset.UTC).toInstant().plusSeconds(86400)
                .minusNanos(1);

        // Obtener alertas entre el inicio y el final del mes
        return aRepository.findByUpdatedAtBetween(startOfMonth, endOfMonth);
    }

    public Alerta create(Alerta alerta) {
        try {
            Alerta a = this.aRepository.save(alerta);
            return a;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Optional<Alerta> findById(String id) {
        try {
            Optional<Alerta> a = this.aRepository.findById(id);
            return a;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void seedDatabase() throws IOException {
                // Eliminar todos los registros primero para limpiar datos y luego cargar los datos semilla
        aRepository.deleteAll();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        InputStream inputStream = new ClassPathResource("alertas.json").getInputStream();
        List<Alerta> alertas = mapper.readValue(inputStream, new TypeReference<List<Alerta>>() {
        });
        aRepository.saveAll(alertas);
    }
}
