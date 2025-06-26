package br.com.net.sqlab_backend.authentication.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import br.com.net.sqlab_backend.authentication.dto.AuthRequest;

public class AuthValidation {

    public static List<String> validateAuthRequest(AuthRequest request) {
        List<String> errors = new ArrayList<>();

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            errors.add("O email é obrigatório.");
        } else {
            if (!isValidEmail(request.getEmail())) {
                errors.add("O email fornecido não é válido.");
            }
            
            if (request.getEmail().length() > 255) {
                errors.add("O email não pode exceder 255 caracteres.");
            }
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            errors.add("A senha é obrigatória.");
        } else {
            if (request.getPassword().length() < 8) {
                errors.add("A senha deve ter no mínimo 8 caracteres.");
            }
            
            if (!isStrongPassword(request.getPassword())) {
                errors.add("A senha deve incluir letras maiúsculas, minúsculas e números.");
            }
        }

        return errors;
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private static boolean isStrongPassword(String password) {
        boolean hasUpper = !password.equals(password.toLowerCase());
        boolean hasLower = !password.equals(password.toUpperCase());
        boolean hasNumber = password.matches(".*\\d.*");
        
        return hasUpper && hasLower && hasNumber;
    }
    
}