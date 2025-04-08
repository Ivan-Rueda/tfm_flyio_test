package com.irueda.tfm_rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.irueda.tfm_rest.domain.Alerta;
import com.irueda.tfm_rest.repositories.AlertaRepository;
import com.irueda.tfm_rest.services.AlertaService;

@SpringBootTest
public class AlertaServiceTest {

    @Mock
    private AlertaRepository alertaRepository;

    @InjectMocks
    private AlertaService alertaService;

    private Alerta alerta;

    @BeforeEach
    void setUp() {
        alerta = new Alerta();
        alerta.setId("1");
        alerta.setDescripcion("Alerta de prueba");
    }

    // Test para getAll()
    @Test
    public void testGetAll() {
        when(alertaRepository.findAll()).thenReturn(Collections.singletonList(alerta));

        List<Alerta> alertas = alertaService.getAll();

        assertNotNull(alertas);
        assertEquals(1, alertas.size());
        verify(alertaRepository, times(1)).findAll();
    }

    // Test para getAlertsByDay()
    @Test
    public void testGetAlertsByDay() {
        LocalDate date = LocalDate.now();
        Instant startOfDay = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endOfDay = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().minusNanos(1);

        when(alertaRepository.findByCreatedAtBetween(startOfDay, endOfDay)).thenReturn(Collections.singletonList(alerta));

        List<Alerta> alertas = alertaService.getAlertsByDay(date);

        assertNotNull(alertas);
        assertEquals(1, alertas.size());
        verify(alertaRepository, times(1)).findByCreatedAtBetween(startOfDay, endOfDay);
    }

    // Test para getAlertsByMonth()
    @Test
    public void testGetAlertsByMonth() {
        int year = 2024;
        int month = 9;
        YearMonth yearMonth = YearMonth.of(year, month);
        Instant startOfMonth = yearMonth.atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endOfMonth = yearMonth.atEndOfMonth().atStartOfDay(ZoneOffset.UTC).toInstant().plusSeconds(86400).minusNanos(1);

        when(alertaRepository.findByUpdatedAtBetween(startOfMonth, endOfMonth)).thenReturn(Collections.singletonList(alerta));

        List<Alerta> alertas = alertaService.getAlertsByMonth(year, month);

        assertNotNull(alertas);
        assertEquals(1, alertas.size());
        verify(alertaRepository, times(1)).findByUpdatedAtBetween(startOfMonth, endOfMonth);
    }

    // Test para create()
    @Test
    public void testCreateAlerta() {
        when(alertaRepository.save(any(Alerta.class))).thenReturn(alerta);

        Alerta createdAlerta = alertaService.create(alerta);

        assertNotNull(createdAlerta);
        assertEquals("1", createdAlerta.getId());
        verify(alertaRepository, times(1)).save(any(Alerta.class));
    }

    // Test para create() con excepción
    @Test
    public void testCreateAlerta_Exception() {
        when(alertaRepository.save(any(Alerta.class))).thenThrow(new IllegalArgumentException("Error al guardar"));

        Alerta result = alertaService.create(alerta);

        assertNull(result);  // Debería devolver null en caso de excepción
        verify(alertaRepository, times(1)).save(any(Alerta.class));
    }

    // Test para findById()
    @Test
    public void testFindById() {
        when(alertaRepository.findById("1")).thenReturn(Optional.of(alerta));

        Optional<Alerta> foundAlerta = alertaService.findById("1");

        assertTrue(foundAlerta.isPresent());
        assertEquals("1", foundAlerta.get().getId());
        verify(alertaRepository, times(1)).findById("1");
    }

    // Test para findById() con excepción
    @Test
    public void testFindById_Exception() {
        when(alertaRepository.findById("1")).thenThrow(new IllegalArgumentException("Error al buscar"));

        Optional<Alerta> result = alertaService.findById("1");

        assertNull(result);  // Debería devolver null en caso de excepción
        verify(alertaRepository, times(1)).findById("1");
    }

    // Test para seedDatabase()
    @Test
    public void testSeedDatabase() throws IOException {
        doNothing().when(alertaRepository).deleteAll();
        when(alertaRepository.saveAll(anyList())).thenReturn(Collections.singletonList(alerta));

        alertaService.seedDatabase();

        // Verificar que se eliminaron todos los registros y se guardaron los nuevos
        verify(alertaRepository, times(1)).deleteAll();
        verify(alertaRepository, times(1)).saveAll(anyList());
    }
}
