package com.irueda.tfm_rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.irueda.tfm_rest.domain.Registro;
import com.irueda.tfm_rest.repositories.RegistroRepository;
import com.irueda.tfm_rest.services.RegistroService;

@SpringBootTest
public class RegistroServiceTest {

    @Mock
    private RegistroRepository registroRepository;

    @InjectMocks
    private RegistroService registroService;

    private Registro registro;

    @BeforeEach
    void setUp() {
        // Inicializa el objeto registro para usarlo en las pruebas
        registro = new Registro();
        registro.setId("1");
        registro.setCreatedAt(Instant.now());
        registro.setMacDispositivo("ES112345678");
    }


    @Test
    public void testCreateRegistro() {
        when(registroRepository.save(any(Registro.class))).thenReturn(registro);

        Registro createdRegistro = registroService.create(registro);

        assertNotNull(createdRegistro);
        assertEquals("1", createdRegistro.getId());
        verify(registroRepository, times(1)).save(any(Registro.class));
    }

       @Test
    public void testGetAll() {
        // Simular una lista de registros devuelta por el repositorio
        List<Registro> registros = new ArrayList<>();
        registros.add(registro);
        when(registroRepository.findAll()).thenReturn(registros);

        List<Registro> result = registroService.getAll();

        // Verifica que el resultado no es nulo y tiene el tamaño correcto
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(registroRepository, times(1)).findAll();
    }

    @Test
    public void testCreateRegistro_Success() {
        // Simula el guardado en el repositorio
        when(registroRepository.save(any(Registro.class))).thenReturn(registro);

        Registro createdRegistro = registroService.create(registro);

        assertNotNull(createdRegistro);
        assertEquals("1", createdRegistro.getId());
        verify(registroRepository, times(1)).save(any(Registro.class));
    }

    @Test
    public void testCreateRegistro_Failure() {
        // Simula un fallo al guardar el registro
        when(registroRepository.save(any(Registro.class))).thenThrow(new IllegalArgumentException());

        Registro result = registroService.create(registro);

        // Verifica que el resultado es nulo debido al fallo
        assertNull(result);
    }

    @Test
    public void testGetRegistrosLastHour() {
        // Se simula una lista de registros devuelta por el repositorio
        List<Registro> registros = new ArrayList<>();
        registros.add(registro);
    
        Instant now = Instant.now();
        Instant oneHourAgo = now.minusSeconds(3600);
    
        // Tolerancia de 1 minuto (60 segundos)
        Instant oneHourAgoTolerance = oneHourAgo.minusSeconds(60);
        Instant nowTolerance = now.plusSeconds(60);
    
        // llamada al repositorio con el rango de tiempo usando tolerancia para evitar el error de tiempos en la prueba
        when(registroRepository.findByCreatedAtBetween(argThat(start -> start.isAfter(oneHourAgoTolerance) && start.isBefore(oneHourAgo.plusSeconds(60))),
                                                        argThat(end -> end.isAfter(now.minusSeconds(60)) && end.isBefore(nowTolerance)))).thenReturn(registros);
    
        List<Registro> result = registroService.getRegistrosLastHour();
    
        // Verificar que el resultado no es nulo y contiene los registros correctos
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(registroRepository, times(1)).findByCreatedAtBetween(any(Instant.class), any(Instant.class));
    }

    @Test
    public void testSeedDatabase() throws IOException {
        doNothing().when(registroRepository).deleteAll();
        
        // Mockea el comportamiento de saveAll para devolver una lista vacía (o ficticia)
        when(registroRepository.saveAll(anyList())).thenReturn(Collections.emptyList());

        registroService.seedDatabase();

        verify(registroRepository, times(1)).deleteAll();
        verify(registroRepository, times(1)).saveAll(anyList());
    }
}
