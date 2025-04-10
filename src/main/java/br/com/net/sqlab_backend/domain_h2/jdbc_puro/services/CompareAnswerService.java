package br.com.net.sqlab_backend.domain_h2.jdbc_puro.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class CompareAnswerService {
    
    public boolean compareExerciseWithMetaData(ResultSet answerProfessor, ResultSet answerStudent) throws SQLException {
        ResultSetMetaData metaData1 = answerProfessor.getMetaData();
        ResultSetMetaData metaData2 = answerStudent.getMetaData();
        
        int columnCount1 = metaData1.getColumnCount();
        int columnCount2 = metaData2.getColumnCount();

        // Verifica se ambas as queries possuem o mesmo número de colunas
        if (columnCount1 != columnCount2) {
            System.out.println("Número de colunas diferente");
            return false;
        }

        while (answerProfessor.next()) {
            if (!answerStudent.next()) {
                System.out.println("Número de registros menor que o resultado da resposta");
                return false; // answerStudent tem menos registros que answerProfessor
            }

            for (int i = 1; i <= columnCount1; i++) {
                Object value1 = answerProfessor.getObject(i);
                Object value2 = answerStudent.getObject(i);

                if (value1 == null && value2 == null) continue;

                if (value1 == null || value2 == null || !value1.equals(value2)) {
                    System.out.println("Valor diferente: " + value1 + " != " + value2);
                    return false;
                }
            }
        }

        // Verifica se answerStudent tem mais registros que answerProfessor
        if (answerStudent.next()) {
            System.out.println("Número de registros maior que o resultado da resposta");
            return false;
        }

        return true;
    }




    public static boolean compareResultSets(ResultSet rs1, ResultSet rs2) throws SQLException {
        List<Map<String, Object>> list1 = resultSetToList(rs1);
        List<Map<String, Object>> list2 = resultSetToList(rs2);

        return list1.equals(list2);
    }

    private static List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(metaData.getColumnName(i), rs.getObject(i));
            }
            resultList.add(row);
        }
        return resultList;
    }

}
