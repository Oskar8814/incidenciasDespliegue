package ies.ruizgijon.gestorIncidencias;

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


    public static final String ATTRIBUTE_ERRORMESSAGE = "errorMessage";
    public static final String ATTRIBUTE_MESSAGEERROR = "mensajeError";
    public static final String ATTRIBUTE_STATUSCODE = "statusCode";
    public static final String ATTRIBUTE_CURRENTUSER = "currentUser";
    public static final String ATTRIBUTE_CONFIRMACION = "confirmacion";
    public static final String ATTRIBUTE_INCIDENCIA = "incidencia";
    public static final String ATTRIBUTE_ROLES = "roles";
    public static final String ATTRIBUTE_USUARIOS = "usuarios";
    public static final String REDIRECT_LOGIN = "redirect:/";
    public static final String REDIRECT_USUARIOLISTAR = "redirect:/usuario/listar";
    public static final String ATTRIBUTE_INCIDENCIASPENDIENTES = "incidenciasPendientes";
    public static final String ATTRIBUTE_INCIDENCIASPROGRESO = "incidenciasProgreso";
    public static final String ATTRIBUTE_INCIDENCIASRESUELTAS = "incidenciasResueltas";
    public static final String ATTRIBUTE_MESSAGENOPENDIENTE = "mensajeNoPendientes";
    public static final String ATTRIBUTE_MESSAGENOPROGRESO = "mensajeNoProgreso";
    public static final String ATTRIBUTE_MESSAGENORESUELTA = "mensajeNoResueltas";

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_TECNICO = "TECNICO";
    public static final String ROLE_AUXILIAR = "AUXILIAR";

    public static final String VIEW_INCIDENCIASPENDIENTES = "incidenciasPendientes";
    public static final String VIEW_INCIDENCIASPROGRESO = "incidenciasProgreso";
    public static final String VIEW_INCIDENCIASRESUELTAS = "incidenciasResueltas";

    public static final String FIELD_TITULO = "titulo";


}
