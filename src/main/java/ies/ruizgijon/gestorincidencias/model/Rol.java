package ies.ruizgijon.gestorincidencias.model;

import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import jakarta.persistence.*;
import lombok.*;

/**
 * Clase que representa un rol de usuario en el sistema.
 * Esta clase implementa la interfaz GrantedAuthority de Spring Security
 * para proporcionar información sobre los roles de usuario.
 * 
 * @author [Óscar García]
 */
/**
 * La anotación @Entity indica que esta clase es una entidad JPA y se mapeará a una tabla en la base de datos.
 * La tabla correspondiente se llamará "rol".
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
public class Rol implements GrantedAuthority {
    /**
     * ID del rol.
     * Este campo es la clave primaria de la entidad Rol y se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre del rol.
     * Este campo es obligatorio y tiene una longitud máxima de 50 caracteres.
     */
    @Column(length = 50, nullable = false)
    private String name;
    
    /**
     * Método para obtener la autoridad del rol.
     * Este método devuelve el nombre del rol con el prefijo "ROLE_",
     * que es el formato esperado por Spring Security.
     */
    @Override
    public String getAuthority() {
        return "ROLE_" + name; // Prefijo que Spring Security espera
    }
    /** 
     * Método hashCode para calcular el código hash del rol.
     * Este método utiliza los campos id y name para calcular el código hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    /**
     * Método equals para comparar dos objetos Rol.
     * Este método compara los campos id y name de ambos objetos.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Rol other = (Rol) obj;
        return Objects.equals(id, other.id) && Objects.equals(name, other.name);
    }
    
}

