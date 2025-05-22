package ies.ruizgijon.gestorincidencias.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa un token de restablecimiento de contraseña.
 * Este token se utiliza para verificar la identidad del usuario durante el proceso
 * de restablecimiento de contraseña.
 * 
 * @author [Óscar García]
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

public class PasswordResetToken {
    /**
     * ID del token.
     * Este campo es la clave primaria de la entidad PasswordResetToken y se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Token de restablecimiento de contraseña.
     * Este campo es obligatorio y debe ser único para cada usuario.
     */
    @Column(nullable = false, unique = true)
    private String token;

    /**
     * Usuario asociado al token.
     * Este campo es obligatorio y representa el usuario que solicitó el restablecimiento de contraseña.
     */
    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /**
     * Fecha de expiración del token.
     * Este campo es obligatorio y almacena la fecha y hora en que el token expira.
     */
    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;

    /**
     * Método para verificar si el token ha expirado.
     * Este método compara la fecha de expiración del token con la fecha y hora actual
     * @return true si el token ha expirado, false en caso contrario.
     * Este método se utiliza para determinar si el token es válido y puede ser utilizado
     */
    public boolean isExpirado() {
        return fechaExpiracion.isBefore(LocalDateTime.now());
    }

}
