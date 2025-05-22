package ies.ruizgijon.gestorincidencias.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ies.ruizgijon.gestorincidencias.util.GConstants;

/**
 * Manejador global de excepciones para la aplicación.
 * 
 * Proporciona respuestas personalizadas para excepciones específicas
 * lanzadas desde los controladores, redirigiendo a una vista de error.
 * 
 * @author Óscar García
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Maneja la excepción {@link UsuarioNoValidoException}.
     * 
     * Agrega detalles del error al modelo y muestra una vista de error personalizada.
     * 
     * @param ex    Excepción lanzada cuando un usuario no es válido.
     * @param model Modelo para pasar datos a la vista.
     * @return Nombre de la vista de error.
     */
    @ExceptionHandler(UsuarioNoValidoException.class)
    public String handleUsuarioNoValidoException(UsuarioNoValidoException ex, Model model) {
        model.addAttribute(GConstants.ATTRIBUTE_STATUSCODE, 400);
        model.addAttribute(GConstants.ATTRIBUTE_ERRORMESSAGE, "El usuario no es válido:");
        model.addAttribute("errores", ex.getErrores());
        return "error"; // Usa error.html
    }

    /**
     * Maneja la excepción {@link IncidenciaNoValidoException}.
     * 
     * Agrega detalles del error al modelo y muestra una vista de error personalizada.
     * 
     * @param ex    Excepción lanzada cuando una incidencia no es válida.
     * @param model Modelo para pasar datos a la vista.
     * @return Nombre de la vista de error.
     */
    @ExceptionHandler(IncidenciaNoValidoException.class)
    public String handleIncidenciaNoValidoException(IncidenciaNoValidoException ex, Model model) {
        model.addAttribute(GConstants.ATTRIBUTE_STATUSCODE, 400);
        model.addAttribute(GConstants.ATTRIBUTE_ERRORMESSAGE, "La incidencia no es válida:");
        model.addAttribute("errores", ex.getErrores());
        return "error"; // Usa error.html
    }
}

