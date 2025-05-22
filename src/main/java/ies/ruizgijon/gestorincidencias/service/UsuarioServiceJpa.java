package ies.ruizgijon.gestorincidencias.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ies.ruizgijon.gestorincidencias.exceptions.UsuarioNoValidoException;
import ies.ruizgijon.gestorincidencias.model.PasswordResetToken;
import ies.ruizgijon.gestorincidencias.model.Usuario;
import ies.ruizgijon.gestorincidencias.repository.PasswordResetTokenRepository;
import ies.ruizgijon.gestorincidencias.repository.UsuarioRepository;
import ies.ruizgijon.gestorincidencias.util.Validaciones;
import jakarta.transaction.Transactional;

/**
 * Implementación de {@link IUsuarioService} que gestiona la lógica de negocio
 * relacionada con los usuarios en el sistema.
 * 
 * Proporciona métodos para crear, modificar, eliminar, buscar usuarios,
 * así como gestionar la autenticación y recuperación de contraseñas.
 * 
 * Autor: Óscar García
 */
@Service
public class UsuarioServiceJpa implements IUsuarioService {

    /**
     * Repositorio para acceder a los usuarios en la base de datos.
     */
    private final UsuarioRepository usuarioRepository;
    /**
     * Repositorio para acceder a los tokens de restablecimiento de contraseña.
     */
    private final BCryptPasswordEncoder passwordEncoder;
    /**
     * Repositorio para acceder a los tokens de restablecimiento de contraseña.
     */
    private final PasswordResetTokenRepository tokenRepository;

    /**
     * Constructor para la inyección de dependencias.
     * 
     * @param usuarioRepository Repositorio para acceder a usuarios.
     * @param passwordEncoder Codificador de contraseñas.
     * @param tokenRepository Repositorio para tokens de recuperación de contraseña.
     */
    @Autowired
    public UsuarioServiceJpa(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder,
            PasswordResetTokenRepository tokenRepository) {
        this.usuarioRepository = usuarioRepository; // Inicializa el repositorio de usuarios
        this.passwordEncoder = passwordEncoder; // Inicializa el codificador de contraseñas
        this.tokenRepository = tokenRepository; // Inicializa el repositorio de tokens de restablecimiento de contraseña
    }

    /**
     * Guarda un nuevo usuario en la base de datos.
     * 
     * Valida el usuario, comprueba que el correo no esté registrado, y codifica
     * la contraseña antes de guardar.
     * 
     * @param usuario Usuario a guardar.
     * @throws UsuarioNoValidoException si el usuario no es válido.
     * @throws IllegalArgumentException si el correo electrónico ya está registrado.
     */
    @Override
    public void guardarUsuario(Usuario usuario) {
        comprobarCorreoExistente(usuario);
        // Verifica si el usuario es válido antes de guardarlo
        List<String> errores = Validaciones.obtenerErroresValidacionUsuario(usuario);
        if (!errores.isEmpty()) {
            throw new UsuarioNoValidoException(errores);
        }

        // Codifica la contraseña del usuario antes de guardarlo
        String contrasenaCodificada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(contrasenaCodificada);
        // Verifica si el usuario ya existe en la base de datos antes de guardarlo
        usuarioRepository.save(usuario);
    }

    /**
     * Modifica un usuario existente.
     * 
     * Valida el usuario, verifica su existencia y codifica la contraseña antes de
     * guardar.
     * 
     * @param usuario Usuario a modificar.
     * @throws UsuarioNoValidoException si el usuario no es válido.
     * @throws IllegalArgumentException si el usuario no existe.
     */
    @Override
    public void modificarUsuario(Usuario usuario) {
        // Verifica si el usuario es válido antes de modificarlo
        List<String> errores = Validaciones.obtenerErroresValidacionUsuario(usuario);
        if (!errores.isEmpty()) {
            throw new UsuarioNoValidoException(errores);
        }
        // Verifica si el usuario ya existe en la base de datos antes de modificarlo
        if (usuarioRepository.existsById(usuario.getId())) {
            // Codifica la contraseña del usuario antes de guardarlo
            String contrasenaCodificada = passwordEncoder.encode(usuario.getPassword());
            usuario.setPassword(contrasenaCodificada);
            usuarioRepository.save(usuario);
        } else {
            throw new IllegalArgumentException("Usuario no encontrado con ID: " + usuario.getId());
        }
    }

    

    /**
     * Elimina un usuario por su ID.
     * 
     * @param idUsuario ID del usuario a eliminar.
     * @throws IllegalArgumentException si el usuario no existe.
     */

    @Override
    public void eliminarUsuario(Integer idUsuario) {
        // Verifica si el usuario existe antes de eliminarlo
        if (usuarioRepository.existsById(idUsuario)) {
            usuarioRepository.deleteById(idUsuario);
        } else {
            throw new IllegalArgumentException("Usuario no encontrado con ID: " + idUsuario);
        }
    }

    /**
     * Busca un usuario por su ID.
     * 
     * @param idUsuario ID del usuario.
     * @return Usuario encontrado.
     * @throws IllegalArgumentException si el usuario no existe.
     */
    @Override
    public Usuario buscarUsuarioPorId(Integer idUsuario) {
        // Busca el usuario por su ID y devuelve el resultado
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + idUsuario));
    }

    /**
     * Busca un usuario por su nombre de usuario.
     * 
     * @param nombreUsuario Nombre de usuario.
     * @return Usuario encontrado.
     * @throws IllegalArgumentException si el usuario no existe.
     */
    @Override
    public Usuario buscarUsuarioPorNombre(String nombreUsuario) {
        Usuario usuario = usuarioRepository.findByNombre(nombreUsuario).orElse(null);
        // Verifica si el usuario existe antes de devolverlo
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado con nombre: " + nombreUsuario);
        }
        // Si existe, devuelve el usuario. Si no existe, lanza una excepción
        return usuario;
    }

    /**
     * Obtiene todos los usuarios.
     * 
     * @return Lista con todos los usuarios.
     */
    @Override
    public List<Usuario> buscarTodos() {
        // Devuelve todos los usuarios de la base de datos
        return usuarioRepository.findAll();
    }

    /**
     * Busca un usuario por correo electrónico.
     * 
     * @param mail Correo electrónico.
     * @return Usuario encontrado o null si no existe.
     */
    @Override
    public Usuario buscarUsuarioPorMail(String mail) {
        return usuarioRepository.findByMail(mail).orElse(null);
    }
    /**
     * Comprueba si el correo electrónico de un usuario ya está registrado.
     * 
     * @param usuario Usuario a comprobar.
     * @throws UsuarioNoValidoException si el correo electrónico ya está registrado.
     */
    private void comprobarCorreoExistente(Usuario usuario) {
        if (usuarioRepository.findByMail(usuario.getMail()).isPresent()) {
            List<String> errores = new ArrayList<>();
            errores.add("El correo electrónico ya está registrado en el sistema.");
            throw new UsuarioNoValidoException(errores);
        }
    }

    /**
     * Obtiene el usuario actualmente autenticado.
     * 
     * @return Usuario autenticado o null si no hay ninguno.
     */
    @Override
    public Usuario getCurrentUser() {
        // Obtiene el contexto de seguridad actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica si el usuario está autenticado y no es anónimo
        String correo = authentication.getName(); // el username es el correo

        return usuarioRepository.findByMail(correo).orElse(null);
    }

    /**
     * Cambia la contraseña de un usuario.
     * 
     * Valida el usuario y codifica la nueva contraseña antes de guardarla.
     * 
     * @param id ID del usuario.
     * @param contrasena Nueva contraseña.
     * @throws IllegalArgumentException si el usuario no existe.
     * @throws UsuarioNoValidoException si el usuario no es válido.
     */
    @Override
    public void cambiarContrasena(Integer id, String contrasena) {
        // Busca el usuario por su ID
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado con ID: " + id);
        }

        usuario.setPassword(contrasena); // Establece la nueva contraseña

        // Verifica si el usuario es válido antes de modificarlo
        List<String> errores = Validaciones.obtenerErroresValidacionUsuario(usuario);
        if (!errores.isEmpty()) {
            throw new UsuarioNoValidoException(errores);
        }

        // Codifica la nueva contraseña del usuario antes de guardarlo
        String contrasenaCodificada = passwordEncoder.encode(contrasena);
        usuario.setPassword(contrasenaCodificada);

        // Guarda el usuario modificado en la base de datos
        usuarioRepository.save(usuario);
    }
    /**
     * Guarda un token de recuperación de contraseña para un usuario.
     * 
     * Elimina cualquier token anterior asociado al usuario antes de guardar el nuevo.
     * 
     * @param usuario Usuario al que se le asigna el token.
     * @param token Token de recuperación.
     */

    @Override
    @Transactional
    public void guardarTokenDeRecuperacion(Usuario usuario, String token) {
        tokenRepository.deleteByUsuario(usuario); // Elimina cualquier token existente del user
        PasswordResetToken prt = new PasswordResetToken();
        prt.setToken(token); // Asignacion del token
        prt.setUsuario(usuario); // Asignamos el usuario al token
        prt.setFechaExpiracion(LocalDateTime.now().plusHours(1)); // Token valido para 1 hora
        tokenRepository.save(prt);
    }

    /**
     * Valida si un token de recuperación es válido y no ha expirado.
     * 
     * @param token Token a validar.
     * @return true si es válido, false en caso contrario.
     */
    @Override
    public boolean validarToken(String token) {
        return tokenRepository.findByToken(token)
                .map(t -> !t.isExpirado())
                .orElse(false);
    }

    /**
     * Actualiza la contraseña de un usuario utilizando un token de recuperación.
     * 
     * Valida el token y al usuario antes de actualizar la contraseña y elimina el token.
     * 
     * @param token Token de recuperación.
     * @param nuevaPassword Nueva contraseña.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws UsuarioNoValidoException si el usuario no es válido.
     * @throws IllegalArgumentException si el token no existe o ha expirado.
     */
    @Override
    @Transactional
    public boolean actualizarPasswordConToken(String token, String nuevaPassword) {
        Optional<PasswordResetToken> optional = tokenRepository.findByToken(token);
        // Verificamos si el token existe y no ha expirado
        if (optional.isPresent() && !optional.get().isExpirado()) {
            Usuario usuario = optional.get().getUsuario(); // Obtener el usuario asociado al token

            usuario.setPassword(nuevaPassword);
            // Verifica si el usuario es válido antes de modificarlo
            List<String> errores = Validaciones.obtenerErroresValidacionUsuario(usuario);
            if (!errores.isEmpty()) {
                throw new UsuarioNoValidoException(errores);
            }

            usuario.setPassword(passwordEncoder.encode(nuevaPassword));
            usuarioRepository.save(usuario);
            tokenRepository.delete(optional.get()); // Elimina el token tras usarlo
            return true;
        }
        return false;
    }

    /**
     * Busca usuarios que coincidan con un ejemplo dado.
     * 
     * @param example Ejemplo con criterios de búsqueda.
     * @return Lista de usuarios que cumplen con el ejemplo.
     */
    @Override
    public List<Usuario> buscarByExample(Example<Usuario> example) {
        return usuarioRepository.findAll(example);
    }

}
