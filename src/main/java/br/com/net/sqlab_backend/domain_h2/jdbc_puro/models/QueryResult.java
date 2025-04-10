package br.com.net.sqlab_backend.domain_h2.jdbc_puro.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryResult {
    public Statement statement;
    public ResultSet resultSet;
    public int updateCount;

    // Para SELECT
    public QueryResult(Statement statement, ResultSet resultSet) {
        this.statement = statement;
        this.resultSet = resultSet;
        this.updateCount = -1;
    }

    // Para UPDATE/DELETE
    public QueryResult(Statement statement, int updateCount) {
        this.statement = statement;
        this.resultSet = null;
        this.updateCount = updateCount;
    }

    public void close() throws SQLException {
        if (resultSet != null) resultSet.close();
        statement.close();
    }
}
