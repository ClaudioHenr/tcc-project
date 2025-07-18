package br.com.net.sqlab_backend.domain.professor.validation;

import br.com.net.sqlab_backend.domain.professor.models.Professor;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ProfessorValidation {

    public static List<String> validateProfessor(Professor professor) {
        List<String> errors = new ArrayList<>();

        if (professor.getName() == null || professor.getName().isEmpty()) {
            errors.add("O nome completo é obrigatório.");
        }

        if (professor.getEmail() == null || professor.getEmail().isEmpty()) {
            errors.add("O email é obrigatório.");
        } else if (!isValidEmail(professor.getEmail())) {
            errors.add("O email fornecido não é válido.");
        }

        if (professor.getPassword() == null || professor.getPassword().isEmpty()) {
            errors.add("A senha é obrigatória.");
        } else if (!isStrongPassword(professor.getPassword())) {
            errors.add("A senha deve ter no mínimo 8 caracteres, incluir uma letra maiúscula, uma minúscula e um número.");
        }

        if (professor.getConfirmPassword() == null || professor.getConfirmPassword().isEmpty()) {
            errors.add("A confirmação da senha é obrigatória.");
        } else if (!professor.getPassword().equals(professor.getConfirmPassword())) {
            errors.add("A senha e a confirmação da senha não coincidem.");
        }

        return errors;
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private static boolean isStrongPassword(String password) {
        String passwordRegex = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}";
        Pattern pattern = Pattern.compile(passwordRegex);
        return pattern.matcher(password).matches();
    }
}
