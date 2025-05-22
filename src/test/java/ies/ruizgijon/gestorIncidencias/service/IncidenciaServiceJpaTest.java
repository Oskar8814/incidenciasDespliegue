package ies.ruizgijon.gestorIncidencias.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ies.ruizgijon.gestorincidencias.exceptions.IncidenciaNoValidoException;
import ies.ruizgijon.gestorincidencias.model.EstadoIncidencia;
import ies.ruizgijon.gestorincidencias.model.Incidencia;
import ies.ruizgijon.gestorincidencias.model.Usuario;
import ies.ruizgijon.gestorincidencias.repository.IncidenciasRepository;
import ies.ruizgijon.gestorincidencias.repository.UsuarioRepository;
import ies.ruizgijon.gestorincidencias.service.IncidenciaServiceJpa;
import ies.ruizgijon.gestorincidencias.util.Validaciones;

/**
 * Clase de prueba para la clase IncidenciaServiceJpa.
 * Esta clase contiene pruebas unitarias para los métodos de la clase
 * IncidenciaServiceJpa.
 */
class IncidenciaServiceJpaTest {

    /**
     * Repositorio de incidencias simulado.
     */
    @Mock
    private IncidenciasRepository incidenciasRepository;

    /**
     * Repositorio de usuarios simulado.
     */
    @Mock
    private UsuarioRepository usuarioRepository;

    /**
     * Servicio de incidencias que se va a probar.
     */
    @InjectMocks
    private IncidenciaServiceJpa incidenciaServiceJpa;

    /**
     * Inicializa los mocks antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
    }

    /**
     * Prueba unitaria para el método guardarIncidencia.
     * Verifica que se guarda una incidencia válida en el repositorio.
     */
    @Test
    void testGuardarIncidenciaValida() {
        // Arrange prepara los datos necesarios para el test
        Usuario usuario = new Usuario();
        usuario.setId(1);
        Incidencia incidencia = new Incidencia();
        incidencia.setId(1);
        incidencia.setTitulo("Incidencia de prueba");
        incidencia.setDescripcion("Descripcion de prueba");
        incidencia.setAula("Aula 101");
        incidencia.setFecha(LocalDate.now());
        incidencia.setImagen("aaa");
        incidencia.setEstado(EstadoIncidencia.PENDIENTE);
        incidencia.setUsuario(usuario);
        incidencia.setCreador(usuario);

        try (MockedStatic<Validaciones> validacionesMock = org.mockito.Mockito.mockStatic(Validaciones.class)) {
            validacionesMock.when(() -> Validaciones.obtenerErroresValidacionIncidencia(incidencia))
                    .thenReturn(Collections.emptyList());

            // Act
            incidenciaServiceJpa.guardarIncidencia(incidencia);

            // Assert
            verify(incidenciasRepository).save(incidencia);
        }
    }

    /**
     * Prueba unitaria para el método guardarIncidencia.
     * Verifica que se lanza una excepción si la incidencia es inválida.
     */
    @Test
    void testGuardarIncidencia_Invalida() {
        // Arrange
        Incidencia incidencia = new Incidencia();
        List<String> errores = List.of("Título obligatorio");

        try (MockedStatic<Validaciones> validacionesMock = mockStatic(Validaciones.class)) {
            validacionesMock.when(() -> Validaciones.obtenerErroresValidacionIncidencia(incidencia))
                    .thenReturn(errores);

            // Act & Assert
            IncidenciaNoValidoException ex = assertThrows(IncidenciaNoValidoException.class, () -> {
                incidenciaServiceJpa.guardarIncidencia(incidencia);
            });

            assertEquals(errores, ex.getErrores());
            verify(incidenciasRepository, never()).save(any());
        }
    }
    /**
     * Prueba unitaria para el método eliminarIncidencia.
     * Verifica que se elimina una incidencia existente en el repositorio.
     */
    @Test
    void testEliminarIncidencia_Existe() {
        // Arrange
        Integer idIncidencia = 1;
        when(incidenciasRepository.existsById(idIncidencia)).thenReturn(true);

        // Act
        incidenciaServiceJpa.eliminarIncidencia(idIncidencia);

        // Assert
        verify(incidenciasRepository).deleteById(idIncidencia);
    }

    /**
     * Prueba unitaria para el método eliminarIncidencia.
     * Verifica que se lanza una excepción si la incidencia no existe.
     */
    @Test
    void testEliminarIncidencia_NoExiste() {
        // Arrange
        Integer idIncidencia = 999;
        when(incidenciasRepository.existsById(idIncidencia)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            incidenciaServiceJpa.eliminarIncidencia(idIncidencia);
        });

        assertEquals("Incidencia no encontrada con ID: " + idIncidencia, exception.getMessage());
        verify(incidenciasRepository, never()).deleteById(any());
    }

    /**
     * Prueba unitaria para el método buscarIncidenciaPorId.
     * Verifica que se busca una incidencia por su ID en el repositorio.
     */
    @Test
    void testBuscarIncidenciaPorId_Existe() {
        // Arrange
        Integer idIncidencia = 1;
        Incidencia mockIncidencia = new Incidencia();
        mockIncidencia.setId(idIncidencia);

        when(incidenciasRepository.findById(idIncidencia)).thenReturn(Optional.of(mockIncidencia));

        // Act
        Incidencia resultado = incidenciaServiceJpa.buscarIncidenciaPorId(idIncidencia);

        // Assert
        assertNotNull(resultado);
        assertEquals(idIncidencia, resultado.getId());
    }

    /**
     * Prueba unitaria para el método buscarIncidenciaPorId.
     * Verifica que se lanza una excepción si la incidencia no existe.
     */
    @Test
    void testBuscarIncidenciaPorId_NoExiste() {
        // Arrange
        Integer idIncidencia = 999;
        when(incidenciasRepository.findById(idIncidencia)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            incidenciaServiceJpa.buscarIncidenciaPorId(idIncidencia);
        });

        assertEquals("Incidencia no encontrada con ID: " + idIncidencia, exception.getMessage());
    }

    /**
     * Prueba unitaria para el método buscarIncidenciasPorUsuario.
     * Verifica que se buscan incidencias por usuario en el repositorio.
     */
    @Test
    void testAsignarIncidencia_ExisteIncidenciaYUsuario() {
        // Arrange
        Integer idIncidencia = 1;
        Integer idUsuario = 2;

        Incidencia incidencia = new Incidencia();
        incidencia.setId(idIncidencia);

        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        when(incidenciasRepository.findById(idIncidencia)).thenReturn(Optional.of(incidencia));
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));

        // Act
        incidenciaServiceJpa.asignarIncidencia(idIncidencia, idUsuario);

        // Assert
        assertEquals(usuario, incidencia.getUsuario());
        assertEquals(EstadoIncidencia.REPARACION, incidencia.getEstado());
        verify(incidenciasRepository).save(incidencia);
    }

    /**
     * Prueba unitaria para el método asignarIncidencia.
     * Verifica que se lanza una excepción si la incidencia no existe.
     */
    @Test
    void testAsignarIncidencia_IncidenciaONoExiste() {
        // Arrange
        Integer idIncidencia = 1;
        Integer idUsuario = 2;

        when(incidenciasRepository.findById(idIncidencia)).thenReturn(Optional.empty());
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(new Usuario()));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            incidenciaServiceJpa.asignarIncidencia(idIncidencia, idUsuario);
        });

        assertEquals("Incidencia no encontrada con ID: " + idIncidencia, exception.getMessage());
        verify(incidenciasRepository, never()).save(any());
    }

    /**
     * Prueba unitaria para el método asignarIncidencia.
     * Verifica que se lanza una excepción si el usuario no existe.
     */
    @Test
    void testAsignarIncidencia_UsuarioNoExiste() {
        // Arrange
        Integer idIncidencia = 1;
        Integer idUsuario = 2;

        when(incidenciasRepository.findById(idIncidencia)).thenReturn(Optional.of(new Incidencia()));
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            incidenciaServiceJpa.asignarIncidencia(idIncidencia, idUsuario);
        });

        assertEquals("Incidencia no encontrada con ID: " + idIncidencia, exception.getMessage());
        verify(incidenciasRepository, never()).save(any());
    }
    /**
     * Prueba unitaria para el método desasignarIncidencia.
     * Verifica que se desasigna una incidencia existente en el repositorio.
     */
    @Test
    void testDesasignarIncidencia_Existe() {
        // Arrange
        Integer idIncidencia = 1;
        Usuario usuario = new Usuario();
        Incidencia incidencia = new Incidencia();
        incidencia.setId(idIncidencia);
        incidencia.setUsuario(usuario);  // Asignamos un usuario previamente
        incidencia.setEstado(EstadoIncidencia.REPARACION); // Estado anterior

        when(incidenciasRepository.findById(idIncidencia)).thenReturn(Optional.of(incidencia));

        // Act
        incidenciaServiceJpa.desasignarIncidencia(idIncidencia);

        // Assert
        assertNull(incidencia.getUsuario());
        assertEquals(EstadoIncidencia.PENDIENTE, incidencia.getEstado());
        verify(incidenciasRepository).save(incidencia);
    }
    /**
     * Prueba unitaria para el método desasignarIncidencia.
     * Verifica que se lanza una excepción si la incidencia no existe.
     */
    @Test
    void testDesasignarIncidencia_NoExiste() {
        // Arrange
        Integer idIncidencia = 999;
        when(incidenciasRepository.findById(idIncidencia)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            incidenciaServiceJpa.desasignarIncidencia(idIncidencia);
        });

        assertEquals("Incidencia no encontrada con ID: " + idIncidencia, exception.getMessage());
        verify(incidenciasRepository, never()).save(any());
    }
    /**
     * Prueba unitaria para el método cerrarIncidencia.
     * Verifica que se cierra una incidencia existente en el repositorio.
     */
    @Test
    void testCerrarIncidencia_Existe() {
        // Arrange
        Integer idIncidencia = 1;
        Incidencia incidencia = new Incidencia();
        incidencia.setId(idIncidencia);
        incidencia.setEstado(EstadoIncidencia.REPARACION); // Estado antes del cierre

        when(incidenciasRepository.findById(idIncidencia)).thenReturn(Optional.of(incidencia));

        // Act
        incidenciaServiceJpa.cerrarIncidencia(idIncidencia);

        // Assert
        assertEquals(EstadoIncidencia.RESUELTA, incidencia.getEstado());
        verify(incidenciasRepository).save(incidencia);
    }
    /**
     * Prueba unitaria para el método cerrarIncidencia.
     * Verifica que se lanza una excepción si la incidencia no existe.
     */
    @Test
    void testCerrarIncidencia_NoExiste() {
        // Arrange
        Integer idIncidencia = 999;
        when(incidenciasRepository.findById(idIncidencia)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            incidenciaServiceJpa.cerrarIncidencia(idIncidencia);
        });

        assertEquals("Incidencia no encontrada con ID: " + idIncidencia, exception.getMessage());
        verify(incidenciasRepository, never()).save(any());
    }
    /**
     * Prueba unitaria para el método buscarIncidenciasPorEstado.
     * Verifica que se buscan incidencias por estado en el repositorio.
     */
    @Test
    void testBuscarByExample() {
        // Arrange
        Example<Incidencia> example = Example.of(new Incidencia());
        Incidencia incidencia1 = new Incidencia();
        Incidencia incidencia2 = new Incidencia();
        when(incidenciasRepository.findAll(example)).thenReturn(Arrays.asList(incidencia1, incidencia2));

        // Act
        List<Incidencia> incidencias = incidenciaServiceJpa.buscarByExample(example);

        // Assert
        assertNotNull(incidencias);
        assertEquals(2, incidencias.size());
        verify(incidenciasRepository).findAll(example);
    }
    /**
     * Prueba unitaria para el método buscarIncidenciasPorEstado.
     * Verifica que se buscan incidencias por estado en el repositorio.
     */
    @Test
    void testBuscarIncidenciasPaginadas() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Incidencia> pageMock = mock(Page.class);
        when(incidenciasRepository.findAll(pageable)).thenReturn(pageMock);

        // Act
        Page<Incidencia> result = incidenciaServiceJpa.buscarIncidenciasPaginadas(pageable);

        // Assert
        assertNotNull(result);
        verify(incidenciasRepository).findAll(pageable);
    }
    /**
     * Prueba unitaria para el método buscarIncidenciasPorEstado.
     * Verifica que se buscan incidencias por estado en el repositorio.
     */
    @Test
    void testBuscarIncidenciasPorEstadoPaginadas() {
        // Arrange
        EstadoIncidencia estado = EstadoIncidencia.PENDIENTE;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Incidencia> pageMock = mock(Page.class);
        when(incidenciasRepository.findByEstado(estado, pageable)).thenReturn(pageMock);

        // Act
        Page<Incidencia> result = incidenciaServiceJpa.buscarIncidenciasPorEstadoPaginadas(estado, pageable);

        // Assert
        assertNotNull(result);
        verify(incidenciasRepository).findByEstado(estado, pageable);
    }
    /**
     * Prueba unitaria para el método buscarIncidenciasByExamplePaginadas.
     * Verifica que se buscan incidencias por ejemplo y se devuelven paginadas.
     */
    @Test
    void testBuscarIncidenciasByExamplePaginadas() {
        // Arrange
        Example<Incidencia> example = Example.of(new Incidencia());
        Pageable pageable = PageRequest.of(0, 10);
        Page<Incidencia> pageMock = mock(Page.class);
        when(incidenciasRepository.findAll(example, pageable)).thenReturn(pageMock);

        // Act
        Page<Incidencia> result = incidenciaServiceJpa.buscarIncidenciasByExamplePaginadas(example, pageable);

        // Assert
        assertNotNull(result);
        verify(incidenciasRepository).findAll(example, pageable);
    }
}
