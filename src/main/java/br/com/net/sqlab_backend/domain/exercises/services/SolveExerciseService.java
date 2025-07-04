package br.com.net.sqlab_backend.domain.exercises.services;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.com.net.sqlab_backend.domain.exercises.dto.AnswerStudentCreateDTO;
import br.com.net.sqlab_backend.domain.exercises.dto.QueryExerciseDTO;
import br.com.net.sqlab_backend.domain.exercises.dto.exercise.ResponseSolveExerciseDTO;
import br.com.net.sqlab_backend.domain.exercises.models.AnswerProfessor;
import br.com.net.sqlab_backend.domain.exercises.models.AnswerStudent;
import br.com.net.sqlab_backend.domain.exercises.models.Exercise;
import br.com.net.sqlab_backend.domain.exercises.models.QueryResult;

@Service
public class SolveExerciseService {

    private AnswerStudentService answerStudentService;

    private AnswerProfessorService answerProfessorService;

    private ExerciseService exerciseService;

    private EnvironmentExerciseService environmentExerciseService;

    public SolveExerciseService(AnswerStudentService answerStudentService, ExerciseService exerciseService,
            EnvironmentExerciseService environmentExerciseService, AnswerProfessorService answerProfessorService) {
        this.answerStudentService = answerStudentService;
        this.exerciseService = exerciseService;
        this.environmentExerciseService = environmentExerciseService;
        this.answerProfessorService = answerProfessorService;
    }

    public ResponseSolveExerciseDTO handleSolveExercise(QueryExerciseDTO query) {
        // ResponseSolveExerciseDTO res = new ResponseSolveExerciseDTO(false, null, 0);
        // Salvar query em answer_student
        AnswerStudentCreateDTO dto = new AnswerStudentCreateDTO(query.query(), null, query.exerciseId(), query.studentId());
        AnswerStudent savedAnswer = answerStudentService.save(dto);
        // Recuperar query resposta de answer_professor/resposta pré cadastrada
        AnswerProfessor answer = answerProfessorService.getByExerciseId(query.exerciseId());
        Exercise exercise = exerciseService.getById(query.exerciseId());
        ResponseSolveExerciseDTO res;

        switch (exercise.getType()) {
            case SELECT:
                res = solveSelectExercise(query, answer.getAnswer());
                break;
            case UPDATE:
            case INSERT:
            case DELETE:
            case ALTER:
                res = solveUpdateExercise(query, answer.getAnswer(), exercise.getTableName());
                break;
            case CREATE:
                res = solveCreateExercise(query, answer.getAnswer(), exercise.getTableName());
                break;
            case DROP:
                res = solveDropExercise(query, answer.getAnswer(), exercise.getTableName());
                break;
            default:
                throw new IllegalArgumentException("Tipo de exercício desconhecido: " + exercise.getType());
        }
    
        // Atualiza o resultado (correto/incorreto) na resposta salva
        savedAnswer.setCorrect(res.isCorrect());
        answerStudentService.update(savedAnswer);
    
        return res;

        // switch (exercise.getType()) {
        //     case SELECT:
        //         res = solveSelectExercise(query, answer.getAnswer());
        //         return res;
        //     case UPDATE:
        //         res = solveUpdateExercise(query, answer.getAnswer(), exercise.getTableName());
        //         return res;
        //     case INSERT:
        //         res = solveUpdateExercise(query, answer.getAnswer(), exercise.getTableName());
        //         return res;
        //     case DELETE:
        //         res = solveUpdateExercise(query, answer.getAnswer(), exercise.getTableName());
        //         return res;
        //     case CREATE:
        //         res = solveCreateExercise(query, answer.getAnswer(), exercise.getTableName());
        //         return res;
        //     case DROP:
        //         res = solveDropExercise(query, answer.getAnswer(), exercise.getTableName());
        //         return res;
        //     case ALTER:
        //         res = solveUpdateExercise(query, answer.getAnswer(), exercise.getTableName());
        //         return res;
        //     default:
        //         throw new IllegalArgumentException("Tipo de exercício desconhecido: " + exercise.getType());
        // }
    }

    public ResponseSolveExerciseDTO solveCreateExercise(QueryExerciseDTO query, String queryAnswer, String tableName) {
        String querySelect = "SELECT * FROM " + tableName;
        // Criar 'ambientes'
        Connection conn = environmentExerciseService.createEnviromentForExercise(query.exerciseId());
        Connection connAnswer = environmentExerciseService.createEnviromentForExercise(query.exerciseId());

        try {
            // Executar as duas queries
            QueryResult resultQueryStudent = executeQueryCreate(conn, query.query());
            QueryResult resultQueryAnswer = executeQueryCreate(connAnswer, queryAnswer);
            int updateCount = resultQueryStudent.updateCount;
            // Executar SELECT *
            QueryResult tableCreatedStudent = executeQuerySelect(conn, querySelect);        

            DatabaseMetaData metaData = conn.getMetaData();
            DatabaseMetaData metaDataAnswer = connAnswer.getMetaData();

            List<Map<String, Object>> schemaStudent = CompareAnswerService.getColumnSchema(metaData, tableName);
            List<Map<String, Object>> schemaAnswer = CompareAnswerService.getColumnSchema(metaDataAnswer, tableName);
            
            // Comparar schemas
            boolean isEqual = CompareAnswerService.compareTableSchemas(schemaStudent, schemaAnswer);
            
            List<Map<String, Object>> tableAfterUpdateStudentList = CompareAnswerService.resultSetToList(tableCreatedStudent.resultSet);
            // Formar DTO de retorno
            ResponseSolveExerciseDTO res = new ResponseSolveExerciseDTO(isEqual, tableAfterUpdateStudentList, updateCount);

            // Fechar ResultSet e Statement após uso
            resultQueryStudent.close();
            resultQueryAnswer.close();
            tableCreatedStudent.close();
            // Retornar
            return res;
        } catch (Exception e) {
            System.err.println("Erro ao executar queries: " + e.getMessage());
            e.printStackTrace();
        } finally {
            environmentExerciseService.closeConnection(conn);
        }
        // Retornar
        ResponseSolveExerciseDTO res = new ResponseSolveExerciseDTO(false, null, 0);
        return res;
    }

    public ResponseSolveExerciseDTO solveDropExercise(QueryExerciseDTO query, String queryAnswer, String tableName) {
        boolean isCorrect;
        Connection conn = environmentExerciseService.createEnviromentForExercise(query.exerciseId());
        Connection connAnswer = environmentExerciseService.createEnviromentForExercise(query.exerciseId());

        try {
            // Executar as duas queries
            QueryResult resultQueryStudent = executeQueryInsertOrUpdateOrDelete(conn, query.query());
            QueryResult resultQueryAnswer = executeQueryInsertOrUpdateOrDelete(connAnswer, queryAnswer);

            boolean existsTableAfterQueryStudent = existsTable(conn, tableName);
            boolean existsTableAfterQueryAnswer = existsTable(connAnswer, tableName);
            System.out.println(existsTableAfterQueryStudent);
            System.out.println(existsTableAfterQueryAnswer);
            if (!existsTableAfterQueryStudent && !existsTableAfterQueryAnswer) {
                System.out.println("Tabelas excluidas com sucesso.....");
                isCorrect = true;
            } else {
                isCorrect = false;
            }

            // Fechar ResultSet e Statement após uso
            resultQueryStudent.close();
            resultQueryAnswer.close();
            // Retornar
            ResponseSolveExerciseDTO res = new ResponseSolveExerciseDTO(isCorrect, null, 0);
            return res;
        } catch (Exception e) {
            System.err.println("Erro ao executar queries: " + e.getMessage());
            e.printStackTrace();
        } finally {
            environmentExerciseService.closeConnection(conn);
        }

        // Conferir se tabela continua a existir

        ResponseSolveExerciseDTO res = new ResponseSolveExerciseDTO(false, null, 0);
        return res;
    }

    public ResponseSolveExerciseDTO solveUpdateExercise(QueryExerciseDTO query, String queryAnswer, String tableName) {
        String querySelect = "SELECT * FROM " + tableName;
        // Criar 'ambientes'
        Connection conn = environmentExerciseService.createEnviromentForExercise(query.exerciseId());
        Connection connAnswer = environmentExerciseService.createEnviromentForExercise(query.exerciseId());

        try {
            // Executar as duas queries
            QueryResult resultQueryStudent = executeQueryInsertOrUpdateOrDelete(conn, query.query());
            QueryResult resultQueryAnswer = executeQueryInsertOrUpdateOrDelete(connAnswer, queryAnswer);
            int updateCount = resultQueryStudent.updateCount;
            // Executar SELECT * pós UPDATE
            QueryResult tableAfterUpdateStudent = executeQuerySelect(conn, querySelect);
            QueryResult tableAfterUpdateAnswer = executeQuerySelect(connAnswer, querySelect);
            List<Map<String, Object>> tableAfterUpdateStudentList = CompareAnswerService.resultSetToList(tableAfterUpdateStudent.resultSet);
            List<Map<String, Object>> tableAfterUpdateAnswerList = CompareAnswerService.resultSetToList(tableAfterUpdateAnswer.resultSet);
            // Comparar tabelas
            boolean isEqual = CompareAnswerService.compareLists(tableAfterUpdateStudentList, tableAfterUpdateAnswerList);

            // Formar DTO de retorno
            ResponseSolveExerciseDTO res = new ResponseSolveExerciseDTO(isEqual, tableAfterUpdateStudentList, updateCount);

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
        ResponseSolveExerciseDTO res = new ResponseSolveExerciseDTO(false, null, 0);
        return res;
    }

    public ResponseSolveExerciseDTO solveSelectExercise(QueryExerciseDTO query, String queryAnswer) {
        // Criar 'ambiente'
        Connection conn = environmentExerciseService.createEnviromentForExercise(query.exerciseId());

        try {
            // Executar as duas queries
            QueryResult resultQueryStudent = executeQuerySelect(conn, query.query());
            QueryResult resultQueryAnswer = executeQuerySelect(conn, queryAnswer);
            List<Map<String, Object>> answerList = CompareAnswerService.resultSetToList(resultQueryAnswer.resultSet);
            List<Map<String, Object>> studentList = CompareAnswerService.resultSetToList(resultQueryStudent.resultSet);
            // Comparar resultados das duas queries
            boolean isEqual = CompareAnswerService.compareLists(answerList, studentList);
            // Setar CORRETO/INCORRETO na resposta do ALUNO
            // answerStudentSaved.setCorrect(isIqual);
            // answerStudentService.update(answerStudentSaved);
            // Formar DTO de retorno
            ResponseSolveExerciseDTO res = new ResponseSolveExerciseDTO(isEqual, studentList, 0);
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
        ResponseSolveExerciseDTO res = new ResponseSolveExerciseDTO(false, null, 0);
        return res;
    }

    public boolean existsTable(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet resultSet = meta.getTables(null, null, tableName.toUpperCase(), new String[]{"TABLE"})) {
            return resultSet.next();
        }
    }

    // EXECUÇÃO DE QUERIES

    public QueryResult executeQuerySelect(Connection conn, String query) {
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

    public QueryResult executeQueryInsertOrUpdateOrDelete(Connection conn, String query) {
        try {
            Statement stmt = conn.createStatement();
            int updateCount = stmt.executeUpdate(query);
            System.out.println("Query DML executada com sucesso, linhas afetadas: " + updateCount);
            return new QueryResult(stmt, updateCount);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao executar a query SQL: " + e.getMessage());
        }
    }

    public QueryResult executeQueryCreate(Connection conn, String query) {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(query);
            System.out.println("Query CREATE executada com sucesso");
            return new QueryResult(stmt, 0);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao executar a query SQL: " + e.getMessage());
        }
    }

}
