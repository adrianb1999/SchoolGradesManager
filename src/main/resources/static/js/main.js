document.addEventListener("DOMContentLoaded", function () {

    if (document.body.classList.contains("passwordResetFrom")) {
        document.querySelector("#passwordResetForm").onsubmit = function () {
            resetPassword()
            return false
        }
    }

    if (document.body.classList.contains("passwordReset")) {
        document.querySelector("#passwordReset").onsubmit = function () {
            sendResetPasswordLink()
            return false
        }
    }

    if (document.body.classList.contains("login")) {
        document.querySelector("#loginForm").onsubmit = function () {
            login()
            return false
        }
    }

    if (document.body.classList.contains("user")) {

        document.querySelector("#logout").onclick = function () {
            logout()
        }

        document.querySelectorAll(".btn-toggle-panel").forEach(function (el) {
            el.onclick = function (e) {
                e.preventDefault()
                let target_el = el.dataset.target

                let active_panel = document.querySelector(".panels .panel.active")
                if (active_panel) {
                    active_panel.classList.remove("active")
                }

                document.querySelector(`.panels .panel${target_el}`).classList.add("active")
            }
        })

        document.querySelector("#updateEmailForm").onsubmit = function () {
            if (emailMatch)
                changeUserInfo('email')
            return false
        }

        document.querySelector("#updatePasswordForm").onsubmit = function () {
            if (passwordMatch)
                changeUserInfo('password')
            return false
        }
    }
})

var passwordMatch = false
var emailMatch = false

//---LOGOUT---
function logout() {
    Swal.fire({
        title: 'Asteapta!',
        text: "Esti sigur?",
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Da!',
        cancelButtonText:"Nu!"
    }).then((result) => {
        if (result.isConfirmed) {
            logoutUser()
        }
    })
}

function logoutUser() {
    deleteAllCookies()
    window.location.href = "index.html"
}

function deleteAllCookies() {
    let cookies = document.cookie.split(";");

    for (let i = 0; i < cookies.length; i++) {
        let cookie = cookies[i];
        let eqPos = cookie.indexOf("=");
        let name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    }
}

//---LOGOUT---

function login() {
    let name = document.getElementById("username").value;
    let password = document.getElementById("password").value;

    fetch('/login',
        {
            method: 'POST',
            body: JSON.stringify({'username': name, 'password': password}),
            credentials: 'include'
        })
        .then(response => {

            if (response.status === 200)
                window.location.href = "user.html"
            else if (response.status === 400) {
                response.json().then(data =>
                    showAlert(data.message)
                )

            } else if (response.status === 404) {
                showAlert("Cannot connect to the server!")
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}

function openModal(modalName) {
    switch (modalName) {
        case "teacherModal":
            document.getElementById("createTeacherModal").style.display = "block"
            break
        case "studentModal":
            document.getElementById("createStudentModal").style.display = "block"
            break
        case "classroomModal":
            document.getElementById("createClassroomModal").style.display = "block"
            break
        case "courseModal":
            document.getElementById("createCourseModal").style.display = "block"
            break
        case "markModal":
            document.getElementById("createMarkModal").style.display = "block"
            break
        case "absenceModal":
            document.getElementById("createAbsenceModal").style.display = "block"
            break
    }
}
function showAlert(message, type = 'error', title = 'Error', timer = 2000) {
    Swal.fire({
        icon: type,
        title: title,
        text: message,
        timer: timer,
    })
}

function changeUserInfo(inputType) {

    let email = document.querySelector("#emailInput").value
    let newPassword = document.querySelector("#passwordInput").value

    let cleanForm
    let data = {}
    if (inputType === "email") {
        data.email = email
        data.password = document.querySelector("#passwordInputEmail").value
        cleanForm = cleanEmailForm
    } else if (inputType === "password") {
        data.newPassword = newPassword
        data.password = document.querySelector("#oldPasswordInput").value
        cleanForm = cleanPasswordForm
    }

    fetch('/api/users/updateInfo',
        {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        }).then(response => {
        if (response.status === 200) {
            showAlert('User information updated!', 'success', 'Success!')
            cleanForm()
            if (inputType === "username")
                logoutUser();
        } else {
            response.json().then(data => {
                showAlert(data.message)
            })
        }
    }).catch((error) => {
        console.error('Error:', error)
    });
}

function setMatchText(match, message, textMessageId) {
    if (match && message !== '') {
        document.getElementById(textMessageId).style.color = 'green';
        document.getElementById(textMessageId).innerHTML = message + ' match!';
    } else if (!match && message !== '') {
        document.getElementById(textMessageId).style.color = 'red';
        document.getElementById(textMessageId).innerHTML = message + ' doens\'t match!';
    } else {
        document.getElementById(textMessageId).innerHTML = '';
    }
}

function matchChecking(firstId, secondId, textMessageId) {

    let match = document.getElementById(firstId).value === document.getElementById(secondId).value
    let currentForm

    if (firstId === 'emailInput') {
        emailMatch = match
        currentForm = 'Emails'
    } else if (firstId === 'passwordInput') {
        passwordMatch = match
        currentForm = 'Passwords'
    }

    if (document.getElementById(firstId).value === '' || document.getElementById(secondId).value === '') {
        setMatchText(match, '', textMessageId)
    } else {
        setMatchText(match, currentForm, textMessageId)
    }
}

function cleanEmailForm() {
    document.getElementById("emailInput").value = ""
    document.getElementById("confirmEmailInput").value = ""
    document.getElementById("passwordInputEmail").value = ""
    document.getElementById("emailWordMessage").innerHTML = ""
}

function cleanPasswordForm() {
    document.getElementById("passwordInput").value = ""
    document.getElementById("confirmPasswordInput").value = ""
    document.getElementById("oldPasswordInput").value = ""
    document.getElementById("passWordMessage").innerHTML = ""
}

function resetPassword() {
    const urlParams = new URLSearchParams(window.location.search);
    const confirmToken = urlParams.get('token')

    let password = document.getElementById("passwordReset").value

    fetch(`/api/user/passwordReset?token=${confirmToken}`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            'password': password
        })
    }).then(response => {
        if (response.status === 200){
            showAlert("Email sent!", 'success', 'Success!')
            window.location.href = "login.html";
        }
    }).catch((error) => {
        console.error('Error:', error);
    });
}

function sendResetPasswordLink() {
    let email = document.getElementById("email").value

    fetch('/api/user/resetPasswordLink', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            'email': email
        })
    }).then(response => {
        if (response.status === 200) {
            showAlert("Email sent!", 'success', 'Success!')
            window.location.href = "login.html";
        } else {
            response.json().then(data =>
                showAlert(data.message)
            )
        }
    }).catch((error) => {
        console.error('Error:', error);
    });
}