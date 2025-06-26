package br.com.net.sqlab_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

	@Value("${mail.host}")
	private String host;

	@Value("${mail.port}")
	private int port;

	@Value("${mail.username}")
	private String username;

	@Value("${mail.password}")
	private String password;

	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		// Configurações específicas para Outlook/Hotmail
		mailSender.setHost("smtp.office365.com"); // Host SMTP oficial do Outlook
		mailSender.setPort(587);
		mailSender.setUsername(username);
		mailSender.setPassword(password);

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");

		// Configurações específicas para TLS
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");

		props.put("mail.smtp.connectiontimeout", 30000); // 30 segundos
		props.put("mail.smtp.timeout", 30000); // 30 segundos
		props.put("mail.smtp.writetimeout", 30000); // 30 segundos

		// Configurações adicionais para Outlook
		props.put("mail.smtp.ssl.trust", "smtp.office365.com");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.put("mail.smtp.socketFactory.port", "587");

		// Debug (pode ser removido em produção)
		props.put("mail.debug", "true");

		return mailSender;
	}
}