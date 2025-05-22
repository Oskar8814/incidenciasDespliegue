package ies.ruizgijon.gestorincidencias.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ies.ruizgijon.gestorincidencias.model.PasswordResetToken;
import ies.ruizgijon.gestorincidencias.model.Usuario;

/**
 * Repositorio para la gestión de tokens de restablecimiento de contraseña en la base de datos.
 * Este repositorio extiende JpaRepository para proporcionar operaciones CRUD
 * sobre la entidad PasswordResetToken.
 * 
 * @author [Óscar García]
 */
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    
    /**
     * Método para buscar un token de restablecimiento de contraseña por su valor.
     * 
     * @param token El valor del token a buscar.
     * @return Un objeto Optional que contiene el token encontrado, o vacío si no se encuentra.
     */
    Optional<PasswordResetToken> findByToken(String token);
    
    /**
     * Método para eliminar un token de restablecimiento de contraseña asociado a un usuario.
     * 
     * @param usuario El usuario asociado al token a eliminar.
     */
    void deleteByUsuario(Usuario usuario);
}
