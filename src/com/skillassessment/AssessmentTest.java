package com.skillassessment;

import java.util.List;
import java.util.Scanner;
import com.skillassessment.dao.ResultDAO;
import com.skillassessment.model.Result;

import com.skillassessment.dao.QuestionDAO;
import com.skillassessment.model.Question;

public class AssessmentTest {

	public static void startAssessment(int userId, Scanner sc) {
    	

        

        System.out.println("===== START ASSESSMENT =====");

        System.out.print("Enter Skill: ");
        String skill = sc.nextLine();

        System.out.print("Enter Topic: ");
        String topic = sc.nextLine();

        System.out.print("Enter Difficulty: ");
        String difficulty = sc.nextLine();

        System.out.print("Enter Number of Questions: ");
        int numberOfQuestions = sc.nextInt();
        sc.nextLine();

        QuestionDAO questionDAO = new QuestionDAO();

        List<Question> questions =
                questionDAO.getQuestionsForAssessment(
                        skill,
                        topic,
                        difficulty,
                        numberOfQuestions
                );

        if (questions.isEmpty()) {

            System.out.println(
                    "No questions available for the selected criteria."
            );

            sc.close();
            return;
        }

        int correct = 0;
        int wrong = 0;

        System.out.println("\n===== ASSESSMENT STARTED =====");

        for (int i = 0; i < questions.size(); i++) {

            Question q = questions.get(i);

            System.out.println("\nQuestion " + (i + 1));
            System.out.println(q.getQuestion());

            System.out.println("A. " + q.getOptionA());
            System.out.println("B. " + q.getOptionB());
            System.out.println("C. " + q.getOptionC());
            System.out.println("D. " + q.getOptionD());

            System.out.print("Your Answer: ");
            String answer = sc.nextLine();

            if (answer.equalsIgnoreCase(q.getCorrectAnswer())) {

                correct++;
                System.out.println("Correct!");

            } else {

                wrong++;
                System.out.println("Wrong!");
                System.out.println(
                        "Correct Answer: " + q.getCorrectAnswer()
                );
            }
        }

        int total = questions.size();

        double score = ((double) correct / total) * 100;
        Result result = new Result(
                userId,
                skill,
                topic,
                difficulty,
                total,
                correct,
                wrong,
                score
        );
        result.setAttemptType("ASSESSMENT");

        ResultDAO resultDAO = new ResultDAO();

        boolean saved = resultDAO.saveResult(result);

        if (saved) {
            System.out.println("Result Saved Successfully!");
        } else {
            System.out.println("Failed to Save Result!");
        }

        System.out.println("\n===== ASSESSMENT RESULT =====");
        System.out.println("Total Questions: " + total);
        System.out.println("Correct: " + correct);
        System.out.println("Wrong: " + wrong);
        System.out.println("Score: " + score + "%");

        
    }
}