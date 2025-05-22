package ies.ruizgijon.gestorincidencias.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * Configuración de la localización y el manejo de mensajes en la aplicación.
 * Esta clase configura el soporte para la internacionalización (i18n) en la aplicación.
 * Se encarga de definir el idioma predeterminado, la fuente de mensajes y el interceptor
 * para cambiar el idioma a través de parámetros en la URL.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configura la fuente de mensajes para la internacionalización.
     * Carga los mensajes desde el archivo "messages.properties" en el classpath.
     *
     * @return la fuente de mensajes configurada
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * Configura el resolutor de locales para manejar la localización de la aplicación.
     * Establece el idioma predeterminado como español (es).
     *
     * @return el resolutor de locales configurado
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(new Locale("es")); // Idioma predeterminado: español
        return slr;
    }

    /**
     * Configura el interceptor para cambiar el idioma de la aplicación.
     * Permite cambiar el idioma a través de un parámetro en la URL (?lang=es o ?lang=en).
     *
     * @return el interceptor de cambio de localización configurado
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang"); // Se cambiará con ?lang=es o ?lang=en
        return lci;
    }

    /**
     * Registra el interceptor de cambio de localización en la configuración de Spring MVC.
     * Esto permite que el interceptor maneje las solicitudes y cambie el idioma según el parámetro en la URL.
     *
     * @param registry el registro de interceptores
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
