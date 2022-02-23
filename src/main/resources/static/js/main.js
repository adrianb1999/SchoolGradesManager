document.addEventListener("DOMContentLoaded", function () {

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

        //TEACHER


        //ADMIN
    }
})
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
