package ies.ruizgijon.gestorincidencias.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * Servicio para el envío de correos electrónicos.
 * Este servicio se encarga de enviar correos electrónicos de recuperación de contraseña
 * a los usuarios que lo solicitan.
 * 
 * @author [Óscar García]
 */
@Service
public class EmailService {

    /**
     * Enviador de correos electrónicos.
     * Este objeto se utiliza para enviar correos electrónicos utilizando JavaMailSender.
     */
    private final JavaMailSender mailSender;

    /**
     * Constructor del servicio EmailService.
     * 
     * @param mailSender Enviador de correos electrónicos.
     * Este constructor utiliza la inyección de dependencias para inicializar el objeto
     * JavaMailSender, que se utiliza para enviar correos electrónicos.
     */
    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender; // Inicializa el JavaMailSender
    }
    /**
     * Envía un correo electrónico de recuperación de contraseña al usuario.
     *
     * @param email  Correo electrónico del destinatario.
     * @param enlace Enlace para restablecer la contraseña.
     *
     * Este método crea un mensaje MIME en formato HTML utilizando la clase
     * MimeMessageHelper y lo envía al correo electrónico proporcionado.
     * El mensaje incluye un botón con el enlace para restablecer la contraseña
     * y una advertencia de que el enlace es válido por 1 hora.
     *
     * Si ocurre un error al crear o enviar el mensaje, se imprime la traza del error
     * en la consola, pero no se lanza ninguna excepción.
     */
    public void enviarRecuperacion(String email, String enlace) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Recuperación de contraseña");

            String contenidoHtml = "<html>" +
                    "<body style=\"font-family: Arial, sans-serif;\">" +
                    "<h2 style=\"color: #2E86C1;\">Recuperación de contraseña</h2>" +
                    "<p>Haz clic en el siguiente enlace para restablecer tu contraseña:</p>" +
                    "<p><a href=\"" + enlace
                    + "\" style=\"display: inline-block; padding: 10px 20px; background-color: #2E86C1; color: white; text-decoration: none; border-radius: 5px;\">Restablecer contraseña</a></p>"
                    +
                    "<p><strong>Nota:</strong> Este enlace es válido por <strong>1 hora</strong>. Después de ese tiempo, tendrás que solicitar uno nuevo.</p>"
                    +
                    "<p>Si tú no solicitaste este cambio, puedes ignorar este mensaje.</p>" +
                    "</body>" +
                    "</html>";

            helper.setText(contenidoHtml, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
