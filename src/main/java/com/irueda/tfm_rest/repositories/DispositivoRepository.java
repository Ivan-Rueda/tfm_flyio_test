package com.irueda.tfm_rest.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.irueda.tfm_rest.domain.Dispositivo;

public interface DispositivoRepository extends MongoRepository<Dispositivo, String> {
    
}
