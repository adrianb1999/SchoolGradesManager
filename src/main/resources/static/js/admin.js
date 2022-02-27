document.addEventListener("DOMContentLoaded", function () {
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
        if (event.target === createTeacher || event.target === createStudent ||
            event.target === createStudentModal ||  event.target === createStudentModal) {
            closeModal()
        }
    }

    //FORMS
    document.querySelector("#createTeacherForm").onsubmit = function () {
        if (requestTeacherType === "POST")
            adminAddTeacher()
        else if (requestTeacherType === "PUT")
            adminEditTeacher()
        return false
    }

    document.querySelector("#createStudentForm").onsubmit = function () {
        if (requestStudentType === "POST")
            adminAddStudent()
        else if (requestStudentType === "PUT")
            adminEditStudent()
        return false
    }

    document.querySelector("#createClassroomForm").onsubmit = function () {
        if (requestClassroomType === "POST")
            adminAddClassroom()
        else if (requestClassroomType === "PUT")
            adminEditClassroom()
        return false
    }

    document.querySelector("#createCourseForm").onsubmit = function () {
        if (requestCourseType === "POST")
            adminAddCourse()
        else if (requestCourseType === "PUT")
            adminEditCourse()
        return false
    }
    adminGetData()
})

function adminGetData() {
    adminGetTeachers()
    adminGetStudents()
    adminGetCourses()
    adminGetClassrooms()
}

let teachers
let courses
let classrooms
let students

function injectInCreateClassroomForm() {
    let form = document.getElementById("classroomClassMaster")
    form.innerHTML = ''
    for (teacher of teachers) {
        form.insertAdjacentHTML('beforeend',
            `<option value="${teacher.id}">${teacher.firstName} ${teacher.lastName}</option>`)
    }
}

function injectInCreateStudentForm() {
    let form = document.getElementById("studentClassroom")
    form.innerHTML = ''
    for (classroom of classrooms) {
        form.insertAdjacentHTML('beforeend',
            `<option value="${classroom.id}">${classroom.name}</option>`)
    }
}

function injectInCreateCourseForm() {
    let teacherSelect = document.getElementById("courseTeacher")
    let classroomSelect = document.getElementById("courseClassroom")

    teacherSelect.innerHTML = ''
    classroomSelect.innerHTML = ''

    for (teacher of teachers) {
        teacherSelect.insertAdjacentHTML('beforeend',
            `<option value="${teacher.id}">${teacher.firstName} ${teacher.lastName}</option>`)
    }

    for (classroom of classrooms) {
        classroomSelect.insertAdjacentHTML('beforeend',
            `<option value="${classroom.id}">${classroom.name}</option>`)
    }
}

function adminGetTeachers() {

    let table = document.querySelector("#teacherTable tbody");

    fetch('/api/admin/allTeachers',
        {
            method: 'GET',
        })
        .then(response => response.json())
        .then(data => {
            teachers = data
            table.innerHTML = ''
            for (i of data) {
                table.insertAdjacentHTML("beforeend",
                    `<tr>
                               <td>${i.firstName} ${i.lastName}</td>
                               <td>${i.email}</td>
                               <td>${i.courses.map(x => `<span class="tag">${x}</span>`).join(" ")}</td>
                               <td>
                                                <button class="changeButton" onClick="editTeacher(${i.id})">
                                                        <span class="material-icons">
                                                            edit
                                                        </span>
                                                </button>
                               </td>
                        </tr>`
                )
            }
            injectInCreateClassroomForm()
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}

function adminGetStudents() {
    let table = document.querySelector("#studentTable tbody");

    fetch('/api/admin/allStudents',
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
                               <td>${i.firstName} ${i.lastName}</td>
                               <td>${i.email}</td>
                               <td>${i.classroom}</td> 
                               <td>
                                    <button class="changeButton" onClick="editStudent(${i.id})">
                                            <span class="material-icons">
                                                edit
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

    fetch('/api/admin/allCourses',
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
                               <td>${i.classroomName}</td>
                               <td>${i.name}</td>
                               <td>${i.teacherName}</td>                           
                               
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

    fetch('/api/admin/allClassrooms',
        {
            method: 'GET',
        })
        .then(response => response.json())
        .then(data => {
            classrooms = data

            table.innerHTML = ''
            for (i of data) {
                table.insertAdjacentHTML("beforeend",
                    `<tr>
                               <td>${i.name}</td>
                               <td>${i.classMasterName}</td>    
                               <td>${i.numOfStudents}</td>                             
                        </tr>`
                )
            }
            injectInCreateStudentForm()
            injectInCreateCourseForm()
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}

//TEACHERS
function adminAddTeacher() {
    let username = document.getElementById("teacherUsername").value
    let firstName = document.getElementById("teacherFirstName").value
    let lastName = document.getElementById("teacherLastName").value
    let email = document.getElementById("teacherEmail").value

    fetch('/api/admin/createTeacher',
        {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'username': username,
                'firstName': firstName,
                'lastName': lastName,
                'email': email
            }),
        })
        .then(response => {
            if (response.status === 200) {
                showAlert('Teacher account created successfully!', 'success', 'Success!')
                closeModal()
                resetTeacherForm()
                adminGetTeachers()
            } else if (response.status === 400) {
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

function adminEditTeacher() {
    let username = document.getElementById("teacherUsername").value
    let firstName = document.getElementById("teacherFirstName").value
    let lastName = document.getElementById("teacherLastName").value
    let email = document.getElementById("teacherEmail").value

    fetch(`/api/admin/editTeacher/${currentTeacherId}`,
        {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'username': username,
                'firstName': firstName,
                'lastName': lastName,
                'email': email,
                'roles': "ROLE_TEACHER"
            }),
        })
        .then(response => {

            if (response.status === 200) {
                showAlert('Teacher account edited successfully!', 'success', 'Success!')
                closeModal()
                resetTeacherForm()
                adminGetTeachers()
            } else if (response.status === 400) {
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
function adminAddStudent() {
    let username = document.getElementById("studentUsername").value
    let firstName = document.getElementById("studentFirstName").value
    let lastName = document.getElementById("studentLastName").value
    let email = document.getElementById("studentEmail").value
    let classroom = document.getElementById("studentClassroom").value

    fetch('/api/admin/createStudent',
        {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'username': username,
                'firstName': firstName,
                'lastName': lastName,
                'email': email,
                'roles': "ROLE_STUDENT",
                'classroom_id': classroom
            }),
        })
        .then(response => {

            if (response.status === 200) {
                showAlert('Student account created successfully!', 'success', 'Success!')
                closeModal()
                resetStudentForm()
                adminGetStudents()
            } else if (response.status === 400) {
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

function adminEditStudent() {
    let username = document.getElementById("studentUsername").value
    let firstName = document.getElementById("studentFirstName").value
    let lastName = document.getElementById("studentLastName").value
    let email = document.getElementById("studentEmail").value
    let classroom = document.getElementById("studentClassroom").value

    fetch(`/api/admin/editStudent/${currentStudentId}`,
        {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'username': username,
                'firstName': firstName,
                'lastName': lastName,
                'email': email,
                'classroom_id': classroom
            }),
        })
        .then(response => {

            if (response.status === 200) {
                showAlert('Student account edited successfully!', 'success', 'Success!')
                closeModal()
                resetStudentForm()
                adminGetStudents()
            } else if (response.status === 400) {
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
function adminAddCourse() {
    let classRoom = document.getElementById("courseClassroom").value
    let courseName = document.getElementById("courseName").value
    let courseTeacher = document.getElementById("courseTeacher").value

    let exam = document.getElementById("examGrade").checked

    fetch('/api/admin/createCourse',
        {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'classroom_id': classRoom,
                'name': courseName,
                'teacher_id': courseTeacher,
                'exam':exam,
            }),
        })
        .then(response => {
            if (response.status === 200) {
                showAlert('Course created successfully!', 'success', 'Success!')
                closeModal()
                resetCourseForm()
                adminGetCourses()
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        })
}

function adminEditCourse() {
    let classRoom = document.getElementById("courseClassroom").value
    let courseName = document.getElementById("courseName").value
    let courseTeacher = document.getElementById("courseTeacher").value

    fetch(`/api/admin/editCourses/${currentCourseId}`,
        {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'classroom_id': classRoom,
                'name': courseName,
                'teacher_id': courseTeacher,
            }),
        })
        .then(response => {
            if (response.status === 200) {
                showAlert('Course edited successfully!', 'success', 'Success!')
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
function adminAddClassroom() {
    let classroomName = document.getElementById("classroomName").value
    let classMaster = document.getElementById("classroomClassMaster").value

    fetch('/api/admin/createClassroom',
        {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'name': classroomName,
                'teacher_id': classMaster,
            }),
        })
        .then(response => {
            if (response.status === 200) {
                showAlert('Classroom created successfully!', 'success', 'Success!')
                closeModal()
                resetClassroomForm()
                adminGetClassrooms()
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        })
}

function adminEditClassroom() {
    let classroomName = document.getElementById("classroomName").value
    let classMaster = document.getElementById("classroomClassMaster").value

    fetch(`/api/admin/editClassroom/${currentClassroomId}`,
        {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'name': classroomName,
                'teacher_id': classMaster,
            }),
        })
        .then(response => {
            if (response.status === 200) {
                showAlert('Classroom edited successfully!', 'success', 'Success!')
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

function editTeacher(teacherId) {
    for (let i = 0; i < teachers.length; i++) {
        if (teachers[i].id === teacherId) {
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

function editStudent(studentId) {
    for (let i = 0; i < students.length; i++) {
        if (students[i].id === studentId) {
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

function editCourse(courseId) {
    for (let i = 0; i < courses.length; i++) {
        if (courses[i].id === courseId) {
            document.getElementById("courseName").value = courses[i].name
            document.getElementById("courseTeacher").value = courses[i].teacherId
            document.getElementById("courseClassroom").value = courses[i].classroomId
            document.getElementById("examGrade").checked = courses[i].examCourse
        }
    }
    document.getElementById("createCourseModal").style.display = "block";

    document.getElementById("createCourseButton").textContent = "Edit course"
    document.getElementById("createCourseFormTitle").textContent = "Edit course"

    requestCourseType = "PUT"

    currentCourseId = courseId
}

function editClassroom(classroomId) {
    for (let i = 0; i < classrooms.length; i++) {
        if (classrooms[i].id === classroomId) {
            document.getElementById("classroomName").value = classrooms[i].name
            document.getElementById("classroomClassMaster").value = classrooms[i].classMasterId
        }
    }

    document.getElementById("createClassroomModal").style.display = "block";

    document.getElementById("createClassroomButton").textContent = "Edit classroom"
    document.getElementById("createClassroomFormTitle").textContent = "Edit classroom"

    requestClassroomType = "PUT"

    currentClassroomId = classroomId
}

//RESET FORMS
function resetTeacherForm() {
    currentTeacherId = null
    requestTeacherType = "POST"

    document.getElementById("teacherUsername").value = ""
    document.getElementById("teacherFirstName").value = ""
    document.getElementById("teacherLastName").value = ""
    document.getElementById("teacherEmail").value = ""

    document.getElementById("createTeacherButton").textContent = "Create"
    document.getElementById("createTeacherFormTitle").textContent = "Create teacher account"
}

function resetStudentForm() {
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

function resetCourseForm() {
    currentCourseId = null
    requestCourseType = "POST"

    document.getElementById("courseName").value = ""
    document.getElementById("courseTeacher").value = ""
    document.getElementById("courseClassroom").value = ""
    document.getElementById("examGrade").checked = false

    document.getElementById("createCourseButton").textContent = "Create"
    document.getElementById("createCourseFormTitle").textContent = "Create course"
}

function resetClassroomForm() {
    currentClassroomId = null
    requestClassroomType = "POST"

    document.getElementById("classroomName").value = ""
    document.getElementById("classroomClassMaster").value = ""

    document.getElementById("createClassroomButton").textContent = "Create"
    document.getElementById("createClassroomFormTitle").textContent = "Create classroom"
}
