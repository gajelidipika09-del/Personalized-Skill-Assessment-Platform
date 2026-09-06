package com.skillassessment;

import java.util.Scanner;

import com.skillassessment.dao.UserDAO;
import com.skillassessment.model.User;

public class RegistrationTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===== STUDENT REGISTRATION =====");

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();
        

        User user = new User(
                name,
                email,
                password,
                "STUDENT"
        );

        UserDAO userDAO = new UserDAO();

        boolean result = userDAO.registerUser(user);

        if (result) {
            System.out.println("Registration Successful!");
        } else {
            System.out.println("Registration Failed!");
        }

        sc.close();
    }
}