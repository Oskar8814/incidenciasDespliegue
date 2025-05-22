package ies.ruizgijon.gestorincidencias.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies.ruizgijon.gestorincidencias.model.Rol;
import ies.ruizgijon.gestorincidencias.repository.RolRepository;

/**
 * Implementación de {@link IRolService} que gestiona la lógica 
 * para la creación, consulta, actualización y eliminación de roles en el sistema.
 * 
 * Autor: Óscar García
 */
@Service
public class RolServiceJpa implements IRolService {

    /**
     * Repositorio para acceder a los roles en la base de datos.
     */
    private final RolRepository rolRepository; // Inyección de dependencia del servicio de roles

    /**
     * Constructor que inyecta el repositorio de roles.
     * 
     * @param rolRepository Repositorio de roles.
     */
    @Autowired
    public RolServiceJpa(RolRepository rolRepository) {
        this.rolRepository = rolRepository; // Inicializa el repositorio de roles
    }

    /**
     * Guarda un rol en la base de datos.
     * 
     * @param rol El rol a guardar.
     * Este método guarda el rol en la base de datos.
     */
    @Override
    public void guardarRol(Rol rol) {
        // Implementación para guardar un rol en la base de datos
        rolRepository.save(rol);        
    }

    /**
     * Elimina un rol existente por su ID.
     * 
     * @param idRol ID del rol a eliminar.
     * @throws IllegalArgumentException si el rol con el ID dado no existe.
     * @throws NullPointerException si el ID es nulo.
     */
    @Override
    public void eliminarRol(Integer idRol) {
        // Verifica si el rol existe antes de eliminarlo
        if (rolRepository.existsById(idRol)) {
            rolRepository.deleteById(idRol);
        } else {
            throw new IllegalArgumentException("Rol no encontrado con ID: " + idRol);
        }
    }

    /**
     * Busca un rol por su ID.
     * 
     * @param idRol ID del rol a buscar.
     * @return El rol encontrado.
     * @throws IllegalArgumentException si el rol con el ID dado no existe.
     * @throws NullPointerException si el ID es nulo.
     */
    @Override
    public Rol buscarRolPorId(Integer idRol) {
        // Busca un rol por su ID en la base de datos
        return rolRepository.findById(idRol).orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con ID: " + idRol));
    }

    /**
     * Obtiene una lista con todos los roles existentes en la base de datos.
     * 
     * @return Lista de roles.
     */
    @Override
    public List<Rol> buscarTodos() {
        // Devuelve todos los roles de la base de datos
        return rolRepository.findAll();
    }
    
}
