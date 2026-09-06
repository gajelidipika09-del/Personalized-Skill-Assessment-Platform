package com.skillassessment.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.skillassessment.DBConnection;

public class AdminAnalyticsDAO {

    // ==========================================
    // 1. TOTAL STUDENTS
    // ==========================================

    public int getTotalStudents() {

        String sql = "SELECT COUNT(*) AS total_students " +
                     "FROM users " +
                     "WHERE role = 'STUDENT'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total_students");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }


    // ==========================================
    // 2. TOTAL ASSESSMENTS
    // ==========================================

    public int getTotalAssessments() {

        String sql = "SELECT COUNT(*) AS total_assessments " +
                     "FROM results " +
                     "WHERE attempt_type = 'ASSESSMENT'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total_assessments");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }


    // ==========================================
    // 3. TOTAL PRACTICE ATTEMPTS
    // ==========================================

    public int getTotalPractice() {

        String sql = "SELECT COUNT(*) AS total_practice " +
                     "FROM results " +
                     "WHERE attempt_type = 'PRACTICE'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total_practice");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }


    // ==========================================
    // 4. AVERAGE ASSESSMENT SCORE
    // ==========================================

    public double getAverageAssessmentScore() {

        String sql = "SELECT AVG(score) AS average_score " +
                     "FROM results " +
                     "WHERE attempt_type = 'ASSESSMENT'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                double score = rs.getDouble("average_score");

                if (rs.wasNull()) {
                    return 0.0;
                }

                return score;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0.0;
    }


    // ==========================================
    // 5. TOTAL QUESTIONS
    // ==========================================

    public int getTotalQuestions() {

        String sql = "SELECT COUNT(*) AS total_questions FROM questions";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total_questions");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }


    // ==========================================
    // 6. SKILL PERFORMANCE
    // ==========================================

    public List<String[]> getSkillPerformance() {

        List<String[]> list = new ArrayList<>();

        String sql =
                "SELECT skill, topic, AVG(score) AS average_score " +
                "FROM results " +
                "WHERE attempt_type = 'ASSESSMENT' " +
                "GROUP BY skill, topic " +
                "ORDER BY average_score ASC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String skill = rs.getString("skill");
                String topic = rs.getString("topic");
                double score = rs.getDouble("average_score");

                list.add(new String[] {
                    skill,
                    topic,
                    String.format("%.2f", score)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    // ==========================================
    // 7. WEAK TOPICS
    // ==========================================

    public List<String[]> getWeakTopics() {

        List<String[]> list = new ArrayList<>();

        String sql =
                "SELECT skill, topic, AVG(score) AS average_score " +
                "FROM results " +
                "WHERE attempt_type = 'ASSESSMENT' " +
                "GROUP BY skill, topic " +
                "HAVING AVG(score) < 75 " +
                "ORDER BY average_score ASC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String skill = rs.getString("skill");
                String topic = rs.getString("topic");
                double score = rs.getDouble("average_score");

                String status;

                if (score < 50) {
                    status = "WEAK";
                } else {
                    status = "NEEDS PRACTICE";
                }

                list.add(new String[] {
                    skill,
                    topic,
                    String.format("%.2f", score),
                    status
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}