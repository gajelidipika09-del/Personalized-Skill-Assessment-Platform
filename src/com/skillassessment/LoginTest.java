package com.skillassessment;

import java.util.Scanner;

import com.skillassessment.dao.UserDAO;
import com.skillassessment.model.User;

public class LoginTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===== STUDENT LOGIN =====");

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        UserDAO userDAO = new UserDAO();

        User user = userDAO.loginUser(email, password);

        if (user != null) {

            System.out.println("Login Successful!");
            System.out.println("Welcome, " + user.getName());
            System.out.println("Role: " + user.getRole());

        } else {

            System.out.println("Invalid Email or Password!");

        }

        sc.close();
    }
}