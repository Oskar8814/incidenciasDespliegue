package ies.ruizgijon.gestorincidencias;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Esta clase se utiliza para inicializar la aplicación Spring Boot cuando se despliega en un servidor de aplicaciones.
 * 
 * @author [Óscar García]
 */
public class ServletInitializer extends SpringBootServletInitializer {
	/**
	 * Configura la aplicación Spring Boot.
	 * 
	 * @param application La aplicación Spring Boot.
	 * @return La aplicación configurada.
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(GestorIncidenciasApplication.class);
	}

}
