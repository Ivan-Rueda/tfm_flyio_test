package com.irueda.tfm_rest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.irueda.tfm_rest.domain.Actividad;
import com.irueda.tfm_rest.repositories.ActividadRepository;
import com.irueda.tfm_rest.services.ActividadService;

@SpringBootTest
class ActividadServiceTest {

    @Mock
    private ActividadRepository actividadRepository;

    @InjectMocks
    private ActividadService actividadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para obtener todas las actividades
    @Test
    void testGetAll() {
        Actividad actividad1 = new Actividad();
        Actividad actividad2 = new Actividad();
        
        when(actividadRepository.findAll()).thenReturn(Arrays.asList(actividad1, actividad2));
        
        List<Actividad> actividades = actividadService.getAll();
        
        assertEquals(2, actividades.size());
        verify(actividadRepository, times(1)).findAll();  // Verifica que se llamó a findAll una vez
    }

    // Test para crear una nueva actividad
    @Test
    void testCreateActividad() {
        Actividad actividad = new Actividad();
        
        when(actividadRepository.save(actividad)).thenReturn(actividad);
        
        Actividad savedActividad = actividadService.create(actividad);
        
        assertNotNull(savedActividad);
        verify(actividadRepository, times(1)).save(actividad); // Verifica que se llamó a save una vez
    }

    // Test para eliminar una actividad
    @Test
    void testDeleteActividad() {
        Actividad actividad = new Actividad();
        
        doNothing().when(actividadRepository).delete(actividad);
        
        boolean result = actividadService.delete(actividad);
        
        assertTrue(result);
        verify(actividadRepository, times(1)).delete(actividad); // Verifica que se llamó a delete una vez
    }

    // Test para encontrar una actividad por ID
    @Test
    void testFindById() {
        Actividad actividad = new Actividad();
        String id = "123";
        
        when(actividadRepository.findById(id)).thenReturn(Optional.of(actividad));
        
        Optional<Actividad> foundActividad = actividadService.findById(id);
        
        assertTrue(foundActividad.isPresent());
        verify(actividadRepository, times(1)).findById(id);  // Verifica que se llamó a findById una vez
    }

    // Test para encontrar la actividad más reciente por la MAC de un dispositivo
    @Test
    void testFindLatestByMac() {
        Actividad actividad1 = new Actividad();
        Actividad actividad2 = new Actividad();
        String mac = "ES123456789";
        
        when(actividadRepository.findByDispositivoMacOrderByCreatedAtDesc(mac))
            .thenReturn(Arrays.asList(actividad2, actividad1)); // Ordenadas de más reciente a más antigua
        
        Optional<Actividad> latestActividad = actividadService.findLatestByMac(mac);
        
        assertTrue(latestActividad.isPresent());
        assertEquals(actividad2, latestActividad.get());  // Verifica que la actividad más reciente es la correcta
        verify(actividadRepository, times(1)).findByDispositivoMacOrderByCreatedAtDesc(mac);
    }

    // Test para obtener actividades de un día específico
    @Test
    void testGetActividadByDay() {
        Actividad actividad1 = new Actividad();
        Actividad actividad2 = new Actividad();
        LocalDate date = LocalDate.of(2024, 9, 1);
        
        Instant startOfDay = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endOfDay = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().minusNanos(1);
        
        when(actividadRepository.findByCreatedAtBetween(startOfDay, endOfDay))
            .thenReturn(Arrays.asList(actividad1, actividad2));
        
        List<Actividad> actividades = actividadService.getActividadByDay(date);
        
        assertEquals(2, actividades.size());
        verify(actividadRepository, times(1)).findByCreatedAtBetween(startOfDay, endOfDay); // Verifica que se llamó correctamente
    }

    // Test para cargar la base de datos con datos iniciales (seedDatabase)
    @Test
    void testSeedDatabase() throws IOException {
        // Simular eliminación y carga de datos
        doNothing().when(actividadRepository).deleteAll();
        when(actividadRepository.saveAll(anyList())).thenReturn(null); // No es necesario retornar nada
        // Ejecutar la semilla de la base de datos
        actividadService.seedDatabase();
        
        // Verificar que se eliminaron todos los registros y se guardaron los nuevos
        verify(actividadRepository, times(1)).deleteAll();
        verify(actividadRepository, times(1)).saveAll(anyList());
    }
}
