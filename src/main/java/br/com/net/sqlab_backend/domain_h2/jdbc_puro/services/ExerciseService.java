package br.com.net.sqlab_backend.domain_h2.jdbc_puro.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.net.sqlab_backend.domain_h2.dto.QueryExerciseDTO;
import br.com.net.sqlab_backend.domain_h2.jdbc_puro.create.ExampleExercise;
import br.com.net.sqlab_backend.domain_h2.jdbc_puro.models.QueryResult;

@Service
public class ExerciseService {
    
    @Autowired
    private ExampleExercise exampleExercise;

    @Autowired
    private CompareAnswerService compareAnswerService;

    public String handleSolveExercise(QueryExerciseDTO query) {
        // CRIAR 'AMBIENTE' PARA TESTAR RESPOSTA DO ALUNO
        Connection conn = exampleExercise.createEnviromentForExercise(query.exerciseId(), query.dialect());

        // QUERY RESPOSTA DO EXERCÍCIO
        String queryAnswer = "SELECT * FROM users;";

        // COMPARAR USANDO METADATA
        try {            
            if (query.type() == 1) {
                QueryResult resultTest = executeQuerySelect(conn, query.query(), query.type());
                QueryResult resultAnswer = executeQuerySelect(conn, queryAnswer, query.type());
                printQuery(resultTest.resultSet);
                boolean resultComparable = compareAnswerService.compareExerciseWithMetaData(resultAnswer.resultSet, resultTest.resultSet);
                System.out.println(resultComparable ? "Mesmo resultado" : "Query com resultado diferente da resposta");
                // Fechar ResultSet e Statement após uso
                resultTest.close();
                resultAnswer.close();
            } else {
                QueryResult resultTest = executeQueryUpdateOrDelete(conn, query.query(), query.type());
                QueryResult resultAnswer = executeQueryUpdateOrDelete(conn, queryAnswer, query.type());
                boolean isEqual = resultTest.updateCount == resultAnswer.updateCount;
                if (isEqual) {
                    System.out.println("Mesma quantidade de linhas alteradas");
                } else {
                    System.out.println("Quantidade diferente de linhas alteradas");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao executar queries: " + e.getMessage());
            e.printStackTrace();
            return "Erro ao processar a query.";
        } finally {
            exampleExercise.closeConnection(conn);
        }
        
        // COMPARAR USANDO QUERY 'EXCEPT'
        // compareAnswerService.compareExerciseWithExcept(conn, queryAnswer, query.query());

        return "";
    }

    // Executa a query e mantém o Statement aberto
    public QueryResult executeQuerySelect(Connection conn, String query, int typeQuery) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);
            System.out.println("Query de select executada com sucesso");
            return new QueryResult(stmt, result);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao executar a query SQL: " + e.getMessage());
        }
    }

    public QueryResult executeQueryUpdateOrDelete(Connection conn, String query, int typeQuery) {
        try {
            Statement stmt = conn.createStatement();
            int updateCount = stmt.executeUpdate(query);
            System.out.println("Query de update ou delete executada com sucesso, linhas afetadas: " + updateCount);
            return new QueryResult(stmt, updateCount);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao executar a query SQL: " + e.getMessage());
        }
    }

    public void printQuery(ResultSet result) {
        System.out.println("======== PRINT DE QUERY ========");
        StringBuilder resultString = new StringBuilder();
        try {            
            ResultSetMetaData resultMetaData = result.getMetaData();
            while (result.next()) {  // Percorre as linhas do resultado
                for (int i = 1; i <= resultMetaData.getColumnCount(); i++) {
                    String columnName = resultMetaData.getColumnName(i);
                    String columnValue = result.getString(columnName); // Pega o valor da coluna
        
                    System.out.println(columnName + ": " + columnValue);
                    resultString.append(columnName).append(": ").append(columnValue).append("\n");
                }
                resultString.append("\n"); // Separação entre registros
            }
        } catch (Exception e) {
        }
        System.out.println(resultString.toString());
    }




    public String getDataExerciseTest(QueryExerciseDTO query) {
        // Criar conexão com H2
        Connection conn = exampleExercise.createConnection(query.dialect());
        if (conn == null) {
            return "Erro ao conectar ao banco H2.";
        }

        try {
            // Criar tabela e inserir dados de exercício
            exampleExercise.executeQueryFromFile(conn, 1L, query.sgbd());

            // Executar exercício (query do usuário)
            StringBuilder resultString = new StringBuilder();
            try (Statement stmt = conn.createStatement();
                ResultSet result = stmt.executeQuery(query.query()))
            {
                System.out.println("Query executada com sucesso");
                ResultSetMetaData resultMetaData = result.getMetaData();
                while (result.next()) {  // Percorre as linhas do resultado
                    for (int i = 1; i <= resultMetaData.getColumnCount(); i++) {
                        String columnName = resultMetaData.getColumnName(i);
                        String columnValue = result.getString(columnName); // Pega o valor da coluna
            
                        System.out.println(columnName + ": " + columnValue);
                        resultString.append(columnName).append(": ").append(columnValue).append("\n");
                    }
                    resultString.append("\n"); // Separação entre registros
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

    // public String insertDataExerciseTest(QueryExerciseDTO query) {
    //    // Criar conexão com H2
    //    Connection conn = exampleExercise.createConnection(query.dialect());
    //    if (conn == null) {
    //        return "Erro ao conectar ao banco H2.";
    //    }

    //    try {
    //        // Criar tabela e inserir dados de exercício
    //        exampleExercise.executeOperations(conn);

    //        // Executar exercício (query do usuário)
    //        StringBuilder resultString = new StringBuilder();
    //        try (Statement stmt = conn.createStatement())
    //        {
    //             int rowsAffected = stmt.executeUpdate(query.query()); // Inserir linha
    //             System.out.println("Linhas afetadas: " + rowsAffected);
    //             ResultSet result = stmt.executeQuery("SELECT * FROM users");
    //             System.out.println("Query executada com sucesso");
    //             while (result.next()) {
    //                 resultString.append("ID: ").append(result.getInt("id"))
    //                            .append(", Nome: ").append(result.getString("name"))
    //                            .append("\n");
    //            }
    //        }

    //        return resultString.toString();
    //    } catch (SQLException e) {
    //         e.printStackTrace();
    //         return "Erro ao executar a query SQL: " + e.getMessage();
    //    } finally {
    //         exampleExercise.closeConnection(conn);
    //    }
    // }

    // public String deleteDataExerciseTest(QueryExerciseDTO query) {
    //     // Criar conexão com H2
    //     Connection conn = exampleExercise.createConnection(query.dialect());
    //     if (conn == null) {
    //         return "Erro ao conectar ao banco H2.";
    //     }

    //     try {
    //         // Criar tabela e inserir dados de exercício
    //         exampleExercise.executeOperations(conn);
 
    //         // Executar exercício (query do usuário)
    //         StringBuilder resultString = new StringBuilder();
    //         try (Statement stmt = conn.createStatement())
    //         {
    //              int rowsAffected = stmt.executeUpdate(query.query()); // Inserir linha
    //              System.out.println("Linhas afetadas: " + rowsAffected);
    //              ResultSet result = stmt.executeQuery("SELECT * FROM users");
    //              System.out.println("Query executada com sucesso");
    //              while (result.next()) {
    //                  resultString.append("ID: ").append(result.getInt("id"))
    //                             .append(", Nome: ").append(result.getString("name"))
    //                             .append("\n");
    //             }
    //         }
 
    //         return resultString.toString();
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         return "Erro ao executar a query SQL: " + e.getMessage();
    //     } finally {
    //         // Fechar conexão
    //         exampleExercise.closeConnection(conn);
    //     }
    //  }

}
