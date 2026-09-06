package com.skillassessment.service;

public class SkillAnalysis {

    public static String analyze(double score) {

        if (score < 50) {
            return "WEAK";
        }

        if (score <= 75) {
            return "NEEDS PRACTICE";
        }

        return "STRONG";
    }
}