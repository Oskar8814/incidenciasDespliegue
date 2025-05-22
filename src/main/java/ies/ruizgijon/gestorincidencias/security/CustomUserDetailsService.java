package ies.ruizgijon.gestorincidencias.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import ies.ruizgijon.gestorincidencias.repository.UsuarioRepository;

/**
 * Servicio personalizado para cargar detalles de usuario.
 * Implementa la interfaz UserDetailsService de Spring Security.
 * 
 * @author [Óscar García]
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    /**
     * Repositorio de usuarios para acceder a la base de datos.
     * Este repositorio proporciona métodos para realizar operaciones CRUD sobre usuarios.
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor del servicio CustomUserDetailsService.
     * 
     * @param usuarioRepository Repositorio de usuarios.
     */
    @Autowired
    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Método para cargar un usuario por su correo electrónico.
     * 
     * @param mail Correo electrónico del usuario a buscar.
     * @return Detalles del usuario encontrado.
     * @throws UsernameNotFoundException Si no se encuentra el usuario con el correo proporcionado.
     */
    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        return usuarioRepository.findByMail(mail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + mail));
    }
}

