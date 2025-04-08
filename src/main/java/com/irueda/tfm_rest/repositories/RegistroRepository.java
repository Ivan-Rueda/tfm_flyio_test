package com.irueda.tfm_rest.repositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.irueda.tfm_rest.domain.Registro;

public interface RegistroRepository extends MongoRepository<Registro, String> {
    List<Registro> findByCreatedAtBetween(Instant startTime, Instant endTime);
}
