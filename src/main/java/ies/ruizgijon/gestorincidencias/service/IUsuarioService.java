package ies.ruizgijon.gestorincidencias.service;

import java.util.List;

import org.springframework.data.domain.Example;
import ies.ruizgijon.gestorincidencias.model.Usuario;

/**
 * Interfaz que define los métodos para gestionar los usuarios en el sistema.
 * Proporciona operaciones para guardar, eliminar, modificar y buscar usuarios,
 * así como para la gestión de recuperación de contraseñas.
 * 
 * @author Óscar
 */
public interface IUsuarioService {
    
    /**
     * Guarda un nuevo usuario en la base de datos.
     * 
     * @param usuario El usuario a guardar.
     */
    void guardarUsuario(Usuario usuario);
    /**
     * Elimina un usuario por su ID.
     * 
     * @param idUsuario ID del usuario a eliminar.
     */
    void eliminarUsuario(Integer idUsuario); // Cambiado a integer para que coincida con el tipo de id en la clase Usuario

    /**
     * Modifica un usuario existente en la base de datos.
     * 
     * @param usuario El usuario con los datos actualizados.
     */
    public void modificarUsuario(Usuario usuario);
    /**
     * Busca un usuario por su ID.
     * 
     * @param idUsuario ID del usuario a buscar.
     * @return El usuario encontrado o null si no existe.
     */
    Usuario buscarUsuarioPorId(Integer idUsuario);
    
    /**
     * Busca un usuario por su nombre de usuario.
     * 
     * @param nombreUsuario Nombre de usuario a buscar.
     * @return El usuario encontrado o null si no existe.
     */
    Usuario buscarUsuarioPorNombre(String nombreUsuario); // Cambiado a String para que coincida con el tipo de nombre en la clase Usuario
    
    /**
     * Recupera todos los usuarios registrados.
     * 
     * @return Lista de todos los usuarios.
     */
    List<Usuario> buscarTodos(); // Cambiado a String para que coincida con el tipo de nombre en la clase Usuario
    
    /**
     * Busca un usuario por su correo electrónico.
     * 
     * @param mail Correo electrónico a buscar.
     * @return El usuario encontrado o null si no existe.
     */
    Usuario buscarUsuarioPorMail(String mail);
    
    /**
     * Obtiene el usuario actualmente autenticado.
     * 
     * @return El usuario actual.
     */
    public Usuario getCurrentUser();

    /**
     * Cambia la contraseña de un usuario.
     * 
     * @param id ID del usuario cuya contraseña será cambiada.
     * @param contrasena Nueva contraseña.
     */
    void cambiarContrasena(Integer id, String contrasena);
    /**
     * Valida un token de recuperación de contraseña.
     * 
     * @param token Token a validar.
     * @return true si el token es válido; false en caso contrario.
     */
    public boolean validarToken(String token);

    /**
     * Actualiza la contraseña de un usuario usando un token de recuperación.
     * 
     * @param token Token de recuperación.
     * @param nuevaPassword Nueva contraseña a establecer.
     * @return true si la actualización fue exitosa; false en caso contrario.
     */
    public boolean actualizarPasswordConToken(String token, String nuevaPassword);
    
    /**
     * Guarda un token de recuperación para un usuario.
     * 
     * @param usuario Usuario al que se le asigna el token.
     * @param token Token de recuperación.
     */
    public void guardarTokenDeRecuperacion(Usuario usuario, String token);
    
    /**
     * Busca usuarios que coincidan con un ejemplo dado.
     * 
     * @param example Ejemplo con los criterios de búsqueda.
     * @return Lista de usuarios que coinciden con el ejemplo.
     */
    List<Usuario> buscarByExample(Example<Usuario> example);
} 
