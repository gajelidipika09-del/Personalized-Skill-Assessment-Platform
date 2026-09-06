package com.skillassessment;

import java.util.List;
import java.util.Scanner;
import com.skillassessment.dao.ResultDAO;
import com.skillassessment.model.Result;

import com.skillassessment.dao.QuestionDAO;
import com.skillassessment.model.Question;

public class PracticeTest {

	public static void startPractice(int userId, Scanner sc) {

        
        System.out.println("===== PERSONALIZED PRACTICE =====");

        System.out.print("Enter Skill: ");
        String skill = sc.nextLine();

        System.out.print("Enter Weak Topic: ");
        String topic = sc.nextLine();

        System.out.print("How many questions? ");
        int numberOfQuestions = sc.nextInt();
        sc.nextLine();

        QuestionDAO questionDAO = new QuestionDAO();

        List<Question> questions =
                questionDAO.getPracticeQuestions(
                        skill,
                        topic,
                        numberOfQuestions
                );

        if (questions.isEmpty()) {

            System.out.println(
                    "No practice questions available for this topic."
            );

            return;
        }

        if (questions.size() < numberOfQuestions) {

            System.out.println(
                    "\nOnly " + questions.size()
                    + " question"
                    + (questions.size() > 1 ? "s" : "")
                    + " is available."
            );

            System.out.println(
                    "Starting practice with "
                    + questions.size()
                    + " question"
                    + (questions.size() > 1 ? "s." : ".")
            );
        }

        int correct = 0;
        int wrong = 0;

        System.out.println("\n===== PRACTICE STARTED =====");

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

                System.out.println(
                        "Explanation: " + q.getExplanation()
                );
            }
        }

        int total = questions.size();

        double score = ((double) correct / total) * 100;
        Result result = new Result(
                userId,
                skill,
                topic,
                "Mixed",
                total,
                correct,
                wrong,
                score
        );

        result.setAttemptType("PRACTICE");

        ResultDAO resultDAO = new ResultDAO();

        boolean saved = resultDAO.saveResult(result);

        if (saved) {
            System.out.println("Practice Result Saved Successfully!");
        }

        System.out.println("\n===== PRACTICE RESULT =====");
        System.out.println("Total Questions: " + total);
        System.out.println("Correct: " + correct);
        System.out.println("Wrong: " + wrong);
        System.out.println("Practice Score: " + score + "%");

        
    }
	public static void startRecommendedPractice(
	        int userId,
	        Scanner sc,
	        String skill,
	        String topic,
	        int numberOfQuestions) {

	    System.out.println("\n===== RECOMMENDED PRACTICE =====");

	    System.out.println("Skill: " + skill);
	    System.out.println("Topic: " + topic);
	    System.out.println("Recommended Questions: " + numberOfQuestions);

	    QuestionDAO questionDAO = new QuestionDAO();

	    List<Question> questions =
	            questionDAO.getPracticeQuestions(
	                    skill,
	                    topic,
	                    numberOfQuestions
	            );

	    if (questions.isEmpty()) {

	        System.out.println(
	                "No practice questions available for this topic."
	        );

	        return;
	    }

	    if (questions.size() < numberOfQuestions) {

	        System.out.println(
	                "\nOnly " + questions.size()
	                + " question"
	                + (questions.size() > 1 ? "s" : "")
	                + " is available."
	        );

	        System.out.println(
	                "Starting practice with "
	                + questions.size()
	                + " question"
	                + (questions.size() > 1 ? "s." : ".")
	        );
	    }

	    int correct = 0;
	    int wrong = 0;

	    System.out.println("\n===== RECOMMENDED PRACTICE STARTED =====");

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

	            System.out.println(
	                    "Explanation: " + q.getExplanation()
	            );
	        }
	    }

	    int total = questions.size();

	    double score = ((double) correct / total) * 100;

	    Result result = new Result(
	            userId,
	            skill,
	            topic,
	            "Mixed",
	            total,
	            correct,
	            wrong,
	            score
	    );

	    result.setAttemptType("PRACTICE");

	    ResultDAO resultDAO = new ResultDAO();

	    boolean saved = resultDAO.saveResult(result);

	    if (saved) {
	        System.out.println(
	                "Practice Result Saved Successfully!"
	        );
	    }

	    System.out.println("\n===== PRACTICE RESULT =====");
	    System.out.println("Skill: " + skill);
	    System.out.println("Topic: " + topic);
	    System.out.println("Total Questions: " + total);
	    System.out.println("Correct: " + correct);
	    System.out.println("Wrong: " + wrong);
	    System.out.println("Practice Score: " + score + "%");
	}
}