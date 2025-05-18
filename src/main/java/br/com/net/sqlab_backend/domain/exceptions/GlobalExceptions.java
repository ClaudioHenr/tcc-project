package br.com.net.sqlab_backend.domain.exceptions;

import java.sql.SQLException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.net.sqlab_backend.domain.exceptions.custom.EntityNotFoundException;
import jakarta.persistence.NonUniqueResultException;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptions {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> handleJdbcSQLSyntaxErrorException(SQLException ex) {
        System.out.println("Capturado em GlobalException");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("De global: " + ex.getMessage());
    }

    @ExceptionHandler(NonUniqueResultException.class)
    public ResponseEntity<String> handleNonUniqueResultException(NonUniqueResultException ex) {
        System.out.println("Capturado em GlobalException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro inesperado, por favor aguarde");
    }
}
