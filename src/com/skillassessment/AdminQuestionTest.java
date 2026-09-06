package com.skillassessment;

import java.util.List;
import java.util.Scanner;

import com.skillassessment.dao.QuestionDAO;
import com.skillassessment.model.Question;

public class AdminQuestionTest {

	static QuestionDAO questionDAO = new QuestionDAO();
	static Scanner sc;
    public static void showMenu(Scanner scanner) {
    	sc=scanner;

        while (true) {

            System.out.println("\n===== ADMIN QUESTION BANK =====");
            System.out.println("1. Add Question");
            System.out.println("2. View Questions");
            System.out.println("3. Search Question");
            System.out.println("4. Edit Question");
            System.out.println("5. Delete Question");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    addQuestion();
                    break;

                case 2:
                    viewQuestions();
                    break;

                case 3:
                    searchQuestion();
                    break;

                case 4:
                    editQuestion();
                    break;

                case 5:
                    deleteQuestion();
                    break;

                case 6:
                    System.out.println("Returning to Admin Dashboard...");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // ADD QUESTION
    public static void addQuestion() {

        System.out.println("\n===== ADD QUESTION =====");

        System.out.print("Enter Skill: ");
        String skill = sc.nextLine();

        System.out.print("Enter Topic: ");
        String topic = sc.nextLine();

        System.out.print("Enter Difficulty: ");
        String difficulty = sc.nextLine();

        System.out.print("Enter Question: ");
        String question = sc.nextLine();

        System.out.print("Enter Option A: ");
        String optionA = sc.nextLine();

        System.out.print("Enter Option B: ");
        String optionB = sc.nextLine();

        System.out.print("Enter Option C: ");
        String optionC = sc.nextLine();

        System.out.print("Enter Option D: ");
        String optionD = sc.nextLine();

        System.out.print("Enter Correct Answer (A/B/C/D): ");
        String correctAnswer = sc.nextLine();

        System.out.print("Enter Explanation: ");
        String explanation = sc.nextLine();

        Question q = new Question(
                skill,
                topic,
                difficulty,
                question,
                optionA,
                optionB,
                optionC,
                optionD,
                correctAnswer,
                explanation
        );

        boolean result = questionDAO.addQuestion(q);

        if (result) {
            System.out.println("Question Added Successfully!");
        } else {
            System.out.println("Failed to Add Question!");
        }
    }

    // VIEW QUESTIONS
    public static void viewQuestions() {

        System.out.println("\n===== ALL QUESTIONS =====");

        List<Question> questions = questionDAO.getAllQuestions();

        if (questions.isEmpty()) {
            System.out.println("No questions found.");
            return;
        }

        for (Question q : questions) {
            System.out.println(q);
            System.out.println("----------------------------");
        }
    }

    // SEARCH QUESTION
    public static void searchQuestion() {

        System.out.println("\n===== SEARCH QUESTION =====");

        System.out.print("Enter Question ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        Question q = questionDAO.getQuestionById(id);

        if (q != null) {
            System.out.println(q);
        } else {
            System.out.println("Question not found!");
        }
    }

    // EDIT QUESTION
    public static void editQuestion() {

        System.out.println("\n===== EDIT QUESTION =====");

        System.out.print("Enter Question ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        Question oldQuestion = questionDAO.getQuestionById(id);

        if (oldQuestion == null) {
            System.out.println("Question not found!");
            return;
        }

        System.out.println("Enter new details:");

        System.out.print("Enter Skill: ");
        String skill = sc.nextLine();

        System.out.print("Enter Topic: ");
        String topic = sc.nextLine();

        System.out.print("Enter Difficulty: ");
        String difficulty = sc.nextLine();

        System.out.print("Enter Question: ");
        String question = sc.nextLine();

        System.out.print("Enter Option A: ");
        String optionA = sc.nextLine();

        System.out.print("Enter Option B: ");
        String optionB = sc.nextLine();

        System.out.print("Enter Option C: ");
        String optionC = sc.nextLine();

        System.out.print("Enter Option D: ");
        String optionD = sc.nextLine();

        System.out.print("Enter Correct Answer (A/B/C/D): ");
        String correctAnswer = sc.nextLine();

        System.out.print("Enter Explanation: ");
        String explanation = sc.nextLine();

        Question q = new Question(
                skill,
                topic,
                difficulty,
                question,
                optionA,
                optionB,
                optionC,
                optionD,
                correctAnswer,
                explanation
        );

        q.setQuestionId(id);

        boolean result = questionDAO.updateQuestion(q);

        if (result) {
            System.out.println("Question Updated Successfully!");
        } else {
            System.out.println("Failed to Update Question!");
        }
    }

    // DELETE QUESTION
    public static void deleteQuestion() {

        System.out.println("\n===== DELETE QUESTION =====");

        System.out.print("Enter Question ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        Question q = questionDAO.getQuestionById(id);

        if (q == null) {
            System.out.println("Question not found!");
            return;
        }

        System.out.println(q);

        System.out.print("Are you sure you want to delete? (yes/no): ");
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("yes")) {

            boolean result = questionDAO.deleteQuestion(id);

            if (result) {
                System.out.println("Question Deleted Successfully!");
            } else {
                System.out.println("Failed to Delete Question!");
            }

        } else {
            System.out.println("Delete cancelled.");
        }
    }
}