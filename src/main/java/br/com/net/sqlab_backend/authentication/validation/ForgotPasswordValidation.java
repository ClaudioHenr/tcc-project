package br.com.net.sqlab_backend.authentication.validation;

import br.com.net.sqlab_backend.authentication.dto.ForgotPasswordRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ForgotPasswordValidation {

    public static List<String> validateForgotPasswordRequest(ForgotPasswordRequest request) {
        List<String> errors = new ArrayList<>();

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            errors.add("O email é obrigatório.");
        } else if (!isValidEmail(request.getEmail())) {
            errors.add("O email fornecido não é válido.");
        } else if (request.getEmail().length() > 255) {
            errors.add("O email não pode exceder 255 caracteres.");
        }

        return errors;
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}