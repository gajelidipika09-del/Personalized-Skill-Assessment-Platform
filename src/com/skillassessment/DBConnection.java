package com.skillassessment;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/skill_assessments";

    private static final String USERNAME = "root";

    private static final String PASSWORD = "Dipu@2005";

    public static Connection getConnection() {

        Connection con = null;

        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    URL,
                    USERNAME,
                    PASSWORD
            );

        } catch (Exception e) {
            e.printStackTrace();
        }

        return con;
    }

    public static void main(String[] args) {

        Connection con = getConnection();

        try {

            if (con != null) {
                System.out.println("Connection Successful!");
                con.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}