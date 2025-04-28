package br.com.net.sqlab_backend.domain.exercises.services;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

@Service
public class CompareAnswerService {
    
    public boolean compareExerciseWithMetaData(ResultSet answer, ResultSet answerStudent) throws SQLException {
        ResultSetMetaData metaData1 = answer.getMetaData();
        ResultSetMetaData metaData2 = answerStudent.getMetaData();
        
        int columnCount1 = metaData1.getColumnCount();
        int columnCount2 = metaData2.getColumnCount();

        // Verifica se ambas as queries possuem o mesmo número de colunas
        if (columnCount1 != columnCount2) {
            System.out.println("Número de colunas diferente");
            return false;
        }

        while (answer.next()) {
            if (!answerStudent.next()) {
                System.out.println("Número de registros menor que o resultado da resposta");
                return false; // answerStudent tem menos registros que answerProfessor
            }

            for (int i = 1; i <= columnCount1; i++) {
                Object value1 = answer.getObject(i);
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

    public static boolean compareLists(List<Map<String, Object>> expected, List<Map<String, Object>> actual) {
        // 1. Verificar se o número de linhas é igual
        if (expected.size() != actual.size()) {
            System.out.println("Tamanhos diferentes: esperado = " + expected.size() + ", atual = " + actual.size());
            return false;
        }

        // 2. Verificar cada linha
        for (int i = 0; i < expected.size(); i++) {
            Map<String, Object> expectedRow = expected.get(i);
            Map<String, Object> actualRow = actual.get(i);

            // 3. Verificar se os mesmos campos existem
            if (!expectedRow.keySet().equals(actualRow.keySet())) {
                System.out.println("Colunas diferentes na linha " + i);
                return false;
            }

            // 4. Verificar os valores de cada campo
            for (String key : expectedRow.keySet()) {
                Object expectedValue = expectedRow.get(key);
                Object actualValue = actualRow.get(key);

                if (!Objects.equals(expectedValue, actualValue)) {
                    System.out.printf("Valor diferente na linha %d, coluna '%s': esperado = %s, atual = %s%n",
                        i, key, expectedValue, actualValue);
                    return false;
                }
            }
        }

        return true;
    }
    
    public static boolean compareResultSets(ResultSet rs1, ResultSet rs2) throws SQLException {
        List<Map<String, Object>> list1 = resultSetToList(rs1);
        List<Map<String, Object>> list2 = resultSetToList(rs2);

        return list1.equals(list2);
    }

    public static List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        System.out.println("Quantidade de colunas: " + columnCount);

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
