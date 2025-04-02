package br.com.net.sqlab_backend.domain_h2.jdbc_puro.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryResult {
    public Statement statement;
    public ResultSet resultSet;

    public QueryResult(Statement statement, ResultSet resultSet) {
        this.statement = statement;
        this.resultSet = resultSet;
    }

    public void close() throws SQLException {
        resultSet.close();
        statement.close();
    }
}
