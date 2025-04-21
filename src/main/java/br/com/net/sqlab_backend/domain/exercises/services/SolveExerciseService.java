package br.com.net.sqlab_backend.domain.exercises.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.net.sqlab_backend.domain.answer.models.Answer;
import br.com.net.sqlab_backend.domain.answer.services.AnswerService;
import br.com.net.sqlab_backend.domain.exercises.dto.AnswerStudentCreateDTO;
import br.com.net.sqlab_backend.domain.exercises.dto.QueryExerciseDTO;
import br.com.net.sqlab_backend.domain.exercises.dto.ResponseExerciseDTO;
import br.com.net.sqlab_backend.domain.exercises.models.QueryResult;

@Service
public class SolveExerciseService {

    @Autowired
    private AnswerStudentService answerStudentService;

    @Autowired
    private AnswerProfessorService answerProfessorService;

    @Autowired
    private EnvironmentExerciseService environmentExerciseService;

    @Autowired
    private AnswerService answerService;

    public ResponseExerciseDTO handleSolveExercise(QueryExerciseDTO query) {
        // Salvar query em answer_student
        AnswerStudentCreateDTO dto = new AnswerStudentCreateDTO(query.query(), null, query.exerciseId(),query.studentId());
        answerStudentService.save(dto);

        // Recuperar query resposta de answer_professor/resposta pré cadastrada
        // String answerProfessor = answerProfessorService.getAnswerProfessorByExerciseId(query.exerciseId());
        Answer answer = answerService.getByExerciseId(query.exerciseId());
        System.out.println(answer.getAnswer());

        // Criar 'ambiente'
        Connection conn = environmentExerciseService.createEnviromentForExercise(query.exerciseId());

        try {
            // Executar as duas queries
            QueryResult resultQueryStudent = executeQuerySelect(conn, query.query(), 0);
            QueryResult resultQueryAnswer = executeQuerySelect(conn, answer.getAnswer(), 0);
            List<Map<String, Object>> answerList = CompareAnswerService.resultSetToList(resultQueryAnswer.resultSet);
            List<Map<String, Object>> studentList = CompareAnswerService.resultSetToList(resultQueryStudent.resultSet);
            // Comparar resultados das duas queries
            boolean isIqual = CompareAnswerService.compareLists(answerList, studentList);
            ResponseExerciseDTO res = new ResponseExerciseDTO(isIqual, studentList);
            // Fechar ResultSet e Statement após uso
            resultQueryStudent.close();
            resultQueryAnswer.close();
            // Retornar
            return res;
        } catch (Exception e) {
            System.err.println("Erro ao executar queries: " + e.getMessage());
            e.printStackTrace();
        } finally {
            environmentExerciseService.closeConnection(conn);
        }

        // Retornar
        ResponseExerciseDTO res = new ResponseExerciseDTO(false, null);
        return res;
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
   
}
