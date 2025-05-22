package ies.ruizgijon.gestorincidencias.service;

import java.util.List;
import ies.ruizgijon.gestorincidencias.model.Rol;

/**
 * Interfaz que define los métodos para gestionar los roles
 * de los usuarios en el sistema.
 * 
 * Proporciona operaciones para guardar, eliminar, buscar y listar roles.
 * 
 * @author Óscar
 */
public interface IRolService {

    /**
     * Guarda un nuevo rol en la base de datos.
     *
     * @param rol El objeto Rol a guardar.
     */
    void guardarRol(Rol rol);
    /**
     * Elimina un rol de la base de datos mediante su ID.
     *
     * @param idRol El identificador del rol a eliminar.
     * @throws IllegalArgumentException si el rol con el ID dado no existe.
     */
    void eliminarRol(Integer idRol);
    /**
     * Busca un rol por su ID.
     *
     * @param idRol El identificador del rol a buscar.
     * @return El rol encontrado o null si no existe.
     */
    Rol buscarRolPorId(Integer idRol);
    
    /**
     * Recupera todos los roles existentes en la base de datos.
     *
     * @return Lista con todos los roles.
     */
    List<Rol> buscarTodos();
}
