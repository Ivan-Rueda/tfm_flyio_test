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
import com.irueda.tfm_rest.domain.Clima;
import com.irueda.tfm_rest.repositories.ClimaRepository;

@Service
public class ClimaService {

    @Autowired
    ClimaRepository cRepository;

    public List<Clima> getAll() {
        return cRepository.findAll();
    }

    public Clima create(Clima clima) {
        try {
            Clima c = this.cRepository.save(clima);
            return c;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Clima getLastestClimaByLatLong(float latitud, float longitud) {
        try {
                //Encuentra el último registro de clima por latitud y longitud
            Optional<Clima> optClima = cRepository.findTopByLatitudAndLongitudOrderByCreatedAtDesc(latitud, longitud);

            if (!optClima.isPresent() || optClima.isEmpty()) {
                return new Clima();
            }

            return optClima.get();

        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void seedDatabase() throws IOException {
                // Eliminar todos los registros primero para limpiar datos y luego cargar los datos semilla
        cRepository.deleteAll();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        InputStream inputStream = new ClassPathResource("climas.json").getInputStream();
        List<Clima> climas = mapper.readValue(inputStream, new TypeReference<List<Clima>>() {
        });
        cRepository.saveAll(climas);
    }

}
