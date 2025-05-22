package ies.ruizgijon.gestorIncidencias.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ies.ruizgijon.gestorIncidencias.GConstants;
import ies.ruizgijon.gestorincidencias.controller.CustomErrorController;
import ies.ruizgijon.gestorincidencias.model.Usuario;
import ies.ruizgijon.gestorincidencias.service.IUsuarioService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

class CustomErrorControllerTest {

    @Mock
    private IUsuarioService usuarioService; // Mock del servicio de usuarios

    @Mock
    private RedirectAttributes redirectAttributes; // Mock de RedirectAttributes

    @Mock
    private HttpServletRequest request;

    @Mock
    private Model model; // Mock del modelo

    private CustomErrorController customErrorController; // Instancia del controlador a probar

    @BeforeEach
    void setUp() {
        // Inicializa los mocks
        MockitoAnnotations.openMocks(this);
        // Inicializa el controlador con los mocks
        customErrorController = new CustomErrorController(usuarioService);
    }

    @Test
    void testHandleError_404() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(404);

        String view = customErrorController.handleError(request, model);

        verify(model).addAttribute(GConstants.ATTRIBUTE_STATUSCODE, 404);
        verify(model).addAttribute(GConstants.ATTRIBUTE_ERRORMESSAGE, "Página no encontrada.");
        assertEquals("error", view);
    }

    @Test
    void testHandleError_400() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(400);

        String view = customErrorController.handleError(request, model);

        verify(model).addAttribute(GConstants.ATTRIBUTE_STATUSCODE, 400);
        verify(model).addAttribute(GConstants.ATTRIBUTE_ERRORMESSAGE, "Solicitud incorrecta.");
        assertEquals("error", view);
    }

    @Test
    void testHandleError_500() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(500);

        String view = customErrorController.handleError(request, model);

        verify(model).addAttribute(GConstants.ATTRIBUTE_STATUSCODE, 500);
        verify(model).addAttribute(GConstants.ATTRIBUTE_ERRORMESSAGE, "Error interno del servidor.");
        assertEquals("error", view);
    }

    @Test
    void testHandleError_403() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(403);

        String view = customErrorController.handleError(request, model);

        verify(model).addAttribute(GConstants.ATTRIBUTE_STATUSCODE, 403);
        verify(model).addAttribute(GConstants.ATTRIBUTE_ERRORMESSAGE, "Acceso denegado.");
        assertEquals("error", view);
    }

    @Test
    void testHandleError_UnknownCode() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(418);

        String view = customErrorController.handleError(request, model);

        verify(model).addAttribute(GConstants.ATTRIBUTE_STATUSCODE, 418);
        verify(model).addAttribute(GConstants.ATTRIBUTE_ERRORMESSAGE, "Algo salió mal.");
        assertEquals("error", view);
    }

    @Test
    void testHandleError_SinCodigo() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(null);

        String view = customErrorController.handleError(request, model);

        verify(model).addAttribute(GConstants.ATTRIBUTE_STATUSCODE, "Error desconocido");
        verify(model).addAttribute(GConstants.ATTRIBUTE_ERRORMESSAGE, "Algo salió mal.");
        assertEquals("error", view);
    }

    @Test
    void testSetGenericos() {
        // Llama al método a probar
        customErrorController.setGenericos(model, request);

        Usuario usuario = usuarioService.getCurrentUser(); // Simula la obtención del usuario actual

        // Verifica que se agregó el usuario actual al modelo
        verify(model).addAttribute(GConstants.ATTRIBUTE_CURRENTUSER, usuario);
    }
}
