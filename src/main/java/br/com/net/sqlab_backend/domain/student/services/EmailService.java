package br.com.net.sqlab_backend.domain.student.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
	@Value("${app.email.from}")
    private String fromEmail;
    
    @Value("${app.email.subject.reset-password}")
    private String resetSubject;
    
    private final JavaMailSender mailSender;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void sendTempPasswordEmail(String toEmail, String tempPassword) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(resetSubject);
        message.setText(
            "Olá,\n\n" +
            "Você solicitou a redefinição de senha. Sua senha temporária é:\n\n" +
            tempPassword + "\n\n" +
            "Por favor, altere esta senha após o login.\n\n" +
            "Atenciosamente,\n" +
            "Equipe SQLab"
        );
        
        try {
            mailSender.send(message);
            logger.info("Email enviado com sucesso para: {}", toEmail);
        } catch (MailAuthenticationException e) {
            logger.error("Falha na autenticação com o servidor de email", e);
            throw new Exception("Falha na autenticação com o servidor de email. Verifique as credenciais.", e);
        } catch (MailSendException e) {
            logger.error("Falha ao enviar email para: {}", toEmail, e);
            throw new Exception("Falha ao enviar email. Verifique a conexão com o servidor SMTP.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao enviar email para: {}", toEmail, e);
            throw new Exception("Erro inesperado ao enviar email.", e);
        }
    }
}