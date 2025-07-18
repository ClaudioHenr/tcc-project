package br.com.net.sqlab_backend.domain.grade.util;

import java.util.Random;

public class RandomCodeGenerator {
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final Random RANDOM = new Random();

    public static String generateCode() {
        StringBuilder code = new StringBuilder();

        // 3 letras
        for (int i = 0; i < 3; i++) {
            code.append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())));
        }

        // 3 números
        for (int i = 0; i < 3; i++) {
            code.append(NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length())));
        }

        return code.toString();
    }
}
