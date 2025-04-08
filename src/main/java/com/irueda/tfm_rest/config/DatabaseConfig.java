package com.irueda.tfm_rest.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoIterable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;

@Configuration
public class DatabaseConfig implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    private final MongoClient mongoClient;
    private final Environment environment;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Value("${spring.data.mongodb.host}")
    private String mongoHost;

    @Value("${spring.data.mongodb.port}")
    private String mongoPort;

    @Value("${spring.data.mongodb.username}")
    private String mongoUser;

    @Value("${spring.data.mongodb.password}")
    private String mongoPassword;

    public DatabaseConfig(MongoClient mongoClient, Environment environment) {
        this.mongoClient = mongoClient;
        this.environment = environment;
    }

    @Override
    public void run(String... args) {
        logger.info("==== Verificando configuración de MongoDB ====");
        logger.info("Base de datos: {}", databaseName);
        logger.info("Host: {}", mongoHost);
        logger.info("Puerto: {}", mongoPort);
        logger.info("Usuario: {}", mongoUser);
        logger.info("Contraseña: {}", mongoPassword);
        
        String mongoUri = String.format("mongodb://%s:%s@%s:%s/%s", mongoUser, mongoPassword, mongoHost, mongoPort, databaseName);
        logger.info("URI completa: {}", mongoUri);

        // Listamos las colecciones
        try {
            MongoIterable<String> collections = mongoClient.getDatabase(databaseName).listCollectionNames();
            logger.info("Colecciones disponibles en la base de datos:");
            for (String name : collections) {
                logger.info(" - {}", name);
            }
        } catch (Exception e) {
            logger.error("Error al listar las colecciones de MongoDB", e);
        }

        // Verificar variables de entorno específicas
        logger.info("RAILWAY_PRIVATE_DOMAIN: {}", environment.getProperty("RAILWAY_PRIVATE_DOMAIN"));
        logger.info("RAILWAY_TCP_PROXY_DOMAIN: {}", environment.getProperty("RAILWAY_TCP_PROXY_DOMAIN"));
        logger.info("RAILWAY_TCP_PROXY_PORT: {}", environment.getProperty("RAILWAY_TCP_PROXY_PORT"));

        logger.info("==== Verificación completada ====");
    }
}
