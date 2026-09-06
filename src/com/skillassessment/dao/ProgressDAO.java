package com.skillassessment.dao;
import com.skillassessment.service.RecommendationService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.skillassessment.PracticeTest;
import java.util.Scanner;

import com.skillassessment.DBConnection;


public class ProgressDAO {

    public void showProgress(int userId) {

        String sql = "SELECT skill, topic, score, test_date "
                   + "FROM results "
                   + "WHERE user_id = ? "
                   + "ORDER BY test_date";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== STUDENT PROGRESS =====");

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println(
                        "Skill: " + rs.getString("skill")
                );

                System.out.println(
                        "Topic: " + rs.getString("topic")
                );

                System.out.println(
                        "Score: " + rs.getDouble("score") + "%"
                );

                System.out.println(
                        "Date: " + rs.getTimestamp("test_date")
                );

                System.out.println("-------------------------");
            }

            if (!found) {
                System.out.println("No assessment history found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showWeakAreas(int userId) {

        String sql = "SELECT skill, topic, AVG(score) AS average_score "
                   + "FROM results "
                   + "WHERE user_id = ? "
                   + "AND attempt_type = 'ASSESSMENT' "
                   + "GROUP BY skill, topic "
                   + "ORDER BY average_score ASC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== SKILL ANALYSIS =====");

            boolean found = false;

            while (rs.next()) {

                found = true;

                String skill = rs.getString("skill");
                String topic = rs.getString("topic");
                double score = rs.getDouble("average_score");

                String status;

                if (score < 50) {
                    status = "🔴 WEAK";
                } else if (score <= 75) {
                    status = "🟡 NEEDS PRACTICE";
                } else {
                    status = "🟢 STRONG";
                }

                System.out.println(
                        skill + " → " + topic
                        + " → " + String.format("%.2f", score)
                        + "% → " + status
                );
            }

            if (!found) {
                System.out.println("No data available.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showImprovement(int userId, String skill, String topic) {

        String sql = "SELECT "
                   + "(SELECT score FROM results "
                   + " WHERE user_id = ? "
                   + " AND skill = ? "
                   + " AND topic = ? "
                   + " AND attempt_type = 'ASSESSMENT' "
                   + " ORDER BY test_date DESC LIMIT 1) AS assessment_score, "

                   + "(SELECT score FROM results "
                   + " WHERE user_id = ? "
                   + " AND skill = ? "
                   + " AND topic = ? "
                   + " AND attempt_type = 'PRACTICE' "
                   + " ORDER BY test_date DESC LIMIT 1) AS practice_score";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, skill);
            ps.setString(3, topic);

            ps.setInt(4, userId);
            ps.setString(5, skill);
            ps.setString(6, topic);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                double before = rs.getDouble("assessment_score");
                boolean assessmentExists = !rs.wasNull();

                double after = rs.getDouble("practice_score");
                boolean practiceExists = !rs.wasNull();

                System.out.println("\n===== PRACTICE IMPROVEMENT =====");
                System.out.println("Skill: " + skill);
                System.out.println("Topic: " + topic);

                // No assessment found
                if (!assessmentExists) {

                    System.out.println(
                        "\nNo assessment result found for this topic."
                    );

                    System.out.println(
                        "Complete an assessment first."
                    );

                    return;
                }

                System.out.println(
                    "Before Practice: "
                    + String.format("%.2f", before)
                    + "%"
                );

                // Assessment exists but practice not done
                if (!practiceExists) {

                    System.out.println(
                        "After Practice: Not attempted"
                    );

                    System.out.println(
                        "Improvement: Not available"
                    );

                    System.out.println(
                        "\nComplete recommended practice "
                        + "to measure improvement."
                    );

                    return;
                }

                System.out.println(
                    "After Practice: "
                    + String.format("%.2f", after)
                    + "%"
                );

                double improvement = after - before;

                System.out.println(
                    "Improvement: "
                    + String.format("%.2f", improvement)
                    + "%"
                );

                if (improvement > 0) {

                    System.out.println(
                        "Status: 🟢 Improvement achieved!"
                    );

                } else if (improvement == 0) {

                    System.out.println(
                        "Status: 🟡 No improvement yet."
                    );

                } else {

                    System.out.println(
                        "Status: 🔴 Score decreased. More practice recommended."
                    );
                }

            } else {

                System.out.println(
                    "\nNo improvement data available."
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showRecommendations(int userId, Scanner sc) {

        String sql = "SELECT skill, topic, AVG(score) AS average_score "
                   + "FROM results "
                   + "WHERE user_id = ? "
                   + "AND attempt_type = 'ASSESSMENT' "
                   + "GROUP BY skill, topic "
                   + "ORDER BY average_score ASC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            RecommendationService service =
                    new RecommendationService();

            System.out.println("\n===== RECOMMENDATIONS =====");

            boolean found = false;
            int count = 0;

            String[] skills = new String[100];
            String[] topics = new String[100];
            double[] scores = new double[100];
            int[] questionCounts = new int[100];

            while (rs.next()) {

                found = true;
                count++;

                String skill = rs.getString("skill");
                String topic = rs.getString("topic");
                double score = rs.getDouble("average_score");

                String recommendation =
                        service.getRecommendation(score);

                int questions;

                if (score < 50) {
                    questions = 10;
                } else if (score <= 75) {
                    questions = 5;
                } else {
                    questions = 0;
                }

                skills[count - 1] = skill;
                topics[count - 1] = topic;
                scores[count - 1] = score;
                questionCounts[count - 1] = questions;

                System.out.println(
                        count + ". "
                        + skill + " → "
                        + topic + " → "
                        + String.format("%.2f", score)
                        + "% → "
                        + recommendation
                );
            }

            if (!found) {

                System.out.println(
                        "No recommendations available."
                );

                return;
            }

            System.out.print(
                    "\nEnter recommendation number to practice "
                    + "(0 to cancel): "
            );

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 0) {
                System.out.println("Returning to dashboard...");
                return;
            }

            if (choice < 1 || choice > count) {

                System.out.println(
                        "Invalid recommendation number."
                );

                return;
            }

            String selectedSkill = skills[choice - 1];
            String selectedTopic = topics[choice - 1];
            int selectedQuestions = questionCounts[choice - 1];

            if (selectedQuestions == 0) {

                System.out.println(
                        "\nThis topic is already strong."
                );

                System.out.println(
                        "No immediate practice required."
                );

                return;
            }

            System.out.println(
                    "\nStarting recommended practice..."
            );

            PracticeTest.startRecommendedPractice(
                    userId,
                    sc,
                    selectedSkill,
                    selectedTopic,
                    selectedQuestions
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showOverallScore(int userId) {

    	String sql = "SELECT AVG(score) AS overall_score "
    	           + "FROM results "
    	           + "WHERE user_id = ? "
    	           + "AND attempt_type = 'ASSESSMENT'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                double score = rs.getDouble("overall_score");

                System.out.println(
                        "\nOverall Skill Score: "
                        + String.format("%.2f", score)
                        + "%"
                );

            } else {

                System.out.println(
                        "\nOverall Skill Score: No data"
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showStudentName(int userId) {

        String sql = "SELECT name FROM users WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String name = rs.getString("name");

                System.out.println("\nWelcome, " + name + "! 👋");

            } else {

                System.out.println("\nStudent not found.");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}