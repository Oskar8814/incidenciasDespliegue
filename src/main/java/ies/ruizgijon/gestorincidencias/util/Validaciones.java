package ies.ruizgijon.gestorincidencias.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import ies.ruizgijon.gestorincidencias.model.Incidencia;
import ies.ruizgijon.gestorincidencias.model.Usuario;

/**
 * Clase que contiene métodos de validación para objetos de tipo Usuario e
 * Incidencia.
 * 
 * @author [Óscar García]
 */
public class Validaciones {

	/**
	 * Constructor privado para evitar la instanciación de la clase utilitaria.
	 */
	private Validaciones() {
		// Constructor privado para evitar la instanciación
	}

	/**
	 * Patrón de expresión regular para validar la contraseña del usuario.
	 * La contraseña debe tener entre 12 y 16 caracteres, al menos una letra
	 * mayúscula, una letra minúscula, un número y un carácter especial.
	 */
	private static final Pattern PATRON_CONTRASENA = Pattern.compile(
			"^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{12,16}$");

	/**
	 * Valida si un objeto Usuario cumple con los requisitos de la base de datos y
	 * los criterios de seguridad.
	 * 
	 * @param usuario El objeto Usuario a validar.
	 * @return Una lista de errores de validación. Si la lista está vacía, el usuario es valido
	 */
	public static List<String> obtenerErroresValidacionUsuario(Usuario usuario) {
		List<String> errores = new ArrayList<>();

		if (usuario == null) {
			errores.add("El objeto Usuario es nulo.");
			return errores;
		}

		if (!esTextoValido(usuario.getNombre(), 50)) {
			errores.add("El nombre no es válido o excede los 50 caracteres.");
		}

		if (!esTextoValido(usuario.getApellido1(), 50)) {
			errores.add("El primer apellido no es válido o excede los 50 caracteres.");
		}

		if (!esTextoValido(usuario.getMail(), 100)) {
			errores.add("El correo electrónico no es válido o excede los 100 caracteres.");
		}

		if (usuario.getId() == null || (usuario.getPassword() != null && !usuario.getPassword().startsWith("$2a$"))) {
			if (!esPasswordValida(usuario.getPassword())) {
				errores.add("La contraseña no cumple con los requisitos de seguridad. Debe tener entre 12 y 16 caracteres, al menos una letra mayúscula, una letra minúscula, un número y un carácter especial.");
			}
		}

		return errores;
	}

	/**
	 * Valida si un objeto Incidencia cumple con los requisitos de la base de datos y
	 * los criterios de seguridad.
	 * 
	 * @param incidencia El objeto Incidencia a validar.
	 * @return Una lista de errores de validación. Si la lista está vacía, la incidencia es valida
	 * 
	 */

	public static List<String> obtenerErroresValidacionIncidencia(Incidencia incidencia) {
		List<String> errores = new ArrayList<>();

		if (incidencia == null) {
			errores.add("La incidencia no puede ser nula.");
			return errores;
		}

		if (!esTextoValido(incidencia.getTitulo(), 100)) {
			errores.add("El título de la incidencia no es válido o excede los 100 caracteres.");
		}
		
		if (incidencia.getFecha() == null) {
			errores.add("La fecha de creación no puede ser nula.");
		}

		if (!esTextoValido(incidencia.getDescripcion(), 4500)) {
			errores.add("La descripción de la incidencia no es válida o excede los 4500 caracteres.");
		}


		if (!esTextoValido(incidencia.getAula(), 20)) {
			errores.add("El aula no es válida o excede los 20 caracteres.");
		}

		if (!esTextoValido(incidencia.getImagen(), 255)) {
			errores.add("La imagen no es válida o excede los 255 caracteres.");
		}

		if (incidencia.getEstado() == null) {
			errores.add("El estado de la incidencia no puede ser nulo.");
		}

		if (incidencia.getCreador() == null) {
			errores.add("El creador de la incidencia no puede ser nulo.");
		}

		return errores;
	}

	/**
	 * Valida si un texto es válido según los criterios establecidos.
	 * 
	 * @param valor     El texto a validar.
	 * @param maxLength La longitud máxima permitida.
	 * @return true si el texto es válido, false en caso contrario.
	 */

	private static boolean esTextoValido(String valor, int maxLength) {
		return valor != null && !valor.trim().isEmpty() && valor.length() <= maxLength;
	}

	/**
	 * Valida si una contraseña es válida según los criterios establecidos.
	 * 
	 * @param pass La contraseña a validar.
	 * @return true si la contraseña es válida, false en caso contrario.
	 */
	public static boolean esPasswordValida(String pass) {
		return pass != null && PATRON_CONTRASENA.matcher(pass).matches();
	}

}
