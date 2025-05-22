package ies.ruizgijon.gestorIncidencias.service;

import ies.ruizgijon.gestorincidencias.model.PasswordResetToken;
import ies.ruizgijon.gestorincidencias.model.Rol;
import ies.ruizgijon.gestorincidencias.model.Usuario;
import ies.ruizgijon.gestorincidencias.repository.PasswordResetTokenRepository;
import ies.ruizgijon.gestorincidencias.repository.UsuarioRepository;
import ies.ruizgijon.gestorincidencias.service.UsuarioServiceJpa;
import ies.ruizgijon.gestorincidencias.util.Validaciones;
import ies.ruizgijon.gestorincidencias.exceptions.UsuarioNoValidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Clase de prueba para la clase UsuarioServiceJpa.
 * Esta clase contiene pruebas unitarias para los métodos de la clase UsuarioServiceJpa.
 * 
 * @author [Óscar García]
 */
class UsuarioServiceJpaTest {
    /**
     * Repositorio de usuarios simulado.
     */
    @Mock
    private UsuarioRepository usuarioRepository;
    /**
     * Servicio de usuarios simulado.
     */
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    /**
     * Servicio de usuarios simulado.
     */
    @InjectMocks
    private UsuarioServiceJpa usuarioServiceJpa;
    /**
     * Repositorio de tokens de restablecimiento de contraseña simulado.
     */
    @Mock
    private PasswordResetTokenRepository tokenRepository;
    /**
     * Repositorio de roles simulado.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
    }
    /**
     * Prueba unitaria para el método guardarUsuario.
     * Verifica que se llame al método save del repositorio con el usuario correcto.
     */
    @Test
    void testGuardarUsuario() {
        Usuario usuario = new Usuario();
        Rol rol = new Rol();
        rol.setId(1);
        rol.setName("ROLE_USER");
        usuario.setNombre("Juan");
        usuario.setApellido1("Pérez");
        usuario.setMail("juan.perez@example.com");
        usuario.setPassword("Qwerty1234$%!*");
        usuario.setRol(rol); // Asignar un rol válido aquí si es necesario

        // Simular que el correo del nuevo usuario no existe en la base de datos
        Mockito.when(usuarioRepository.findByMail(usuario.getMail())).thenReturn(Optional.empty());
        // Simular que la contraseña se codifica correctamente
        Mockito.when(passwordEncoder.encode(usuario.getPassword())).thenReturn("encodedPassword");

        // simulamos que el correo del usuario esta en la base de datos
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setMail("javier.gonzalez@example.com");
        Mockito.when(usuarioRepository.findByMail(usuarioExistente.getMail()))
                .thenReturn(Optional.of(usuarioExistente));

        // Simular que hay un usuario autenticado con el rol de ADMIN
        Usuario usuarioAutenticado = new Usuario();
        usuarioAutenticado.setRol(new Rol(1, "ROLE_ADMIN"));
        String email = "javier.gonzalez@example.com";

        // Simular que el usuario autenticado es un ADMIN
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(email); // Simulamos que el nombre de usuario es el correo
        when(authentication.isAuthenticated()).thenReturn(true); // Simulamos que el usuario está autenticado

        Mockito.when(usuarioRepository.findByMail(usuarioAutenticado.getMail()))
                .thenReturn(Optional.of(usuarioAutenticado));

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext); // Esta línea asegura que el contexto esté bien configurado

        // Simular que el usuario se guarda correctamente en la base de datos
        Mockito.when(usuarioRepository.save(usuario)).thenReturn(usuario);
        // Act
        Usuario result = usuarioRepository.save(usuario); // Llamamos al método a probar

        // Assert
        assertNotNull(result); // Verificamos que el resultado no sea null
        assertEquals("juan.perez@example.com", result.getMail()); // Verificamos que el correo del nuevo usuario coincida con el correo registrado
        verify(usuarioRepository, times(1)).save(usuario); // Verificamos que se haya llamado a save en el repositorio
    }
    /**
     * Prueba unitaria para el método guardarUsuario.
     * Verifica que se lanza una excepción si el correo ya existe en la base de datos.
     */
    @Test
    void testRegistrarUsuarioConCorreoExistente() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setMail("javier.gonzalez@example.com");

        when(usuarioRepository.findByMail(usuario.getMail())).thenReturn(Optional.of(usuario)); // Simulamos que el correo ya existe

        // Act & Assert
        assertThrows(UsuarioNoValidoException.class, () -> {
            usuarioServiceJpa.guardarUsuario(usuario);
        });
    }
    /**
     * Prueba unitaria para el metodo getCurrentUser.
     * Verifica que se devuelve el usuario correcto si está autenticado.
     */
    @Test
    void testGetCurrentUser() {
        // Arrange
        String email = "javier.gonzalez@example.com";
        Usuario usuario = new Usuario();
        usuario.setMail(email);

        // Simular que el usuario existe en la base de datos
        when(usuarioRepository.findByMail(email)).thenReturn(Optional.of(usuario));

        // Crear un mock de la autenticacion
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email); // Simulamos que el correo es el nombre de usuario
        when(authentication.isAuthenticated()).thenReturn(true); // Simulamos que el usuario está autenticado

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext); // Esta línea asegura que el contexto esté bien configurado
        // Act
        Usuario result = usuarioServiceJpa.getCurrentUser();
        // Assert
        assertNotNull(result);
        assertEquals(email, result.getMail());
        verify(usuarioRepository, times(1)).findByMail(email); // Verificamos que se haya llamado al método del repositorio
    }
    /**
     * Prueba unitaria para el método getCurrentUser.
     * Verifica que se devuelve null si no hay usuario autenticado.
     */
    @Test
    void testGetCurrentUserSinUsuarioAutenticado() {

        // Crear un mock de la autenticación
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("anonymousUser"); // Simula un usuario no válido

        // Establecer el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Usuario result = usuarioServiceJpa.getCurrentUser();

        assertNull(result, "Se espera que el resultado sea null cuando no hay usuario autenticado");
    }

    /**
     * Prueba unitaria para el método cambiarContraseña.
     * Verifica que se llame al método save del repositorio con el usuario correcto.
     * Verifica que la contraseña se codifique correctamente.
     * Verifica que la contraseña no sea null.
     * Verifica que la contraseña sea la correcta.
     */
    @Test
    void testcambiarContrasena() {
        // Arrange
        String email = "javier.gonzalez@example.com";
        String newPassword = "Qwerty1234$@%!";
        Usuario usuario = new Usuario();
        Rol rol = new Rol();
        rol.setId(1);
        rol.setName("ROLE_USER");
        usuario.setNombre("Juan");
        usuario.setApellido1("Pérez");
        usuario.setMail("juan.perez@example.com");
        usuario.setPassword("Qwerty1234$%!*");
        usuario.setRol(rol); // Asignar un rol válido es necesario

        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario)); // Simula que el usuario existe en la base de datos
        when(usuarioRepository.findByMail(email)).thenReturn(Optional.of(usuario)); // Simula que el usuario existe en la base de datos
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword"); // Simula que la pass se codifica de forma correcta

        usuarioServiceJpa.cambiarContrasena(usuario.getId(), newPassword); // Llamada al metodo para probar

        // Assert
        assertNotNull(usuario.getPassword()); // Verifica que la pass no sea null
        assertEquals("encodedNewPassword", usuario.getPassword()); // Verifica que la pass sea la correcta
        verify(usuarioRepository, times(1)).save(usuario); // Verificamos que se haya llamado a save en el repositorio
    }

    /**
     * Prueba unitaria para el método guardarTokenDeRecuperacion.
     */
    @Test
    void testGuardarTokenDeRecuperacion() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1);
        String token = "abc123";

        // Act
        usuarioServiceJpa.guardarTokenDeRecuperacion(usuario, token);

        // Assert
        verify(tokenRepository, times(1)).deleteByUsuario(usuario);
        ArgumentCaptor<PasswordResetToken> captor = ArgumentCaptor.forClass(PasswordResetToken.class);
        verify(tokenRepository).save(captor.capture());

        PasswordResetToken savedToken = captor.getValue();
        assertEquals(token, savedToken.getToken());
        assertEquals(usuario, savedToken.getUsuario());

        // Verifica que la fecha de expiración sea aproximadamente dentro de 1 hora
        LocalDateTime expectedExpirationLowerBound = LocalDateTime.now().plusMinutes(59);
        LocalDateTime expectedExpirationUpperBound = LocalDateTime.now().plusMinutes(61);
        assertTrue(savedToken.getFechaExpiracion().isAfter(expectedExpirationLowerBound) &&
                savedToken.getFechaExpiracion().isBefore(expectedExpirationUpperBound));
    }
    /**
     * Prueba unitaria para el método actualizarPasswordConToken.
     * Verifica que se llame al método save del repositorio con el usuario correcto.
     * Verifica que se llame al método delete del repositorio de tokens.
     */
    @Test
    void testActualizarPasswordConToken_TokenValidoYUsuarioValido() {
        // Arrange
        String token = "valid-token";
        String nuevaPassword = "newPassword123";
        String encodedPassword = "encodedPassword123";

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setMail("test@example.com");

        PasswordResetToken prt = mock(PasswordResetToken.class);
        when(prt.isExpirado()).thenReturn(false);
        when(prt.getUsuario()).thenReturn(usuario);

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(prt));
        when(passwordEncoder.encode(nuevaPassword)).thenReturn(encodedPassword);

        // Nos aseguramos de que no hay errores de validación
        try (MockedStatic<Validaciones> validacionesMock = mockStatic(Validaciones.class)) {
            validacionesMock.when(() -> Validaciones.obtenerErroresValidacionUsuario(any()))
                    .thenReturn(new ArrayList<>());

            // Act
            boolean resultado = usuarioServiceJpa.actualizarPasswordConToken(token, nuevaPassword);

            // Assert
            assertTrue(resultado);
            assertEquals(encodedPassword, usuario.getPassword());
            verify(usuarioRepository).save(usuario);
            verify(tokenRepository).delete(prt);
        }
    }
    
    @Test
    void testValidarToken_TokenValido() {
        // Arrange
        String token = "tokenValido";
        PasswordResetToken prt = mock(PasswordResetToken.class);
        when(prt.isExpirado()).thenReturn(false);
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(prt));

        // Act
        boolean resultado = usuarioServiceJpa.validarToken(token);

        // Assert
        assertTrue(resultado);
    }

    @Test
    void testValidarToken_TokenExpirado() {
        // Arrange
        String token = "tokenExpirado";
        PasswordResetToken prt = mock(PasswordResetToken.class);
        when(prt.isExpirado()).thenReturn(true);
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(prt));

        // Act
        boolean resultado = usuarioServiceJpa.validarToken(token);

        // Assert
        assertFalse(resultado);
    }

    @Test
    void testValidarToken_TokenNoEncontrado() {
        // Arrange
        String token = "tokenInexistente";
        when(tokenRepository.findByToken(token)).thenReturn(Optional.empty());

        // Act
        boolean resultado = usuarioServiceJpa.validarToken(token);

        // Assert
        assertFalse(resultado);
    }

    @Test
    void testEliminarUsuario_UsuarioExistente() {
        // Arrange
        Integer idUsuario = 1;
        when(usuarioRepository.existsById(idUsuario)).thenReturn(true);

        // Act
        usuarioServiceJpa.eliminarUsuario(idUsuario);

        // Assert
        verify(usuarioRepository).deleteById(idUsuario);
    }

    @Test
    void testEliminarUsuario_UsuarioNoExiste() {
        // Arrange
        Integer idUsuario = 99;
        when(usuarioRepository.existsById(idUsuario)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            usuarioServiceJpa.eliminarUsuario(idUsuario);
        });

        assertEquals("Usuario no encontrado con ID: 99", ex.getMessage());
        verify(usuarioRepository, never()).deleteById(any());
    }

}
