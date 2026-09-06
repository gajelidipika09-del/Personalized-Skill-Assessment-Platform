package com.skillassessment.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.skillassessment.DBConnection;
import com.skillassessment.model.Result;

public class ResultDAO {

    public boolean saveResult(Result result) {

        String sql = "INSERT INTO results "
                + "(user_id, skill, topic, difficulty, "
                + "total_questions, correct_answers, "
                + "wrong_answers, score, attempt_type) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, result.getUserId());
            ps.setString(2, result.getSkill());
            ps.setString(3, result.getTopic());
            ps.setString(4, result.getDifficulty());
            ps.setInt(5, result.getTotalQuestions());
            ps.setInt(6, result.getCorrectAnswers());
            ps.setInt(7, result.getWrongAnswers());
            ps.setDouble(8, result.getScore());
            ps.setString(9, result.getAttemptType());

            int rows = ps.executeUpdate();

            if (rows > 0) {

                // Update student's daily streak
                StreakDAO streakDAO = new StreakDAO();
                streakDAO.updateStreak(result.getUserId());

                return true;
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}