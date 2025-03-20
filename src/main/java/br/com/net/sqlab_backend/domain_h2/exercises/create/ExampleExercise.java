package br.com.net.sqlab_backend.domain_h2.exercises.create;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Service;

@Service
public class ExampleExercise {

    public Connection createConnection() {
        String url = "jdbc:h2:mem:db_exercise_test";
        try {
            System.out.println("======= CRIAR CONEXÃO ======");
            Class.forName("org.h2.Driver"); // Carrega o driver manualmente
            return DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver H2 não encontrado.");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void executeOperations(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            System.out.println("======= EXECUTAR QUERYS ======");
            stmt.execute("CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(100))");
            stmt.execute("INSERT INTO users (id, name) VALUES (1, 'Claudio Henrique')");
            stmt.execute("INSERT INTO users (id, name) VALUES (2, 'Elisabeth')");
            System.out.println("Registro inserido com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void closeConnection(Connection conn) {
        try {
            conn.close();
            System.out.println("====== CONEXÃO FECHADA =======");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
