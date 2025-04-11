package br.com.net.sqlab_backend.domain.exercises.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import br.com.net.sqlab_backend.domain.exercises.enums.Dialect;
import br.com.net.sqlab_backend.domain.exercises.models.Exercise;

@Service
public class EnvironmentExerciseService {

    @Autowired
    ExerciseService exerciseService;
    
    // Função global de responsabilidade de criar 'ambiente' de execução de query do aluno
    public Connection createEnviromentForExercise(Long exerciseId) {
        Exercise exercise = exerciseService.getById(exerciseId);

        Connection conn = createConnection(exercise.getDialect());
        if (conn == null) {
            throw new RuntimeException("Erro ao conectar ao banco H2.");
        }
        executeQueryFromFile(conn, exercise.getDialect());
        return conn;
    }

    public Connection createConnection(Dialect dialect) {
        System.out.println("CRIANDO CONEXÃO COM DIALETO: " + dialect);
        String url = "jdbc:h2:mem:db_exercise;MODE=" + dialect.getDescription();
        System.out.println("URL: " + url);
        // String url = "jdbc:h2:file:./database_tests;DB_CLOSE_ON_EXIT=FALSE;" + dialect;
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

    public void executeQueryFromFile(Connection conn, Dialect dialect) { // 1 - postgresql / 2 - mysql
        String filename = "";
        if (dialect == Dialect.POSTGRESQL) {
            filename = "/postgresql/user_orders.sql";
        } if (dialect == Dialect.MYSQL) {
            filename = "/mysql/user_orders.sql";
        }
        try {
            // Lê o conteúdo do arquivo SQL
            ClassPathResource resource = new ClassPathResource("exercises/" + filename);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                String sql = reader.lines().collect(Collectors.joining("\n"));
                // CHAMA FUNÇÃO PARA EXECUTAR QUERIES DO ARQUIVO
                executeOperations(conn, sql);
            }
            System.out.println("Arquivo '" + filename + "' executado com sucesso!");
            
        } catch (Exception e) {
            System.out.println("Erro ao executar o arquivo: " + filename);
            e.printStackTrace();
        }
    }

    public void executeOperations(Connection conn, String operations) {
        try (Statement stmt = conn.createStatement()) {
            System.out.println("======= EXECUTAR QUERYS DE ARQUIVO ======");
            stmt.execute(operations);
            System.out.println("REGISTROS DE EXERCÍCIO INSERIDOS COM SUCESSO");
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
