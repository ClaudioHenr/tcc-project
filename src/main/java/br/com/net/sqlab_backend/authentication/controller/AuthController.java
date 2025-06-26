package br.com.net.sqlab_backend.authentication.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.net.sqlab_backend.authentication.dto.AuthRequest;
import br.com.net.sqlab_backend.authentication.dto.AuthResponse;
import br.com.net.sqlab_backend.authentication.dto.ForgotPasswordRequest;
import br.com.net.sqlab_backend.authentication.service.AuthenticationService;
import br.com.net.sqlab_backend.authentication.validation.AuthValidation;
import br.com.net.sqlab_backend.authentication.validation.ForgotPasswordValidation;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthenticationService authService;

	public AuthController(AuthenticationService authService) {
		this.authService = authService;
	}

	@PostMapping("/signin")
	public ResponseEntity<?> login(@RequestBody AuthRequest request) {
		List<String> validationErrors = AuthValidation.validateAuthRequest(request);

		if (!validationErrors.isEmpty()) {
			return ResponseEntity.badRequest().body(validationErrors);
		}

		try {
			AuthResponse response = authService.authenticate(request);
			return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + response.getToken()).body(response);
		} catch (RuntimeException e) {
			return ResponseEntity.status(401).body(List.of(e.getMessage()));
		}
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
		List<String> validationErrors = ForgotPasswordValidation.validateForgotPasswordRequest(request);
		if (!validationErrors.isEmpty()) {
			return ResponseEntity.badRequest().body(validationErrors);
		}

		String msg = "Se o email existir em nossa base de dados, será enviado uma senha temporária para o email digitado.";
		try {
			authService.processForgotPassword(request.getEmail());
			return ResponseEntity.ok().body(List.of(msg));
		} catch (Exception e) {
			return ResponseEntity.ok().body(List.of(msg));
		}
	}
}
