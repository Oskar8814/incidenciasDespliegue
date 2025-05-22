package ies.ruizgijon.gestorincidencias.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ies.ruizgijon.gestorincidencias.model.EstadoIncidencia;
import ies.ruizgijon.gestorincidencias.model.Incidencia;

/**
 * Repositorio para la gestión de incidencias en la base de datos.
 * Este repositorio extiende JpaRepository para proporcionar operaciones CRUD
 * sobre la entidad Incidencia.
 * 
 * @author [Óscar García]
 */
public interface IncidenciasRepository extends JpaRepository<Incidencia, Integer> {

    /**
     * Método para buscar incidencias por su estado.
     * 
     * @param estado El estado de la incidencia a buscar.
     * @return Una lista de incidencias que coinciden con el estado proporcionado.
     */
    List<Incidencia> findByEstado(EstadoIncidencia estado);
    
    /**
     * Método para buscar incidencias por su estado con paginación.
     * 
     * @param estado El estado de la incidencia a buscar.
     * @param pageable Objeto Pageable que contiene información de paginación.
     * @return Una página de incidencias que coinciden con el estado proporcionado.
     */
    Page<Incidencia> findByEstado(EstadoIncidencia estado, Pageable pageable);
}
