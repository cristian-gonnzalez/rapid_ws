package com.meli.backend.rapid.db;

import java.sql.*;

public class DataBase {
    
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
        
    public DataBase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } 
        catch (ClassNotFoundException ex) {
        }
    }

    public Boolean connect() {

        String user = "root";
        String pswd = "root";
        String url = "jdbc:mysql://localhost:3306/dbtest";
    
        try {
            conn = DriverManager.getConnection(url, user, pswd);            
        }
        catch (SQLException exception) {
            System.out.println(exception);
            return false;
        }
        return true;
    }

    public Boolean disconnect() {
        try {
            conn.close();
        }
        catch (SQLException exception) {
            System.out.println(exception);
            return false;
        }
        return true;
    }

    public void begin() throws SQLException {
        conn.setAutoCommit(false);
    }

    public void rollback()  throws SQLException {
        conn.rollback();
        conn.setAutoCommit(true);
    }
   
    public void commit()  throws SQLException {
        conn.commit();
        conn.setAutoCommit(true);
    }


    public void prepareStmt() {
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public ResultSet execute(String querystr) throws SQLException {
        rs = stmt.executeQuery(querystr);
        return rs;
    }

    public boolean executeUpdte(String sql) throws SQLException {
        int x = stmt.executeUpdate(sql);
        return (x > 0);
    }

    public void closeStmt() {
        try {
            if( rs != null)
                rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        rs = null;
        stmt = null;        
    }
}
