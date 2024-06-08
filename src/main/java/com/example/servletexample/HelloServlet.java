package com.example.servletexample;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";

    }

    private Connection getConnection() {
        String dbUrl = System.getenv("DB_URL");
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");

        if (dbUrl == null || username == null || password == null) {
            // Environment variables not set, try to read from file
            String credentialsFilePath = getServletContext().getRealPath("/WEB-INF/db_credentials.txt");
            File credentialsFile = new File(credentialsFilePath);
            try (Scanner scanner = new Scanner(credentialsFile)) {
                if (scanner.hasNextLine()) {
                    dbUrl = scanner.nextLine();
                }
                if (scanner.hasNextLine()) {
                    username = scanner.nextLine();
                }
                if (scanner.hasNextLine()) {
                    password = scanner.nextLine();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(dbUrl, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");

        Connection connection = getConnection();
        if (connection != null) {
            out.println("<h2>Connection to MySQL database established!</h2>");
        } else {
            out.println("<h2>Failed to make connection!</h2>");
        }

        out.println("</body></html>");
    }

    public void destroy() {
    }
}