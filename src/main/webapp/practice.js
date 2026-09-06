let skill = "";
let topic = "";
let count = 5;

let questions = [];


// Load practice information from URL
function loadPracticePage() {

    const params = new URLSearchParams(
        window.location.search
    );

    skill = params.get("skill");
    topic = params.get("topic");

    const countFromURL = params.get("count");

    if (countFromURL) {
        count = parseInt(countFromURL);
    }


    // Check required values
    if (!skill || !topic) {

        document.getElementById(
            "questionsContainer"
        ).innerHTML =
            "<p>Invalid practice request.</p>";

        return;
    }


    // Display information
    document.getElementById(
        "practiceSkill"
    ).textContent = skill;

    document.getElementById(
        "practiceTopic"
    ).textContent = topic;

    document.getElementById(
        "practiceCount"
    ).textContent = count;


    loadQuestions();
}


// Get questions from Java backend
async function loadQuestions() {

    try {

        const response = await fetch(
            "/PersonalizedSkillAssessment/api/practice"
            + "?skill="
            + encodeURIComponent(skill)
            + "&topic="
            + encodeURIComponent(topic)
            + "&count="
            + count
        );


        if (!response.ok) {

            throw new Error(
                "Server returned "
                + response.status
            );

        }


        const data = await response.json();

        console.log(
            "Practice questions:",
            data
        );


        if (!data.success) {

            document.getElementById(
                "questionsContainer"
            ).innerHTML =
                "<p>"
                + data.message
                + "</p>";

            return;
        }


		questions = data.questions;

		// Update question count with actual number available
		document.getElementById(
		    "practiceCount"
		).textContent = questions.length;

		displayQuestions();
    }
    catch (error) {

        console.error(
            "Practice loading error:",
            error
        );

        document.getElementById(
            "questionsContainer"
        ).innerHTML =
            "<p>Unable to load practice questions.</p>";
    }
}


// Display questions
function displayQuestions() {

    const container =
        document.getElementById(
            "questionsContainer"
        );

    container.innerHTML = "";


    questions.forEach(
        (q, index) => {

            container.innerHTML += `

                <div class="practice-question">

                    <h3>
                        Question ${index + 1}
                    </h3>

                    <p>
                        ${q.question}
                    </p>


                    <label>
                        <input
                            type="radio"
                            name="question${q.questionId}"
                            value="A">

                        A. ${q.optionA}
                    </label>


                    <label>
                        <input
                            type="radio"
                            name="question${q.questionId}"
                            value="B">

                        B. ${q.optionB}
                    </label>


                    <label>
                        <input
                            type="radio"
                            name="question${q.questionId}"
                            value="C">

                        C. ${q.optionC}
                    </label>


                    <label>
                        <input
                            type="radio"
                            name="question${q.questionId}"
                            value="D">

                        D. ${q.optionD}
                    </label>

                </div>
            `;
        }
    );
}


// Submit practice
async function submitPractice() {

    const userId =
        sessionStorage.getItem("userId");


    if (!userId) {

        alert("Please login first.");

        window.location.href =
            "index.html";

        return;
    }


    // Store selected answers
    const answers = {};


    questions.forEach(function(q) {

        const selected =
            document.querySelector(
                'input[name="question'
                + q.questionId
                + '"]:checked'
            );


        if (selected) {

            answers[q.questionId] =
                selected.value;
        }

    });


    // Check whether all questions are answered
	// Check whether all questions are answered

	if (
	    Object.keys(answers).length
	    !== questions.length
	) {

	    const message =
	        document.getElementById("practiceMessage");

	    message.textContent =
	        "Please answer all questions before submitting.";

	    message.style.display = "block";

	    return;
	}


    const requestData = {

        userId: parseInt(userId),

        skill: skill,

        topic: topic,

        answers: answers
    };


    console.log(
        "Submitting practice:",
        requestData
    );


    try {

        const response =
            await fetch(
                "/PersonalizedSkillAssessment/api/practice",
                {
                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify(
                            requestData
                        )
                }
            );


        if (!response.ok) {

            throw new Error(
                "Server returned "
                + response.status
            );
        }


        const data =
            await response.json();


        console.log(
            "Practice result:",
            data
        );


        if (!data.success) {

            alert(
                data.message
                || "Practice submission failed."
            );

            return;
        }


        // Show result
        showPracticeResult(data);

    }
    catch (error) {

        console.error(
            "Practice submission error:",
            error
        );

        alert(
            "Unable to submit practice."
        );
    }
}
function showPracticeResult(data) {

    const container =
        document.getElementById(
            "questionsContainer"
        );


    container.innerHTML = `

        <div class="practice-result">

            <h2>🎉 Practice Completed!</h2>

            <p>
                <strong>Total Questions:</strong>
                ${data.total}
            </p>

            <p>
                <strong>Correct:</strong>
                ${data.correct}
            </p>

            <p>
                <strong>Wrong:</strong>
                ${data.wrong}
            </p>

            <p>
                <strong>Practice Score:</strong>
                ${data.score}%
            </p>

            <p>
                ${
                    data.score >= 75
                    ? "🟢 Great job!"
                    : data.score >= 50
                    ? "🟡 Keep practicing!"
                    : "🔴 More practice recommended."
                }
            </p>

        </div>

    `;


    // Hide submit button
    document.getElementById(
        "submitPracticeBtn"
    ).style.display = "none";
}
loadPracticePage();