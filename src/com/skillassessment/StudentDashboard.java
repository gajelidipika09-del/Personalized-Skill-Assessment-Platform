package com.skillassessment;

import java.util.Scanner;

import com.skillassessment.dao.ProgressDAO;

public class StudentDashboard {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===== STUDENT DASHBOARD =====");

        System.out.print("Enter Student ID: ");
        int userId = sc.nextInt();
        sc.nextLine();

        ProgressDAO progressDAO = new ProgressDAO();

        progressDAO.showStudentName(userId);

        boolean running = true;

        while (running) {

            System.out.println("\n===== STUDENT MENU =====");
            System.out.println("1. Start Assessment");
            System.out.println("2. Personalized Practice");
            System.out.println("3. View Progress");
            System.out.println("4. View Skill Analysis");
            System.out.println("5. View Recommendations");
            System.out.println("6. View Improvement");
            System.out.println("7. Exit");

            System.out.print("\nEnter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:

                    AssessmentTest.startAssessment(userId, sc);
                    break;

                case 2:

                    PracticeTest.startPractice(userId, sc);
                    break;

                case 3:

                    progressDAO.showProgress(userId);
                    break;

                case 4:

                    progressDAO.showWeakAreas(userId);
                    break;

                case 5:

                    progressDAO.showRecommendations(userId, sc);
                    break;
                case 6:

                    System.out.print("Enter Skill: ");
                    String skill = sc.nextLine();

                    System.out.print("Enter Topic: ");
                    String topic = sc.nextLine();

                    progressDAO.showImprovement(
                            userId,
                            skill,
                            topic
                    );

                    break;

                case 7:

                    running = false;
                    System.out.println("\nThank you for using Skill Assessment Platform!");

                    break;

                default:

                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }

        sc.close();
    }
}