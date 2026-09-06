package com.skillassessment.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.skillassessment.DBConnection;
import com.skillassessment.model.Question;

public class QuestionDAO {

    // 1. ADD QUESTION
    public boolean addQuestion(Question q) {

        String sql = "INSERT INTO questions "
                + "(skill, topic, difficulty, question, option_a, option_b, "
                + "option_c, option_d, correct_answer, explanation) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, q.getSkill());
            ps.setString(2, q.getTopic());
            ps.setString(3, q.getDifficulty());
            ps.setString(4, q.getQuestion());
            ps.setString(5, q.getOptionA());
            ps.setString(6, q.getOptionB());
            ps.setString(7, q.getOptionC());
            ps.setString(8, q.getOptionD());
            ps.setString(9, q.getCorrectAnswer());
            ps.setString(10, q.getExplanation());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. VIEW ALL QUESTIONS
    public List<Question> getAllQuestions() {

        List<Question> questions = new ArrayList<>();

        String sql = "SELECT * FROM questions";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Question q = new Question();

                q.setQuestionId(rs.getInt("question_id"));
                q.setSkill(rs.getString("skill"));
                q.setTopic(rs.getString("topic"));
                q.setDifficulty(rs.getString("difficulty"));
                q.setQuestion(rs.getString("question"));
                q.setOptionA(rs.getString("option_a"));
                q.setOptionB(rs.getString("option_b"));
                q.setOptionC(rs.getString("option_c"));
                q.setOptionD(rs.getString("option_d"));
                q.setCorrectAnswer(rs.getString("correct_answer"));
                q.setExplanation(rs.getString("explanation"));

                questions.add(q);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }

    // 3. SEARCH QUESTION BY ID
    public Question getQuestionById(int id) {

        String sql = "SELECT * FROM questions WHERE question_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Question q = new Question();

                q.setQuestionId(rs.getInt("question_id"));
                q.setSkill(rs.getString("skill"));
                q.setTopic(rs.getString("topic"));
                q.setDifficulty(rs.getString("difficulty"));
                q.setQuestion(rs.getString("question"));
                q.setOptionA(rs.getString("option_a"));
                q.setOptionB(rs.getString("option_b"));
                q.setOptionC(rs.getString("option_c"));
                q.setOptionD(rs.getString("option_d"));
                q.setCorrectAnswer(rs.getString("correct_answer"));
                q.setExplanation(rs.getString("explanation"));

                return q;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // 4. UPDATE QUESTION
    public boolean updateQuestion(Question q) {

        String sql = "UPDATE questions SET "
                + "skill = ?, topic = ?, difficulty = ?, question = ?, "
                + "option_a = ?, option_b = ?, option_c = ?, option_d = ?, "
                + "correct_answer = ?, explanation = ? "
                + "WHERE question_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, q.getSkill());
            ps.setString(2, q.getTopic());
            ps.setString(3, q.getDifficulty());
            ps.setString(4, q.getQuestion());
            ps.setString(5, q.getOptionA());
            ps.setString(6, q.getOptionB());
            ps.setString(7, q.getOptionC());
            ps.setString(8, q.getOptionD());
            ps.setString(9, q.getCorrectAnswer());
            ps.setString(10, q.getExplanation());
            ps.setInt(11, q.getQuestionId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. DELETE QUESTION
    public boolean deleteQuestion(int id) {

        String sql = "DELETE FROM questions WHERE question_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Question> getQuestionsForAssessment(
            String skill,
            String topic,
            String difficulty,
            int limit) {

        List<Question> questions = new ArrayList<>();

        String sql = "SELECT * FROM questions "
                + "WHERE LOWER(skill) = LOWER(?) "
                + "AND LOWER(topic) = LOWER(?) "
                + "AND LOWER(difficulty) = LOWER(?) "
                + "ORDER BY RAND() "
                + "LIMIT ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, skill);
            ps.setString(2, topic);
            ps.setString(3, difficulty);
            ps.setInt(4, limit);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Question q = new Question();

                q.setQuestionId(rs.getInt("question_id"));
                q.setSkill(rs.getString("skill"));
                q.setTopic(rs.getString("topic"));
                q.setDifficulty(rs.getString("difficulty"));
                q.setQuestion(rs.getString("question"));
                q.setOptionA(rs.getString("option_a"));
                q.setOptionB(rs.getString("option_b"));
                q.setOptionC(rs.getString("option_c"));
                q.setOptionD(rs.getString("option_d"));
                q.setCorrectAnswer(rs.getString("correct_answer"));
                q.setExplanation(rs.getString("explanation"));

                questions.add(q);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }
    public List<Question> getPracticeQuestions(String skill, String topic, int limit) {

        List<Question> questions = new ArrayList<>();

        String sql = "SELECT * FROM questions "
                   + "WHERE skill = ? "
                   + "AND topic = ? "
                   + "ORDER BY RAND() "
                   + "LIMIT ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, skill);
            ps.setString(2, topic);
            ps.setInt(3, limit);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Question q = new Question();

                q.setQuestionId(rs.getInt("question_id"));
                q.setSkill(rs.getString("skill"));
                q.setTopic(rs.getString("topic"));
                q.setDifficulty(rs.getString("difficulty"));
                q.setQuestion(rs.getString("question"));
                q.setOptionA(rs.getString("option_a"));
                q.setOptionB(rs.getString("option_b"));
                q.setOptionC(rs.getString("option_c"));
                q.setOptionD(rs.getString("option_d"));
                q.setCorrectAnswer(rs.getString("correct_answer"));
                q.setExplanation(rs.getString("explanation"));

                questions.add(q);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }
}