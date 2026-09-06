package com.skillassessment.service;

public class RecommendationService {

    public String getRecommendation(double score) {

        if (score < 50) {
            return "🔴 HIGH PRIORITY - Practice 10 questions";
        }

        if (score <= 75) {
            return "🟡 MEDIUM PRIORITY - Practice 5 questions";
        }

        return "🟢 STRONG - No immediate practice required";
    }
}