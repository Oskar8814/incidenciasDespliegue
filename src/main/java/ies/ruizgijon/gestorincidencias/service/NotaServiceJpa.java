package ies.ruizgijon.gestorincidencias.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies.ruizgijon.gestorincidencias.model.Nota;
import ies.ruizgijon.gestorincidencias.repository.IncidenciasRepository;
import ies.ruizgijon.gestorincidencias.repository.NotaRepository;
import ies.ruizgijon.gestorincidencias.repository.UsuarioRepository;

/**
 * Implementación de {@link INotaService} que gestiona la lógica
 * para la creación, actualización, eliminación y consulta de notas asociadas
 * a incidencias y usuarios en el sistema.
 * 
 * @author Óscar García
 */
@Service
public class NotaServiceJpa implements INotaService {

    /**
     * Repositorio para acceder a las notas en la base de datos.
     */
    private final NotaRepository notaRepository;
    
    /**
     * Repositorio para acceder a las incidencias en la base de datos.
     */
    private final IncidenciasRepository incidenciaRepository;
    
    /**
     * Repositorio para acceder a los usuarios en la base de datos.
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor con inyección de dependencias de los repositorios necesarios.
     * 
     * @param notaRepository       Repositorio para operaciones sobre notas.
     * @param incidenciaRepository Repositorio para operaciones sobre incidencias.
     * @param usuarioRepository    Repositorio para operaciones sobre usuarios.
     */
    @Autowired
    public NotaServiceJpa(NotaRepository notaRepository, IncidenciasRepository incidenciaRepository, UsuarioRepository usuarioRepository) {
        this.notaRepository = notaRepository;
        this.incidenciaRepository = incidenciaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Crea y guarda una nueva nota asociada a una incidencia y un usuario.
     * 
     * @param idIncidencia ID de la incidencia relacionada.
     * @param contenido    Contenido textual de la nota.
     * @param idUsuario    ID del usuario autor de la nota.
     * @return La nota creada y persistida.
     */
    @Override
    public Nota crearNota(Integer idIncidencia, String contenido, Integer idUsuario) {
        // Lógica para crear una nueva nota
        Nota nuevaNota = new Nota();
        nuevaNota.setContenido(contenido);
        nuevaNota.setAutor(usuarioRepository.findById(idUsuario).orElse(null));
        nuevaNota.setIncidencia(incidenciaRepository.findById(idIncidencia).orElse(null));
        
        return notaRepository.save(nuevaNota);
    }

    /**
     * Elimina una nota por su ID.
     * 
     * @param idNota ID de la nota a eliminar.
     */
    @Override
    public void eliminarNota(Integer idNota) {
        // Lógica para eliminar una nota por su ID
        notaRepository.deleteById(idNota);
    }

    /**
     * Actualiza el contenido de una nota.
     * Si la nota no existe, no se realiza ninguna acción.
     * 
     * @param idNota ID de la nota a actualizar.
     * @param contenido Nuevo contenido de la nota.
     * @return La nota actualizada en caso de que exista sino null.
     */
    @Override
    public Nota actualizarNota(Integer idNota, String contenido) {
        // Lógica para actualizar el contenido de una nota
        Nota notaExistente = notaRepository.findById(idNota).orElse(null);
        if (notaExistente != null) {
            notaExistente.setContenido(contenido);
            return notaRepository.save(notaExistente);
        }
        return null;
    }

    /**
     * Obtiene una nota por su ID.
     * 
     * @param idNota ID de la nota a buscar.
     * @return La nota encontrada o null si no se encuentra.
     */
    @Override
    public Nota obtenerNotaPorId(Integer idNota) {
        // Lógica para obtener una nota por su ID
        return notaRepository.findById(idNota).orElse(null);
    }

    /**
     * Obtiene todas las notas asociadas a una incidencia específica.
     * 
     * @param idIncidencia ID de la incidencia a la que se asocian las notas.
     * @return Una lista de notas asociadas a la incidencia.
     */
    @Override
    public List<Nota> obtenerNotasPorIncidencia(Integer idIncidencia) {
        // Lógica para obtener todas las notas asociadas a una incidencia
        return notaRepository.findAllByIncidenciaId(idIncidencia);
    }

    /**
     * Recupera todas las notas almacenadas en el sistema.
     * 
     * @return Lista de todas las notas.
     */
    @Override
    public List<Nota> obtenerTodasLasNotas() {
        // Lógica para obtener todas las notas
        return notaRepository.findAll();
    }

}
