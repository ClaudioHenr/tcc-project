package br.com.net.sqlab_backend.domain_h2.exercises.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.net.sqlab_backend.domain_h2.exercises.create.ExampleExercise;

@Service
public class ExerciseService {
    
    @Autowired
    private ExampleExercise exampleExercise;

    public String getDataExerciseTest(String query) {
        // Criar conexão com H2
        Connection conn = exampleExercise.createConnection();
        if (conn == null) {
            return "Erro ao conectar ao banco H2.";
        }

        try {
            // Criar tabela e inserir dados de exercício
            exampleExercise.executeOperations(conn);

            // Executar exercício (query do usuário)
            StringBuilder resultString = new StringBuilder();
            try (Statement stmt = conn.createStatement();
                ResultSet result = stmt.executeQuery(query))
            {
                System.out.println("Query executada com sucesso");
                while (result.next()) {
                    resultString.append("ID: ").append(result.getInt("id"))
                                .append(", Nome: ").append(result.getString("name"))
                                .append("\n");
                }
            }

            return resultString.toString().isEmpty() ? "Nenhum dado encontrado." : resultString.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao executar a query.";
        } finally {
            // Fechar conexão
            exampleExercise.closeConnection(conn);
        }
    }

}
