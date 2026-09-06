package com.skillassessment;

import com.skillassessment.dao.AdminAnalyticsDAO;

public class AdminAnalyticsTest {

    public static void main(String[] args) {

        AdminAnalyticsDAO analyticsDAO = new AdminAnalyticsDAO();

        System.out.println("\n===== ADMIN ANALYTICS =====");

        System.out.println("Total Students: "
                + analyticsDAO.getTotalStudents());

        System.out.println("Total Assessments: "
                + analyticsDAO.getTotalAssessments());

        System.out.println("Total Practice: "
                + analyticsDAO.getTotalPractice());

        System.out.println("Average Assessment Score: "
                + analyticsDAO.getAverageAssessmentScore());

        System.out.println("Total Questions: "
                + analyticsDAO.getTotalQuestions());

        System.out.println("\n===== SKILL PERFORMANCE =====");

        for (String[] row : analyticsDAO.getSkillPerformance()) {

            System.out.println(
                    "Skill: " + row[0]
                    + " | Topic: " + row[1]
                    + " | Average Score: " + row[2] + "%"
            );
        }

        System.out.println("\n===== WEAK TOPICS =====");

        for (String[] row : analyticsDAO.getWeakTopics()) {

            System.out.println(
                    "Skill: " + row[0]
                    + " | Topic: " + row[1]
                    + " | Score: " + row[2] + "%"
                    + " | Status: " + row[3]
            );
        }

        System.out.println("\n===== ANALYTICS COMPLETE =====");
    }
}