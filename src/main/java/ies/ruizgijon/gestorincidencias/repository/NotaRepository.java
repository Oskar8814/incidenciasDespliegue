package ies.ruizgijon.gestorincidencias.repository;

import ies.ruizgijon.gestorincidencias.model.Nota;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la gestión de notas en la base de datos.
 * Este repositorio extiende JpaRepository para proporcionar operaciones CRUD
 * sobre la entidad Nota.
 * 
 * @author [Óscar García]
 */
public interface NotaRepository extends JpaRepository<Nota, Integer> {
    /**
     * Método para buscar notas por el ID de la incidencia asociada.
     * 
     * @param idIncidencia El ID de la incidencia asociada a las notas.
     * @return Una lista de notas que están asociadas a la incidencia con el ID proporcionado.
     */
    List<Nota> findAllByIncidenciaId(Integer idIncidencia);
}
