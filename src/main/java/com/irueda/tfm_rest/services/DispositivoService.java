package com.irueda.tfm_rest.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.irueda.tfm_rest.domain.Dispositivo;
import com.irueda.tfm_rest.repositories.DispositivoRepository;

@Service
public class DispositivoService {
    @Autowired
    DispositivoRepository dRepository;

    public List<Dispositivo> getAll() {
        return dRepository.findAll();
    }

    public Dispositivo create(Dispositivo dispositivo) {
        try {
            Dispositivo d = this.dRepository.save(dispositivo);
            return d;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public boolean delete(Dispositivo dispositivo) {
        try {
            this.dRepository.delete(dispositivo);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public Optional<Dispositivo> findById(String id) {
        try {
            Optional<Dispositivo> d = this.dRepository.findById(id);
            return d;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void seedDatabase() throws IOException {
                // Eliminar todos los registros primero para limpiar datos y luego cargar los datos semilla
        dRepository.deleteAll();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        InputStream inputStream = new ClassPathResource("dispositivos.json").getInputStream();
        List<Dispositivo> dispositivos = mapper.readValue(inputStream, new TypeReference<List<Dispositivo>>() {
        });
        dRepository.saveAll(dispositivos);
    }
}
