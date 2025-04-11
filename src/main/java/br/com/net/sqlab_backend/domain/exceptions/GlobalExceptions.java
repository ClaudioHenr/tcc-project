package br.com.net.sqlab_backend.domain.exceptions;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class GlobalExceptions {
    
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> handleJdbcSQLSyntaxErrorException(SQLException ex) {
        System.out.println("Capturado em GlobalException");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("De global: " + ex.getMessage());
    }

}
