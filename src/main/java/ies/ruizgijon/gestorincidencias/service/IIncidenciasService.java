package ies.ruizgijon.gestorincidencias.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;


import ies.ruizgijon.gestorincidencias.model.EstadoIncidencia;
import ies.ruizgijon.gestorincidencias.model.Incidencia; // Import Incidencia class

/**
 * Esta interfaz define los métodos que deben implementarse para gestionar incidencias.
 * Incluye métodos para crear, consultar, actualizar, eliminar, asignar, filtrar y paginar incidencias.
 * 
 * @author [Óscar García]
 */
public interface IIncidenciasService {
    /**
     * Guarda una incidencia en la base de datos.
     * 
     * @param incidencia La incidencia a guardar.
     */
    void guardarIncidencia(Incidencia incidencia);
    
    /**
     * Elimina una incidencia por su ID.
     * 
     * @param idIncidencia El ID de la incidencia a eliminar.
     */
    void eliminarIncidencia(Integer idIncidencia); // Cambiado a integer para que coincida con el tipo de id en la clase Incidencia
    /**
     * Busca una incidencia por su ID.
     * 
     * @param idIncidencia El ID de la incidencia a buscar.
     * @return La incidencia encontrada, o {@code null} si no existe.
     */
    Incidencia buscarIncidenciaPorId(Integer idIncidencia);
    /**
     * Busca todas las incidencias.
     * 
     * @return Una lista de todas las incidencias.
     * Este método busca todas las incidencias en la base de datos.
     */
    List<Incidencia> buscarTodas();
    
    /**
     * Busca incidencias por su estado.
     * 
     * @param estado El estado de las incidencias a buscar.
     * @return Una lista de incidencias con el estado especificado.
     * Este método busca incidencias en la base de datos por su estado.
     */
    List<Incidencia> buscarPorEstado(EstadoIncidencia estado); // Cambiado a Enum para que coincida con el tipo de estado en la clase Incidencia
    
    /**
     * Asigna una incidencia a un usuario.
     * 
     * @param idIncidencia El ID de la incidencia a asignar.
     * @param idUsuario El ID del usuario al que se asigna la incidencia.
     * Este método asigna una incidencia a un usuario en la base de datos.
     */
    public void asignarIncidencia(Integer idIncidencia, Integer idUsuario);
    
    /**
     * Desasigna una incidencia de un usuario.
     * 
     * @param idIncidencia El ID de la incidencia a desasignar.
     * Este método desasigna una incidencia de un usuario en la base de datos.
     */
    public void desasignarIncidencia(Integer idIncidencia);
    
    /**
     * Marca una incidencia como cerrada.
     * 
     * @param idIncidencia El ID de la incidencia a cerrar.
     * Este método cierra una incidencia en la base de datos.
     */
    public void cerrarIncidencia(Integer idIncidencia);
    
    /**
     * Busca incidencias que coincidan con un ejemplo dado.
     * 
     * @param example El ejemplo de incidencia a buscar.
     * @return Una lista de incidencias que coinciden con el ejemplo.
     * Este método busca incidencias en la base de datos por un ejemplo dado.
     */
    List<Incidencia> buscarByExample(Example<Incidencia> example);
    
    /**
     * Busca incidencias paginadas.
     * 
     * @param pageable La información de paginación.
     * @return Una página de incidencias.
     * Este método busca incidencias en la base de datos y devuelve una página de resultados.
     */
    Page<Incidencia> buscarIncidenciasPaginadas(Pageable pageable);
    
    /**
     * Busca incidencias que coincidan con un ejemplo dado, con resultados paginados.
     * 
     * @param estado El estado de las incidencias a buscar.
     * @param pageable La información de paginación.
     * @return Una página de incidencias con el estado especificado.
     * 
     */
    public Page<Incidencia> buscarIncidenciasPorEstadoPaginadas(EstadoIncidencia estado, Pageable pageable);
    
    /**
     * Busca incidencias que coincidan con un ejemplo dado, con resultados paginados.
     * 
     * @param example El ejemplo de incidencia a buscar.
     * @param pageable La información de paginación.
     * @return Una página de incidencias que coinciden con el ejemplo.
     */
    public Page<Incidencia> buscarIncidenciasByExamplePaginadas(Example<Incidencia> example, Pageable pageable); 
}
