package com.skillassessment;

import java.util.Scanner;

import com.skillassessment.dao.UserDAO;
import com.skillassessment.model.User;

public class Login {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===== PERSONALIZED SKILL ASSESSMENT =====");
        System.out.println("===== LOGIN =====");

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        UserDAO userDAO = new UserDAO();

        User user = userDAO.loginUser(email, password);

        if (user == null) {

            System.out.println("Invalid Email or Password!");

        } else {

            System.out.println("\nLogin Successful!");
            System.out.println("Welcome, " + user.getName());

            if (user.getRole().equalsIgnoreCase("ADMIN")) {

                System.out.println("Role: ADMIN");

                AdminDashboard.main(null);
            }

            else if (user.getRole().equalsIgnoreCase("STUDENT")) {

                System.out.println("Role: STUDENT");

                StudentDashboard.main(null);
            }
        }

        sc.close();
    }
}