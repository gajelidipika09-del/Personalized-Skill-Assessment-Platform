package com.skillassessment;

import java.util.Scanner;

import com.skillassessment.dao.ProgressDAO;

public class ProgressTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===== PROGRESS TRACKING =====");

        System.out.print("Enter Student ID: ");
        int userId = sc.nextInt();
        sc.nextLine();

        ProgressDAO progressDAO = new ProgressDAO();
        
        progressDAO.showProgress(userId);

        progressDAO.showWeakAreas(userId);
        progressDAO.showRecommendations(userId, sc);

        System.out.print("\nEnter Skill for Improvement: ");
        String skill = sc.nextLine();

        System.out.print("Enter Topic for Improvement: ");
        String topic = sc.nextLine();

        progressDAO.showImprovement(userId, skill, topic);

        
        sc.close();
    }
}