package ies.ruizgijon.gestorincidencias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Gestor de Incidencias.
 * Esta clase contiene el método main que inicia la aplicación Spring Boot.
 * 
 * @author [Óscar García]
 */
@SpringBootApplication
public class GestorIncidenciasApplication {
	/**
	 * Método principal que inicia la aplicación.
	 * 
	 * @param args Argumentos de línea de comandos.
	 * Este método utiliza SpringApplication.run() para iniciar la aplicación
	 * y cargar el contexto de Spring.
	 */
	public static void main(String[] args) {
		SpringApplication.run(GestorIncidenciasApplication.class, args);
	}

}
