function showLogin() {
    document.getElementById("loginSection").style.display = "flex";
    document.getElementById("registerSection").style.display = "none";

    window.scrollTo({
        top: document.getElementById("loginSection").offsetTop,
        behavior: "smooth"
    });
}

function showRegister() {
    document.getElementById("registerSection").style.display = "flex";
    document.getElementById("loginSection").style.display = "none";

    window.scrollTo({
        top: document.getElementById("registerSection").offsetTop,
        behavior: "smooth"
    });
}

function login() {

    const email = document.getElementById("loginEmail").value;
    const password = document.getElementById("loginPassword").value;

    if (email === "" || password === "") {

        document.getElementById("loginMessage").innerText =
            "Please enter email and password.";

        return;
    }

    document.getElementById("loginMessage").innerText =
        "Login UI working!";
}

function register() {

    const name = document.getElementById("registerName").value;
    const email = document.getElementById("registerEmail").value;
    const password = document.getElementById("registerPassword").value;

    if (name === "" || email === "" || password === "") {

        document.getElementById("registerMessage").innerText =
            "Please fill all fields.";

        return;
    }

    document.getElementById("registerMessage").innerText =
        "Registration UI working!";
}