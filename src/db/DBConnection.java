package db;

import java.sql.*;

public class DBConnection {
    private  final String DBURL = "jdbc:mysql://localhost:3306/w20?useSSL=false";
    private  final String username = "root";
    private  final String password = "root";
    private  Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private Statement statement = null;
    private ResultSet resultSet = null;


    public DBConnection() {

    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public void setPreparedStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    /**
     * This method is to get the db connection
     * @return
     * @throws SQLException
     */

    public  Connection getConnection() throws SQLException {
        try {
            connection = DriverManager.getConnection(DBURL,username,password);
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return connection;
    }

    /**
     * this method is to close the db connection
     * @param connection
     * @param preparedStatement
     * @param resultSet
     * @throws SQLException
     */

    public void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) throws SQLException {
        if(connection != null) {
            connection.close();
        }
        if(preparedStatement != null) {
            preparedStatement.close();
        }
        if(resultSet != null) {
            resultSet.close();
        }
    }

    /**
     * overloading method, this method will close the db connection where the statement exists
     * @param connection
     * @param statement
     * @param resultSet
     * @throws SQLException
     */
    public void close(Connection connection, Statement statement, ResultSet resultSet) throws SQLException {
        if(connection != null) {
            connection.close();
        }
        if(statement != null) {
            statement.close();
        }
        if(resultSet != null) {
            resultSet.close();
        }
    }

    public void close(Connection connection, PreparedStatement preparedStatement) throws SQLException {
        if(connection != null) {
            connection.close();
        }
        if(preparedStatement != null) {
            preparedStatement.close();
        }
    }


}
