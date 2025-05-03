package br.com.net.sqlab_backend.authentication.service;

import br.com.net.sqlab_backend.authentication.dto.AuthRequest;
import br.com.net.sqlab_backend.authentication.dto.AuthResponse;
import br.com.net.sqlab_backend.authentication.repository.UserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthenticationService(AuthenticationManager authManager, UserRepository userRepository, JwtService jwtService) {
        this.authManager = authManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public AuthResponse authenticate(AuthRequest request) {
        try {
            authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserDetails user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

            String token = jwtService.generateToken(user);
            
            return new AuthResponse(token);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Email ou senha incorretos");
        }
    }
}