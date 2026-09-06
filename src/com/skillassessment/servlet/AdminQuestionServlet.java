package com.skillassessment.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skillassessment.dao.QuestionDAO;
import com.skillassessment.model.Question;

@WebServlet("/api/admin/questions")
public class AdminQuestionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private QuestionDAO questionDAO = new QuestionDAO();

    // =====================================================
    // GET - LOAD ALL QUESTIONS
    // =====================================================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        try {

            List<Question> questions = questionDAO.getAllQuestions();

            StringBuilder json = new StringBuilder();

            json.append("{\"success\":true,\"questions\":[");

            for (int i = 0; i < questions.size(); i++) {

                Question q = questions.get(i);

                json.append("{")
                    .append("\"questionId\":")
                    .append(q.getQuestionId())
                    .append(",")

                    .append("\"skill\":\"")
                    .append(escapeJson(q.getSkill()))
                    .append("\",")

                    .append("\"topic\":\"")
                    .append(escapeJson(q.getTopic()))
                    .append("\",")

                    .append("\"difficulty\":\"")
                    .append(escapeJson(q.getDifficulty()))
                    .append("\",")

                    .append("\"question\":\"")
                    .append(escapeJson(q.getQuestion()))
                    .append("\",")

                    .append("\"optionA\":\"")
                    .append(escapeJson(q.getOptionA()))
                    .append("\",")

                    .append("\"optionB\":\"")
                    .append(escapeJson(q.getOptionB()))
                    .append("\",")

                    .append("\"optionC\":\"")
                    .append(escapeJson(q.getOptionC()))
                    .append("\",")

                    .append("\"optionD\":\"")
                    .append(escapeJson(q.getOptionD()))
                    .append("\",")

                    .append("\"correctAnswer\":\"")
                    .append(escapeJson(q.getCorrectAnswer()))
                    .append("\",")

                    .append("\"explanation\":\"")
                    .append(escapeJson(q.getExplanation()))
                    .append("\"")

                    .append("}");

                if (i < questions.size() - 1) {
                    json.append(",");
                }
            }

            json.append("]}");

            out.print(json.toString());

        } catch (Exception e) {

            e.printStackTrace();

            out.print(
                "{\"success\":false,\"message\":\"Unable to load questions.\"}"
            );
        }
    }


    // =====================================================
    // POST - ADD / UPDATE / DELETE QUESTION
    // =====================================================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        try {

            String action = request.getParameter("action");

            // =================================================
            // DELETE
            // =================================================

            if ("delete".equalsIgnoreCase(action)) {

                String idParameter = request.getParameter("questionId");

                if (isEmpty(idParameter)) {

                    out.print(
                        "{\"success\":false,\"message\":\"Question ID is required.\"}"
                    );

                    return;
                }

                int questionId = Integer.parseInt(idParameter);

                boolean deleted =
                        questionDAO.deleteQuestion(questionId);

                if (deleted) {

                    out.print(
                        "{\"success\":true,\"message\":\"Question deleted successfully.\"}"
                    );

                } else {

                    out.print(
                        "{\"success\":false,\"message\":\"Failed to delete question.\"}"
                    );
                }

                return;
            }


            // =================================================
            // UPDATE
            // =================================================

            if ("update".equalsIgnoreCase(action)) {

                String idParameter =
                        request.getParameter("questionId");

                if (isEmpty(idParameter)) {

                    out.print(
                        "{\"success\":false,\"message\":\"Question ID is required.\"}"
                    );

                    return;
                }

                int questionId =
                        Integer.parseInt(idParameter);

                String skill =
                        request.getParameter("skill");

                String topic =
                        request.getParameter("topic");

                String difficulty =
                        request.getParameter("difficulty");

                String questionText =
                        request.getParameter("question");

                String optionA =
                        request.getParameter("optionA");

                String optionB =
                        request.getParameter("optionB");

                String optionC =
                        request.getParameter("optionC");

                String optionD =
                        request.getParameter("optionD");

                String correctAnswer =
                        request.getParameter("correctAnswer");

                String explanation =
                        request.getParameter("explanation");


                // VALIDATION

                if (isEmpty(skill)
                        || isEmpty(topic)
                        || isEmpty(difficulty)
                        || isEmpty(questionText)
                        || isEmpty(optionA)
                        || isEmpty(optionB)
                        || isEmpty(optionC)
                        || isEmpty(optionD)
                        || isEmpty(correctAnswer)) {

                    out.print(
                        "{\"success\":false,\"message\":\"Please fill all required fields.\"}"
                    );

                    return;
                }


                Question q = new Question();

                q.setQuestionId(questionId);
                q.setSkill(skill);
                q.setTopic(topic);
                q.setDifficulty(difficulty);
                q.setQuestion(questionText);
                q.setOptionA(optionA);
                q.setOptionB(optionB);
                q.setOptionC(optionC);
                q.setOptionD(optionD);
                q.setCorrectAnswer(correctAnswer);
                q.setExplanation(
                    explanation == null ? "" : explanation
                );


                boolean updated =
                        questionDAO.updateQuestion(q);


                if (updated) {

                    out.print(
                        "{\"success\":true,\"message\":\"Question updated successfully.\"}"
                    );

                } else {

                    out.print(
                        "{\"success\":false,\"message\":\"Failed to update question.\"}"
                    );
                }

                return;
            }


            // =================================================
            // ADD QUESTION
            // =================================================

            String skill =
                    request.getParameter("skill");

            String topic =
                    request.getParameter("topic");

            String difficulty =
                    request.getParameter("difficulty");

            String questionText =
                    request.getParameter("question");

            String optionA =
                    request.getParameter("optionA");

            String optionB =
                    request.getParameter("optionB");

            String optionC =
                    request.getParameter("optionC");

            String optionD =
                    request.getParameter("optionD");

            String correctAnswer =
                    request.getParameter("correctAnswer");

            String explanation =
                    request.getParameter("explanation");


            // VALIDATION

            if (isEmpty(skill)
                    || isEmpty(topic)
                    || isEmpty(difficulty)
                    || isEmpty(questionText)
                    || isEmpty(optionA)
                    || isEmpty(optionB)
                    || isEmpty(optionC)
                    || isEmpty(optionD)
                    || isEmpty(correctAnswer)) {

                out.print(
                    "{\"success\":false,\"message\":\"Please fill all required fields.\"}"
                );

                return;
            }


            Question q = new Question();

            q.setSkill(skill);
            q.setTopic(topic);
            q.setDifficulty(difficulty);
            q.setQuestion(questionText);
            q.setOptionA(optionA);
            q.setOptionB(optionB);
            q.setOptionC(optionC);
            q.setOptionD(optionD);
            q.setCorrectAnswer(correctAnswer);
            q.setExplanation(
                explanation == null ? "" : explanation
            );


            boolean added =
                    questionDAO.addQuestion(q);


            if (added) {

                out.print(
                    "{\"success\":true,\"message\":\"Question added successfully.\"}"
                );

            } else {

                out.print(
                    "{\"success\":false,\"message\":\"Failed to add question.\"}"
                );
            }

        } catch (NumberFormatException e) {

            e.printStackTrace();

            out.print(
                "{\"success\":false,\"message\":\"Invalid question ID.\"}"
            );

        } catch (Exception e) {

            e.printStackTrace();

            out.print(
                "{\"success\":false,\"message\":\"Server error while processing question.\"}"
            );
        }
    }


    // =====================================================
    // CHECK EMPTY
    // =====================================================

    private boolean isEmpty(String value) {

        return value == null
                || value.trim().isEmpty();
    }


    // =====================================================
    // JSON ESCAPE
    // =====================================================

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