package ies.ruizgijon.gestorincidencias.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ies.ruizgijon.gestorincidencias.model.Usuario;

/**
 * Repositorio para la gestión de usuarios en la base de datos.
 * Este repositorio extiende JpaRepository para proporcionar operaciones CRUD
 * sobre la entidad Usuario.
 * 
 * @author [Óscar García]
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    /**
     * Método para buscar un usuario por su nombre de usuario.
     * 
     * @param nombreUsuario El nombre de usuario a buscar.
     * @return Un objeto Optional que contiene el usuario encontrado, o vacío si no se encuentra.
     */
    Optional<Usuario> findByNombre(String nombreUsuario);
    /**
     * Método para buscar un usuario por su correo electrónico.
     * 
     * @param mail El correo electrónico a buscar.
     * @return Un objeto Optional que contiene el usuario encontrado, o vacío si no se encuentra.
     */
    Optional<Usuario> findByMail(String mail);
}
