package com.skillassessment;

import java.util.Scanner;

import com.skillassessment.dao.AdminAnalyticsDAO;

public class AdminDashboard {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        AdminAnalyticsDAO analyticsDAO =
                new AdminAnalyticsDAO();

        boolean running = true;

        while (running) {

            System.out.println("\n===== ADMIN DASHBOARD =====");

            System.out.println("1. Question Bank");
            System.out.println("2. View Analytics");
            System.out.println("3. View Skill Performance");
            System.out.println("4. View Weak Topics");
            System.out.println("5. Exit");

            System.out.print("\nEnter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:

                    AdminQuestionTest.showMenu(sc);
                    break;

                case 2:

                    System.out.println(
                            "\n===== ADMIN ANALYTICS ====="
                    );

                    analyticsDAO.getTotalStudents();
                    analyticsDAO.getTotalAssessments();
                    analyticsDAO.getTotalPractice();
                    analyticsDAO.getAverageAssessmentScore();

                    break;

                case 3:

                    analyticsDAO.getSkillPerformance();
                    break;

                case 4:

                    analyticsDAO.getWeakTopics();
                    break;

                case 5:

                    running = false;

                    System.out.println(
                            "\nExiting Admin Dashboard..."
                    );

                    break;

                default:

                    System.out.println(
                            "Invalid choice!"
                    );
            }
        }

        sc.close();
    }
}