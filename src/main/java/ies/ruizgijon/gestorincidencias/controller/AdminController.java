package ies.ruizgijon.gestorincidencias.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ies.ruizgijon.gestorincidencias.model.Incidencia;
import ies.ruizgijon.gestorincidencias.model.Usuario;
import ies.ruizgijon.gestorincidencias.service.IIncidenciasService;
import ies.ruizgijon.gestorincidencias.service.IUsuarioService;
import ies.ruizgijon.gestorincidencias.util.GConstants;
import jakarta.servlet.http.HttpServletRequest;


/**
 * Controlador encargado de gestionar las incidencias y usuarios desde la perspectiva del administrador.
 * 
 * Este controlador permite realizar operaciones administrativas sobre el sistema de gestión de incidencias, como:
 * - Eliminar incidencias.
 * - Editar incidencias en distintos estados.
 * - Gestionar la lista de usuarios.
 * 
 * También agrega el usuario autenticado al modelo de manera global para que esté disponible en las vistas.
 * 
 * Utiliza los servicios {@link IIncidenciasService} y {@link IUsuarioService} para interactuar con los datos.
 * 
 * @author Óscar García
 */
@Controller
public class AdminController {
    
    /**
     * Servicio para la gestión de incidencias.
     * Este servicio proporciona métodos para realizar operaciones CRUD sobre incidencias.
     */
    private final IIncidenciasService incidenciasService;
    /**
     * Servicio para la gestión de usuarios.
     * Este servicio proporciona métodos para realizar operaciones CRUD sobre usuarios.
     */
    private final IUsuarioService usuarioService;

    /**
     * Crea una nueva instancia del controlador {@code AdminController}.
     * 
     * @param incidenciasService Servicio para operaciones sobre incidencias (crear, buscar, editar, eliminar).
     * @param usuarioService Servicio para operaciones sobre usuarios del sistema.
     */
    @Autowired
    public AdminController( IIncidenciasService incidenciasService, IUsuarioService usuarioService){
        this.incidenciasService = incidenciasService;
        this.usuarioService = usuarioService;
    }

  /**
     * Elimina una incidencia existente mediante su identificador único.
     * 
     * Este método maneja una solicitud GET para eliminar la incidencia correspondiente al ID proporcionado.
     * Después de completar la eliminación, redirige al usuario a la vista principal de incidencias y agrega un mensaje
     * de confirmación sobre la acción realizada en los atributos flash, para que el mensaje sea visible en la siguiente vista.
     * 
     * @param id El ID único de la incidencia a eliminar.
     * @param attributes Los atributos flash utilizados para enviar el mensaje de confirmación a la vista siguiente.
     * @return Una redirección a la vista de listado de incidencias, donde el mensaje de éxito se mostrará al usuario.
     */
    @GetMapping("/admin/eliminarIncidencia/{id}")
    public String eliminarIncidencia(@PathVariable("id") int id, RedirectAttributes attributes) {
        // Llamar al servicio para eliminar la incidencia por su ID
        incidenciasService.eliminarIncidencia(id);

        // Agregar un mensaje de éxito al redirigir a la página después de eliminar la incidencia
        attributes.addFlashAttribute(GConstants.ATTRIBUTE_CONFIRMACION, "Incidencia "+ id +" eliminada con éxito.");

        return "redirect:/incidencias/index"; // Redirigir a la lista de incidencias después de eliminar
    }


    /**
     * Carga el formulario de edición para una incidencia que se encuentra en estado "Pendiente".
     * 
     * Este método maneja una solicitud GET y recupera la incidencia correspondiente al ID proporcionado. 
     * También obtiene la lista de usuarios disponibles para asignación, que se mostrará en el formulario de edición.
     * Los datos de la incidencia y la lista de usuarios se añaden al modelo para que puedan ser accesibles en la vista.
     * 
     * @param id El ID de la incidencia que se desea editar.
     * @param model El objeto del modelo que se utiliza para enviar los datos necesarios a la vista de edición.
     * @return El nombre de la vista para editar una incidencia pendiente, que se encargará de presentar el formulario.
     */

    @GetMapping("/admin/editarIncidenciaPendiente/{id}")
    public String editarIncidencia(@PathVariable("id") int id, Model model) {
        // Llamar al servicio para obtener la incidencia por su ID
        Incidencia incidencia = incidenciasService.buscarIncidenciaPorId(id);
        
        // Obtener la lista de usuarios para el formulario de edición
        List<Usuario> usuarios = usuarioService.buscarTodos();

        // Agregar la incidencia y la lista de usuarios al modelo para la vista de edición
        model.addAttribute(GConstants.ATTRIBUTE_INCIDENCIA, incidencia);
        model.addAttribute(GConstants.ATTRIBUTE_USUARIOS, usuarios); // Agregar la lista de usuarios al modelo para el formulario
        return "crearIncidenciaForm"; // Devuelve la vista para editar una incidencia existente
    }

    /**
     * Carga el formulario de edición para una incidencia que se encuentra en estado "En progreso".
     * 
     * Este método funciona de manera similar al formulario de incidencias pendientes, pero se adapta al estado "En progreso". 
     * Permite modificar detalles como el usuario asignado, la descripción o el estado de la incidencia.
     * 
     * @param id El ID de la incidencia que se desea editar.
     * @param model El objeto del modelo utilizado para enviar datos a la vista de edición.
     * @return El nombre de la vista del formulario de edición para incidencias en progreso.
     */
    @GetMapping("/admin/editarIncidencia/{id}")
    public String editarIncidenciaProgreso(@PathVariable("id") int id, Model model) {
        // Llamar al servicio para obtener la incidencia por su ID
        Incidencia incidencia = incidenciasService.buscarIncidenciaPorId(id);
        
        // Obtener la lista de usuarios para el formulario de edición
        List<Usuario> usuarios = usuarioService.buscarTodos();

        // Agregar la incidencia y la lista de usuarios al modelo para la vista de edición
        model.addAttribute(GConstants.ATTRIBUTE_INCIDENCIA, incidencia);
        model.addAttribute(GConstants.ATTRIBUTE_USUARIOS, usuarios); // Agregar la lista de usuarios al modelo para el formulario
        return "editarIncidenciaForm"; // Devuelve la vista para editar una incidencia existente
    }

    /**
     * Procesa el envío del formulario de edición de una incidencia.
     * 
     * Este método se ejecuta cuando el formulario es enviado. Recibe el objeto {@code Incidencia}
     * ya enlazado con los datos del formulario y lo guarda mediante el servicio correspondiente.
     * 
     * También agrega un mensaje de éxito y redirige al listado de incidencias.
     * 
     * @param incidencia Objeto de incidencia con los datos modificados.
     * @param attributes Atributos flash para mensajes temporales.
     * @return Redirección al listado de incidencias.
     */
    @PostMapping("/admin/incidencia/edit")
    public String editarIncidencia(Incidencia incidencia, RedirectAttributes attributes) {
        // Llamar al servicio para guardar la incidencia editada
        incidenciasService.guardarIncidencia(incidencia);

        // Agregar un mensaje de éxito al redirigir a la página después de editar la incidencia
        attributes.addFlashAttribute(GConstants.ATTRIBUTE_CONFIRMACION, "Incidencia editada con éxito.");

        return "redirect:/incidencias/index"; // Redirigir a la lista de incidencias después de editar
    }
    
    /**
     * Agrega el usuario actualmente autenticado al modelo antes de ejecutar cualquier método del controlador.
     * 
     * Esto permite que el usuario actual esté disponible globalmente en todas las vistas renderizadas
     * por este controlador. Es útil, por ejemplo, para mostrar información del usuario en la interfaz
     * (como el nombre o el rol).
     * 
     * @param model Objeto de modelo compartido con todas las vistas del controlador.
     * @param request Objeto de solicitud HTTP para obtener información adicional, como la URL actual.
     */
    @ModelAttribute()
    public void setGenericos(Model model, HttpServletRequest request) {
        Usuario usuario = usuarioService.getCurrentUser(); //Obtener el usuario actualmente logeado

        model.addAttribute(GConstants.ATTRIBUTE_CURRENTUSER, usuario); // Agregar el usuario actual al modelo para la vista
        model.addAttribute("currentUrl", request.getRequestURI());
    }

}
