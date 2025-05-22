package ies.ruizgijon.gestorincidencias.model;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.*;

/**
 * Clase que representa un usuario en el sistema.
 * Esta clase implementa la interfaz UserDetails de Spring Security
 * para proporcionar información sobre el usuario y sus roles.
 * 
 * @author [Óscar García]
 */
/**
 * La anotación @Entity indica que esta clase es una entidad JPA y se mapeará a una tabla en la base de datos.
 * La tabla correspondiente se llamará "usuario".
 */
@Entity
/**
 * La anotación @Data genera automáticamente los métodos getter y setter,
 * así como el método toString(), equals() y hashCode() para la clase.
 */
@Data
/**
 * La anotación @NoArgsConstructor genera un constructor sin argumentos para la clase.
 */
@NoArgsConstructor
/**
 * La anotación @AllArgsConstructor genera un constructor con todos los argumentos
 * para la clase.
 */
@AllArgsConstructor
public class Usuario implements UserDetails {
    /**
     * ID del usuario.
     * Este campo es la clave primaria de la entidad Usuario y se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre del usuario.
     * Este campo es obligatorio y tiene una longitud máxima de 50 caracteres.
     */
    @Column(length = 50, nullable = false)
    private String nombre;

    /**
     * Apellido 1 del usuario.
     * Este campo es obligatorio y tiene una longitud máxima de 50 caracteres.
     */
    @Column(length = 50, nullable = false)
    private String apellido1;

    /**
     * Apellido 2 del usuario.
     * Este campo es opcional y tiene una longitud máxima de 50 caracteres.
     */
    @Column(length = 50)
    private String apellido2;

    /**
     * Correo electrónico del usuario.
     * Este campo es obligatorio, tiene una longitud máxima de 100 caracteres
     * y debe ser único en la base de datos.
     */
    @Column(length = 100, nullable = false, unique = true)
    private String mail;

    /**
     * Teléfono del usuario.
     * Este campo es opcional y tiene una longitud máxima de 15 caracteres.
     */
    @Column(length = 255, nullable = false)
    private String password;

    /**
     * Rol del usuario.
     * Este campo es obligatorio y establece una relación con la entidad Rol.
     * Representa el rol asignado al usuario en el sistema.
     */
    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;
    /**
     * GrantedAuthority del usuario.
     * Este método devuelve una colección de autoridades (roles) del usuario.
     * En este caso, se devuelve un conjunto que contiene el rol del usuario.
     * Este método es parte de la interfaz UserDetails
     * y se utiliza para la autenticación y autorización en Spring Security.
     * @return una colección de autoridades (roles) del usuario.
     * En este caso, se devuelve un conjunto que contiene el rol del usuario.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(rol); // rol ya implementa GrantedAuthority
    }  

    /**
    * Método para obtener el nombre de usuario.
    * Este método devuelve el correo electrónico del usuario como nombre de usuario.
    * Este método es parte de la interfaz UserDetails
    * y se utiliza para la autenticación en Spring Security.
    * @return el correo electrónico del usuario.
    */
    @Override
    public String getUsername() {
        return mail; // Devuelve el correo electrónico como nombre de usuario
    }

    /**
     * Método para verificar si la cuenta del usuario no ha expirado.
     * Este método siempre devuelve true, ya que la cuenta nunca expira en este caso.
     * Este método es parte de la interfaz UserDetails
     * y se utiliza para la autenticación en Spring Security.
     * @return true si la cuenta no ha expirado, false en caso contrario.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true; // La cuenta nunca expira
    }
    /**
     * Método para verificar si la cuenta del usuario no está bloqueada.
     * Este método siempre devuelve true, ya que la cuenta nunca está bloqueada en este caso.
     * Este método es parte de la interfaz UserDetails
     * y se utiliza para la autenticación en Spring Security.
     * @return true si la cuenta no está bloqueada, false en caso contrario.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true; // La cuenta nunca está bloqueada
    }
    /**
     * Método para verificar si las credenciales del usuario no han expirado.
     * Este método siempre devuelve true, ya que las credenciales nunca expiran en este caso.
     * Este método es parte de la interfaz UserDetails
     * y se utiliza para la autenticación en Spring Security.
     * @return true si las credenciales no han expirado, false en caso contrario.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Las credenciales nunca expiran
    }

    /**
     * Método para verificar si la cuenta del usuario está habilitada.
     * Este método siempre devuelve true, ya que la cuenta siempre está habilitada en este caso.
     * Este método es parte de la interfaz UserDetails
     * y se utiliza para la autenticación en Spring Security.
     * @return true si la cuenta está habilitada, false en caso contrario.
     */
    @Override
    public boolean isEnabled() {
        return true; // La cuenta siempre está habilitada
    }

    /**
     * Método para obtener la contraseña del usuario.
     * Este método devuelve la contraseña del usuario.
     * Este método es parte de la interfaz UserDetails
     * y se utiliza para la autenticación en Spring Security.
     * @return la contraseña del usuario.
     */
    @Override
    public String getPassword() {
        return password; // Devuelve la contraseña del usuario
    }

}
