package ies.ruizgijon.gestorincidencias.util;

/**
* Clase que define constantes globales utilizadas en la aplicación.
* Estas constantes incluyen rutas de acceso a los controladores y nombres
* de las vistas de la aplicación, así como valores específicos como el número
* de preguntas en los cuestionarios.
*/
public class GConstants {

	/**
	 * Constructor privado para evitar la instanciación de la clase utilitaria.
	 */
	private GConstants() {
		//Constructor privado para evitar la instanciación
	}

    /**
     * Constantes de configuración de la aplicación.
     */
    /** Atributo para almacenar mensajes de error */
    public static final String ATTRIBUTE_ERRORMESSAGE = "errorMessage";
    /** Atributo para almacenar mensajes de error en español */
    public static final String ATTRIBUTE_MESSAGEERROR = "mensajeError";
    /** Atributo para almacenar el código de estado HTTP. */
    public static final String ATTRIBUTE_STATUSCODE = "statusCode";
    /** Atributo que representa el usuario actualmente autenticado. */
    public static final String ATTRIBUTE_CURRENTUSER = "currentUser";
    /** Atributo para indicar confirmaciones exitosas. */
    public static final String ATTRIBUTE_CONFIRMACION = "confirmacion";
    /** Atributo que representa una incidencia en el modelo. */
    public static final String ATTRIBUTE_INCIDENCIA = "incidencia";
    /** Atributo que contiene los roles disponibles en el sistema. */
    public static final String ATTRIBUTE_ROLES = "roles";
    /** Atributo que representa una lista de usuarios. */
    public static final String ATTRIBUTE_USUARIOS = "usuarios";
    /** Redirección a la página de inicio de sesión. */
    public static final String REDIRECT_LOGIN = "redirect:/";
    /** Redirección a la lista de usuarios. */
    public static final String REDIRECT_USUARIOLISTAR = "redirect:/usuario/listar";
    /** Atributo con incidencias en estado pendiente. */
    public static final String ATTRIBUTE_INCIDENCIASPENDIENTES = "incidenciasPendientes";
    /** Atributo con incidencias en progreso. */
    public static final String ATTRIBUTE_INCIDENCIASPROGRESO = "incidenciasProgreso";
    /** Atributo con incidencias resueltas. */
    public static final String ATTRIBUTE_INCIDENCIASRESUELTAS = "incidenciasResueltas";
    /** Atributo para mensaje cuando no hay incidencias pendientes. */
    public static final String ATTRIBUTE_MESSAGENOPENDIENTE = "mensajeNoPendientes";
    /** Atributo para mensaje cuando no hay incidencias en progreso. */
    public static final String ATTRIBUTE_MESSAGENOPROGRESO = "mensajeNoProgreso";
    /** Atributo para mensaje cuando no hay incidencias resueltas. */
    public static final String ATTRIBUTE_MESSAGENORESUELTA = "mensajeNoResueltas";

    /**
     * Constantes de configuración de roles de usuario.
     */
    /** Rol de usuario administrador. */
    public static final String ROLE_ADMIN = "ADMIN";
    /** Rol de usuario técnico. */
    public static final String ROLE_TECNICO = "TECNICO";
    /** Rol de usuario auxiliar. */
    public static final String ROLE_AUXILIAR = "AUXILIAR";

    /**
     * Constantes de configuración de vistas.
     */
     /** Nombre de la vista para incidencias pendientes. */
    public static final String VIEW_INCIDENCIASPENDIENTES = "incidenciasPendientes";
    /** Nombre de la vista para incidencias en progreso. */
    public static final String VIEW_INCIDENCIASPROGRESO = "incidenciasProgreso";
    /** Nombre de la vista para incidencias resueltas. */
    public static final String VIEW_INCIDENCIASRESUELTAS = "incidenciasResueltas";
    /** Nombre del campo 'título' utilizado en formularios. */
    public static final String FIELD_TITULO = "titulo";

}
