-- ============================================================
-- Personalized Skill Assessment & Practice Platform
-- Database Setup Script
-- ============================================================

CREATE DATABASE IF NOT EXISTS skill_assessments;
USE skill_assessments;

-- ============================================================
-- 1. USERS
-- ============================================================

CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- ============================================================
-- 2. QUESTIONS
-- ============================================================

CREATE TABLE IF NOT EXISTS questions (
    question_id INT PRIMARY KEY AUTO_INCREMENT,
    skill VARCHAR(50) NOT NULL,
    topic VARCHAR(100) NOT NULL,
    difficulty VARCHAR(20) NOT NULL,
    question TEXT NOT NULL,
    option_a VARCHAR(255) NOT NULL,
    option_b VARCHAR(255) NOT NULL,
    option_c VARCHAR(255) NOT NULL,
    option_d VARCHAR(255) NOT NULL,
    correct_answer CHAR(1) NOT NULL,
    explanation TEXT
);

-- ============================================================
-- 3. RESULTS
-- ============================================================

CREATE TABLE IF NOT EXISTS results (
    result_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    skill VARCHAR(50) NOT NULL,
    topic VARCHAR(100) NOT NULL,
    difficulty VARCHAR(30) NOT NULL,
    total_questions INT NOT NULL,
    correct_answers INT NOT NULL,
    wrong_answers INT NOT NULL,
    score DOUBLE NOT NULL,
    test_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    attempt_type VARCHAR(20) DEFAULT 'ASSESSMENT',
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- ============================================================
-- 4. STUDENT STREAK
-- ============================================================

CREATE TABLE IF NOT EXISTS student_streak (
    user_id INT PRIMARY KEY,
    current_streak INT DEFAULT 0,
    longest_streak INT DEFAULT 0,
    last_activity_date DATE,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- ============================================================
-- OPTIONAL SAMPLE ADMIN
-- Password is intentionally not included for public GitHub.
-- Create your own admin account using the application's
-- registration/admin setup if required.
-- ============================================================

-- ============================================================
-- USEFUL CHECK QUERIES
-- These are included so the database can be tested after setup.
-- ============================================================

SHOW TABLES;

DESCRIBE users;
DESCRIBE questions;
DESCRIBE results;
DESCRIBE student_streak;

SELECT * FROM users;
SELECT * FROM questions;
SELECT * FROM results;
SELECT * FROM student_streak;

-- ============================================================
-- EXAMPLE PROJECT QUERIES
-- ============================================================

-- View Java Collections questions
SELECT question, option_a, option_b, option_c, option_d, correct_answer
FROM questions
WHERE skill = 'Java'
AND topic = 'Collections';

-- View a student's results
SELECT result_id,
       user_id,
       skill,
       topic,
       score,
       attempt_type,
       test_date
FROM results
WHERE user_id = 1
ORDER BY test_date;

-- View admin users
SELECT user_id, name, email, role
FROM users
WHERE role = 'ADMIN';

-- ============================================================
-- END OF DATABASE SETUP
-- ============================================================
