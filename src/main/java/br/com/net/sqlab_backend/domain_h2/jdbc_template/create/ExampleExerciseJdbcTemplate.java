package br.com.net.sqlab_backend.domain_h2.jdbc_template.create;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ExampleExerciseJdbcTemplate {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    public ExampleExerciseJdbcTemplate(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    public void executeSqlWithConnection(Long exerciseId, String query) {
        try {
            // Obtém a conexão
            // Connection connection = dataSource.getConnection();

            // Executa o arquivo SQL
            executeSqlFile(exerciseId);

            // Executa a query adicional
            jdbcTemplate.execute(query);
            System.out.println("Query adicional executada!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Conexão fechada. Tabelas e dados excluídos.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Executa um arquivo SQL localizado dentro de "src/main/resources/sql/"
     * @param filename Nome do arquivo SQL (ex: "user.sql")
     */
    public void executeSqlFile(Long exerciseId) {
        String filename = "";
        if (exerciseId == 1) {
            filename = "user.sql"; 
        } else {
            filename = "address.sql";
        }
        try {
            // Lê o conteúdo do arquivo SQL
            ClassPathResource resource = new ClassPathResource("exercises/" + filename);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                String sql = reader.lines().collect(Collectors.joining("\n"));
                jdbcTemplate.execute(sql);
            }
            // Executa o SQL
            System.out.println("Arquivo '" + filename + "' executado com sucesso!");
            
        } catch (Exception e) {
            System.out.println("Erro ao executar o arquivo: " + filename);
            e.printStackTrace();
        }
    }

    public void executeQuery(String query) {
        // jdbcTemplate.execute(query);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        for (Map<String,Object> map : result) {
            System.out.println(map);
        }
        System.out.println(result);
    }

    // public void executeOperations() {
    //     System.out.println("======= EXECUTAR QUERYS ======");
        
    //     // Criação da tabela e inserção de dados
    //     String createTableSQL = "CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(100))";
    //     String insertUser1 = "INSERT INTO users (id, name) VALUES (1, 'Claudio Henrique')";
    //     String insertUser2 = "INSERT INTO users (id, name) VALUES (2, 'Elisabeth')";
        
    //     // Executando as queries usando jdbcTemplate
    //     jdbcTemplate.execute(createTableSQL);
    //     jdbcTemplate.update(insertUser1);
    //     jdbcTemplate.update(insertUser2);

    //     System.out.println("REGISTROS DE TESTE INSERIDOS COM SUCESSO");
    // }
}
