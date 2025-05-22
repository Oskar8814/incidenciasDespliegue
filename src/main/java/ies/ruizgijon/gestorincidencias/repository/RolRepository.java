package ies.ruizgijon.gestorincidencias.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ies.ruizgijon.gestorincidencias.model.Rol;

/**
 * Repositorio para la gestión de roles en la base de datos.
 * Este repositorio extiende JpaRepository para proporcionar operaciones CRUD
 * sobre la entidad Rol.
 * 
 * @author [Óscar García]
 */
public interface RolRepository extends JpaRepository<Rol, Integer> {
    // Aquí se pueden agregar métodos personalizados si es necesario
}
