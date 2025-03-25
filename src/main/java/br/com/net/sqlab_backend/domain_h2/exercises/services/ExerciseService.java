package br.com.net.sqlab_backend.domain_h2.exercises.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.net.sqlab_backend.domain_h2.dto.QueryExerciseDTO;
import br.com.net.sqlab_backend.domain_h2.exercises.create.ExampleExercise;

@Service
public class ExerciseService {
    
    @Autowired
    private ExampleExercise exampleExercise;

    public String getDataExerciseTest(QueryExerciseDTO query) {
        // Criar conexão com H2
        Connection conn = exampleExercise.createConnection(query.dialect());
        if (conn == null) {
            return "Erro ao conectar ao banco H2.";
        }

        try {
            // Criar tabela e inserir dados de exercício
            exampleExercise.executeOperations(conn);

            // Executar exercício (query do usuário)
            StringBuilder resultString = new StringBuilder();
            try (Statement stmt = conn.createStatement();
                ResultSet result = stmt.executeQuery(query.query()))
            {
                System.out.println("Query executada com sucesso");
                while (result.next()) {
                    resultString.append("ID: ").append(result.getInt("id"))
                                .append(", Nome: ").append(result.getString("name"))
                                .append("\n");
                }
            }

            return resultString.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao executar a query SQL: " + e.getMessage();
        } finally {
            // Fechar conexão
            exampleExercise.closeConnection(conn);
        }
    }

    public String createTableExerciseTest(QueryExerciseDTO query) {
        // Criar conexão com H2
        Connection conn = exampleExercise.createConnection(query.dialect());
        if (conn == null) {
            return "Erro ao conectar ao banco H2.";
        }

        try {
            // Executar exercício (query do usuário)
            StringBuilder resultString = new StringBuilder();
            try (Statement stmt = conn.createStatement()) {

                boolean resultQuery = stmt.execute(query.query());
                System.out.println("Query executada: " + resultQuery);

                // Verifique se a query foi executada com sucesso (para criação de tabela, normalmente não haverá retorno)
                if (!resultQuery) {
                    resultString.append("Tabela criada com sucesso ou query executada.\n");
                }

                // Obtém as colunas da tabela (se a tabela foi criada com sucesso)
                ResultSet resultColumns = stmt.executeQuery("SELECT * FROM users LIMIT 0"); // Consulta sem dados, apenas para obter colunas
                ResultSetMetaData metaData = resultColumns.getMetaData();

                resultString.append("Colunas da tabela criada:\n");
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    resultString.append(metaData.getColumnName(i) + " ");
                    resultString.append(metaData.getColumnTypeName(i)).append("\n");
                }

                // Caso a tabela esteja vazia, já teríamos capturado as colunas.
                if (resultString.length() == 0) {
                    resultString.append("Tabela vazia.");
                }
            }
            System.out.println(resultString);
            return resultString.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao executar a query SQL: " + e.getMessage();
        } finally {
            // Fechar conexão
            exampleExercise.closeConnection(conn);
        }
    }

    public String insertDataExerciseTest(QueryExerciseDTO query) {
       // Criar conexão com H2
       Connection conn = exampleExercise.createConnection(query.dialect());
       if (conn == null) {
           return "Erro ao conectar ao banco H2.";
       }

       try {
           // Criar tabela e inserir dados de exercício
           exampleExercise.executeOperations(conn);

           // Executar exercício (query do usuário)
           StringBuilder resultString = new StringBuilder();
           try (Statement stmt = conn.createStatement())
           {
                int rowsAffected = stmt.executeUpdate(query.query()); // Inserir linha
                System.out.println("Linhas afetadas: " + rowsAffected);
                ResultSet result = stmt.executeQuery("SELECT * FROM users");
                System.out.println("Query executada com sucesso");
                while (result.next()) {
                    resultString.append("ID: ").append(result.getInt("id"))
                               .append(", Nome: ").append(result.getString("name"))
                               .append("\n");
               }
           }

           return resultString.toString();
       } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao executar a query SQL: " + e.getMessage();
       } finally {
            exampleExercise.closeConnection(conn);
       }
    }

    public String deleteDataExerciseTest(QueryExerciseDTO query) {
        // Criar conexão com H2
        Connection conn = exampleExercise.createConnection(query.dialect());
        if (conn == null) {
            return "Erro ao conectar ao banco H2.";
        }

        try {
            // Criar tabela e inserir dados de exercício
            exampleExercise.executeOperations(conn);
 
            // Executar exercício (query do usuário)
            StringBuilder resultString = new StringBuilder();
            try (Statement stmt = conn.createStatement())
            {
                 int rowsAffected = stmt.executeUpdate(query.query()); // Inserir linha
                 System.out.println("Linhas afetadas: " + rowsAffected);
                 ResultSet result = stmt.executeQuery("SELECT * FROM users");
                 System.out.println("Query executada com sucesso");
                 while (result.next()) {
                     resultString.append("ID: ").append(result.getInt("id"))
                                .append(", Nome: ").append(result.getString("name"))
                                .append("\n");
                }
            }
 
            return resultString.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao executar a query SQL: " + e.getMessage();
        } finally {
            // Fechar conexão
            exampleExercise.closeConnection(conn);
        }
     }

}
