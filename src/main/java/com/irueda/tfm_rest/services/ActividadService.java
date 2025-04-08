package com.irueda.tfm_rest.services;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.irueda.tfm_rest.domain.Actividad;
import com.irueda.tfm_rest.repositories.ActividadRepository;


@Service
public class ActividadService {
    @Autowired
    ActividadRepository aRepository;

    public List<Actividad> getAll() {
        return aRepository.findAll();
    }

    public Actividad create(Actividad actividad) {
        try {
            Actividad a = this.aRepository.save(actividad);
            return a;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public boolean delete(Actividad actividad) {
        try {
            this.aRepository.delete(actividad);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public Optional<Actividad> findById(String id) {
        try {
            // Retorna una actividad por su ID
            Optional<Actividad> a = this.aRepository.findById(id);
            return a;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Optional<Actividad> findLatestByMac(String macDispositivo) {
        List<Actividad> actividades = aRepository.findByDispositivoMacOrderByCreatedAtDesc(macDispositivo);
        return actividades.isEmpty() ? Optional.empty() : Optional.of(actividades.get(0));
    }

        public List<Actividad> getActividadByDay(LocalDate date) {
        // Calcular el inicio del día (00:00:00)
        Instant startOfDay = date.atStartOfDay(ZoneOffset.UTC).toInstant();

        // Calcular el final del día (23:59:59.999999999)
        Instant endOfDay = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().minusNanos(1);

        return aRepository.findByCreatedAtBetween(startOfDay, endOfDay);
    }

    public void seedDatabase() throws IOException {
        // Eliminar todos los registros primero para limpiar datos y luego cargar los datos semilla
        aRepository.deleteAll();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        InputStream inputStream = new ClassPathResource("actividades.json").getInputStream();
        List<Actividad> actividades = mapper.readValue(inputStream, new TypeReference<List<Actividad>>() {
        });
        aRepository.saveAll(actividades);
    }
}
