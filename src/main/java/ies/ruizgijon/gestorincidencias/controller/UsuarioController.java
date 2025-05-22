package ies.ruizgijon.gestorincidencias.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ies.ruizgijon.gestorincidencias.model.Rol;
import ies.ruizgijon.gestorincidencias.model.Usuario;
import ies.ruizgijon.gestorincidencias.service.IRolService;
import ies.ruizgijon.gestorincidencias.service.IUsuarioService;
import ies.ruizgijon.gestorincidencias.util.GConstants;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Controlador para la gestión de usuarios.
 * Maneja las operaciones de creación, edición, eliminación y búsqueda de usuarios.
 * 
 * Utiliza los servicios de usuarios y roles para realizar estas operaciones.
 * 
 * @author [Óscar García]
 */

// Este controlador utiliza el servicio de usuarios y el servicio de roles para realizar las operaciones necesarias.
@Controller
public class UsuarioController {

    /**
     * Servicio para la gestión de usuarios.
     * Este servicio proporciona métodos para realizar operaciones CRUD sobre usuarios.
     */
    private final IUsuarioService usuarioService; 
    /**
     * Servicio para la gestión de roles.
     * Este servicio proporciona métodos para realizar operaciones CRUD sobre roles.
     */
    private final IRolService rolService;

    /**
     * Constructor del controlador UsuarioController.
     * 
     * @param usuarioService Servicio para la gestión de usuarios.
     * @param rolService     Servicio para la gestión de roles.
     */
    @Autowired
    public UsuarioController(IUsuarioService usuarioService, IRolService rolService) {
        this.usuarioService = usuarioService; // Inicializa el servicio de usuarios
        this.rolService = rolService; // Inicializa el servicio de roles
    }

    /**
     * Muestra la lista de todos los usuarios registrados.
     * 
     * @param model Modelo para pasar datos a la vista.
     * @return Nombre de la vista que lista los usuarios.
     */
    @GetMapping("/usuario/listar")
    public String listarUsuarios(Model model) {
        // Llamar al servicio para obtener la lista de usuarios y devolverla a la vista
        List<Usuario> usuarios = usuarioService.buscarTodos();
        
        // Verificar si la lista de usuarios está vacía
        if (usuarios.isEmpty()) {
            // Si la lista está vacía, puedes agregar un mensaje al modelo para mostrarlo en la vista
            model.addAttribute("mensajeNoUsuarios", "No hay usuarios registrados.");
        }

        // Agregar la lista de usuarios al modelo para que esté disponible en la vista
        model.addAttribute(GConstants.ATTRIBUTE_USUARIOS, usuarios);
        return "usuarios"; // Devuelve la vista de listar usuarios
    }

    /**
     * Busca usuarios por nombre utilizando coincidencia parcial (like).
     * 
     * @param busqueda Objeto Usuario con los criterios de búsqueda.
     * @param model Modelo para pasar datos a la vista.
     * @return Nombre de la vista con los resultados de la búsqueda.
     */
    @PostMapping("/usuario/buscar")
    public String buscarUsuario(@ModelAttribute ("search") Usuario busqueda, Model model) {
        // Personalizar el tipo de busqueda
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("nombre", ExampleMatcher.GenericPropertyMatchers.contains());

        // Crear un ejemplo de búsqueda utilizando el objeto Usuario
        Example<Usuario> example = Example.of(busqueda, matcher);

        List<Usuario> usuarios = usuarioService.buscarByExample(example); // Llamar al servicio para buscar usuarios por ejemplo

        // Verificar si la lista de usuarios está vacía
        if (usuarios.isEmpty()) {
            // Si la lista está vacía, puedes agregar un mensaje al modelo para mostrarlo en la vista
            model.addAttribute("mensajeNoUsuarios", "No se encontraron usuarios con ese nombre.");
        }

        // Agregar la lista de usuarios al modelo para que esté disponible en la vista
        model.addAttribute(GConstants.ATTRIBUTE_USUARIOS, usuarios);
        return "usuarios"; // Devuelve la vista de listar usuarios
    }

    /**
     * Elimina un usuario por su ID. 
     * 
     * @param idUsuario ID del usuario a eliminar.
     * @param model Modelo para pasar mensajes a la vista.
     * @param attributes Atributos de redirección para mostrar mensajes.
     * @return Redirección a la vista de lista de usuarios.
     */
    @GetMapping("/usuario/delete/{id}")
    public String eliminarUsuario(@PathVariable("id") Integer idUsuario, Model model, RedirectAttributes attributes) {
        // Verificar si el ID del usuario existe antes de intentar eliminarlo
        Usuario usuario = usuarioService.buscarUsuarioPorId(idUsuario);
        if (usuario == null) {
            // Si el usuario no existe, redirigir a la lista de usuarios y mostrar un mensaje de error
            model.addAttribute(GConstants.ATTRIBUTE_MESSAGEERROR, "Usuario no encontrado con ID: " + idUsuario);
            return GConstants.REDIRECT_USUARIOLISTAR; // Redirigir a la lista de usuarios si no se encuentra el usuario
        }
        // Llamar al servicio para eliminar el usuario por su ID
        usuarioService.eliminarUsuario(idUsuario);

        // Agregar un mensaje de éxito al redirigir a la página después de eliminar el usuario
        attributes.addFlashAttribute(GConstants.ATTRIBUTE_CONFIRMACION, "Usuario eliminado con éxito.");

        return GConstants.REDIRECT_USUARIOLISTAR; // Redirigir a la lista de usuarios después de eliminar
    }

    /**
     * Método para mostrar el formulario de creación de un nuevo usuario.
     * 
     * @param model El modelo para pasar datos a la vista.
     * @return La vista del formulario de creación de un nuevo usuario.
     * Este método utiliza el servicio de roles para obtener la lista de roles
     * y mostrarlas en el formulario de creación de usuario.
     */
    @GetMapping("/usuario/crear")
    public String crearUsuario(Model model) {
        // Crear un nuevo objeto Usuario y agregarlo al modelo para que esté disponible en la vista
        Usuario nuevoUsuario = new Usuario();

        //Listado de roles para el formulario de creación de usuario
        List<Rol> roles = rolService.buscarTodos(); // Obtener la lista de roles desde el servicio

        model.addAttribute(GConstants.ATTRIBUTE_ROLES, roles); // Agregar la lista de roles al modelo para que esté disponible en la vista
        model.addAttribute("usuario", nuevoUsuario);
        return "crearUsuarioForm"; // Devuelve la vista para crear un nuevo usuario
    }

    /**
     * Guarda un nuevo usuario o modifica uno existente según el correo electrónico.
     * @param usuario    El objeto Usuario a guardar o modificar.
     * @param model      El modelo para pasar datos a la vista.
     * @param attributes Atributos para redirección.
     * @return Redirección a la lista de usuarios después de guardar o modificar el usuario.
     * Este método utiliza el servicio de usuarios para guardar o modificar un usuario
     * y redirige a la lista de usuarios después de la operación.
     * Si el usuario no existe, se guarda como un nuevo usuario.
     * Si el usuario existe, se modifica y se muestra un mensaje de confirmación.
     */
    @PostMapping("/usuario/save")
    public String guardarUsuario(Usuario usuario, Model model, RedirectAttributes attributes) {
        // Llamar al servicio para guardar el nuevo usuario
        // Verificar si el usuario se encuentra en la base de datos
        Usuario usuarioExistente = usuarioService.buscarUsuarioPorMail(usuario.getMail());
        if (usuarioExistente != null) {
            usuarioService.modificarUsuario(usuario); // Modificar el usuario existente
            // Agregar un mensaje de éxito al redirigir a la página después de modificar el usuario
            attributes.addFlashAttribute(GConstants.ATTRIBUTE_CONFIRMACION, "Usuario modificado con éxito.");
        }else {
            // Si el usuario no existe, se guarda como un nuevo usuario
            usuarioService.guardarUsuario(usuario);
            attributes.addFlashAttribute(GConstants.ATTRIBUTE_CONFIRMACION, "Usuario guardado con éxito.");
        }
        return GConstants.REDIRECT_USUARIOLISTAR; // Redirigir a la lista de usuarios después de guardar
    }

    /**
     * Método para mostrar el formulario de edición de un usuario existente.
     * 
     * @param idUsuario El ID del usuario a editar.
     * @param model     El modelo para pasar datos a la vista.
     * @return La vista del formulario de edición de un usuario existente.
     * Este método utiliza el servicio de usuarios para buscar el usuario por su ID
     * y la lista de roles para mostrarlas en el formulario de edición de usuario.
     * Si el usuario no existe, se muestra un mensaje de error en la vista.
     */
    @GetMapping("/usuario/edit/{id}")
    public String editarUsuario(@PathVariable("id") Integer idUsuario, Model model) {
        // Buscar el usuario por su ID
        Usuario usuario = usuarioService.buscarUsuarioPorId(idUsuario);
        if (usuario == null) {
            // Si el usuario no existe, redirigir a la lista de usuarios y mostrar un mensaje de error
            model.addAttribute(GConstants.ATTRIBUTE_MESSAGEERROR, "Usuario no encontrado con ID: " + idUsuario);
            return GConstants.REDIRECT_USUARIOLISTAR; // Redirigir a la lista de usuarios si no se encuentra el usuario
        }

        //Listado de roles para el formulario de edición de usuario
        List<Rol> roles = rolService.buscarTodos(); // Obtener la lista de roles desde el servicio

        model.addAttribute(GConstants.ATTRIBUTE_ROLES, roles); // Agregar la lista de roles al modelo para que esté disponible en la vista
        model.addAttribute("usuario", usuario); // Agregar el usuario al modelo para que esté disponible en la vista
        return "editUsuarioForm"; // Devuelve la vista para editar un usuario existente
    }

    /**
     * Agrega atributos comunes al modelo para todas las vistas manejadas por este controlador.
     * Incluye el usuario actual logueado y el objeto de búsqueda.
     * 
     * @param model Modelo para pasar datos a las vistas.
     * @param request Objeto de solicitud HTTP para obtener información adicional.
     */

    @ModelAttribute()
    public void setGenericos(Model model, HttpServletRequest request) {
        Usuario usuario = usuarioService.getCurrentUser(); //Obtener el usuario actualmente logeado
        Usuario usuarioSearch = new Usuario();

        model.addAttribute("search", usuarioSearch); // Agregar el objeto de búsqueda al modelo para la vista
        model.addAttribute(GConstants.ATTRIBUTE_CURRENTUSER, usuario); // Agregar el usuario actual al modelo para la vista
        model.addAttribute("currentUrl", request.getRequestURI()); // Agregar la URL actual al modelo
    }

}
