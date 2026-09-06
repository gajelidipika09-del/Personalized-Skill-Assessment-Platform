package com.skillassessment.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;
import com.skillassessment.dao.QuestionDAO;
import com.skillassessment.model.Question;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/assessment")
public class AssessmentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private QuestionDAO questionDAO = new QuestionDAO();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String skill = request.getParameter("skill");
        String topic = request.getParameter("topic");
        String difficulty = request.getParameter("difficulty");
        String limitValue = request.getParameter("limit");

        PrintWriter out = response.getWriter();

        if (skill == null || topic == null ||
            difficulty == null || limitValue == null) {

            out.print("{\"success\":false,\"message\":\"Missing assessment details.\"}");
            return;
        }

        try {

            int limit = Integer.parseInt(limitValue);

            List<Question> questions =
                    questionDAO.getQuestionsForAssessment(
                            skill,
                            topic,
                            difficulty,
                            limit
                    );

            if (questions.isEmpty()) {

                out.print(
                    "{\"success\":false,\"message\":\"No questions available for the selected criteria.\"}"
                );

                return;
            }

            Gson gson = new Gson();

            out.print(
                "{\"success\":true,\"questions\":"
                + gson.toJson(questions)
                + "}"
            );

        } catch (Exception e) {

            e.printStackTrace();

            out.print(
                "{\"success\":false,\"message\":\"Server error while loading assessment.\"}"
            );
        }
    }
}