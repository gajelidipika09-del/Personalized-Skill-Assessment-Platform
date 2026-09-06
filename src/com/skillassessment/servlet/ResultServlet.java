package com.skillassessment.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.skillassessment.dao.ResultDAO;
import com.skillassessment.model.Result;

@WebServlet("/api/result")
public class ResultServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ResultDAO resultDAO = new ResultDAO();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {

            // Get data from JavaScript
            int userId = Integer.parseInt(
                    request.getParameter("userId"));

            String skill = request.getParameter("skill");
            String topic = request.getParameter("topic");
            String difficulty = request.getParameter("difficulty");

            int totalQuestions = Integer.parseInt(
                    request.getParameter("totalQuestions"));

            int correctAnswers = Integer.parseInt(
                    request.getParameter("correctAnswers"));

            int wrongAnswers = Integer.parseInt(
                    request.getParameter("wrongAnswers"));

            double score = Double.parseDouble(
                    request.getParameter("score"));

            String attemptType = request.getParameter("attemptType");

            // Create Result object
            Result result = new Result();

            result.setUserId(userId);
            result.setSkill(skill);
            result.setTopic(topic);
            result.setDifficulty(difficulty);
            result.setTotalQuestions(totalQuestions);
            result.setCorrectAnswers(correctAnswers);
            result.setWrongAnswers(wrongAnswers);
            result.setScore(score);
            result.setAttemptType(attemptType);

            // Save to database
            boolean saved = resultDAO.saveResult(result);

            Gson gson = new Gson();

            if (saved) {

                response.getWriter().print(
                    gson.toJson(
                        new ResponseMessage(
                            true,
                            "Assessment result saved successfully."
                        )
                    )
                );

            } else {

                response.getWriter().print(
                    gson.toJson(
                        new ResponseMessage(
                            false,
                            "Failed to save assessment result."
                        )
                    )
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().print(
                "{\"success\":false,\"message\":\"Server error while saving result.\"}"
            );
        }
    }

    // Small response class
    private static class ResponseMessage {

        boolean success;
        String message;

        ResponseMessage(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}