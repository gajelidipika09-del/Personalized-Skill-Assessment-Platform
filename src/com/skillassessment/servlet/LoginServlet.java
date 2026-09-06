package com.skillassessment.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import com.skillassessment.dao.UserDAO;
import com.skillassessment.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        System.out.println("EMAIL RECEIVED: [" + email + "]");
        System.out.println("PASSWORD RECEIVED: [" + password + "]");

        if (email == null || password == null ||
            email.trim().isEmpty() || password.trim().isEmpty()) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            out.println(
                "{\"success\":false,\"message\":\"Email and password are required\"}"
            );

            return;
        }

        User user = userDAO.loginUser(email, password);

        if (user == null) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            out.println(
                "{\"success\":false,\"message\":\"Invalid email or password\"}"
            );

            return;
        }

        out.println(
            "{"
            + "\"success\":true,"
            + "\"message\":\"Login successful\","
            + "\"userId\":" + user.getUserId() + ","
            + "\"name\":\"" + user.getName() + "\","
            + "\"email\":\"" + user.getEmail() + "\","
            + "\"role\":\"" + user.getRole() + "\""
            + "}"
        );
    }
}