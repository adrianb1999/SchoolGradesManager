document.addEventListener("DOMContentLoaded", function () {

    if (document.body.classList.contains("login")) {
        document.querySelector("#loginForm").onsubmit = function () {
            login()
            return false
        }
    }

    if (document.body.classList.contains("user")) {

        //TEACHER
        teacherGetCourses()

        //ADMIN
        let createTeacher = document.getElementById("createTeacherModal");
        let createStudent = document.getElementById("createStudentModal");
        let createClassroom = document.getElementById("createClassroomModal");
        let createCourse = document.getElementById("createCourseModal");

        let span1 = document.getElementsByClassName("close")[0];
        let span2 = document.getElementsByClassName("close")[1];
        let span3 = document.getElementsByClassName("close")[2];
        let span4 = document.getElementsByClassName("close")[3];

        span1.onclick = function (f) {
            resetTeacherForm()
            createTeacher.style.display = "none";
        }
        span2.onclick = function (f) {
            resetStudentForm()
            createStudent.style.display = "none";
        }
        span3.onclick = function (f) {
            resetClassroomForm()
            createClassroom.style.display = "none";
        }
        span4.onclick = function (f) {
            resetCourseForm()
            createCourse.style.display = "none";
        }

        //CLOSE MODAL
        window.onclick = function (event) {
            // if (event.target === createTeacher || event.target === createStudent ||
            //     event.target === createStudentModal ||  event.target === createStudentModal) {
            //     closeModal()
            // }
        }

        //SWITCH PANELS
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

        //FORMS
        document.querySelector("#createTeacherForm").onsubmit = function () {
            if(requestTeacherType === "POST")
                adminAddTeacher()
            else if(requestTeacherType === "PUT")
                adminEditTeacher()
            return false
        }

        document.querySelector("#createStudentForm").onsubmit = function () {
            if(requestStudentType === "POST")
                adminAddStudent()
            else if(requestStudentType === "PUT")
                adminEditStudent()
            return false
        }

        document.querySelector("#createClassroomForm").onsubmit = function () {
            if(requestClassroomType === "POST")
                adminAddClassroom()
            else if(requestClassroomType === "PUT")
                adminEditClassroom()
            return false
        }

        document.querySelector("#createCourseForm").onsubmit = function () {
            if(requestCourseType === "POST")
                adminAddCourse()
            else if(requestCourseType === "PUT")
                adminEditCourse()
            return false
        }
        adminGetData()


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

//--- ADMIN JS START ---

//TODO FOOTER, PAGE BUTTONS, PAGE IN BACKEND, DELETE IN BACKEND AND FRONTEND
function adminGetData() {
    adminGetCourses()
    adminGetStudents()
    adminGetClassrooms()
    adminGetTeachers()
}

let teachers
let courses
let classrooms
let students

function injectInCreateClassroomForm() {
    let form = document.getElementById("classroomClassMaster")
    form.innerHTML = ''
    for(teacher of teachers){
        form.insertAdjacentHTML('beforeend',
            `<option value="${teacher.id}">${teacher.firstName} ${teacher.lastName}</option>`)
    }
}

function injectInCreateStudentForm(){
    let form = document.getElementById("studentClassroom")
    form.innerHTML = ''
    for(classroom of classrooms){
        form.insertAdjacentHTML('beforeend',
            `<option value="${classroom.id}">${classroom.name}</option>`)
    }
}

function injectInCreateCourseForm(){
    let teacherSelect = document.getElementById("courseTeacher")
    let classroomSelect = document.getElementById("courseClassroom")

    teacherSelect.innerHTML = ''
    classroomSelect.innerHTML = ''

    for(teacher of teachers){
        teacherSelect.insertAdjacentHTML('beforeend',
            `<option value="${teacher.id}">${teacher.firstName} ${teacher.lastName}</option>`)
    }

    for(classroom of classrooms){
        classroomSelect.insertAdjacentHTML('beforeend',
            `<option value="${classroom.id}">${classroom.name}</option>`)
    }
}

function adminGetTeachers() {

    let table = document.querySelector("#teacherTable tbody");

    fetch('/api/teachers',
        {
            method: 'GET',
        })
        .then(response => response.json())
        .then(data => {
            teachers = data
            table.innerHTML = ''
            injectInCreateClassroomForm()
            for (i of data) {
                table.insertAdjacentHTML("beforeend",
                    `<tr>
                               <td>${i.firstName}</td>
                               <td>${i.lastName}</td>
                               <td>${i.courses.join(",")}</td>
                               <td>
                                                <button class="changeButton" onClick="editTeacher(${i.id})">
                                                        <span class="material-icons">
                                                            edit
                                                        </span>
                                                </button>
                                                <button class="deleteButton" onClick="deleteTeacher(${i.id})"> 
                                                        <span class="material-icons">
                                                            clear
                                                        </span>                                               
                                                </button>
                               </td>
                        </tr>`
                )
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}

function adminGetStudents() {
    let table = document.querySelector("#studentTable tbody");

    fetch('/api/students',
        {
            method: 'GET',
        })
        .then(response => response.json())
        .then(data => {
            table.innerHTML = ''
            students = data
            for (i of data) {
                table.insertAdjacentHTML("beforeend",
                    `<tr>
                               <td>${i.firstName}</td>
                               <td>${i.lastName}</td>
                               <td>${i.classroom}</td> 
                               <td>
                                    <button class="changeButton" onClick="editStudent(${i.id})">
                                            <span class="material-icons">
                                                edit
                                            </span>
                                    </button>
                                    <button class="deleteButton" onClick="deleteStudent(${i.id})"> 
                                            <span class="material-icons">
                                                clear
                                            </span>                                               
                                    </button>
                               </td>
                        </tr>`
                )
            }

        })
        .catch((error) => {
            console.error('Error:', error);
        });
}

function adminGetCourses() {
    let table = document.querySelector("#coursesTable tbody");

    fetch('/api/allCourses',
        {
            method: 'GET',
        })
        .then(response => response.json())
        .then(data => {
            courses = data
            table.innerHTML = ''
            for (i of data) {
                table.insertAdjacentHTML("beforeend",
                    `<tr>
                               <td>${i.name}</td>
                               <td>${i.teacher.firstName} ${i.teacher.lastName}</td> 
                               <td>${i.classRoom.name}</td>
                               <td>
                                    <button class="changeButton" onClick="editCourse(${i.id})">
                                            <span class="material-icons">
                                                edit
                                            </span>
                                    </button>
                                    <button class="deleteButton" onClick="deleteCourse(${i.id})"> 
                                            <span class="material-icons">
                                                clear
                                            </span>                                               
                                    </button>
                               </td>
                        </tr>`
                )
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}

function adminGetClassrooms() {
    let table = document.querySelector("#classroomsTable tbody");

    fetch('/api/classrooms',
        {
            method: 'GET',
        })
        .then(response => response.json())
        .then(data => {
            classrooms = data
            injectInCreateStudentForm()
            injectInCreateCourseForm()
            table.innerHTML = ''
            for (i of data) {
                table.insertAdjacentHTML("beforeend",
                    `<tr>
                                <td>${i.name}</td>
                               <td>${i.classMaster.firstName} ${i.classMaster.lastName}</td>      
                               <td>
                                    <button class="changeButton" onClick="editClassroom(${i.id})">
                                            <span class="material-icons">
                                                edit
                                            </span>
                                    </button>
                                    <button class="deleteButton" onClick="deleteClassroom(${i.id})"> 
                                            <span class="material-icons">
                                                clear
                                            </span>                                               
                                    </button>
                               </td>        
                        </tr>`
                )
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
    }
}

//TEACHERS
function adminAddTeacher(){
    let username = document.getElementById("teacherUsername").value
    let firstName = document.getElementById("teacherFirstName").value
    let lastName = document.getElementById("teacherLastName").value
    let email = document.getElementById("teacherEmail").value

    fetch('/api/createTeacher',
        {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'username': username,
                'firstName':firstName,
                'lastName':lastName,
                'email': email
            }),
        })
        .then(response => {
            if (response.status === 200){
                console.log("Teacher created successfully!")
                closeModal()
                resetTeacherForm()
                adminGetTeachers()
            }
            else if (response.status === 400) {
                response.json().then(data =>
                    console.log(data.message)
                )

            } else if (response.status === 404) {
                console.log("")
                //showAlert("Cannot connect to the server!")
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        });

}

function adminEditTeacher(){
    let username = document.getElementById("teacherUsername").value
    let firstName = document.getElementById("teacherFirstName").value
    let lastName = document.getElementById("teacherLastName").value
    let email = document.getElementById("teacherEmail").value

    fetch(`/api/createTeacher/${currentTeacherId}`,
        {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'username': username,
                'firstName':firstName,
                'lastName':lastName,
                'email':email,
                'roles':"ROLE_TEACHER"
            }),
        })
        .then(response => {

            if (response.status === 200){
                console.log("Teacher created successfully!")
                closeModal()
                resetTeacherForm()
                adminGetTeachers()
            }
            else if (response.status === 400) {
                response.json().then(data =>
                    console.log(data.message)
                )

            } else if (response.status === 404) {
                console.log("")
                //showAlert("Cannot connect to the server!")
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}

//STUDENTS
function adminAddStudent(){
    let username = document.getElementById("studentUsername").value
    let firstName = document.getElementById("studentFirstName").value
    let lastName = document.getElementById("studentLastName").value
    let email = document.getElementById("studentEmail").value
    let classroom = document.getElementById("studentClassroom").value
    fetch('/api/createStudent',
        {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'username': username,
                'firstName':firstName,
                'lastName':lastName,
                'email':email,
                'roles':"ROLE_STUDENT",
                'classroom_id':classroom
            }),
        })
        .then(response => {

            if (response.status === 200){
                console.log("Student created successfully!")
                closeModal()
                resetStudentForm()
                adminGetStudents()
            }
            else if (response.status === 400) {
                response.json().then(data =>
                    console.log(data.message)
                )

            } else if (response.status === 404) {
                console.log("")
                //showAlert("Cannot connect to the server!")
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}

function adminEditStudent(){
    let username = document.getElementById("studentUsername").value
    let firstName = document.getElementById("studentFirstName").value
    let lastName = document.getElementById("studentLastName").value
    let email = document.getElementById("studentEmail").value
    let classroom = document.getElementById("studentClassroom").value

    fetch(`/api/createStudent/${currentStudentId}`,
        {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'username': username,
                'firstName':firstName,
                'lastName':lastName,
                'email':email,
                'classroom_id':classroom
            }),
        })
        .then(response => {

            if (response.status === 200){
                console.log("Student created successfully!")
                closeModal()
                resetStudentForm()
                adminGetStudents()
            }
            else if (response.status === 400) {
                response.json().then(data =>
                    console.log(data.message)
                )

            } else if (response.status === 404) {
                console.log("")
                //showAlert("Cannot connect to the server!")
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}

//COURSES
function adminAddCourse(){
    let classRoom = document.getElementById("courseClassroom").value
    let courseName = document.getElementById("courseName").value
    let courseTeacher = document.getElementById("courseTeacher").value

    fetch('/api/courses',
        {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'classroom_id': classRoom,
                'name': courseName,
                'teacher_id':courseTeacher,
            }),
        })
        .then(response => {
            if (response.status === 200) {
                console.log("Course created successfully!")
                closeModal()
                resetCourseForm()
                adminGetCourses()
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        })
}

function adminEditCourse(){
    let classRoom = document.getElementById("courseClassroom").value
    let courseName = document.getElementById("courseName").value
    let courseTeacher = document.getElementById("courseTeacher").value

    fetch(`/api/courses/${currentCourseId}`,
        {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'classroom_id': classRoom,
                'name': courseName,
                'teacher_id':courseTeacher,
            }),
        })
        .then(response => {
            if (response.status === 200) {
                console.log("Course created successfully!")
                closeModal()
                resetCourseForm()
                adminGetCourses()
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        })
}

//CLASSROOM
function adminAddClassroom(){
    let classroomName = document.getElementById("classroomName").value
    let classMaster = document.getElementById("classroomClassMaster").value

    fetch('/api/createClassroom',
        {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'name': classroomName,
                'teacher_id':classMaster,
            }),
        })
        .then(response => {
            if (response.status === 200) {
                console.log("Course created successfully!")
                closeModal()
                resetClassroomForm()
                adminGetClassrooms()
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        })
}

function adminEditClassroom(){
    let classroomName = document.getElementById("classroomName").value
    let classMaster = document.getElementById("classroomClassMaster").value

    fetch(`/api/createClassroom/${currentClassroomId}`,
        {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'name': classroomName,
                'teacher_id':classMaster,
            }),
        })
        .then(response => {
            if (response.status === 200) {
                console.log("Course edited successfully!")
                closeModal()
                resetClassroomForm()
                adminGetClassrooms()
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        })
}

function closeModal() {
    document.getElementById("createTeacherModal").style.display = "none"
    document.getElementById("createStudentModal").style.display = "none"
    document.getElementById("createClassroomModal").style.display = "none"
    document.getElementById("createCourseModal").style.display = "none"
}

//EDIT MODALS

let requestTeacherType = "POST"
let requestStudentType = "POST"
let requestClassroomType = "POST"
let requestCourseType = "POST"

let currentTeacherId
let currentStudentId
let currentCourseId
let currentClassroomId

function editTeacher(teacherId){
    for(let i = 0; i < teachers.length; i++){
        if(teachers[i].id === teacherId) {
            document.getElementById("teacherUsername").value = teachers[i].username
            document.getElementById("teacherFirstName").value = teachers[i].firstName
            document.getElementById("teacherLastName").value = teachers[i].lastName
            document.getElementById("teacherEmail").value = teachers[i].email
        }
    }
    document.getElementById("createTeacherModal").style.display = "block";

    document.getElementById("createTeacherButton").textContent = "Edit teacher"
    document.getElementById("createTeacherFormTitle").textContent = "Edit teacher"

    requestTeacherType = "PUT"

    currentTeacherId = teacherId
}

function editStudent(studentId){
    for(let i = 0; i < students.length; i++){
        if(students[i].id === studentId) {
            document.getElementById("studentUsername").value = students[i].username
            document.getElementById("studentFirstName").value = students[i].firstName
            document.getElementById("studentLastName").value = students[i].lastName
            document.getElementById("studentEmail").value = students[i].email
            document.getElementById("studentClassroom").value = students[i].classroomId
        }
    }
    document.getElementById("createStudentModal").style.display = "block";

    document.getElementById("createStudentButton").textContent = "Edit student"
    document.getElementById("createStudentFormTitle").textContent = "Edit student"

    requestStudentType = "PUT"

    currentStudentId = studentId
}

function editCourse(courseId){
    for(let i = 0; i < courses.length; i++){
        if(courses[i].id === courseId){
            document.getElementById("courseName").value = courses[i].name
            document.getElementById("courseTeacher").value = courses[i].teacher.id
            document.getElementById("courseClassroom").value = courses[i].classRoom.id
        }
    }
    document.getElementById("createCourseModal").style.display = "block";

    document.getElementById("createCourseButton").textContent = "Edit course"
    document.getElementById("createCourseFormTitle").textContent = "Edit course"

    requestCourseType = "PUT"

    currentCourseId = courseId
}

function editClassroom(classroomId){
    for(let i = 0; i < classrooms.length; i++){
        if(classrooms[i].id === classroomId){
            document.getElementById("classroomName").value = classrooms[i].name
            document.getElementById("classroomClassMaster").value = classrooms[i].classMaster.id
        }
    }

    document.getElementById("createClassroomModal").style.display = "block";

    document.getElementById("createClassroomButton").textContent = "Edit classroom"
    document.getElementById("createClassroomFormTitle").textContent = "Edit classroom"

    requestClassroomType = "PUT"

    currentClassroomId = classroomId
}

//RESET FORMS
function resetTeacherForm(){
    currentTeacherId = null
    requestTeacherType = "POST"

    document.getElementById("teacherUsername").value = ""
    document.getElementById("teacherFirstName").value = ""
    document.getElementById("teacherLastName").value = ""
    document.getElementById("teacherEmail").value = ""

    document.getElementById("createTeacherButton").textContent = "Create"
    document.getElementById("createTeacherFormTitle").textContent = "Create teacher account"
}

function resetStudentForm(){
    currentStudentId = null
    requestStudentType = "POST"

    document.getElementById("studentUsername").value = ""
    document.getElementById("studentFirstName").value = ""
    document.getElementById("studentLastName").value = ""
    document.getElementById("studentEmail").value = ""
    document.getElementById("studentClassroom").value = ""

    document.getElementById("createStudentButton").textContent = "Create"
    document.getElementById("createStudentFormTitle").textContent = "Create student account"
}

function resetCourseForm(){
    currentCourseId = null
    requestCourseType = "POST"

    document.getElementById("courseName").value = ""
    document.getElementById("courseTeacher").value = ""
    document.getElementById("courseClassroom").value = ""

    document.getElementById("createCourseButton").textContent = "Create"
    document.getElementById("createCourseFormTitle").textContent = "Create course"
}

function resetClassroomForm(){
    currentClassroomId = null
    requestClassroomType = "POST"

    document.getElementById("classroomName").value = ""
    document.getElementById("classroomClassMaster").value = ""

    document.getElementById("createClassroomButton").textContent = "Create"
    document.getElementById("createClassroomFormTitle").textContent = "Create classroom"
}
//--- ADMIN JS END ---

//--- TEACHER JS START ---

let teacherCourses
let teacherClassrooms

function teacherGetCourses(){
    fetch('/api/courses',
        {
            method: 'GET',
        })
        .then(response => response.json())
        .then(data => {

            console.log("GET COURSE")

            let classroomList = document.getElementById("teacherClassroomsList")

            classroomList.innerHTML = ""
            teacherCourses = data
            for(let i of data){
                classroomList.insertAdjacentHTML('beforeend',`
                    <option value="${i.classRoom.id}">${i.name} ${i.classRoom.name}</option>`
                )}
            teacherLoadStudent()
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}

function teacherLoadStudent(){
    let classroomId =  document.getElementById("teacherClassroomsList").value

    let teacherTable = document.querySelector("#teacherTable tbody")
    console.log("load stud " + classroomId)
    teacherTable.innerHTML = ''
    
    for(let i of teacherCourses){
        if(i.classRoom.id == classroomId){

            let student = i.classRoom.students
            console.log("students=" + student)
            for(let j of student){
            teacherTable.insertAdjacentHTML('beforeend',
                `
                <tr>
                    <td>${j.firstName}</td>
                    <td>${j.lastName}</td>
                </tr>
                `
            )}
        }
    }
}

//--- TEACHER JS END ---