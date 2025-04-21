package br.com.net.sqlab_backend.domain.exercises.enums;

public enum Dialect {
    POSTGRESQL("PostgreSQL"),
    MYSQL("MySQL");

    private final String description;

    Dialect(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
