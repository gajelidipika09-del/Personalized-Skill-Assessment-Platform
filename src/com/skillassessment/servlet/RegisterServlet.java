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

@WebServlet("/api/register")
public class RegisterServlet extends HttpServlet {

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

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        System.out.println("REGISTER NAME: [" + name + "]");
        System.out.println("REGISTER EMAIL: [" + email + "]");
        System.out.println("REGISTER PASSWORD: [" + password + "]");

        // Check empty fields
        if (name == null || email == null || password == null ||
            name.trim().isEmpty() ||
            email.trim().isEmpty() ||
            password.trim().isEmpty()) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            out.println(
                "{\"success\":false,\"message\":\"Please fill all fields\"}"
            );

            return;
        }
        name = name.trim();
        email = email.trim();
        password = password.trim();

        // Validate email format
        String emailRegex =
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        if (!email.matches(emailRegex)) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            out.println(
                "{\"success\":false,\"message\":\"Please enter a valid email address\"}"
            );

            return;
        }

        // Validate password
        if (password.length() < 6) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            out.println(
                "{\"success\":false,\"message\":\"Password must be at least 6 characters\"}"
            );

            return;
        }
        if (userDAO.emailExists(email)) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            out.println(
                "{\"success\":false,\"message\":\"This email is already registered\"}"
            );

            return;
        }
        // Create User object
        User user = new User();

        user.setName(name.trim());
        user.setEmail(email.trim());
        user.setPassword(password.trim());

        // Every new registration will be STUDENT
        user.setRole("STUDENT");

        // Save user
        boolean registered = userDAO.registerUser(user);

        if (registered) {

            out.println(
                "{\"success\":true,\"message\":\"Registration successful\"}"
            );

        } else {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            out.println(
                "{\"success\":false,\"message\":\"Registration failed\"}"
            );
        }
    }
}