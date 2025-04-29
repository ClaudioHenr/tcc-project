package br.com.net.sqlab_backend.domain.student.validation;

import br.com.net.sqlab_backend.domain.student.models.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StudentValidation {

    public static List<String> validateStudent(Student student) {
        List<String> errors = new ArrayList<>();

        if (student.getName() == null || student.getName().trim().isEmpty()) {
            errors.add("O nome completo é obrigatório.");
        } else if (student.getName().length() > 255) {
            errors.add("O nome não pode exceder 255 caracteres.");
        }

        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            errors.add("O email é obrigatório.");
        } else if (!isValidEmail(student.getEmail())) {
            errors.add("O email fornecido não é válido.");
        }

        if (student.getConfirmEmail() == null || student.getConfirmEmail().trim().isEmpty()) {
            errors.add("A confirmação do email é obrigatória.");
        } else if (!student.getEmail().equals(student.getConfirmEmail())) {
            errors.add("O email e a confirmação do email não coincidem.");
        }

        if (student.getRegistrationNumber() == null || student.getRegistrationNumber().trim().isEmpty()) {
            errors.add("O número de matrícula é obrigatório.");
        } else if (student.getRegistrationNumber().length() > 100) {
            errors.add("O número de matrícula não pode exceder 100 caracteres.");
        }

        if (student.getPassword() == null || student.getPassword().trim().isEmpty()) {
            errors.add("A senha é obrigatória.");
        } else if (!isStrongPassword(student.getPassword())) {
            errors.add("A senha deve ter no mínimo 8 caracteres, incluir uma letra maiúscula, uma minúscula e um número.");
        }

        if (student.getConfirmPassword() == null || student.getConfirmPassword().trim().isEmpty()) {
            errors.add("A confirmação da senha é obrigatória.");
        } else if (!student.getPassword().equals(student.getConfirmPassword())) {
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
