package com.skillassessment.servlet;
import com.skillassessment.dao.StreakDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.skillassessment.DBConnection;

@WebServlet("/api/progress")
public class ProgressServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonObject result = new JsonObject();

        try {

            String userIdValue = request.getParameter("userId");

            if (userIdValue == null) {

                result.addProperty("success", false);
                result.addProperty("message", "User ID is missing.");

                response.getWriter().print(result.toString());
                return;
            }

            int userId = Integer.parseInt(userIdValue);

            try (Connection con = DBConnection.getConnection()) {

                // ==========================================
                // 1. STUDENT NAME
                // ==========================================

                String nameSql =
                        "SELECT name FROM users WHERE user_id = ?";

                try (PreparedStatement ps =
                        con.prepareStatement(nameSql)) {

                    ps.setInt(1, userId);

                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        result.addProperty(
                                "studentName",
                                rs.getString("name")
                        );
                    }
                }

                // ==========================================
                // 2. OVERALL SCORE
                // ==========================================

                String scoreSql =
                        "SELECT AVG(score) AS overall_score " +
                        "FROM results " +
                        "WHERE user_id = ? " +
                        "AND attempt_type = 'ASSESSMENT'";

                try (PreparedStatement ps =
                        con.prepareStatement(scoreSql)) {

                    ps.setInt(1, userId);

                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {

                        double score = rs.getDouble("overall_score");

                        result.addProperty(
                                "overallScore",
                                Math.round(score * 100.0) / 100.0
                        );
                    }
                }

                // ==========================================
                // 3. ASSESSMENT HISTORY
                // ==========================================

                JsonArray history = new JsonArray();

                String historySql =
                        "SELECT skill, topic, difficulty, score, " +
                        "test_date " +
                        "FROM results " +
                        "WHERE user_id = ? " +
                        "AND attempt_type = 'ASSESSMENT' " +
                        "ORDER BY test_date DESC";

                try (PreparedStatement ps =
                        con.prepareStatement(historySql)) {

                    ps.setInt(1, userId);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {

                        JsonObject item = new JsonObject();

                        item.addProperty(
                                "skill",
                                rs.getString("skill")
                        );

                        item.addProperty(
                                "topic",
                                rs.getString("topic")
                        );

                        item.addProperty(
                                "difficulty",
                                rs.getString("difficulty")
                        );

                        item.addProperty(
                                "score",
                                rs.getDouble("score")
                        );

                        item.addProperty(
                                "date",
                                rs.getTimestamp("test_date").toString()
                        );

                        history.add(item);
                    }
                }

                result.add("assessmentHistory", history);

                // ==========================================
                // 4. SKILL-WISE PERFORMANCE
                // ==========================================

                JsonArray skills = new JsonArray();

                String skillSql =
                        "SELECT skill, AVG(score) AS average_score " +
                        "FROM results " +
                        "WHERE user_id = ? " +
                        "AND attempt_type = 'ASSESSMENT' " +
                        "GROUP BY skill " +
                        "ORDER BY average_score DESC";

                try (PreparedStatement ps =
                        con.prepareStatement(skillSql)) {

                    ps.setInt(1, userId);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {

                        JsonObject item = new JsonObject();

                        double score =
                                rs.getDouble("average_score");

                        item.addProperty(
                                "skill",
                                rs.getString("skill")
                        );

                        item.addProperty(
                                "score",
                                Math.round(score * 100.0) / 100.0
                        );

                        skills.add(item);
                    }
                }

                result.add("skillPerformance", skills);

                // ==========================================
                // 5. SKILL / TOPIC ANALYSIS
                // ==========================================

                JsonArray analysis = new JsonArray();

                String analysisSql =
                        "SELECT skill, topic, " +
                        "AVG(score) AS average_score " +
                        "FROM results " +
                        "WHERE user_id = ? " +
                        "AND attempt_type = 'ASSESSMENT' " +
                        "GROUP BY skill, topic " +
                        "ORDER BY average_score ASC";

                try (PreparedStatement ps =
                        con.prepareStatement(analysisSql)) {

                    ps.setInt(1, userId);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {

                        double score =
                                rs.getDouble("average_score");

                        String status;

                        if (score < 50) {
                            status = "WEAK";
                        } else if (score <= 75) {
                            status = "NEEDS PRACTICE";
                        } else {
                            status = "STRONG";
                        }

                        JsonObject item = new JsonObject();

                        item.addProperty(
                                "skill",
                                rs.getString("skill")
                        );

                        item.addProperty(
                                "topic",
                                rs.getString("topic")
                        );

                        item.addProperty(
                                "score",
                                Math.round(score * 100.0) / 100.0
                        );

                        item.addProperty(
                                "status",
                                status
                        );

                        analysis.add(item);
                    }
                }

                result.add("skillAnalysis", analysis);
             // ==========================================
             // 6. RECOMMENDATIONS
             // ==========================================

             JsonArray recommendations = new JsonArray();

             String recommendationSql =
                     "SELECT skill, topic, AVG(score) AS average_score " +
                     "FROM results " +
                     "WHERE user_id = ? " +
                     "AND attempt_type = 'ASSESSMENT' " +
                     "GROUP BY skill, topic " +
                     "ORDER BY average_score ASC";

             try (PreparedStatement ps =
                     con.prepareStatement(recommendationSql)) {

                 ps.setInt(1, userId);

                 ResultSet rs = ps.executeQuery();

                 com.skillassessment.service.RecommendationService service =
                         new com.skillassessment.service.RecommendationService();

                 while (rs.next()) {

                     double score = rs.getDouble("average_score");

                     JsonObject item = new JsonObject();

                     item.addProperty(
                             "skill",
                             rs.getString("skill")
                     );

                     item.addProperty(
                             "topic",
                             rs.getString("topic")
                     );

                     item.addProperty(
                             "score",
                             Math.round(score * 100.0) / 100.0
                     );

                     item.addProperty(
                             "recommendation",
                             service.getRecommendation(score)
                     );

                     recommendations.add(item);
                 }
             }

             result.add("recommendations", recommendations);
          // ==========================================
          // 7. STUDENT STREAK
          // ==========================================

          StreakDAO streakDAO = new StreakDAO();

          int currentStreak = streakDAO.getCurrentStreak(userId);
          int longestStreak = streakDAO.getLongestStreak(userId);

          result.addProperty("currentStreak", currentStreak);
          result.addProperty("longestStreak", longestStreak);

                result.addProperty("success", true);

            }

        } catch (Exception e) {

            e.printStackTrace();

            result.addProperty("success", false);
            result.addProperty(
                    "message",
                    "Unable to load student progress."
            );
        }

        Gson gson = new Gson();

        response.getWriter().print(
                gson.toJson(result)
        );
    }
    
}