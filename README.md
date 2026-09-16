# Personalized Skill Assessment & Practice Platform

> A Java-based web application that helps students assess their skills, identify weak areas, practice targeted questions, track progress, and receive personalized recommendations.

## 📌 Overview

**Personalized Skill Assessment & Practice Platform (SmartSkill)** is a full-stack web application designed to provide students with a personalized learning and skill-improvement experience.

The platform allows students to take skill assessments, analyze their performance, practice questions based on their weak areas, track improvement, and maintain learning streaks. Administrators can manage the question bank and monitor overall platform analytics.

## ✨ Features

### 👨‍🎓 Student Features

* 🔐 Student registration and login
* 📝 Skill-based assessments
* 🎯 Personalized practice based on performance
* 📊 Progress tracking
* 🔍 Skill and topic-wise performance analysis
* 💡 Personalized recommendations
* 📈 Assessment vs. practice improvement tracking
* 🔥 Daily learning streak
* 🏆 Longest streak tracking
* 👋 Personalized student dashboard

### 👨‍💼 Admin Features

* 🔐 Admin authentication
* 🗂️ Question Bank management
* ➕ Add questions
* ✏️ Edit questions
* 🗑️ Delete questions
* 📊 Platform analytics
* 👥 Total student count
* 📝 Total assessment count
* 🎯 Practice attempt statistics
* 📚 Total question statistics
* 📈 Average assessment score
* 🔍 Skill performance analysis
* ⚠️ Weak topic identification

## 🧠 Personalization Logic

The platform analyzes assessment performance to classify student skill areas:

| Score     | Performance    | Recommendation                 |
| --------- | -------------- | ------------------------------ |
| Below 50% | Weak           | High-priority practice         |
| 50% – 75% | Needs Practice | Medium-priority practice       |
| Above 75% | Strong         | No immediate practice required |

Based on the identified weak areas, the system recommends targeted practice questions.

## 🔥 Learning Streak

SmartSkill includes a daily learning streak system.

A student's streak is updated when they complete an assessment or practice activity.

* Multiple activities on the same day count as one learning day.
* Consecutive learning days increase the current streak.
* Missing a day resets the current streak.
* The longest streak is stored separately.

## 🛠️ Technology Stack

### Backend

* Java
* Java Servlets
* JDBC
* Apache Tomcat

### Database

* MySQL

### Frontend

* HTML5
* CSS3
* JavaScript

### Libraries

* MySQL Connector/J
* Gson

### Development Tools

* Eclipse IDE
* Git & GitHub

## 🏗️ Project Architecture

The application follows a layered architecture:

```text
PersonalizedSkillAssessment/
│
├── src/
│   └── com.skillassessment/
│       ├── dao/
│       ├── model/
│       ├── service/
│       ├── servlet/
│       └── ...
│
├── frontend/
│   ├── HTML files
│   ├── CSS files
│   └── JavaScript files
│
├── .gitignore
└── README.md
```

### Main Layers

**Model**

* Represents application data such as users, questions, and results.

**DAO**

* Handles database operations using JDBC.

**Service**

* Contains application business logic such as skill analysis and recommendations.

**Servlet**

* Handles HTTP requests and connects the frontend with the backend.

**Frontend**

* Provides the user interface for students and administrators.

## 🗄️ Database

The application uses **MySQL** with the following major data areas:

* Users
* Questions
* Assessment results
* Practice results
* Student streaks

Example database:

```text
skill_assessments
```

The student streak system uses:

```text
student_streak
```

with information such as:

* `user_id`
* `current_streak`
* `longest_streak`
* `last_activity_date`

## ⚙️ How to Run

### Prerequisites

Make sure you have installed:

* Java 21
* Eclipse IDE
* Apache Tomcat 9
* MySQL Server
* MySQL Connector/J

### 1. Clone the Repository

```bash
git clone https://github.com/gajelidipika09-del/Personalized-Skill-Assessment-Platform.git
```

### 2. Import into Eclipse

Import the project into Eclipse as an existing Java/Web project.

### 3. Configure MySQL

Create the database:

```sql
CREATE DATABASE skill_assessments;
```

Create the required tables according to the SQL schema used by the application.

### 4. Configure Database Connection

Update the database connection details in:

```text
DBConnection.java
```

Example:

```text
jdbc:mysql://localhost:3306/skill_assessments
```

Use your own local MySQL username and password.

### 5. Configure Tomcat

Add the project to an Apache Tomcat 9 server in Eclipse.

Start the server and deploy the application.

### 6. Open the Application

Open the application through your local Tomcat server.

Example:

```text
http://localhost:8080/PersonalizedSkillAssessment/
```

## 🔐 User Roles

SmartSkill supports two main roles:

### Student

Students can:

* Take assessments
* Practice questions
* View progress
* Analyze skills
* View recommendations
* Track improvement
* Maintain learning streaks

### Admin

Administrators can:

* Manage questions
* View platform statistics
* Analyze skill performance
* Identify weak topics

## 📊 Application Flow

```text
                ┌──────────────┐
                │    Login     │
                └──────┬───────┘
                       │
             ┌─────────┴─────────┐
             │                   │
          Student              Admin
             │                   │
             ▼                   ▼
      Student Dashboard    Admin Dashboard
             │                   │
     ┌───────┼────────┐      ┌───┴──────────┐
     │       │        │      │              │
 Assessment Practice Progress Question Bank Analytics
     │       │        │      │              │
     └───────┴────────┘      └──────────────┘
             │
             ▼
      Skill Analysis
             │
             ▼
      Recommendations
             │
             ▼
      Improvement Tracking
```

## 🎯 Project Objectives

* Provide personalized skill assessment
* Identify student strengths and weaknesses
* Recommend targeted practice
* Track student progress over time
* Encourage consistent learning through streaks
* Provide administrators with useful platform analytics

## 🚀 Future Enhancements

Possible future improvements include:

* Adaptive question difficulty
* More advanced recommendation algorithms
* Gamification and badges
* Performance charts and visualizations
* Email notifications
* More skill categories
* Deployment to a cloud platform

## 👩‍💻 Author

**Dipika Gajeli**

Computer Science & Engineering Student

**Nandini Vallal**

Computer Science & Engineering Student

### Technologies

`Java` `JDBC` `Servlets` `MySQL` `HTML` `CSS` `JavaScript` `Tomcat`

---

⭐ If you find this project useful, feel free to explore the repository and learn from it.
