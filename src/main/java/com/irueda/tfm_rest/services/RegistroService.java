package com.irueda.tfm_rest.services;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.irueda.tfm_rest.domain.Registro;
import com.irueda.tfm_rest.repositories.RegistroRepository;

@Service
public class RegistroService {

    @Autowired
    RegistroRepository rRepository;

    public List<Registro> getAll() {
        return rRepository.findAll();
    }

    public Registro create(Registro registro) {
        try {
            Registro r = this.rRepository.save(registro);
            return r;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public List<Registro> getRegistrosLastHour() {
        // Calcular el tiempo actual
        Instant now = Instant.now();

        // Calcular el tiempo hace una hora desde ahora
        Instant oneHourAgo = now.minusSeconds(3600);

        // Obtener alertas entre hace una hora y ahora
        return rRepository.findByCreatedAtBetween(oneHourAgo, now);
    }

    public void seedDatabase() throws IOException {
                // Eliminar todos los registros primero para limpiar datos y luego cargar los datos semilla +++
        rRepository.deleteAll();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        InputStream inputStream = new ClassPathResource("registros.json").getInputStream();
        List<Registro> registros = mapper.readValue(inputStream, new TypeReference<List<Registro>>() {
        });
        rRepository.saveAll(registros);
    }

}
