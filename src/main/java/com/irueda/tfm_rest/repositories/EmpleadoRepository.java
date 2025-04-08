package com.irueda.tfm_rest.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.irueda.tfm_rest.domain.Empleado;

public interface EmpleadoRepository extends MongoRepository<Empleado, String> {
    
}
