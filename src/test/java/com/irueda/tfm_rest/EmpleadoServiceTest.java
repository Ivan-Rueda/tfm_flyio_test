package com.irueda.tfm_rest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.irueda.tfm_rest.domain.Empleado;
import com.irueda.tfm_rest.repositories.EmpleadoRepository;
import com.irueda.tfm_rest.services.EmpleadoService;

@SpringBootTest
public class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoService empleadoService;

    private Empleado empleado;

    @BeforeEach
    void setUp() {
        empleado = new Empleado();
        empleado.setDni("12345");
        empleado.setNombres("Juan");
        empleado.setApellidos("Pérez");
        empleado.setN_telefono("623456789");
        empleado.setEmail("juan.perez@example.com");
    }

    @Test
    public void testCreateEmpleado() {
        when(empleadoRepository.save(any(Empleado.class))).thenReturn(empleado);

        Empleado createdEmpleado = empleadoService.create(empleado);

        assertNotNull(createdEmpleado);
        assertEquals("12345", createdEmpleado.getDni());
        verify(empleadoRepository, times(1)).save(any(Empleado.class));
    }

    @Test
    public void testFindById() {
        when(empleadoRepository.findById("12345")).thenReturn(Optional.of(empleado));

        Optional<Empleado> foundEmpleado = empleadoService.findById("12345");

        assertTrue(foundEmpleado.isPresent());
        assertEquals("12345", foundEmpleado.get().getDni());
        verify(empleadoRepository, times(1)).findById("12345");
    }

    @Test
    public void testDeleteEmpleado() {
        // Simula que la operación de eliminar es exitosa
        doNothing().when(empleadoRepository).delete(any(Empleado.class));

        boolean result = empleadoService.delete(empleado);

        assertTrue(result);
        verify(empleadoRepository, times(1)).delete(any(Empleado.class));
    }

    @Test
    public void testDeleteEmpleadoFailure() {
        // Simular que eliminar un empleado lanza una IllegalArgumentException
        doThrow(new IllegalArgumentException()).when(empleadoRepository).delete(any(Empleado.class));

        boolean result = empleadoService.delete(empleado);

        assertFalse(result);
        verify(empleadoRepository, times(1)).delete(any(Empleado.class));
    }
    

    
}
