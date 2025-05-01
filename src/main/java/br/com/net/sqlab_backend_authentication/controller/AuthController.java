package br.com.net.sqlab_backend_authentication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.net.sqlab_backend_authentication.dto.AuthRequest;
import br.com.net.sqlab_backend_authentication.dto.AuthResponse;
import br.com.net.sqlab_backend_authentication.service.AuthenticationService;
import br.com.net.sqlab_backend_authentication.validation.AuthValidation;

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
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(List.of(e.getMessage()));
        }
    }
}