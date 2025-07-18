package br.com.net.sqlab_backend.authentication.service;

import br.com.net.sqlab_backend.authentication.dto.AuthRequest;
import br.com.net.sqlab_backend.authentication.dto.AuthResponse;
import br.com.net.sqlab_backend.authentication.repository.UserRepository;

import br.com.net.sqlab_backend.domain.shared.models.UserEntity;

import br.com.net.sqlab_backend.domain.professor.models.Professor;
import br.com.net.sqlab_backend.domain.professor.repository.SelfRegistrationProfessorRepository;
import br.com.net.sqlab_backend.domain.student.models.Student;
import br.com.net.sqlab_backend.domain.student.repositories.StudentRepository;
import br.com.net.sqlab_backend.domain.student.services.EmailService;

import java.util.Random;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

	private final AuthenticationManager authManager;
	private final UserRepository userRepository;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;
	private final SelfRegistrationProfessorRepository professorRepository;
	private final StudentRepository studentRepository;

	public AuthenticationService(AuthenticationManager authManager, UserRepository userRepository,
			JwtService jwtService, PasswordEncoder passwordEncoder, EmailService emailService,
			SelfRegistrationProfessorRepository professorRepository, StudentRepository studentRepository) {
		this.authManager = authManager;
		this.userRepository = userRepository;
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
		this.professorRepository = professorRepository;
		this.studentRepository = studentRepository;
	}

	public AuthResponse authenticate(AuthRequest request) {
		try {
			authManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            // UserDetails user = userRepository.findByEmail(request.getEmail())
            //     .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));
            
            UserEntity userEntity = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

            String token = jwtService.generateToken(userEntity);
            
            return new AuthResponse(token);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Email ou senha incorretos");
        }
    }

	public void processForgotPassword(String email) throws Exception {
		try {
			userRepository.findByEmail(email).ifPresent(user -> {
				String tempPassword = generateTempPassword();
				String encryptedPassword = passwordEncoder.encode(tempPassword);

				if (user instanceof Professor) {
					Professor professor = (Professor) user;
					professor.setPassword(encryptedPassword);
					professorRepository.save(professor); 
				} else if (user instanceof Student) {
					Student student = (Student) user;
					student.setPassword(encryptedPassword);
					studentRepository.save(student); 
				}

				try {
					emailService.sendTempPasswordEmail(email, tempPassword);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	private String generateTempPassword() {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();
	}
}