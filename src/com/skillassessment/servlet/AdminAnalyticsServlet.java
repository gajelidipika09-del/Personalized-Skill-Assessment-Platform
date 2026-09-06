package com.skillassessment.servlet;



import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.skillassessment.dao.AdminAnalyticsDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/admin/analytics")
public class AdminAnalyticsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private AdminAnalyticsDAO analyticsDAO;

    @Override
    public void init() throws ServletException {
        analyticsDAO = new AdminAnalyticsDAO();
    }


    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        try {

            int totalStudents =
                    analyticsDAO.getTotalStudents();

            int totalAssessments =
                    analyticsDAO.getTotalAssessments();

            int totalPractice =
                    analyticsDAO.getTotalPractice();

            int totalQuestions =
                    analyticsDAO.getTotalQuestions();

            double averageScore =
                    analyticsDAO.getAverageAssessmentScore();

            List<String[]> skillPerformance =
                    analyticsDAO.getSkillPerformance();

            List<String[]> weakTopics =
                    analyticsDAO.getWeakTopics();


            StringBuilder json = new StringBuilder();

            json.append("{");

            // Summary
            json.append("\"success\":true,");
            json.append("\"totalStudents\":").append(totalStudents).append(",");
            json.append("\"totalAssessments\":").append(totalAssessments).append(",");
            json.append("\"totalPractice\":").append(totalPractice).append(",");
            json.append("\"totalQuestions\":").append(totalQuestions).append(",");
            json.append("\"averageScore\":")
                .append(String.format("%.2f", averageScore))
                .append(",");


            // Skill Performance
            json.append("\"skillPerformance\":[");

            for (int i = 0; i < skillPerformance.size(); i++) {

                String[] item = skillPerformance.get(i);

                json.append("{");

                json.append("\"skill\":\"")
                    .append(escapeJson(item[0]))
                    .append("\",");

                json.append("\"topic\":\"")
                    .append(escapeJson(item[1]))
                    .append("\",");

                json.append("\"score\":")
                    .append(item[2]);

                json.append("}");

                if (i < skillPerformance.size() - 1) {
                    json.append(",");
                }
            }

            json.append("],");


            // Weak Topics
            json.append("\"weakTopics\":[");

            for (int i = 0; i < weakTopics.size(); i++) {

                String[] item = weakTopics.get(i);

                json.append("{");

                json.append("\"skill\":\"")
                    .append(escapeJson(item[0]))
                    .append("\",");

                json.append("\"topic\":\"")
                    .append(escapeJson(item[1]))
                    .append("\",");

                json.append("\"score\":")
                    .append(item[2])
                    .append(",");

                json.append("\"status\":\"")
                    .append(escapeJson(item[3]))
                    .append("\"");

                json.append("}");

                if (i < weakTopics.size() - 1) {
                    json.append(",");
                }
            }

            json.append("]");

            json.append("}");

            out.print(json.toString());

        } catch (Exception e) {

            e.printStackTrace();

            out.print(
                "{\"success\":false,\"message\":\"Analytics loading failed\"}"
            );
        }
    }


    private String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}

