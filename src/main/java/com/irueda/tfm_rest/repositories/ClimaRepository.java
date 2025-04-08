package com.irueda.tfm_rest.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.irueda.tfm_rest.domain.Clima;

public interface ClimaRepository extends MongoRepository<Clima, String> {
    Optional<Clima> findTopByLatitudAndLongitudOrderByCreatedAtDesc(float latitud, float longitud);
}
