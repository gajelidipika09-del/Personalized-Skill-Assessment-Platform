package com.skillassessment.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.skillassessment.dao.QuestionDAO;
import com.skillassessment.dao.ResultDAO;
import com.skillassessment.model.Question;
import com.skillassessment.model.Result;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/practice")
public class PracticeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private Gson gson = new Gson();

    // ==========================================
    // GET - LOAD PRACTICE QUESTIONS
    // ==========================================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        try {

            String skill = request.getParameter("skill");
            String topic = request.getParameter("topic");
            String countParameter = request.getParameter("count");

            System.out.println("===== PRACTICE REQUEST =====");
            System.out.println("Skill: " + skill);
            System.out.println("Topic: " + topic);
            System.out.println("Count: " + countParameter);

            if (skill == null || skill.trim().isEmpty()
                    || topic == null || topic.trim().isEmpty()
                    || countParameter == null) {

                out.print(
                    "{\"success\":false,"
                    + "\"message\":\"Skill, topic and count are required.\"}"
                );

                return;
            }

            int count = Integer.parseInt(countParameter);

            QuestionDAO questionDAO = new QuestionDAO();

            List<Question> questions =
                    questionDAO.getPracticeQuestions(
                            skill,
                            topic,
                            count
                    );

            System.out.println(
                "Questions found: " + questions.size()
            );

            if (questions.isEmpty()) {

                out.print(
                    "{\"success\":false,"
                    + "\"message\":\"No practice questions available for this topic.\"}"
                );

                return;
            }

            String jsonQuestions =
                    gson.toJson(questions);

            out.print(
                "{\"success\":true,"
                + "\"questions\":"
                + jsonQuestions
                + "}"
            );

        }
        catch (NumberFormatException e) {

            out.print(
                "{\"success\":false,"
                + "\"message\":\"Invalid question count.\"}"
            );

        }
        catch (Exception e) {

            e.printStackTrace();

            out.print(
                "{\"success\":false,"
                + "\"message\":\"Server error while loading practice questions.\"}"
            );
        }
    }


    // ==========================================
    // POST - SUBMIT PRACTICE
    // ==========================================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        try {

            // Read JSON sent by JavaScript
            StringBuilder jsonBuilder = new StringBuilder();

            BufferedReader reader =
                    request.getReader();

            String line;

            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            String json = jsonBuilder.toString();

            System.out.println(
                "===== PRACTICE SUBMISSION ====="
            );

            System.out.println(
                "Received: " + json
            );


            // Convert JSON to object
            JsonObject requestData =
                    gson.fromJson(
                        json,
                        JsonObject.class
                    );


            // Get user ID
            int userId =
                    requestData
                    .get("userId")
                    .getAsInt();


            // Get skill and topic
            String skill =
                    requestData
                    .get("skill")
                    .getAsString();

            String topic =
                    requestData
                    .get("topic")
                    .getAsString();


            // Get answers
            JsonObject answersObject =
                    requestData
                    .getAsJsonObject("answers");


            Map<String, String> answers =
                    gson.fromJson(
                        answersObject,
                        new TypeToken<Map<String, String>>() {}
                    );


            QuestionDAO questionDAO =
                    new QuestionDAO();


            int correct = 0;
            int wrong = 0;


            // Check every submitted answer
            for (Map.Entry<String, String> entry
                    : answers.entrySet()) {

                int questionId =
                        Integer.parseInt(
                            entry.getKey()
                        );

                String studentAnswer =
                        entry.getValue();


                Question question =
                        questionDAO.getQuestionById(
                            questionId
                        );


                if (question != null) {

                    String correctAnswer =
                            question.getCorrectAnswer();


                    if (studentAnswer != null
                            && studentAnswer.equalsIgnoreCase(
                                correctAnswer)) {

                        correct++;

                    } else {

                        wrong++;
                    }
                }
            }


            int total = answers.size();


            // Calculate score
            double score = 0;

            if (total > 0) {

                score =
                    ((double) correct / total) * 100;
            }


            System.out.println(
                "Total: " + total
            );

            System.out.println(
                "Correct: " + correct
            );

            System.out.println(
                "Wrong: " + wrong
            );

            System.out.println(
                "Score: " + score
            );


            // Create Result
            Result result =
                    new Result(
                        userId,
                        skill,
                        topic,
                        "Mixed",
                        total,
                        correct,
                        wrong,
                        score
                    );


            // Mark this as PRACTICE
            result.setAttemptType(
                "PRACTICE"
            );


            // Save result
            ResultDAO resultDAO =
                    new ResultDAO();

            boolean saved =
                    resultDAO.saveResult(
                        result
                    );


            // Send response
            out.print(
                "{"
                + "\"success\":true,"
                + "\"saved\":" + saved + ","
                + "\"total\":" + total + ","
                + "\"correct\":" + correct + ","
                + "\"wrong\":" + wrong + ","
                + "\"score\":" + score
                + "}"
            );

        }
        catch (Exception e) {

            e.printStackTrace();

            out.print(
                "{"
                + "\"success\":false,"
                + "\"message\":\"Error while submitting practice.\""
                + "}"
            );
        }
    }
}