package ies.ruizgijon.gestorincidencias.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import ies.ruizgijon.gestorincidencias.util.GConstants;

/**
 * Configuración de seguridad para la aplicación.
 * Esta clase configura la seguridad de la aplicación utilizando Spring Security.
 * 
 * @author [Óscar García]
 */

@Configuration
/**
 * Habilita la configuración de seguridad web en la aplicación.
 */
@EnableWebSecurity
public class SecurityConfig {
	/**
	 * Servicio personalizado para la gestión de detalles de usuario.
	 * Este servicio se utiliza para cargar los detalles del usuario durante la autenticación.
	 */
	private final CustomUserDetailsService customUserDetailsService;

	/**
	 * Constructor de la clase SecurityConfig.
	 * 
	 * @param customUserDetailsService Servicio personalizado para la gestión de detalles de usuario.
	 */
	public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
		this.customUserDetailsService = customUserDetailsService;
	}
	/**
	 * Configuración del filtro de seguridad.
	 * Este método configura las reglas de autorización y autenticación para la aplicación.
	 * 
	 * @param http La configuración de seguridad HTTP.
	 * @return El objeto SecurityFilterChain configurado.
	 * @throws Exception Si ocurre un error durante la configuración.
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		/**
		 * Configura las reglas de autorización para las solicitudes HTTP.
		 * 
		 * @param auth La configuración de autorización HTTP.
		 * @return El objeto SecurityFilterChain configurado.
		 * @throws Exception Si ocurre un error durante la configuración.
		 * Esta configuración define qué rutas son accesibles para los diferentes roles de usuario.
		 * Recuerda que los roles en Spring Security se definen con el prefijo "ROLE_".
		 * Recursos estáticos (CSS, JS, imágenes, fuentes) son accesibles para todos.
		 * Endpoints públicos (login, recuperar contraseña) son accesibles para todos.
		 * Endpoints privados requieren autenticación y roles específicos.
		 * 1) Recursos estáticos
		 * 2) Endpoints públicos
		 * 3) Endpoints privados
		 * 4) Cualquier otra ruta que no hayas mapeado => denegada o autenticada
		 * 
		 */

		http.authorizeHttpRequests(auth -> auth
				// 1) Recursos estáticos
				.requestMatchers("/css/**", "/js/**", "/img/**","/font/**").permitAll()

				// 2) Endpoints públicos
				.requestMatchers("/", "/login","/recuperar-password","/reset-password").permitAll()

				// 3) Endpoints privados
				.requestMatchers("/usuario/**").hasRole(GConstants.ROLE_ADMIN)
				.requestMatchers("/admin/**").hasRole(GConstants.ROLE_ADMIN)
				.requestMatchers("/incidencias/crearIncidencia/**").hasAnyRole(GConstants.ROLE_TECNICO, GConstants.ROLE_ADMIN,GConstants.ROLE_AUXILIAR) //Al ser mas especifica que la posterior Auxiliar solo se le permite crear incidecias
				.requestMatchers("/incidencias/**").hasAnyRole(GConstants.ROLE_TECNICO, GConstants.ROLE_ADMIN)
				.requestMatchers("/modificarContrasena/**").hasAnyRole(GConstants.ROLE_ADMIN,GConstants.ROLE_TECNICO,GConstants.ROLE_AUXILIAR)
				.requestMatchers("/error").permitAll()
            	.anyRequest().authenticated()

                // Cualquier otra ruta que no hayas mapeado => denegada o autenticada
                //    - .denyAll() para bloquear lo que no esté contemplado
                //    - .authenticated() para requerir cualquier usuario
                // .anyRequest().permitAll()
            )

				// Configuración de login
				.formLogin(form -> form.loginPage("/") // Indica que la ruta GET "/" muestra tu plantilla login
						.usernameParameter("mail") // name del input en tu formulario
						.passwordParameter("password") // name del input password
						.defaultSuccessUrl("/incidencias/index", true) // Redirección al hacer login exitoso
						.permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/") // A dónde se redirige tras hacer logout
						.permitAll());

		return http.build();
	}

	/**
	 * Configuración del codificador de contraseñas.
	 * Este método configura el codificador de contraseñas utilizado para almacenar
	 * y verificar las contraseñas de los usuarios.
	 * 
	 * @return El objeto BCryptPasswordEncoder configurado.
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// El AuthenticationManager se necesita si vas a inyectarlo en servicios para hacer login manual
	/**
	 * Configuración del AuthenticationManager.
	 * Este método configura el AuthenticationManager utilizado para la autenticación
	 * de los usuarios en la aplicación.
	 * 
	 * @param http La configuración de seguridad HTTP.
	 * @return El objeto AuthenticationManager configurado.
	 * @throws Exception Si ocurre un error durante la configuración.
	 */
	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
	    AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
	    authBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());

	    return authBuilder.build();
	}


}