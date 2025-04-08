package com.irueda.tfm_rest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.irueda.tfm_rest.domain.Dispositivo;
import com.irueda.tfm_rest.repositories.DispositivoRepository;
import com.irueda.tfm_rest.services.DispositivoService;

@SpringBootTest
class DispositivoServiceTest {

    @Mock
    private DispositivoRepository dispositivoRepository;

    @InjectMocks
    private DispositivoService dispositivoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para obtener todos los dispositivos
    @Test
    void testGetAll() {
        Dispositivo dispositivo1 = new Dispositivo();
        Dispositivo dispositivo2 = new Dispositivo();
        
        when(dispositivoRepository.findAll()).thenReturn(Arrays.asList(dispositivo1, dispositivo2));
        
        List<Dispositivo> dispositivos = dispositivoService.getAll();
        
        assertEquals(2, dispositivos.size());
        verify(dispositivoRepository, times(1)).findAll();  // Verifica que se llamó a findAll una vez
    }

    // Test para crear un nuevo dispositivo
    @Test
    void testCreateDispositivo() {
        Dispositivo dispositivo = new Dispositivo();
        
        when(dispositivoRepository.save(dispositivo)).thenReturn(dispositivo);
        
        Dispositivo savedDispositivo = dispositivoService.create(dispositivo);
        
        assertNotNull(savedDispositivo);
        verify(dispositivoRepository, times(1)).save(dispositivo); // Verifica que se llamó a save una vez
    }

    // Test para eliminar un dispositivo
    @Test
    void testDeleteDispositivo() {
        Dispositivo dispositivo = new Dispositivo();
        
        doNothing().when(dispositivoRepository).delete(dispositivo);
        
        boolean result = dispositivoService.delete(dispositivo);
        
        assertTrue(result);
        verify(dispositivoRepository, times(1)).delete(dispositivo); // Verifica que se llamó a delete una vez
    }

    // Test para encontrar un dispositivo por ID
    @Test
    void testFindById() {
        Dispositivo dispositivo = new Dispositivo();
        String id = "123";
        
        when(dispositivoRepository.findById(id)).thenReturn(Optional.of(dispositivo));
        
        Optional<Dispositivo> foundDispositivo = dispositivoService.findById(id);
        
        assertTrue(foundDispositivo.isPresent());
        verify(dispositivoRepository, times(1)).findById(id);  // Verifica que se llamó a findById una vez
    }
}
