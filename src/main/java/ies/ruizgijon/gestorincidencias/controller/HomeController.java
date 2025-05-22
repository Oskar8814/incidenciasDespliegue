package ies.ruizgijon.gestorincidencias.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ies.ruizgijon.gestorincidencias.model.Usuario;
import ies.ruizgijon.gestorincidencias.service.IUsuarioService;
import ies.ruizgijon.gestorincidencias.util.GConstants;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Controlador encargado de manejar la vista de inicio de sesión y el cambio de contraseña
 * de los usuarios autenticados.
 * 
 * Proporciona funcionalidades para:
 * <ul>
 *   <li>Mostrar la página de inicio de sesión</li>
 *   <li>Acceder al formulario para cambiar la contraseña</li>
 *   <li>Procesar el cambio de contraseña</li>
 * </ul>
 * 
 * Utiliza {@link IUsuarioService} para acceder y modificar datos del usuario.
 * 
 * @author Óscar García
 */
@Controller
public class HomeController {

    /**
     * Servicio encargado de operaciones relacionadas con los usuarios.
     */
	private final IUsuarioService usuarioService;

	/**
	 * Constructor que inyecta el servicio de usuario.
	 *
	 * @param usuarioService Servicio encargado de operaciones sobre usuarios.
	 */
	@Autowired
	public HomeController(IUsuarioService usuarioService) {
		this.usuarioService = usuarioService; // Inicializa el servicio de usuarios
	}

	/**
	 * Muestra la vista de inicio de sesión de la aplicación.
	 * 
	 * @return Nombre de la plantilla de login (login.html).
	 */
    @GetMapping("/")
	public String showLogin() {
		return "login"; // Retorna la vista de inicio de sesión.
	}


    /**
     * Muestra el formulario para modificar la contraseña del usuario autenticado.
     * 
     * Si no hay un usuario en sesión, se redirige a la página de login.
     *
     * @param model Objeto para pasar atributos a la vista.
     * @return Nombre de la vista de cambio de contraseña, o redirección al login si no hay sesión activa.
     */
    @GetMapping("/modificarContrasena")
    public String modificarContrasena(Model model) {
        // Obtener el usuario actualmente logeado
        Usuario usuarioLogeado = usuarioService.getCurrentUser(); // Obtener el usuario actualmente logeado
		if (usuarioLogeado == null) {
			// Si no hay un usuario logeado, redirigir a la página de inicio de sesión
			return GConstants.REDIRECT_LOGIN;
		}
		String mail = usuarioLogeado.getMail(); // Obtener el correo electrónico del usuario logeado
        model.addAttribute("mailLogeado", mail);
        return "modificarContrasena"; // Devuelve la vista para modificar la contraseña
    }

    /**
     * Procesa la solicitud para cambiar la contraseña del usuario autenticado.
     *
     * Valida que el correo electrónico recibido coincida con el del usuario autenticado. 
     * En caso contrario, redirige al login. Si es correcto, actualiza la contraseña 
     * mediante el servicio correspondiente y muestra un mensaje de éxito.
     *
     * @param mail        Correo electrónico del usuario autenticado.
     * @param contrasena  Nueva contraseña a establecer.
     * @param attributes  Atributos flash para mostrar mensajes tras la redirección.
     * @return Redirección a la vista de login tras completar el cambio o en caso de error.
     */
	@PostMapping("/cambiarContrasena/save")
	public String cambiarContrasena (@RequestParam("mailLogeado") String mail, @RequestParam("password") String contrasena, RedirectAttributes attributes) {
		// Obtener el usuario actualmente logeado
		Usuario usuarioLogeado = usuarioService.getCurrentUser(); // Obtener el usuario actualmente logeado
		if (usuarioLogeado == null) {
			// Si no hay un usuario logeado, redirigir a la página de inicio de sesión
			return GConstants.REDIRECT_LOGIN;
		}
		// Verificar si el correo electrónico coincide con el del usuario logeado
		if (!usuarioLogeado.getMail().equals(mail)) {
			// Si no coincide, redirigir a la página de inicio de sesión
			return GConstants.REDIRECT_LOGIN;
		}
		// Llamar al servicio para cambiar la contraseña del usuario
		usuarioService.cambiarContrasena(usuarioLogeado.getId(), contrasena); // Cambiar la contraseña del usuario logeado
		
		// Agregar un mensaje de éxito al redirigir a la página después de cambiar la contraseña
		attributes.addFlashAttribute("confirmacion", "Contraseña modificada con éxito.");
		return GConstants.REDIRECT_LOGIN; 
	}

    /**
     * Agrega el usuario autenticado al modelo antes de procesar cualquier petición del controlador.
     * 
     * Esto permite que la información del usuario esté disponible en todas las vistas renderizadas.
     *
     * @param model Modelo compartido con la vista.
	 * @param request Objeto de solicitud HTTP para obtener información adicional, como la URL actual.
     */
	@ModelAttribute()
    public void setGenericos(Model model, HttpServletRequest request) {
        Usuario usuario = usuarioService.getCurrentUser(); //Obtener el usuario actualmente logeado

        model.addAttribute(GConstants.ATTRIBUTE_CURRENTUSER, usuario); // Agregar el usuario actual al modelo para la vista
		model.addAttribute("currentUrl", request.getRequestURI()); // Agregar la URL actual al modelo
    }
}
