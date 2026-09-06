document.addEventListener("DOMContentLoaded", function () {

    const adminName = sessionStorage.getItem("userName");

    if (adminName) {
        document.getElementById("adminName").textContent = adminName;
    }

    loadQuestions();

});


// =====================================================
// LOAD QUESTIONS
// =====================================================

function loadQuestions() {

    fetch("./api/admin/questions")

        .then(response => {

            if (!response.ok) {
                throw new Error(
                    "Server returned " + response.status
                );
            }

            return response.json();

        })

        .then(data => {

            console.log("Admin questions:", data);

            if (!data.success) {

                document.getElementById(
                    "questionsContainer"
                ).innerHTML =
                    "<p>Unable to load questions.</p>";

                return;
            }

            displayQuestions(data.questions);

        })

        .catch(error => {

            console.error(
                "Question loading error:",
                error
            );

            document.getElementById(
                "questionsContainer"
            ).innerHTML =
                "<p>Unable to load questions.</p>";
        });
}


// =====================================================
// DISPLAY QUESTIONS
// =====================================================

function displayQuestions(questions) {

    const container =
        document.getElementById("questionsContainer");

    if (questions.length === 0) {

        container.innerHTML =
            "<p>No questions found.</p>";

        return;
    }

    container.innerHTML = "";

    questions.forEach(function (q) {

        container.innerHTML += `

            <div class="admin-question-card">

                <div class="question-card-top">

                    <span>
                        Question #${q.questionId}
                    </span>

                    <span class="difficulty">
                        ${q.difficulty}
                    </span>

                </div>

                <h3>
                    ${q.question}
                </h3>

                <p>
                    <strong>Skill:</strong>
                    ${q.skill}
                </p>

                <p>
                    <strong>Topic:</strong>
                    ${q.topic}
                </p>

                <div class="options">

                    <div>A. ${q.optionA}</div>

                    <div>B. ${q.optionB}</div>

                    <div>C. ${q.optionC}</div>

                    <div>D. ${q.optionD}</div>

                </div>

                <div class="correct-answer">

                    Correct Answer:
                    <strong>${q.correctAnswer}</strong>

                </div>

                ${
                    q.explanation
                    ? `
                    <div class="question-explanation">
                        <strong>Explanation:</strong>
                        ${q.explanation}
                    </div>
                    `
                    : ""
                }

                <!-- ACTION BUTTONS -->

                <div class="question-actions">

                    <button
                        type="button"
                        class="edit-question-btn"
                        onclick='editQuestion(${JSON.stringify(q)})'>
                        ✏️ Edit
                    </button>

                    <button
                        type="button"
                        class="delete-question-btn"
                        onclick="deleteQuestion(${q.questionId})">
                        🗑️ Delete
                    </button>

                </div>

            </div>
        `;
    });
}


// =====================================================
// SHOW ADD QUESTION FORM
// =====================================================

function showAddQuestion() {

    const modal =
        document.getElementById("addQuestionModal");

    modal.style.display = "flex";

    document.querySelector(".modal-header h2").textContent =
        "Add New Question";

    document.querySelector(
        "#addQuestionForm .add-question-btn"
    ).textContent = "Add Question";

    document.getElementById("questionId").value = "";
}


// =====================================================
// CLOSE QUESTION FORM
// =====================================================

function closeAddQuestion() {

    const modal =
        document.getElementById("addQuestionModal");

    modal.style.display = "none";

    document.getElementById(
        "addQuestionForm"
    ).reset();

    document.getElementById("questionId").value = "";

    document.querySelector(".modal-header h2").textContent =
        "Add New Question";

    document.querySelector(
        "#addQuestionForm .add-question-btn"
    ).textContent = "Add Question";
}


// =====================================================
// EDIT QUESTION
// =====================================================

function editQuestion(q) {

    const modal =
        document.getElementById("addQuestionModal");

    // Fill existing data

    document.getElementById("questionId").value =
        q.questionId;

    document.getElementById("skill").value =
        q.skill;

    document.getElementById("topic").value =
        q.topic;

    document.getElementById("difficulty").value =
        q.difficulty;

    document.getElementById("question").value =
        q.question;

    document.getElementById("optionA").value =
        q.optionA;

    document.getElementById("optionB").value =
        q.optionB;

    document.getElementById("optionC").value =
        q.optionC;

    document.getElementById("optionD").value =
        q.optionD;

    document.getElementById("correctAnswer").value =
        q.correctAnswer;

    document.getElementById("explanation").value =
        q.explanation || "";


    // Change modal title

    document.querySelector(".modal-header h2").textContent =
        "Edit Question";


    // Change button text

    document.querySelector(
        "#addQuestionForm .add-question-btn"
    ).textContent = "Update Question";


    // Open modal

    modal.style.display = "flex";
}


// =====================================================
// ADD / UPDATE QUESTION
// =====================================================

function addQuestion(event) {

    event.preventDefault();

    const questionId =
        document.getElementById("questionId").value;

    const action =
        questionId ? "update" : "add";


    const formData =
        new URLSearchParams();


    formData.append(
        "action",
        action
    );

    if (questionId) {

        formData.append(
            "questionId",
            questionId
        );
    }


    formData.append(
        "skill",
        document.getElementById("skill").value.trim()
    );

    formData.append(
        "topic",
        document.getElementById("topic").value.trim()
    );

    formData.append(
        "difficulty",
        document.getElementById("difficulty").value
    );

    formData.append(
        "question",
        document.getElementById("question").value.trim()
    );

    formData.append(
        "optionA",
        document.getElementById("optionA").value.trim()
    );

    formData.append(
        "optionB",
        document.getElementById("optionB").value.trim()
    );

    formData.append(
        "optionC",
        document.getElementById("optionC").value.trim()
    );

    formData.append(
        "optionD",
        document.getElementById("optionD").value.trim()
    );

    formData.append(
        "correctAnswer",
        document.getElementById("correctAnswer").value
    );

    formData.append(
        "explanation",
        document.getElementById("explanation").value.trim()
    );


    // SEND TO SERVLET

    fetch("./api/admin/questions", {

        method: "POST",

        headers: {
            "Content-Type":
                "application/x-www-form-urlencoded"
        },

        body: formData.toString()

    })

    .then(response => {

        if (!response.ok) {

            throw new Error(
                "Server returned " + response.status
            );
        }

        return response.json();

    })

    .then(data => {

        console.log("Question response:", data);

        if (data.success) {

            if (action === "update") {

                alert(
                    "Question updated successfully!"
                );

            } else {

                alert(
                    "Question added successfully!"
                );
            }

            closeAddQuestion();

            loadQuestions();

        } else {

            alert(
                data.message ||
                "Operation failed."
            );
        }

    })

    .catch(error => {

        console.error(
            "Question operation error:",
            error
        );

        alert(
            "Error while processing question."
        );
    });
}


// =====================================================
// DELETE QUESTION
// =====================================================

function deleteQuestion(questionId) {

    const confirmed =
        confirm(
            "Are you sure you want to delete Question #" +
            questionId +
            "?"
        );

    if (!confirmed) {
        return;
    }


    const formData =
        new URLSearchParams();

    formData.append(
        "action",
        "delete"
    );

    formData.append(
        "questionId",
        questionId
    );


    fetch("./api/admin/questions", {

        method: "POST",

        headers: {

            "Content-Type":
                "application/x-www-form-urlencoded"
        },

        body: formData.toString()

    })

    .then(response => {

        if (!response.ok) {

            throw new Error(
                "Server returned " + response.status
            );
        }

        return response.json();

    })

    .then(data => {

        console.log("Delete response:", data);

        if (data.success) {

            alert(
                "Question deleted successfully!"
            );

            loadQuestions();

        } else {

            alert(
                data.message ||
                "Failed to delete question."
            );
        }

    })

    .catch(error => {

        console.error(
            "Delete question error:",
            error
        );

        alert(
            "Error while deleting question."
        );
    });
}


// =====================================================
// LOGOUT
// =====================================================

function logout() {

    sessionStorage.clear();

    window.location.href = "index.html";
}