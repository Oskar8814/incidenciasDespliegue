package ies.ruizgijon.gestorincidencias.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ies.ruizgijon.gestorincidencias.model.Usuario;
import ies.ruizgijon.gestorincidencias.service.EmailService;
import ies.ruizgijon.gestorincidencias.service.IUsuarioService;

/**
 * Controlador encargado de la autenticación relacionada con la recuperación de contraseñas.
 * 
 * Este controlador gestiona el flujo completo de recuperación de credenciales:
 * - Mostrar el formulario para solicitar el restablecimiento de contraseña.
 * - Procesar la solicitud enviando un correo con un enlace único.
 * - Mostrar el formulario de restablecimiento basado en el token.
 * - Procesar el nuevo valor de la contraseña y aplicarlo.
 * 
 * Utiliza los servicios {@link IUsuarioService} y {@link EmailService} para acceder a datos de usuario
 * y enviar notificaciones por correo electrónico respectivamente.
 * 
 * El token de recuperación se genera de forma aleatoria y tiene un tiempo de validez limitado (ver implementación del servicio).
 * 
 * @author Óscar García
 */
@Controller
public class AuthController {
    /**
     * URL base de la aplicación, utilizada para construir enlaces seguros en los correos enviados al usuario.
     * Se configura en el archivo `application.properties` como `app.url.base`.
     */
    @Value("${app.url.base}")
    private String baseUrl;

    /**
     * Servicio para la gestión de usuarios.
     * Este servicio proporciona métodos para realizar operaciones CRUD sobre usuarios.
     */
    private final IUsuarioService usuarioService;
    
    /**
     * Servicio para el envío de correos electrónicos.
     * Este servicio proporciona métodos para enviar correos electrónicos de recuperación de contraseña.
     */
    private final EmailService emailService;

    /**
     * Constructor de inyección de dependencias para el controlador.
     *
     * @param usuarioService Servicio de lógica de negocio para gestión de usuarios.
     * @param emailService Servicio para envío de correos electrónicos de notificación.
     */
    @Autowired
    public AuthController(IUsuarioService usuarioService, EmailService emailService) {
        this.usuarioService = usuarioService;
        this.emailService = emailService;
    }
    /**
     * Muestra el formulario donde los usuarios pueden solicitar recuperar su contraseña.
     *
     * @return Nombre de la vista que contiene el formulario de recuperación de contraseña.
     */
    @GetMapping("/recuperar-password")
    public String mostrarFormularioRecuperacion() {
        return "recuperar-password";
    }

    /**
     * Procesa la solicitud de recuperación de contraseña enviada por el usuario.
     * 
     * Si el correo electrónico existe en el sistema, se genera un token único y se envía
     * un enlace al usuario para restablecer la contraseña. Por razones de seguridad,
     * no se indica si el correo existe o no en el sistema.
     *
     * @param email Dirección de correo electrónico ingresada por el usuario.
     * @param attrs Atributos de redirección para mostrar mensajes en la vista siguiente.
     * @return Redirección a la página principal con un mensaje de confirmación.
     */
    @PostMapping("/recuperar-password")
    public String procesarRecuperacion(@RequestParam String email, RedirectAttributes attrs) {
        Usuario usuario = usuarioService.buscarUsuarioPorMail(email);
        if (usuario != null) {
            String token = UUID.randomUUID().toString();
            usuarioService.guardarTokenDeRecuperacion(usuario, token);
            emailService.enviarRecuperacion(email, baseUrl.trim() + "/reset-password?token=" + token); // URL local,cambiar por la real
        }
        attrs.addFlashAttribute("confirmacion", "Si el correo existe, se enviaron instrucciones.");
        return "redirect:/";
    }

    /**
     * Muestra el formulario para que el usuario introduzca una nueva contraseña.
     *
     * El acceso a esta vista requiere un token válido que se envió al correo del usuario.
     * Si el token no es válido o ha expirado, se redirige al inicio con un error.
     *
     * @param token Token de recuperación recibido por el usuario.
     * @param model Modelo utilizado para pasar el token a la vista.
     * @return Nombre de la vista con el formulario para restablecer la contraseña.
     */
    @GetMapping("/reset-password")
    public String mostrarResetForm(@RequestParam String token, Model model) {
        if (!usuarioService.validarToken(token))
            return "redirect:/?errorToken";
        model.addAttribute("token", token);
        return "reset-password";
    }

    /**
     * Procesa el restablecimiento de contraseña cuando el usuario envía el formulario.
     *
     * Verifica que el token sea válido y actualiza la contraseña del usuario en caso de éxito.
     * Luego, muestra un mensaje indicando si la operación fue exitosa o fallida.
     *
     * @param token Token de recuperación que autentica la solicitud.
     * @param password Nueva contraseña proporcionada por el usuario.
     * @param attrs Atributos flash para mostrar mensajes de confirmación o error.
     * @return Redirección a la página principal.
     */
    @PostMapping("/reset-password")
    public String procesarReset(@RequestParam String token,
            @RequestParam String password,
            RedirectAttributes attrs) {
        boolean exito = usuarioService.actualizarPasswordConToken(token, password);
        attrs.addFlashAttribute(exito ? "confirmacion" : "error",
                exito ? "Contraseña actualizada correctamente." : "Token inválido o expirado.");
        return "redirect:/";
    }

}
