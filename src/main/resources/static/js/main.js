document.addEventListener("DOMContentLoaded", function () {

    if (document.body.classList.contains("login")) {
        document.querySelector("#loginForm").onsubmit = function () {
            console.log("LOGIN!")
            login()
            return false
        }
    }
})

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
                    console.log(data.message)
                )

            } else if (response.status === 404) {
                showAlert("Cannot connect to the server!")
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}