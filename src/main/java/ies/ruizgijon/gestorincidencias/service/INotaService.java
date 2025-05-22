package ies.ruizgijon.gestorincidencias.service;

import java.util.List;

import ies.ruizgijon.gestorincidencias.model.Nota;

/**
 * Interfaz que define los métodos del servicio para la gestión de notas
 * asociadas a incidencias en el sistema.
 * <p>
 * Proporciona operaciones para crear, actualizar, eliminar y recuperar notas.
 * </p>
 */
public interface INotaService {

    /**
     * Crea una nueva nota asociada a una incidencia específica.
     *
     * @param idIncidencia ID de la incidencia a la que se asociará la nota.
     * @param contenido    Contenido textual de la nota.
     * @param idUsuario    ID del usuario que crea la nota.
     * @return La nota creada y persistida.
     * @throws IllegalArgumentException si la incidencia o el usuario no existen.
     */
    Nota crearNota(Integer idIncidencia, String contenido, Integer idUsuario);

    /**
     * Elimina una nota por su identificador único.
     *
     * @param idNota ID de la nota a eliminar.
     * @throws IllegalArgumentException si la nota no existe.
     */
    void eliminarNota(Integer idNota);

    /**
     * Actualiza el contenido de una nota existente.
     *
     * @param idNota    ID de la nota a actualizar.
     * @param contenido Nuevo contenido para la nota.
     * @return La nota actualizada.
     * @throws IllegalArgumentException si la nota no existe.
     */
    Nota actualizarNota(Integer idNota, String contenido);

    /**
     * Obtiene una nota a partir de su identificador.
     *
     * @param idNota ID de la nota a obtener.
     * @return La nota correspondiente al ID especificado.
     * @throws IllegalArgumentException si la nota no existe.
     */
    Nota obtenerNotaPorId(Integer idNota);
    /**
     * Obtiene todas las notas asociadas a una incidencia específica.
     *
     * @param idIncidencia ID de la incidencia cuyas notas se desean recuperar.
     * @return Lista de notas asociadas a la incidencia.
     */
    List<Nota> obtenerNotasPorIncidencia(Integer idIncidencia);
    /**
     * Recupera todas las notas registradas en el sistema.
     *
     * @return Lista completa de notas.
     */
    List<Nota> obtenerTodasLasNotas();
    
}
