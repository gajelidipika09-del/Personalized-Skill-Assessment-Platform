package com.skillassessment.model;

public class Result {

    private int resultId;
    private int userId;
    private String skill;
    private String topic;
    private String difficulty;
    private int totalQuestions;
    private int correctAnswers;
    private int wrongAnswers;
    private double score;
    private String attemptType;

    public Result() {
    }

    public Result(int userId, String skill, String topic,
                  String difficulty, int totalQuestions,
                  int correctAnswers, int wrongAnswers,
                  double score) {

        this.userId = userId;
        this.skill = skill;
        this.topic = topic;
        this.difficulty = difficulty;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
        this.score = score;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(int wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
    public String getAttemptType() {
        return attemptType;
    }
    public void setAttemptType(String attemptType) {
        this.attemptType = attemptType;
    }
}