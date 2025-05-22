package ies.ruizgijon.gestorIncidencias.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ies.ruizgijon.gestorIncidencias.GConstants;
import ies.ruizgijon.gestorincidencias.controller.HomeController;
import ies.ruizgijon.gestorincidencias.model.Usuario;
import ies.ruizgijon.gestorincidencias.service.IUsuarioService;
import jakarta.servlet.http.HttpServletRequest;

class HomeControllerTest {

    @Mock
    private IUsuarioService usuarioService; // Mock del servicio de usuarios

    @Mock
    private RedirectAttributes redirectAttributes; // Mock de RedirectAttributes

    @Mock
    private Model model; // Mock del modelo

    @Mock
    private HttpServletRequest request;

    private HomeController homeController; // Instancia del controlador a probar

    @BeforeEach
    void setUp() {
        // Inicializa los mocks
        MockitoAnnotations.openMocks(this);
        // Inicializa el controlador con los mocks
        homeController = new HomeController(usuarioService);
    }

    @Test
    void testShowLogin() {
        // Ejecutar el método
        String viewName = homeController.showLogin();

        // Verificar que devuelve la vista "login"
        assertEquals("login", viewName);
    }

    @Test
    void testModificarContrasena_conUsuarioLogeado() {
        // Simular un usuario logueado
        Usuario usuario = new Usuario();
        usuario.setMail("usuario@ejemplo.com");
        when(usuarioService.getCurrentUser()).thenReturn(usuario);

        // Ejecutar el método
        String vista = homeController.modificarContrasena(model);

        // Verificar que se añade el atributo correcto al modelo
        verify(model).addAttribute("mailLogeado", "usuario@ejemplo.com");

        // Verificar que devuelve la vista esperada
        assertEquals("modificarContrasena", vista);
    }

    @Test
    void testCambiarContrasena_conUsuarioLogeadoYEmailCoincidente() {
        // Datos simulados
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setMail("usuario@ejemplo.com");

        when(usuarioService.getCurrentUser()).thenReturn(usuario);

        // Ejecutar método
        String vista = homeController.cambiarContrasena("usuario@ejemplo.com", "NuevaPassword123", redirectAttributes);

        // Verificaciones
        verify(usuarioService).cambiarContrasena(1, "NuevaPassword123");
        verify(redirectAttributes).addFlashAttribute("confirmacion", "Contraseña modificada con éxito.");
        assertEquals(GConstants.REDIRECT_LOGIN, vista);
    }

    @Test
    void testCambiarContrasena_conUsuarioLogeadoYEmailNoCoincidente() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setMail("usuario@ejemplo.com");

        when(usuarioService.getCurrentUser()).thenReturn(usuario);

        String vista = homeController.cambiarContrasena("otro@ejemplo.com", "NuevaPassword123", redirectAttributes);

        // Verifica que no se cambia la contraseña
        verify(usuarioService, never()).cambiarContrasena(anyInt(), anyString());
        assertEquals(GConstants.REDIRECT_LOGIN, vista);
    }

    @Test
    void testSetGenericos() {
        // Llama al método a probar
        homeController.setGenericos(model, request);

        Usuario usuario = usuarioService.getCurrentUser(); // Simula la obtención del usuario actual

        // Verifica que se agregó el usuario actual al modelo
        verify(model).addAttribute(GConstants.ATTRIBUTE_CURRENTUSER, usuario);
    }
}
