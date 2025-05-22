package ies.ruizgijon.gestorIncidencias.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ies.ruizgijon.gestorincidencias.controller.AuthController;
import ies.ruizgijon.gestorincidencias.model.Usuario;
import ies.ruizgijon.gestorincidencias.service.EmailService;
import ies.ruizgijon.gestorincidencias.service.IUsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

class AuthControllerTest {

    @Mock
    private IUsuarioService usuarioService;

    @Mock
    private EmailService emailService;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private Model model;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(usuarioService, emailService);
        // Simular el valor de baseUrl
        ReflectionTestUtils.setField(authController, "baseUrl", "http://localhost:8080");
    }

    @Test
    void testMostrarFormularioRecuperacion() {
        String view = authController.mostrarFormularioRecuperacion();
        assertEquals("recuperar-password", view);
    }

    @Test
    void testProcesarRecuperacion_usuarioExiste() {
        String email = "test@example.com";
        Usuario usuario = new Usuario();
        when(usuarioService.buscarUsuarioPorMail(email)).thenReturn(usuario);

        String result = authController.procesarRecuperacion(email, redirectAttributes);

        verify(usuarioService).guardarTokenDeRecuperacion(eq(usuario), anyString());
        verify(emailService).enviarRecuperacion(eq(email), contains("/reset-password?token="));
        verify(redirectAttributes).addFlashAttribute(eq("confirmacion"), anyString());
        assertEquals("redirect:/", result);
    }

    @Test
    void testProcesarRecuperacion_usuarioNoExiste() {
        String email = "noexiste@example.com";
        when(usuarioService.buscarUsuarioPorMail(email)).thenReturn(null);

        String result = authController.procesarRecuperacion(email, redirectAttributes);

        verify(emailService, never()).enviarRecuperacion(anyString(), anyString());
        verify(redirectAttributes).addFlashAttribute(eq("confirmacion"), anyString());
        assertEquals("redirect:/", result);
    }

    @Test
    void testMostrarResetForm_tokenValido() {
        String token = "valid-token";
        when(usuarioService.validarToken(token)).thenReturn(true);

        String view = authController.mostrarResetForm(token, model);

        verify(model).addAttribute("token", token);
        assertEquals("reset-password", view);
    }

    @Test
    void testMostrarResetForm_tokenInvalido() {
        String token = "invalid-token";
        when(usuarioService.validarToken(token)).thenReturn(false);

        String view = authController.mostrarResetForm(token, model);

        assertEquals("redirect:/?errorToken", view);
    }

    @Test
    void testProcesarReset_exito() {
        String token = "token";
        String newPassword = "Password123!";
        when(usuarioService.actualizarPasswordConToken(token, newPassword)).thenReturn(true);

        String result = authController.procesarReset(token, newPassword, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("confirmacion", "Contraseña actualizada correctamente.");
        assertEquals("redirect:/", result);
    }

    @Test
    void testProcesarReset_fallo() {
        String token = "token";
        String newPassword = "Password123!";
        when(usuarioService.actualizarPasswordConToken(token, newPassword)).thenReturn(false);

        String result = authController.procesarReset(token, newPassword, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("error", "Token inválido o expirado.");
        assertEquals("redirect:/", result);
    }
}
