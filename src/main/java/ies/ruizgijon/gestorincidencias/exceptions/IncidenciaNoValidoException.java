package ies.ruizgijon.gestorincidencias.exceptions;

import java.util.List;

/**
 * Excepción personalizada lanzada cuando una incidencia no supera las validaciones.
 * 
 * Contiene una lista de errores detallando los fallos de validación detectados.
 * 
 * @author Óscar García
 */
public class IncidenciaNoValidoException extends RuntimeException {

    /**
     * Lista de errores de validación asociados a la incidencia.
     * Esta lista contiene mensajes de error que describen los problemas encontrados
     * durante la validación de la incidencia.
     */
    private List<String> errores;

    /**
     * Constructor de la excepción IncidenciaNoValidoException.
     * 
     * @param errores Lista de errores de validación asociados a la incidencia.
     * Este constructor inicializa la lista de errores y establece un mensaje
     * predeterminado para la excepción.
     */
    public IncidenciaNoValidoException(List<String> errores) {
        super("Errores de validación en la Incidencia.");
        this.errores = errores;
    }

    /**
     * Método para obtener la lista de errores de validación.
     * 
     * @return La lista de errores de validación asociados a la incidencia.
     */
    public List<String> getErrores() {
        return errores;
    }
}
