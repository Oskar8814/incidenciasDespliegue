package ies.ruizgijon.gestorincidencias.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ies.ruizgijon.gestorincidencias.model.Usuario;
import ies.ruizgijon.gestorincidencias.service.IUsuarioService;
import ies.ruizgijon.gestorincidencias.util.GConstants;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Controlador personalizado para la gestión de errores HTTP.
 * 
 * Este controlador intercepta errores del sistema como 404, 500, etc., y presenta al usuario
 * una vista de error personalizada con mensajes amigables. Además, incorpora el usuario
 * autenticado en el modelo de forma automática para su uso en las vistas de error.
 * 
 * <p>Este controlador es activado automáticamente por Spring Boot cuando ocurre un error en la aplicación
 * y se ha configurado `/error` como punto de entrada de error.</p>
 * 
 * @author Óscar García
 */
@Controller
public class CustomErrorController implements ErrorController {

    /**
     * Servicio para la gestión de usuarios.
     * Este servicio proporciona métodos para realizar operaciones CRUD sobre usuarios.
     */
    private final IUsuarioService usuarioService;
    

    /**
     * Inyección del servicio de usuarios para poder recuperar el usuario actual en los errores.
     *
     * @param usuarioService Servicio para operaciones sobre usuarios.
     */
    @Autowired
    public CustomErrorController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

/**
     * Maneja las solicitudes que resultan en un error HTTP.
     * 
     * Detecta el código de error desde la solicitud, construye un mensaje amigable
     * y lo pasa a la vista `error.html`. Soporta códigos como:
     * - 404: No encontrado
     * - 500: Error interno
     * - 403: Acceso denegado
     * - 400: Solicitud incorrecta
     * - 401: No autorizado
     * 
     * Si el código no está reconocido, se muestra un mensaje genérico.
     *
     * @param request Objeto `HttpServletRequest` que contiene información del error.
     * @param model Modelo de datos para pasar información a la vista.
     * @return Nombre de la vista de error (por convención `templates/error.html`).
     */
    @RequestMapping(value = "/error", method = { RequestMethod.GET, RequestMethod.POST })
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute(GConstants.ATTRIBUTE_STATUSCODE, statusCode);

            if (statusCode == 404) {
                model.addAttribute(GConstants.ATTRIBUTE_ERRORMESSAGE, "Página no encontrada.");
            } else if (statusCode == 500) { 
                model.addAttribute(GConstants.ATTRIBUTE_ERRORMESSAGE, "Error interno del servidor.");
            } else if (statusCode == 403) {
                model.addAttribute(GConstants.ATTRIBUTE_ERRORMESSAGE, "Acceso denegado.");
            } else if (statusCode == 400) {
                model.addAttribute(GConstants.ATTRIBUTE_ERRORMESSAGE, "Solicitud incorrecta.");
            } else if (statusCode == 401) {
                model.addAttribute(GConstants.ATTRIBUTE_ERRORMESSAGE, "No autorizado.");
            } else {
                model.addAttribute(GConstants.ATTRIBUTE_ERRORMESSAGE, "Algo salió mal.");
            }
        } else {
            model.addAttribute(GConstants.ATTRIBUTE_STATUSCODE, "Error desconocido");
            model.addAttribute(GConstants.ATTRIBUTE_ERRORMESSAGE, "Algo salió mal.");
        }

        return "error"; // templates/error.html
    }

    /**
     * Añade automáticamente el usuario actual autenticado al modelo.
     * 
     * Este método se ejecuta antes de cada handler del controlador y permite que
     * el usuario esté disponible como atributo en todas las vistas, incluso en la de error.
     *
     * @param model Modelo de datos para la vista.
     * @param request Objeto `HttpServletRequest` para obtener información adicional.
     */
    @ModelAttribute()
    public void setGenericos(Model model, HttpServletRequest request) {
        Usuario usuario = usuarioService.getCurrentUser(); //Obtener el usuario actualmente logeado

        model.addAttribute(GConstants.ATTRIBUTE_CURRENTUSER, usuario); // Agregar el usuario actual al modelo para la vista
        model.addAttribute("currentUrl", request.getRequestURI()); // Agregar la URL actual al modelo
    }
}


