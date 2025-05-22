package ies.ruizgijon.gestorincidencias.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa una nota asociada a una incidencia.
 * Contiene contenido textual, la fecha de creación, el autor y la incidencia relacionada.
 * 
 * @author Óscar García
 */

/**
 * La anotación @Entity indica que esta clase es una entidad JPA y se mapeará a una tabla en la base de datos.
 * La tabla correspondiente se llamará "nota".
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
public class Nota {

    /**
     * ID de la nota.
     * Este campo es la clave primaria de la entidad Nota y se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Contenido de la nota.
     * Este campo es obligatorio y almacena el texto de la nota.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    /**
     * Fecha de creación de la nota.
     * Este campo es obligatorio y almacena la fecha y hora en que se creó la nota.
     */
    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    /**
     * Autor de la nota.
     * Este campo es opcional y representa el usuario que creó la nota.
     */
    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = true)
    private Usuario autor;

    /**
     * Incidencia asociada a la nota.
     * Este campo es opcional y representa la incidencia a la que está vinculada la nota.
     */
    @ManyToOne
    @JoinColumn(name = "incidencia_id", nullable = true)
    private Incidencia incidencia;
}

