package com.irueda.tfm_rest.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.irueda.tfm_rest.domain.Curso;
import com.irueda.tfm_rest.domain.Empleado;
import com.irueda.tfm_rest.repositories.EmpleadoRepository;

@Service
public class EmpleadoService {

    @Autowired
    EmpleadoRepository eRepository;

    @Value("${moodle.api.token}")
    private String moodleToken;

    @Value("${moodle.api.url}")
    private String moodleApiUrl;

    public List<Empleado> getAll() {
        return eRepository.findAll();
    }

    public Empleado create(Empleado empleado) {
        try {
            Empleado e = this.eRepository.save(empleado);
            return e;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public boolean delete(Empleado empleado) {
        try {
            this.eRepository.delete(empleado);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public Optional<Empleado> findById(String id) {
        try {
            Optional<Empleado> e = this.eRepository.findById(id);
            return e;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Empleado createEmpleadoWithMoodle(Empleado empleado) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    // Construir los parámetros del cuerpo de la solicitud
    StringBuilder requestBody = new StringBuilder();
    try {
        requestBody.append(URLEncoder.encode("wstoken", "UTF-8"))
                   .append("=")
                   .append(URLEncoder.encode(moodleToken, "UTF-8"));
        requestBody.append("&")
                   .append(URLEncoder.encode("wsfunction", "UTF-8"))
                   .append("=")
                   .append(URLEncoder.encode("core_user_create_users", "UTF-8"));
        requestBody.append("&")
                   .append(URLEncoder.encode("moodlewsrestformat", "UTF-8"))
                   .append("=")
                   .append(URLEncoder.encode("json", "UTF-8"));
        requestBody.append("&")
                   .append(URLEncoder.encode("users[0][username]", "UTF-8"))
                   .append("=")
                   .append(URLEncoder.encode(empleado.getDni(), "UTF-8"));
        requestBody.append("&")
                   .append(URLEncoder.encode("users[0][password]", "UTF-8"))
                   .append("=")
                   .append(URLEncoder.encode("Clave*2024", "UTF-8"));
        requestBody.append("&")
                   .append(URLEncoder.encode("users[0][firstname]", "UTF-8"))
                   .append("=")
                   .append(URLEncoder.encode(empleado.getNombres(), "UTF-8"));
        requestBody.append("&")
                   .append(URLEncoder.encode("users[0][lastname]", "UTF-8"))
                   .append("=")
                   .append(URLEncoder.encode(empleado.getApellidos(), "UTF-8"));
        requestBody.append("&")
                   .append(URLEncoder.encode("users[0][email]", "UTF-8"))
                   .append("=")
                   .append(URLEncoder.encode(empleado.getEmail(), "UTF-8"));
    } catch (UnsupportedEncodingException e) {
        throw new RuntimeException("Error al codificar los parámetros", e);
    }

    HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.postForEntity(moodleApiUrl, entity, String.class);

    String responseBody = response.getBody();
    System.out.println("Respuesta de Moodle al crear el usuario: " + responseBody);

    // Verificar si la respuesta es válida y contiene el ID de Moodle
    try {
        ObjectMapper mapper = new ObjectMapper();
        Map[] responseMap = mapper.readValue(responseBody, Map[].class);
        if (responseMap != null && responseMap.length > 0 && responseMap[0].containsKey("id")) {
            String moodleId = String.valueOf(responseMap[0].get("id"));
            empleado.setId_moodle(moodleId);
            
            // Crear la lista de cursos con los valores predeterminados
            List<Curso> cursos = new ArrayList<>();
            Curso curso = new Curso("2", "EPIs 101", false);
            cursos.add(curso);
            empleado.setCursos(cursos);
            
            // Guardar empleado en la base de datos
            Empleado savedEmpleado = eRepository.save(empleado);
            
            // Llamar a la función de enrolamiento en el curso
            enrollUserInCourse(moodleId);
            
            return savedEmpleado;
        } else {
            throw new RuntimeException("Error al crear el usuario en Moodle");
        }
    } catch (Exception e) {
        throw new RuntimeException("Error al procesar la respuesta de Moodle: " + e.getMessage(), e);
    }
}

    private void enrollUserInCourse(String moodleUserId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Construir los parámetros del cuerpo de la solicitud
        StringBuilder requestBody = new StringBuilder();
        try {
            requestBody.append(URLEncoder.encode("wstoken", "UTF-8"))
                       .append("=")
                       .append(URLEncoder.encode(moodleToken, "UTF-8"));
            requestBody.append("&")
                       .append(URLEncoder.encode("wsfunction", "UTF-8"))
                       .append("=")
                       .append(URLEncoder.encode("enrol_manual_enrol_users", "UTF-8"));
            requestBody.append("&")
                       .append(URLEncoder.encode("moodlewsrestformat", "UTF-8"))
                       .append("=")
                       .append(URLEncoder.encode("json", "UTF-8"));
            requestBody.append("&")
                       .append(URLEncoder.encode("enrolments[0][roleid]", "UTF-8"))
                       .append("=")
                       .append(URLEncoder.encode("5", "UTF-8"));
            requestBody.append("&")
                       .append(URLEncoder.encode("enrolments[0][userid]", "UTF-8"))
                       .append("=")
                       .append(URLEncoder.encode(moodleUserId, "UTF-8"));
            requestBody.append("&")
                       .append(URLEncoder.encode("enrolments[0][courseid]", "UTF-8"))
                       .append("=")
                       .append(URLEncoder.encode("2", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error al codificar los parámetros", e);
        }

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(moodleApiUrl, entity, String.class);

        String responseBody = response.getBody();
        System.out.println("Respuesta de Moodle al inscribir usuario en el curso: " + responseBody);
    }

    public void seedDatabase() throws IOException {
        eRepository.deleteAll();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        InputStream inputStream = new ClassPathResource("empleados.json").getInputStream();
        List<Empleado> empleados = mapper.readValue(inputStream, new TypeReference<List<Empleado>>() {
        });
        eRepository.saveAll(empleados);
    }

}
