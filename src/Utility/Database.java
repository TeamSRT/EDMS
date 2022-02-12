/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author ktoufiquee
 */
public class Database {
    private Statement statement;
    private Connection conn;
    
    public void connect() throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=EDMS;";
        conn = DriverManager.getConnection(url);
        statement = conn.createStatement();
    }
    
    public void disconnect() throws SQLException {
        if (statement != null) {
            statement.close();
        }
        if (conn != null) {
            conn.close();
        }
    }
    
    public ResultSet getResult(String query) throws SQLException {
        return statement.executeQuery(query);
    }
    
    public void updateTable(String query) throws SQLException {
        statement.executeUpdate(query);
    }
    
    public ResultSet updateTableWithKeys(String query)  throws SQLException {
        statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
        return statement.getGeneratedKeys();
    }
}
