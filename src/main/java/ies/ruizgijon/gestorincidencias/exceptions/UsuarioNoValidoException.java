package ies.ruizgijon.gestorincidencias.exceptions;

import java.util.List;

/**
 * Excepción personalizada lanzada cuando un usuario no supera las validaciones.
 * 
 * Contiene una lista de errores que detallan los fallos encontrados.
 * 
 * @author Óscar García
 */
public class UsuarioNoValidoException extends RuntimeException {
    /**
     * Lista de errores de validación asociados al usuario.
     * Esta lista contiene mensajes de error que describen los problemas encontrados
     * durante la validación del usuario.
     */
    private List<String> errores;

    /**
     * Constructor de la excepción UsuarioNoValidoException.
     * 
     * @param errores Lista de errores de validación asociados al usuario.
     * Este constructor inicializa la lista de errores y establece un mensaje
     * predeterminado para la excepción.
     */
    public UsuarioNoValidoException(List<String> errores) {
        super("Errores de validación en el usuario.");
        this.errores = errores;
    }
    /**
     * Método para obtener la lista de errores de validación.
     * 
     * @return La lista de errores de validación asociados al usuario.
     */
    public List<String> getErrores() {
        return errores;
    }
}
