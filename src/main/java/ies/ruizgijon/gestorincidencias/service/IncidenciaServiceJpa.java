package ies.ruizgijon.gestorincidencias.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ies.ruizgijon.gestorincidencias.exceptions.IncidenciaNoValidoException;
import ies.ruizgijon.gestorincidencias.model.EstadoIncidencia;
import ies.ruizgijon.gestorincidencias.model.Incidencia;
import ies.ruizgijon.gestorincidencias.model.Usuario;
import ies.ruizgijon.gestorincidencias.repository.IncidenciasRepository;
import ies.ruizgijon.gestorincidencias.repository.UsuarioRepository;
import ies.ruizgijon.gestorincidencias.util.Validaciones;

/**
 * Implementación del servicio de gestión de incidencias utilizando JPA.
 * Proporciona operaciones CRUD, asignación, desasignación y búsqueda paginada de incidencias.
 * 
 * @author Óscar García
 */
@Service
public class IncidenciaServiceJpa implements IIncidenciasService {
    /**
     * Repositorio para operaciones con entidades Incidencia.
     */
    private final IncidenciasRepository incidenciasRepository;

    /**
     * Repositorio para operaciones con entidades Usuario.
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor de la clase IncidenciaServiceJpa.
     * 
     * @param incidenciasRepository Repositorio de incidencias.
     * @param usuarioRepository Repositorio de usuarios.
     * Este constructor utiliza la inyección de dependencias para inicializar los
     * repositorios necesarios para la gestión de incidencias y usuarios.
     */
    @Autowired
    public IncidenciaServiceJpa(IncidenciasRepository incidenciasRepository, UsuarioRepository usuarioRepository) {
        this.incidenciasRepository = incidenciasRepository; // Inicializa el repositorio de incidencias
        this.usuarioRepository = usuarioRepository; // Inicializa el repositorio de usuarios
    }


    /**
     * Guarda una incidencia tras validarla.
     * 
     * @param incidencia La incidencia a guardar.
     * Este método guarda la incidencia en la base de datos.
     * @throws IncidenciaNoValidoException Si la incidencia no es válida.
     * Este método utiliza la clase Validaciones para verificar si la incidencia es
     * válida antes de guardarla.
     * Si la incidencia no es válida, lanza una excepción IncidenciaNoValidoException
     */
    @Override
    public void guardarIncidencia(Incidencia incidencia) {

        // Verifica si la incidencia es válida antes de guardarla
        List<String> errores = Validaciones.obtenerErroresValidacionIncidencia(incidencia);
        if (!errores.isEmpty()) {
            throw new IncidenciaNoValidoException(errores);
        }

        incidenciasRepository.save(incidencia);
    }

    /**
     * Elimina una incidencia por su ID.
     * 
     * @param idIncidencia El ID de la incidencia a eliminar.
     * Este método elimina la incidencia de la base de datos.
     * @throws IllegalArgumentException Si la incidencia no existe.
     * Este método verifica si la incidencia existe antes de eliminarla.
     * Si la incidencia no existe, lanza una excepción IllegalArgumentException.
     */
    @Override
    public void eliminarIncidencia(Integer idIncidencia) {
        // Verifica si la incidencia existe antes de eliminarla
        if (incidenciasRepository.existsById(idIncidencia)) {
            incidenciasRepository.deleteById(idIncidencia);
        } else {
            throw new IllegalArgumentException("Incidencia no encontrada con ID: " + idIncidencia);
        }
    }

    /**
     * Busca una incidencia por su ID.
     * 
     * @param idIncidencia El ID de la incidencia a buscar.
     * @return La incidencia encontrada o null si no se encuentra.
     * Este método busca una incidencia en la base de datos por su ID.
     * Este método verifica si la incidencia existe antes de devolverla.
     * Si la incidencia no existe, lanza una excepción IllegalArgumentException.
     * @throws IllegalArgumentException Si la incidencia no es válida.
     */
    @Override
    public Incidencia buscarIncidenciaPorId(Integer idIncidencia) {
        Optional<Incidencia> incidenciaOpt = incidenciasRepository.findById(idIncidencia);
        if (incidenciaOpt.isPresent()) {
            return incidenciaOpt.get();
        } else {
            throw new IllegalArgumentException("Incidencia no encontrada con ID: " + idIncidencia);
        }
    }

    /**
     * Obtiene todas las incidencias.
     * 
     * @return Una lista de todas las incidencias.
     * Este método busca todas las incidencias en la base de datos.
     */
    @Override
    public List<Incidencia> buscarTodas() {
        // Devuelve todas las incidencias de la base de datos
        return incidenciasRepository.findAll();
    }

    /**
     * Busca incidencias por estado.
     * 
     * @param estado El estado de las incidencias a buscar.
     * @return Una lista de incidencias con el estado especificado.
     * Este método busca incidencias en la base de datos por su estado.
     */
    @Override
    public List<Incidencia> buscarPorEstado(EstadoIncidencia estado) {
        // Devuelve todas las incidencias que coinciden con el estado dado
        return incidenciasRepository.findByEstado(estado);
    }

    // //Metodo para asignar una incidencia a un usuario
    /**
     * Asigna una incidencia a un usuario.
     * 
     * @param idIncidencia El ID de la incidencia a asignar.
     * @param idUsuario    El ID del usuario al que se asigna la incidencia.
     * Este método asigna una incidencia a un usuario en la base de datos.
     * Este método busca la incidencia y el usuario por sus IDs.
     * Si la incidencia o el usuario no existen, lanza una excepción IllegalArgumentException.
     * Cambia el estado de la incidencia a "EN PROGRESO/REPARACION".
     * Guarda la incidencia actualizada en la base de datos.
     * @throws IllegalArgumentException Si la incidencia no es válida.
     * Este método verifica si la incidencia es válida antes de asignarla.
     * Si la incidencia no es válida, lanza una excepción IncidenciaNoValidoException.
     * Este método utiliza la clase Validaciones para verificar si la incidencia es
     * válida antes de asignarla.
     * Si la incidencia no es válida, lanza una excepción IncidenciaNoValidoException.
     * @throws IncidenciaNoValidoException Si la incidencia no es válida.
     */
    @Override
    public void asignarIncidencia(Integer idIncidencia, Integer idUsuario) {
        // Busca la incidencia por su ID
        Optional<Incidencia> incidenciaOpt = incidenciasRepository.findById(idIncidencia);
        // Busca el usuario por su ID (aquí se asume que tienes un método para buscar usuarios)
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        // Verifica si la incidencia existe
        if (incidenciaOpt.isPresent() && usuarioOpt.isPresent()) {
            // Obtiene la incidencia y el usuario
            Incidencia incidencia = incidenciaOpt.get();
            Usuario usuario = usuarioOpt.get();

            // Asigna el usuario a la incidencia
            incidencia.setUsuario(usuario);
            // Cambia el estado de la incidencia a "EN PROGRESO/REPARACION"
            incidencia.setEstado(EstadoIncidencia.REPARACION);
            
            // Guarda la incidencia actualizada en la base de datos
            incidenciasRepository.save(incidencia);
        } else {
            throw new IllegalArgumentException("Incidencia no encontrada con ID: " + idIncidencia);
        }
    }

    // Metodo para desasignar una incidencia a un usuario
    /**
     * Desasigna una incidencia de un usuario.
     * 
     * @param idIncidencia El ID de la incidencia a desasignar.
     * Este método desasigna una incidencia de un usuario en la base de datos.
     * Este método busca la incidencia por su ID.
     * Si la incidencia no existe, lanza una excepción IllegalArgumentException.
     * Cambia el estado de la incidencia a "PENDIENTE".
     * Guarda la incidencia actualizada en la base de datos.
     * @throws IllegalArgumentException Si la incidencia no es válida.
     */
    @Override
    public void desasignarIncidencia(Integer idIncidencia) {
        // Busca la incidencia por su ID
        Optional<Incidencia> incidenciaOpt = incidenciasRepository.findById(idIncidencia);
        // Verifica si la incidencia existe
        if (incidenciaOpt.isPresent()) {
            // Obtiene la incidencia
            Incidencia incidencia = incidenciaOpt.get();
            // Cambia el estado de la incidencia a "PENDIENTE"
            incidencia.setEstado(EstadoIncidencia.PENDIENTE);
            // Desasigna el usuario de la incidencia
            incidencia.setUsuario(null);
            // Guarda la incidencia actualizada en la base de datos
            incidenciasRepository.save(incidencia);
        } else {
            throw new IllegalArgumentException("Incidencia no encontrada con ID: " + idIncidencia);
        }
    }

    /**
     * Cierra una incidencia cambiando su estado a RESUELTA.
     * 
     * @param idIncidencia El ID de la incidencia a cerrar.
     * Este método cierra una incidencia en la base de datos.
     * Este método busca la incidencia por su ID.
     * Si la incidencia no existe, lanza una excepción IllegalArgumentException.
     * Cambia el estado de la incidencia a "RESUELTA".
     * Guarda la incidencia actualizada en la base de datos.
     * @throws IllegalArgumentException Si la incidencia no es válida.
     */
    @Override
    public void cerrarIncidencia(Integer idIncidencia) {
        // Busca la incidencia por su ID
        Optional<Incidencia> incidenciaOpt = incidenciasRepository.findById(idIncidencia);
        // Verifica si la incidencia existe
        if (incidenciaOpt.isPresent()) {
            // Obtiene la incidencia
            Incidencia incidencia = incidenciaOpt.get();
            // Cambia el estado de la incidencia a "RESUELTA"
            incidencia.setEstado(EstadoIncidencia.RESUELTA);
            // Guarda la incidencia actualizada en la base de datos
            incidenciasRepository.save(incidencia);
        } else {
            throw new IllegalArgumentException("Incidencia no encontrada con ID: " + idIncidencia);
        }
    }

    /**
     * Busca incidencias que coincidan con un ejemplo dado.
     * @param example Ejemplo de incidencia.
     * @return Una lista de incidencias que coinciden con el Example.
     */
    @Override
    public List<Incidencia> buscarByExample(Example<Incidencia> example) {
        return incidenciasRepository.findAll(example);
    }

    /**
     * Busca todas las incidencias de forma paginada.
     * @param pageable Objeto Pageable que contiene la información de paginación.
     * @return Una página de incidencias.
     * Este método busca incidencias en la base de datos y devuelve una página de
     * resultados.
     * @throws IllegalArgumentException Si la incidencia no es válida.
     * 
     */
    @Override
    public Page<Incidencia> buscarIncidenciasPaginadas(Pageable pageable) {
        return incidenciasRepository.findAll(pageable);
    }

    /**
     * Busca incidencias por estado de forma paginada.
     * 
     * @param estado   El estado de las incidencias a buscar.
     * @param pageable Objeto Pageable que contiene la información de paginación.
     * @return Una página de incidencias con el estado especificado.
     * Este método busca incidencias en la base de datos por su estado y devuelve una
     * página de resultados.
     */
    @Override
    public Page<Incidencia> buscarIncidenciasPorEstadoPaginadas(EstadoIncidencia estado, Pageable pageable) {
        return incidenciasRepository.findByEstado(estado, pageable);
    }

    /**
     * Busca incidencias por un ejemplo y devuelve los resultados paginados.
     * 
     * @param example  El ejemplo de incidencia a buscar.
     * @param pageable Objeto Pageable que contiene la información de paginación.
     * @return Una página de incidencias que coinciden con el ejemplo.
     * Este método busca incidencias en la base de datos por un ejemplo y devuelve una
     * página de resultados.
     */
    @Override
    public Page<Incidencia> buscarIncidenciasByExamplePaginadas(Example<Incidencia> example, Pageable pageable) {
        return incidenciasRepository.findAll(example, pageable);
    }

}
