package org.example;

import dataaccess.ConnectionFactory;

import java.sql.Connection;


public class Main {
    public static void main(String[] args) {
        Connection testConnection = ConnectionFactory.getConnection();

        if (testConnection != null) {
            System.out.println("SUCCESS! The Java project is officially connected to MySQL!");
            ConnectionFactory.close(testConnection);
        } else {
            System.out.println("FAILED. Check your password and make sure MySQL is running.");
        }
    }
}