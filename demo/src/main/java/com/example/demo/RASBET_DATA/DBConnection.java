package com.example.demo.RASBET_DATA;

import java.sql.*;

public class DBConnection {
    private Connection con;
    private String servername = "jdbc:mysql://localhost:3306/RASBET";
    private String username = "utilizador";
    private String password = "Bdli42122!";

    // MUDAR
    public DBConnection() throws SQLException {
        try {
            this.con = DriverManager.getConnection(this.servername, this.username, this.password);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Connection getConnection() {
        return this.con;
    }

    public void closeConnection() {
        try {
            this.con.close();
        } catch (SQLException s) {
            System.out.println("Error closing connection.");
        }
    }

    public Statement createStatement() throws SQLException {

        return this.con.createStatement();
    }

}