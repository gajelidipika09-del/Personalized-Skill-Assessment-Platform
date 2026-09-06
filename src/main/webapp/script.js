function showLogin() {


const loginModal = document.getElementById("loginSection");

if (!loginModal) {
    console.error("Login modal not found!");
    return;
}

loginModal.hidden = false;
loginModal.style.setProperty("display", "flex", "important");

document.body.style.overflow = "hidden";


}

function closeLogin() {


const loginModal = document.getElementById("loginSection");

if (!loginModal) {
    console.error("Login modal not found!");
    return;
}

loginModal.hidden = true;
loginModal.style.setProperty("display", "none", "important");

document.body.style.overflow = "";


}

function showRegister() {


const registerModal = document.getElementById("registerSection");

if (!registerModal) {
    console.error("Register modal not found!");
    return;
}

registerModal.hidden = false;
registerModal.style.setProperty("display", "flex", "important");

document.body.style.overflow = "hidden";


}

function closeRegister() {


const registerModal = document.getElementById("registerSection");

if (!registerModal) {
    console.error("Register modal not found!");
    return;
}

registerModal.hidden = true;
registerModal.style.setProperty("display", "none", "important");

document.body.style.overflow = "";


}

function login() {


const emailElement = document.getElementById("loginEmail");
const passwordElement = document.getElementById("loginPassword");
const message = document.getElementById("loginMessage");

if (!emailElement || !passwordElement || !message) {
    console.error("Login fields not found!");
    return;
}

const email = emailElement.value.trim();
const password = passwordElement.value.trim();

if (email === "" || password === "") {

    message.innerText = "Please enter email and password.";
    return;
}

message.innerText = "Logging in...";

fetch("./api/login", {

    method: "POST",

    headers: {
        "Content-Type": "application/x-www-form-urlencoded"
    },

    body:
        "email=" + encodeURIComponent(email) +
        "&password=" + encodeURIComponent(password)

})

.then(response => {

    if (!response.ok) {
        throw new Error("Server returned " + response.status);
    }

    return response.json();
})

.then(data => {

    if (data.success) {

        message.innerText = "Login successful!";

        // Save logged-in user information
        sessionStorage.setItem("userId", data.userId);
        sessionStorage.setItem("userName", data.name);
        sessionStorage.setItem("userEmail", data.email);
        sessionStorage.setItem("userRole", data.role);

        console.log("Logged in user:", data);

        // Redirect according to role
        if (data.role && data.role.toUpperCase() === "STUDENT") {

            window.location.href = "student-dashboard.html";

        }
		else if (data.role && data.role.toUpperCase() === "ADMIN") {
		    window.location.href = "admin-dashboard.html";
		}
        else {

            console.error("Unknown user role:", data.role);
            message.innerText = "Unknown user role.";

        }

    }
    else {

        message.innerText =
            data.message || "Invalid email or password.";

    }

})

.catch(error => {

    console.error("Login Error:", error);

    message.innerText =
        "Unable to connect to server.";

});


}

function register() {


const nameElement = document.getElementById("registerName");
const emailElement = document.getElementById("registerEmail");
const passwordElement = document.getElementById("registerPassword");
const message = document.getElementById("registerMessage");

if (!nameElement || !emailElement || !passwordElement || !message) {
    console.error("Register fields not found!");
    return;
}

const name = nameElement.value.trim();
const email = emailElement.value.trim();
const password = passwordElement.value.trim();

if (name === "" || email === "" || password === "") {

    message.innerText = "Please fill all fields.";
    return;
}

message.innerText = "Creating account...";

fetch("./api/register", {

    method: "POST",

    headers: {
        "Content-Type": "application/x-www-form-urlencoded"
    },

    body:
        "name=" + encodeURIComponent(name) +
        "&email=" + encodeURIComponent(email) +
        "&password=" + encodeURIComponent(password)

})

.then(response => {

    if (!response.ok) {
        throw new Error("Server returned " + response.status);
    }

    return response.json();
})

.then(data => {

    if (data.success) {

        message.innerText =
            "Account created successfully!";

        // Clear fields
        nameElement.value = "";
        emailElement.value = "";
        passwordElement.value = "";

        console.log("Registration successful:", data);

    }
    else {

        message.innerText =
            data.message || "Registration failed.";

    }

})

.catch(error => {

    console.error("Registration Error:", error);

    message.innerText =
        "Unable to connect to server.";

});


}

// =====================================================
// STUDENT DASHBOARD
// =====================================================

document.addEventListener("DOMContentLoaded", function () {

    const userRole = sessionStorage.getItem("userRole");
    const userName = sessionStorage.getItem("userName");

    const studentName =
        document.getElementById("studentName");

    const welcomeStudentName =
        document.getElementById("welcomeStudentName");

    // Only run this code on the student dashboard
    if (studentName || welcomeStudentName) {

        if (userRole !== "STUDENT") {
            window.location.href = "index.html";
            return;
        }

        if (studentName) {
            studentName.innerText =
                userName || "Student";
        }

        if (welcomeStudentName) {
            welcomeStudentName.innerText =
                userName || "Student";
        }

        // Load student's streak
        loadStudentStreak();
    }

});
// =====================================================
// STUDENT STREAK
// =====================================================

async function loadStudentStreak() {

    const userId = sessionStorage.getItem("userId");

    console.log("Loading streak for user:", userId);

    if (!userId) {
        console.error("User ID not found in sessionStorage.");
        return;
    }

    try {

        const response = await fetch(
            "./api/progress?userId=" +
            encodeURIComponent(userId)
        );

        console.log("Progress API status:", response.status);

        if (!response.ok) {
            throw new Error(
                "Server returned " + response.status
            );
        }

        const data = await response.json();

        console.log("FULL PROGRESS DATA:", data);
        console.log("Current streak from API:", data.currentStreak);
        console.log("Longest streak from API:", data.longestStreak);

        if (!data.success) {
            console.error(
                data.message || "Unable to load streak."
            );
            return;
        }

        const currentStreak =
            document.getElementById("currentStreak");

        const longestStreak =
            document.getElementById("longestStreak");

        console.log("Current streak element:", currentStreak);
        console.log("Longest streak element:", longestStreak);

        if (currentStreak) {
            currentStreak.textContent =
                data.currentStreak;
        }

        if (longestStreak) {
            longestStreak.textContent =
                data.longestStreak;
        }

    } catch (error) {

        console.error(
            "Streak loading error:",
            error
        );

    }
}
function logout() {


sessionStorage.clear();

window.location.href = "index.html";


}

function startAssessment() {


window.location.href = "assessment.html";


}


async function startPractice() {

    const userId = sessionStorage.getItem("userId");

    if (!userId) {
        alert("Please login first.");
        window.location.href = "index.html";
        return;
    }

    try {

        const response = await fetch(
            "/PersonalizedSkillAssessment/api/progress?userId="
            + encodeURIComponent(userId)
        );

        if (!response.ok) {
            throw new Error(
                "Server returned " + response.status
            );
        }

        const data = await response.json();

        console.log("Practice recommendation data:", data);

        if (!data.success) {
            alert(
                data.message ||
                "Unable to find your practice recommendation."
            );
            return;
        }

        if (
            !data.skillAnalysis ||
            data.skillAnalysis.length === 0
        ) {
            alert(
                "Complete an assessment first to get a personalized practice recommendation."
            );
            return;
        }

        // Find weakest topic
        let weakestTopic = data.skillAnalysis[0];

        data.skillAnalysis.forEach(function(item) {

            if (
                parseFloat(item.score) <
                parseFloat(weakestTopic.score)
            ) {
                weakestTopic = item;
            }

        });

        console.log("Weakest topic:", weakestTopic);

        // Practice question count
        let count = 5;

        if (parseFloat(weakestTopic.score) < 50) {
            count = 10;
        }

        // Open personalized practice
        window.location.href =
            "practice.html"
            + "?skill="
            + encodeURIComponent(weakestTopic.skill)
            + "&topic="
            + encodeURIComponent(weakestTopic.topic)
            + "&count="
            + count;

    }
    catch (error) {

        console.error(
            "Start Practice error:",
            error
        );

        alert(
            "Unable to start personalized practice."
        );
    }
}


function viewProgress() {


const userId =
    sessionStorage.getItem("userId");

if (!userId) {

    alert("Please login first.");
    return;
}

console.log(
    "Opening progress page for user:",
    userId
);

window.location.href = "progress.html";


}

function viewSkillAnalysis() {


const userId =
    sessionStorage.getItem("userId");

if (!userId) {

    alert("Please login first.");
    return;
}

window.location.href =
    "skill-analysis.html";


}

function viewRecommendations() {


const userId =
    sessionStorage.getItem("userId");

if (!userId) {

    alert("Please login first.");
    return;
}

window.location.href =
    "recommendations.html";


}

function viewImprovement() {
    const userId = sessionStorage.getItem("userId");

    if (!userId) {
        alert("Please login first.");
        return;
    }

    window.location.href = "improvement.html";
}

// =====================================================
// PROGRESS PAGE
// =====================================================

async function loadProgressPage() {


const userId =
    sessionStorage.getItem("userId");

if (!userId) {

    window.location.href = "index.html";
    return;
}

try {

    const response = await fetch(
        "/PersonalizedSkillAssessment/api/progress?userId=" +
        encodeURIComponent(userId)
    );

    if (!response.ok) {
        throw new Error(
            "Server returned " + response.status
        );
    }

    const data = await response.json();

    console.log("Progress data:", data);

    if (!data.success) {

        console.error(data.message);
        return;
    }


    // Student name
    const nameElement =
        document.getElementById(
            "progressStudentName"
        );

    if (nameElement) {

        nameElement.textContent =
            data.studentName;
    }


    // Overall score
    const overallScore =
        document.getElementById(
            "overallScore"
        );

    if (overallScore) {

        overallScore.textContent =
            data.overallScore + "%";
    }


    // Skill performance
    const skillContainer =
        document.getElementById(
            "skillPerformance"
        );

    if (skillContainer) {

        skillContainer.innerHTML = "";

        if (data.skillPerformance) {

            data.skillPerformance.forEach(skill => {

                skillContainer.innerHTML += `
                    <div class="performance-item">

                        <div class="performance-title">

                            <strong>
                                ${skill.skill}
                            </strong>

                            <span>
                                ${skill.score}%
                            </span>

                        </div>

                        <div class="progress-bar">

                            <div
                                class="progress-fill"
                                style="width:${skill.score}%">
                            </div>

                        </div>

                    </div>
                `;
            });
        }
    }


    // Skill analysis
    const analysisContainer =
        document.getElementById(
            "skillAnalysis"
        );

    if (analysisContainer) {

        analysisContainer.innerHTML = "";

        if (data.skillAnalysis) {

            data.skillAnalysis.forEach(item => {

                analysisContainer.innerHTML += `
                    <div class="analysis-item">

                        <div>

                            <strong>
                                ${item.skill}
                            </strong>

                            <p>
                                ${item.topic}
                            </p>

                        </div>

                        <div>

                            <strong>
                                ${item.score}%
                            </strong>

                            <span class="status">
                                ${item.status}
                            </span>

                        </div>

                    </div>
                `;
            });
        }
    }


    // Assessment history
    const historyContainer =
        document.getElementById(
            "assessmentHistory"
        );

    if (historyContainer) {

        historyContainer.innerHTML = "";

        if (data.assessmentHistory) {

            data.assessmentHistory.forEach(item => {

                historyContainer.innerHTML += `
                    <div class="history-item">

                        <div>

                            <strong>
                                ${item.skill}
                            </strong>

                            <p>
                                ${item.topic}
                                •
                                ${item.difficulty}
                            </p>

                        </div>

                        <div>

                            <strong>
                                ${item.score}%
                            </strong>

                            <p>
                                ${item.date}
                            </p>

                        </div>

                    </div>
                `;
            });
        }
    }

}
catch (error) {

    console.error(
        "Progress loading error:",
        error
    );
}


}

// =====================================================
// ASSESSMENT
// =====================================================

let assessmentQuestions = [];
let currentQuestionIndex = 0;
let userAnswers = [];

async function loadAssessment() {


const skill =
    document.getElementById(
        "assessmentSkill"
    ).value.trim();

const topic =
    document.getElementById(
        "assessmentTopic"
    ).value.trim();

const difficulty =
    document.getElementById(
        "assessmentDifficulty"
    ).value;

const limit =
    document.getElementById(
        "questionCount"
    ).value;

const message =
    document.getElementById(
        "assessmentMessage"
    );


if (!skill || !topic || !difficulty || !limit) {

    message.textContent =
        "Please fill all assessment details.";

    return;
}


message.textContent =
    "Loading questions...";


try {

    const response =
        await fetch("api/assessment", {

            method: "POST",

            headers: {
                "Content-Type":
                    "application/x-www-form-urlencoded"
            },

            body:
                "skill=" +
                encodeURIComponent(skill) +

                "&topic=" +
                encodeURIComponent(topic) +

                "&difficulty=" +
                encodeURIComponent(difficulty) +

                "&limit=" +
                encodeURIComponent(limit)
        });


    if (!response.ok) {

        throw new Error(
            "Server returned " +
            response.status
        );
    }


    const data =
        await response.json();


    if (!data.success) {

        message.textContent =
            data.message ||
            "Unable to load questions.";

        return;
    }


    assessmentQuestions =
        data.questions || [];

    currentQuestionIndex = 0;

    userAnswers = [];


    if (assessmentQuestions.length === 0) {

        message.textContent =
            "No questions found for the selected criteria.";

        return;
    }


    document.getElementById(
        "assessmentSetup"
    ).style.display = "none";


    document.getElementById(
        "questionsSection"
    ).style.display = "block";


    displayQuestion();

}
catch (error) {

    console.error(
        "Assessment error:",
        error
    );

    message.textContent =
        "Unable to load assessment questions.";
}


}

function displayQuestion() {


const question =
    assessmentQuestions[
        currentQuestionIndex
    ];

if (!question) {

    console.error(
        "Question not found at index:",
        currentQuestionIndex
    );

    return;
}


const container =
    document.getElementById(
        "questionsContainer"
    );


if (!container) {

    console.error(
        "questionsContainer not found!"
    );

    return;
}


document.getElementById(
    "questionNumber"
).textContent =
    "Question " +
    (currentQuestionIndex + 1);


document.getElementById(
    "questionProgress"
).textContent =
    (currentQuestionIndex + 1) +
    " / " +
    assessmentQuestions.length;


container.innerHTML = `
    <div class="question-card">

        <h3>
            ${question.question}
        </h3>

        <label>
            <input
                type="radio"
                name="answer"
                value="A">

            ${question.optionA}
        </label>

        <label>
            <input
                type="radio"
                name="answer"
                value="B">

            ${question.optionB}
        </label>

        <label>
            <input
                type="radio"
                name="answer"
                value="C">

            ${question.optionC}
        </label>

        <label>
            <input
                type="radio"
                name="answer"
                value="D">

            ${question.optionD}
        </label>

    </div>
`;


const nextButton =
    document.getElementById(
        "nextQuestionBtn"
    );


if (nextButton) {

    if (
        currentQuestionIndex ===
        assessmentQuestions.length - 1
    ) {

        nextButton.textContent =
            "Submit Assessment →";

    }
    else {

        nextButton.textContent =
            "Next Question →";
    }
}


}

function nextQuestion() {


const selected =
    document.querySelector(
        'input[name="answer"]:checked'
    );


if (!selected) {

    alert("Please select an answer.");
    return;
}


// Save selected answer
userAnswers[currentQuestionIndex] =
    selected.value;


console.log(
    "Saving answer for question",
    currentQuestionIndex + 1,
    ":",
    selected.value
);


// More questions remaining
if (
    currentQuestionIndex <
    assessmentQuestions.length - 1
) {

    currentQuestionIndex++;

    displayQuestion();

}
else {

    // Last question
    finishAssessment();
}


}

// =====================================================
// FINISH ASSESSMENT
// =====================================================

async function finishAssessment() {


let correct = 0;


assessmentQuestions.forEach(
    (question, index) => {

        console.log(
            "Question:",
            question.question
        );

        console.log(
            "Selected:",
            userAnswers[index]
        );

        console.log(
            "Correct from database:",
            question.correctAnswer
        );


        if (

            userAnswers[index] &&

            question.correctAnswer &&

            userAnswers[index]
                .trim()
                .toUpperCase() ===

            question.correctAnswer
                .trim()
                .toUpperCase()

        ) {

            correct++;
        }
    }
);


const total =
    assessmentQuestions.length;


if (total === 0) {

    alert(
        "No assessment questions available."
    );

    return;
}


const wrong =
    total - correct;


const score =
    Math.round(
        (correct / total) * 100
    );


console.log(
    "Total:",
    total
);

console.log(
    "Correct:",
    correct
);

console.log(
    "Wrong:",
    wrong
);

console.log(
    "Final Score:",
    score
);


// Get logged-in student ID
const userId =
    sessionStorage.getItem(
        "userId"
    );


if (!userId) {

    alert(
        "User session not found. Please login again."
    );

    return;
}


// Get assessment details
const skill =
    document.getElementById(
        "assessmentSkill"
    ).value.trim();


const topic =
    document.getElementById(
        "assessmentTopic"
    ).value.trim();


const difficulty =
    document.getElementById(
        "assessmentDifficulty"
    ).value;


try {

    const response =
        await fetch("api/result", {

            method: "POST",

            headers: {
                "Content-Type":
                    "application/x-www-form-urlencoded"
            },

            body:

                "userId=" +
                encodeURIComponent(userId) +

                "&skill=" +
                encodeURIComponent(skill) +

                "&topic=" +
                encodeURIComponent(topic) +

                "&difficulty=" +
                encodeURIComponent(difficulty) +

                "&totalQuestions=" +
                encodeURIComponent(total) +

                "&correctAnswers=" +
                encodeURIComponent(correct) +

                "&wrongAnswers=" +
                encodeURIComponent(wrong) +

                "&score=" +
                encodeURIComponent(score) +

                "&attemptType=ASSESSMENT"
        });


    if (!response.ok) {

        throw new Error(
            "Server returned " +
            response.status
        );
    }


    const data =
        await response.json();


    console.log(
        "Save Result Response:",
        data
    );


    if (!data.success) {

        alert(
            data.message ||
            "Result could not be saved."
        );

        return;
    }


    // Hide questions
    const questionsSection =
        document.getElementById(
            "questionsSection"
        );

    if (questionsSection) {

        questionsSection.style.display =
            "none";
    }


    // Show result
    const resultSection =
        document.getElementById(
            "resultSection"
        );

    if (resultSection) {

        resultSection.style.display =
            "block";
    }


    const resultContent =
        document.getElementById(
            "resultContent"
        );


    if (resultContent) {

        resultContent.innerHTML = `
            <div class="result-score">

                <h3>
                    ${score}%
                </h3>

                <p>
                    You answered
                    <strong>${correct}</strong>
                    out of
                    <strong>${total}</strong>
                    questions correctly.
                </p>

                <p>
                    ${getResultMessage(score)}
                </p>

            </div>
        `;
    }

}
catch (error) {

    console.error(
        "Save Result Error:",
        error
    );

    alert(
        "Unable to save assessment result."
    );
}


}

// =====================================================
// SKILL ANALYSIS
// =====================================================

async function loadSkillAnalysisPage() {

    const userId = sessionStorage.getItem("userId");

    if (!userId) {
        window.location.href = "index.html";
        return;
    }

    try {

        const response = await fetch(
            "/PersonalizedSkillAssessment/api/progress?userId=" +
            encodeURIComponent(userId)
        );

        const data = await response.json();

        console.log("Skill Analysis data:", data);

        if (!data.success) {
            alert(data.message || "Unable to load skill analysis.");
            return;
        }

        /* ==============================
           STUDENT NAME
           ============================== */

        const nameElement =
            document.getElementById("analysisStudentName");

        if (nameElement) {
            nameElement.textContent = data.studentName;
        }


        /* ==============================
           CONTAINER
           ============================== */

        const container =
            document.getElementById("skillAnalysis");

        if (!container) {
            console.error("skillAnalysis container not found!");
            return;
        }

        container.innerHTML = "";


        /* ==============================
           NO ANALYSIS
           ============================== */

        if (
            !data.skillAnalysis ||
            data.skillAnalysis.length === 0
        ) {

            container.innerHTML = `
                <div class="recommendation-card">

                    <div class="recommendation-topic">
                        No Skill Analysis Available
                    </div>

                    <div class="recommendation-skill">
                        Complete an assessment to see your performance.
                    </div>

                </div>
            `;

            return;
        }


        /* ==============================
           DISPLAY SKILL ANALYSIS
           ============================== */

        data.skillAnalysis.forEach(item => {

            let analysisMessage = "";
            let statusClass = "";

            if (item.score < 50) {

                analysisMessage =
                    "🔴 Weak Area - Needs significant practice";

                statusClass = "weak";

            }

            else if (item.score <= 75) {

                analysisMessage =
                    "🟡 Needs Practice - Improve with more practice";

                statusClass = "practice";

            }

            else {

                analysisMessage =
                    "🟢 Strong Area - Good understanding of this topic";

                statusClass = "strong";
            }


            /* ==============================
               SAME CARD AS RECOMMENDATIONS
               ============================== */

            container.innerHTML += `

                <div class="recommendation-card skill-analysis-result-card">

                    <div class="recommendation-top">

                        <div>

                            <div class="recommendation-topic">
                                ${item.topic}
                            </div>

                            <div class="recommendation-skill">
                                Skill: ${item.skill}
                            </div>

                        </div>


                        <div class="recommendation-score">
                            ${item.score}%
                        </div>

                    </div>


                    <!-- PROGRESS BAR -->

                    <div class="skill-analysis-progress">

                        <div
                            class="skill-analysis-progress-fill"
                            style="width: ${item.score}%">
                        </div>

                    </div>


                    <!-- ANALYSIS MESSAGE -->

                    <div class="recommendation-message ${statusClass}">
                        ${analysisMessage}
                    </div>


                    <!-- STATUS -->

                    <div class="analysis-status-text">
                        ${item.status}
                    </div>

                </div>
            `;
        });

    }

    catch (error) {

        console.error(
            "Skill Analysis error:",
            error
        );

        alert(
            "Unable to connect to skill analysis server."
        );
    }
}

// =====================================================
// RECOMMENDATIONS
// =====================================================

async function loadRecommendationsPage() {


const userId =
    sessionStorage.getItem("userId");


if (!userId) {

    window.location.href =
        "index.html";

    return;
}


try {

    const response =
        await fetch(
            "/PersonalizedSkillAssessment/api/progress?userId=" +
            encodeURIComponent(userId)
        );


    if (!response.ok) {

        throw new Error(
            "Server returned " +
            response.status
        );
    }


    const data =
        await response.json();


    console.log(
        "Recommendation data:",
        data
    );


    if (!data.success) {

        console.error(
            data.message
        );

        return;
    }


    // Student name
    const nameElement =
        document.getElementById(
            "recommendationStudentName"
        );


    if (nameElement) {

        nameElement.textContent =
            data.studentName;
    }


    // Recommendations container
    const container =
        document.getElementById(
            "recommendationsContainer"
        );


    if (!container) {

        console.error(
            "recommendationsContainer not found!"
        );

        return;
    }


    container.innerHTML = "";


    // No recommendations
    if (
        !data.skillAnalysis ||
        data.skillAnalysis.length === 0
    ) {

        container.innerHTML = `
            <div class="no-recommendations">

                <h3>
                    🎉 No recommendations yet
                </h3>

                <p>
                    Complete an assessment to receive
                    personalized recommendations.
                </p>

            </div>
        `;

        return;
    }


    // Create recommendations
    data.skillAnalysis.forEach(
        item => {

            let recommendation = "";

            let showPracticeButton =
                false;


            if (item.score < 50) {

                recommendation =
                    "🔴 HIGH PRIORITY - Practice 10 questions";

                showPracticeButton =
                    true;

            }
            else if (item.score <= 75) {

                recommendation =
                    "🟡 MEDIUM PRIORITY - Practice 5 questions";

                showPracticeButton =
                    true;

            }
            else {

                recommendation =
                    "🟢 STRONG - No immediate practice required";
            }


            container.innerHTML += `
                <div class="recommendation-card">

                    <div class="recommendation-top">

                        <div>

                            <div class="recommendation-topic">
                                ${item.topic}
                            </div>

                            <div class="recommendation-skill">
                                Skill: ${item.skill}
                            </div>

                        </div>


                        <div class="recommendation-score">
                            ${item.score}%
                        </div>

                    </div>


                    <div class="recommendation-message">
                        ${recommendation}
                    </div>


                    ${
                        showPracticeButton
                        ?
                        `
                        <button
                            class="practice-btn"
                            onclick="startRecommendedPractice('${item.skill}', '${item.topic}', ${item.score < 50 ? 10 : 5})">

                            🎯 Start Practice

                        </button>
                        `
                        :
                        ""
                    }

                </div>
            `;
        }
    );

}
catch (error) {

    console.error(
        "Recommendation loading error:",
        error
    );
}


}

function startRecommendedPractice(skill, topic, numberOfQuestions) {

    window.location.href =
        "practice.html?skill=" +
        encodeURIComponent(skill) +
        "&topic=" +
        encodeURIComponent(topic) +
        "&count=" +
        numberOfQuestions;
}

// =====================================================
// RESULT MESSAGE
// =====================================================

function getResultMessage(score) {


if (score >= 80) {

    return "Excellent! 🎉 You have a strong understanding of this topic.";
}


if (score >= 60) {

    return "Good job! 👍 A little more practice will make you stronger.";
}


if (score >= 40) {

    return "Keep practicing! 💪 You are making progress.";
}


return "Don't worry! 📚 Practice this topic and try again.";


}

// =====================================================
// PAGE LOAD FUNCTIONS
// =====================================================

// Progress page
document.addEventListener(
"DOMContentLoaded",
function () {


    if (
        document.getElementById(
            "overallScore"
        )
    ) {

        loadProgressPage();
    }
}


);

// Recommendations page
document.addEventListener(
"DOMContentLoaded",
function () {


    if (
        document.getElementById(
            "recommendationsContainer"
        )
    ) {

        loadRecommendationsPage();
    }
}


);

// Skill Analysis page
document.addEventListener(
"DOMContentLoaded",
function () {


    if (
        document.getElementById(
            "skillAnalysis"
        ) &&
        document.getElementById(
            "analysisStudentName"
        )
    ) {

        loadSkillAnalysisPage();
    }
}


);

// =====================================================
// IMPROVEMENT PAGE
// =====================================================

async function loadImprovementPage() {

    const userId = sessionStorage.getItem("userId");

    if (!userId) {

        window.location.href = "index.html";

        return;
    }

    try {

        const response = await fetch(
            "/PersonalizedSkillAssessment/api/progress?userId=" +
            encodeURIComponent(userId)
        );

        const data = await response.json();

        console.log("Improvement data:", data);

        if (!data.success) {

            console.error(data.message);

            return;
        }


        // Student name

        const nameElement =
            document.getElementById("improvementStudentName");

        if (nameElement) {

            nameElement.textContent =
                data.studentName || "Student";
        }


        // Overall score

        const overallScore =
            document.getElementById("currentOverallScore");

        if (overallScore) {

            overallScore.textContent =
                (data.overallScore || 0) + "%";
        }


        // Overall status

        const overallStatus =
            document.getElementById("overallImprovementStatus");

        if (overallStatus) {

            const score =
                Number(data.overallScore || 0);

            if (score >= 80) {

                overallStatus.textContent =
                    "Excellent 🎉";

            } else if (score >= 60) {

                overallStatus.textContent =
                    "Good 👍";

            } else if (score >= 40) {

                overallStatus.textContent =
                    "Needs Practice 💪";

            } else {

                overallStatus.textContent =
                    "Needs Improvement 📚";
            }
        }


        // Improvement container

        const container =
            document.getElementById("improvementContainer");

        if (!container) {

            return;
        }


        container.innerHTML = "";


        if (
            !data.skillAnalysis ||
            data.skillAnalysis.length === 0
        ) {

            container.innerHTML = `

                <div class="no-improvement">

                    <h3>
                        📈 No improvement data available
                    </h3>

                    <p>
                        Complete assessments to track your improvement.
                    </p>

                </div>

            `;

            return;
        }


        // Display each skill

        data.skillAnalysis.forEach(item => {

            const score =
                Number(item.score || 0);


            let message = "";

            let messageClass = "";


            if (score >= 80) {

                message =
                    "🟢 Strong performance - Keep maintaining this level.";

                messageClass =
                    "improved";

            } else if (score >= 60) {

                message =
                    "🟡 Good progress - Continue practicing to improve further.";

                messageClass =
                    "no-change";

            } else {

                message =
                    "🔴 Needs improvement - Practice this topic more.";

                messageClass =
                    "decreased";
            }


            container.innerHTML += `

                <div class="improvement-item">

                    <div class="improvement-top">

                        <div>

                            <div class="improvement-topic">

                                ${item.topic}

                            </div>

                            <div class="improvement-skill">

                                Skill: ${item.skill}

                            </div>

                        </div>


                        <div class="improvement-score">

                            ${score}%

                        </div>

                    </div>


                    <div class="improvement-progress">

                        <div
                            class="improvement-progress-fill"
                            style="width: ${score}%">
                        </div>

                    </div>


                    <div class="score-comparison">

                        <div class="current-score">

                            Current Score:
                            <strong>${score}%</strong>

                        </div>

                    </div>


                    <div class="improvement-message ${messageClass}">

                        ${message}

                    </div>

                </div>

            `;
        });


    } catch (error) {

        console.error(
            "Improvement loading error:",
            error
        );
    }
}
document.addEventListener("DOMContentLoaded", function () {

    if (
        document.getElementById("improvementContainer")
    ) {

        loadImprovementPage();

    }

});
