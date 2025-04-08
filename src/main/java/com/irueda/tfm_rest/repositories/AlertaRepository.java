package com.irueda.tfm_rest.repositories;


import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.irueda.tfm_rest.domain.Alerta;

public interface AlertaRepository extends MongoRepository<Alerta, String> {
    List<Alerta> findByCreatedAtBetween(Instant startOfDay, Instant endOfDay);
    List<Alerta> findByUpdatedAtBetween(Instant startOfMonth, Instant endOfMonth);
}
