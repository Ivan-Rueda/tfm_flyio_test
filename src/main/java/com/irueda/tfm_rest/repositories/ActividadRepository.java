package com.irueda.tfm_rest.repositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.irueda.tfm_rest.domain.Actividad;


public interface ActividadRepository extends MongoRepository<Actividad, String> {
    List<Actividad> findByCreatedAtBetween(Instant startOfDay, Instant endOfDay);
    List<Actividad> findByDispositivoMacOrderByCreatedAtDesc(String macDispositivo);
}
