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

/*    @Autowired
    private AnswerProfessorService answerProfessorService;*/

    @Autowired
    private EnvironmentExerciseService environmentExerciseService;

    @Autowired
    private AnswerService answerService;

    public ResponseExerciseDTO handleSolveExercise(QueryExerciseDTO query) {
        ResponseExerciseDTO res = new ResponseExerciseDTO(false, null, 0);
        // Salvar query em answer_student
        AnswerStudentCreateDTO dto = new AnswerStudentCreateDTO(query.query(), null, query.exerciseId(),query.studentId());
        answerStudentService.save(dto);
        // Recuperar query resposta de answer_professor/resposta pré cadastrada
        // String answerProfessor = answerProfessorService.getAnswerProfessorByExerciseId(query.exerciseId());
        Answer answer = answerService.getByExerciseId(query.exerciseId());
        System.out.println(answer.getAnswer());
        switch (answer.getTypeExercise()) {
            case 1:
                res = solveSelectExercise(query, answer.getAnswer());
                return res;
            case 2:
                res = solveUpdateExercise(query, answer.getAnswer());
                return res;
            default:
                return res;
        }
    }

    public ResponseExerciseDTO solveUpdateExercise(QueryExerciseDTO query, String queryAnswer) {
        // Criar 'ambientes'
        Connection conn = environmentExerciseService.createEnviromentForExercise(query.exerciseId());
        Connection connAnswer = environmentExerciseService.createEnviromentForExercise(query.exerciseId());

        try {
            // Executar as duas queries
            QueryResult resultQueryStudent = executeQueryInsertOrUpdateOrDelete(conn, query.query(), 0);
            QueryResult resultQueryAnswer = executeQueryInsertOrUpdateOrDelete(connAnswer, queryAnswer, 0);
            int updateCount = resultQueryStudent.updateCount;
            // Executar SELECT * pós UPDATE
            QueryResult tableAfterUpdateStudent = executeQuerySelect(conn, "SELECT * FROM users", 0);
            QueryResult tableAfterUpdateAnswer = executeQuerySelect(connAnswer, "SELECT * FROM users", 0);
            List<Map<String, Object>> tableAfterUpdateStudentList = CompareAnswerService.resultSetToList(tableAfterUpdateStudent.resultSet);
            List<Map<String, Object>> tableAfterUpdateAnswerList = CompareAnswerService.resultSetToList(tableAfterUpdateAnswer.resultSet);

            // Comparar tabelas
            boolean isEqual = CompareAnswerService.compareLists(tableAfterUpdateStudentList, tableAfterUpdateAnswerList);

            // Formar DTO de retorno
            ResponseExerciseDTO res = new ResponseExerciseDTO(isEqual, tableAfterUpdateStudentList, updateCount);

            // Fechar ResultSet e Statement após uso
            resultQueryStudent.close();
            resultQueryAnswer.close();
            tableAfterUpdateStudent.close();
            tableAfterUpdateAnswer.close();
            // Retornar
            return res;
        } catch (Exception e) {
            System.err.println("Erro ao executar queries: " + e.getMessage());
            e.printStackTrace();
        } finally {
            environmentExerciseService.closeConnection(conn);
        }
        // Retornar
        ResponseExerciseDTO res = new ResponseExerciseDTO(false, null, 0);
        return res;
    }

    public ResponseExerciseDTO solveSelectExercise(QueryExerciseDTO query, String queryAnswer) {
        // Criar 'ambiente'
        Connection conn = environmentExerciseService.createEnviromentForExercise(query.exerciseId());

        try {
            // Executar as duas queries
            QueryResult resultQueryStudent = executeQuerySelect(conn, query.query(), 0);
            QueryResult resultQueryAnswer = executeQuerySelect(conn, queryAnswer, 0);
            List<Map<String, Object>> answerList = CompareAnswerService.resultSetToList(resultQueryAnswer.resultSet);
            List<Map<String, Object>> studentList = CompareAnswerService.resultSetToList(resultQueryStudent.resultSet);
            // Comparar resultados das duas queries
            boolean isEqual = CompareAnswerService.compareLists(answerList, studentList);
            // Setar CORRETO/INCORRETO na resposta do ALUNO
            // answerStudentSaved.setCorrect(isIqual);
            // answerStudentService.update(answerStudentSaved);
            // Formar DTO de retorno
            ResponseExerciseDTO res = new ResponseExerciseDTO(isEqual, studentList, 0);
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
        ResponseExerciseDTO res = new ResponseExerciseDTO(false, null, 0);
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

    public QueryResult executeQueryInsertOrUpdateOrDelete(Connection conn, String query, int typeQuery) {
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
