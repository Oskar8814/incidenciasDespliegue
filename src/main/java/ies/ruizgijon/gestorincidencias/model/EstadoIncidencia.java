package ies.ruizgijon.gestorincidencias.model;

/**
 * Enum que representa los posibles estados de una incidencia.
 * <ul>
 *   <li><b>PENDIENTE:</b> Aún no ha sido atendida.</li>
 *   <li><b>REPARACION:</b> Está siendo reparada.</li>
 *   <li><b>RESUELTA:</b> Ha sido solucionada.</li>
 * </ul>
 * 
 * @author Óscar García
 */
public enum EstadoIncidencia {
    
    /**
     * Estado de la incidencia que indica que está pendiente de atención.
     */
    PENDIENTE,
    
    /**
     * Estado de la incidencia que indica que está en proceso de reparación.
     */
    REPARACION,

    /**
     * Estado de la incidencia que indica que ha sido resuelta.
     */
    RESUELTA;
    
    /**
     * Devuelve el estado correspondiente a una cadena.
     * 
     * @param estado Cadena con el nombre del estado (sin importar mayúsculas/minúsculas).
     * @return El valor del enum que coincide con la cadena.
     * @throws IllegalArgumentException si la cadena no corresponde a ningún estado válido.
     */
    public static EstadoIncidencia fromString(String estado) {
        return EstadoIncidencia.valueOf(estado.toUpperCase());
    }
}
