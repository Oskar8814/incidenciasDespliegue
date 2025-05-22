package ies.ruizgijon.gestorincidencias.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Entidad que representa una incidencia reportada en el sistema.
 * Contiene información como el título, descripción, fecha, aula afectada,
 * imagen, estado actual, usuarios relacionados y notas asociadas.
 * 
 * @author Óscar García
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Incidencia {
    /**
     * ID de la incidencia.
     * Este campo es la clave primaria de la entidad Incidencia y se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Título de la incidencia.
     * Este campo es obligatorio y tiene una longitud máxima de 100 caracteres.
     */
    @Column(length = 100, nullable = false)
    private String titulo;

    /**
     * Fecha de la incidencia.
     * Este campo es obligatorio y almacena la fecha en que se reportó la incidencia.
     */
    @Column(nullable = false)
    private LocalDate fecha;

    /**
     * Descripción de la incidencia.
     * Este campo es obligatorio y almacena una descripción detallada de la incidencia.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    /**
     * Aula de la incidencia.
     * Este campo es opcional y tiene una longitud máxima de 20 caracteres.
     */
    @Column(length = 20)
    private String aula;

    /**
     * Imagen de la incidencia.
     * Este campo es opcional y tiene una longitud máxima de 255 caracteres.
     */
    @Column(length = 255)
    private String imagen;

    /**
     * Estado de la incidencia.
     * Este campo es obligatorio y almacena el estado actual de la incidencia.
     * Se utiliza un enumerador para definir los posibles estados.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private EstadoIncidencia estado = EstadoIncidencia.PENDIENTE;

    /**
     * Usuario asignado a la incidencia.
     * Este campo es opcional y establece una relación con la entidad Usuario.
     * Representa al usuario que gestiona la incidencia.
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario; // Quien gestiona

    /**
     * Usuario creador de la incidencia.
     * Este campo es opcional y establece una relación con la entidad Usuario.
     * Representa al usuario que reportó la incidencia.
     */
    @ManyToOne
    @JoinColumn(name = "creador_id", nullable = true)
    private Usuario creador; // Quien reportó

    /**
     * Notas asociadas a la incidencia.
     * Este campo establece una relación uno a muchos con la entidad Nota.
     * Representa las notas o comentarios relacionados con la incidencia.
     */
    @OneToMany(mappedBy = "incidencia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Nota> notas;
}

