package com.irueda.tfm_rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.irueda.tfm_rest.domain.Clima;
import com.irueda.tfm_rest.repositories.ClimaRepository;
import com.irueda.tfm_rest.services.ClimaService;

@SpringBootTest
public class ClimaServiceTest {

    @Mock
    private ClimaRepository climaRepository;

    @InjectMocks
    private ClimaService climaService;

    private Clima clima;

    @BeforeEach
    void setUp() {
        clima = new Clima();
        clima.setId("123");
        clima.setTemperatura(25);
    }

    @Test
    public void testGetAll() {
        when(climaRepository.findAll()).thenReturn(Collections.singletonList(clima));

        List<Clima> climas = climaService.getAll();

        assertNotNull(climas);
        assertEquals(1, climas.size());
        verify(climaRepository, times(1)).findAll();
    }

    // Test para create()
    @Test
    public void testCreateClima() {
        when(climaRepository.save(any(Clima.class))).thenReturn(clima);

        Clima createdClima = climaService.create(clima);

        assertNotNull(createdClima);
        assertEquals("123", createdClima.getId());
        verify(climaRepository, times(1)).save(any(Clima.class));
    }

    // Test para create() con excepción
    @Test
    public void testCreateClima_Exception() {
        when(climaRepository.save(any(Clima.class))).thenThrow(new IllegalArgumentException("Error al guardar"));

        Clima result = climaService.create(clima);

        assertNull(result);  // Debería devolver null en caso de excepción
        verify(climaRepository, times(1)).save(any(Clima.class));
    }

    // Test para getLastestClimaByLatLong()
    @Test
    public void testGetLastestClimaByLatLong_Success() {
        when(climaRepository.findTopByLatitudAndLongitudOrderByCreatedAtDesc(10.0f, 20.0f))
            .thenReturn(Optional.of(clima));

        Clima foundClima = climaService.getLastestClimaByLatLong(10.0f, 20.0f);

        assertNotNull(foundClima);
        assertEquals(25f, foundClima.getTemperatura());
        verify(climaRepository, times(1))
            .findTopByLatitudAndLongitudOrderByCreatedAtDesc(10.0f, 20.0f);
    }

    // Test para getLastestClimaByLatLong() cuando no se encuentra clima
    @Test
    public void testGetLastestClimaByLatLong_Empty() {
        when(climaRepository.findTopByLatitudAndLongitudOrderByCreatedAtDesc(10.0f, 20.0f))
            .thenReturn(Optional.empty());

        Clima foundClima = climaService.getLastestClimaByLatLong(10.0f, 20.0f);

        assertNotNull(foundClima);  // Debería devolver un nuevo objeto Clima
        assertNull(foundClima.getId());  // El ID del nuevo objeto debería ser null
        verify(climaRepository, times(1))
            .findTopByLatitudAndLongitudOrderByCreatedAtDesc(10.0f, 20.0f);
    }

    // Test para seedDatabase()
    @Test
    public void testSeedDatabase() throws IOException {
        doNothing().when(climaRepository).deleteAll();
        when(climaRepository.saveAll(anyList())).thenReturn(Collections.singletonList(clima));

        climaService.seedDatabase();
        
        // Verificar que se eliminaron todos los registros y se guardaron los nuevos
        verify(climaRepository, times(1)).deleteAll();
        verify(climaRepository, times(1)).saveAll(anyList());
    }
}
