package br.com.net.sqlab_backend.domain.exercises.services;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class CompareAnswerService {

    public static boolean compareLists(List<Map<String, Object>> expected, List<Map<String, Object>> actual) {
        // Verificar quantidade de colunas

        // Verificar nomes de colunas

        // Verificar tipos de colunas
        
        // Verificar se o número de linhas é igual
        if (expected.size() != actual.size()) {
            System.out.println("Tamanhos diferentes: esperado = " + expected.size() + ", atual = " + actual.size());
            return false;
        }

        // Verificar cada linha
        for (int i = 0; i < expected.size(); i++) {
            Map<String, Object> expectedRow = expected.get(i);
            Map<String, Object> actualRow = actual.get(i);

            // Verificar se os mesmos campos existem
            if (!expectedRow.keySet().equals(actualRow.keySet())) {
                System.out.println("Colunas diferentes na linha " + i);
                return false;
            }

            // Verificar os valores de cada campo
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

    public static boolean compareTableSchemas(
        List<Map<String, Object>> studentSchema,
        List<Map<String, Object>> answerSchema
    ) {
        if (studentSchema.size() != answerSchema.size()) {
            System.out.println("Número diferente de colunas.");
            return false;
        }

        for (int i = 0; i < studentSchema.size(); i++) {
            Map<String, Object> studentColumn = studentSchema.get(i);
            Map<String, Object> answerColumn = answerSchema.get(i);

            // Verificar constraints -> 

            // Verifica se o nome das colunas é o mesmo
            if (!Objects.equals(studentColumn.get("name"), answerColumn.get("name"))) {
                System.out.println("Diferença de NOME na coluna " + i);
                return false;
            }

            if (!Objects.equals(studentColumn.get("primaryKey"), answerColumn.get("primaryKey"))) {
                System.out.println("Diferença em PRIMARY KEY na coluna " + studentColumn.get("name"));
                return false;
            }

            // CÓDIGO DE TIPO E NOME DE TIPO -> talvéz retirar nome de tipo
            // Verifica se o tipo da coluna é o mesmo
            if (!Objects.equals(studentColumn.get("type"), answerColumn.get("type"))) {
                System.out.println("Diferença de TIPO na coluna " + studentColumn.get("name"));
                return false;
            } // Verificar tipo pelo valor númerico do tipo
            if (!Objects.equals(studentColumn.get("typeCode"), answerColumn.get("typeCode"))) {
                System.out.println("Diferença de CÓDIGO DE TIPO na coluna " + studentColumn.get("name"));
                return false;
            }

            // Verifica tamanho da coluna (se aplicável)
            if (!Objects.equals(studentColumn.get("columnSize"), answerColumn.get("columnSize"))) {
                System.out.println("Diferença em TAMANHO na coluna " + studentColumn.get("name"));
                return false;
            }

            // Verificar casas deccimais
            if (!Objects.equals(studentColumn.get("decimalDigits"), answerColumn.get("decimalDigits"))) {
                System.out.println("Diferença no NÚMERO DE CASAS DECIMAIS na coluna" + studentColumn.get("name"));
                return false;
            }

            // Verifica se Nullable
            if (!Objects.equals(studentColumn.get("nullable"), answerColumn.get("nullable"))) {
                System.out.println("Diferença em NULLABLE na coluna " + studentColumn.get("name"));
                return false;
            }

            // Verifica auto incremento
            if (!Objects.equals(studentColumn.get("autoIncrement"), answerColumn.get("autoIncrement"))) {
                System.out.println("Diferença em AUTOINCREMENT na coluna " + studentColumn.get("name"));
                return false;
            }

            // Verifica valor default
            if (!Objects.equals(studentColumn.get("default"), answerColumn.get("default"))) {
                System.out.println("Diferença em default na coluna " + studentColumn.get("name"));
                return false;
            }
        }
        return true;
    }

    public static List<Map<String, Object>> getColumnSchema(DatabaseMetaData metaData, String tableName) throws SQLException {
        List<Map<String, Object>> schema = new ArrayList<>();

        // Chaves primárias
        Set<String> primaryKeys = new HashSet<>();
        try (ResultSet pk = metaData.getPrimaryKeys(null, null, tableName.toUpperCase())) {
            while (pk.next()) {
                primaryKeys.add(pk.getString("COLUMN_NAME"));
            }
        }

        printIndexResult(metaData, tableName);

        // Colunas
        try (ResultSet columns = metaData.getColumns(null, null, tableName.toUpperCase(), null)) {
            while (columns.next()) {
                Map<String, Object> columnInfo = new HashMap<>();

                String columnName = columns.getString("COLUMN_NAME");
                columnInfo.put("name", columnName);
                columnInfo.put("type", columns.getString("TYPE_NAME"));
                columnInfo.put("typeCode", columns.getInt("DATA_TYPE"));
                columnInfo.put("columnSize", columns.getInt("COLUMN_SIZE"));
                columnInfo.put("decimalDigits", columns.getInt("DECIMAL_DIGITS"));
                columnInfo.put("nullable", "YES".equals(columns.getString("IS_NULLABLE")));
                columnInfo.put("default", columns.getString("COLUMN_DEF"));
                columnInfo.put("autoIncrement", "YES".equals(columns.getString("IS_AUTOINCREMENT")));
                columnInfo.put("primaryKey", primaryKeys.contains(columnName));

                schema.add(columnInfo);
            }
        }

        return schema;
    }    

    public static List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        System.out.println("Quantidade de colunas: " + columnCount);
    
        boolean hasRows = false;
    
        while (rs.next()) {
            hasRows = true;
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                System.out.println("Nome coluna: " + metaData.getColumnName(i));
                row.put(metaData.getColumnName(i), rs.getObject(i));
            }
            resultList.add(row);
        }
    
        // Se não houver nenhuma linha, ainda assim adiciona um "esqueleto"
        if (!hasRows) {
            Map<String, Object> emptyRow = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                emptyRow.put(metaData.getColumnName(i), null);
            }
            resultList.add(emptyRow);
        }
    
        return resultList;
    }
    
    public static void printIndexResult(DatabaseMetaData metaData, String tableName) throws SQLException {
        try (ResultSet resultSet = metaData.getIndexInfo(null, null, tableName, false, false)) {
            while (resultSet.next()) {
                System.out.println("INDEX_NAME: " + resultSet.getString("INDEX_NAME"));
                System.out.println("NON_UNIQUE: " + resultSet.getString("NON_UNIQUE"));
                System.out.println("TYPE: " + resultSet.getString("TYPE"));
            }
        }
    }

}
