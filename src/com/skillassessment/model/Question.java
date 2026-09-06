package com.skillassessment.model;

public class Question {

    private int questionId;
    private String skill;
    private String topic;
    private String difficulty;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    private String explanation;

    // Default constructor
    public Question() {
    }

    // Constructor without ID
    public Question(String skill, String topic, String difficulty,
                    String question, String optionA, String optionB,
                    String optionC, String optionD,
                    String correctAnswer, String explanation) {

        this.skill = skill;
        this.topic = topic;
        this.difficulty = difficulty;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
    }

    // Getters and Setters

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    @Override
    public String toString() {
        return "\nQuestion ID: " + questionId
                + "\nSkill: " + skill
                + "\nTopic: " + topic
                + "\nDifficulty: " + difficulty
                + "\nQuestion: " + question
                + "\nA. " + optionA
                + "\nB. " + optionB
                + "\nC. " + optionC
                + "\nD. " + optionD
                + "\nCorrect Answer: " + correctAnswer
                + "\nExplanation: " + explanation;
    }
}