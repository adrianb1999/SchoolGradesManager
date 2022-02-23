document.addEventListener("DOMContentLoaded", function () {
    teacherGetCourses()
    teacherGetClassroom()
    document.querySelector("#createMarkForm").onsubmit = function () {
        teacherAddMark()
        return false
    }

    document.querySelector("#createAbsenceForm").onsubmit = function () {
        teacherAddAbsence()
        return false
    }

    let span1 = document.getElementsByClassName("close")[0];
    let span2 = document.getElementsByClassName("close")[1];

    span1.onclick = function (f) {
        resetMarkModal()
        createMarkModal.style.display = "none";
    }
    span1.onclick = function (f) {
        resetAbsenceModal()
        createAbsenceModal.style.display = "none";
    }
})


let teacherCourses
let teacherClassrooms

function teacherGetCourses() {
    fetch('/api/teacher/courses',
        {
            method: 'GET',
        })
        .then(response => response.json())
        .then(data => {

            console.log("GET COURSE")

            let classroomList = document.getElementById("teacherClassroomsList")

            classroomList.innerHTML = ""
            teacherCourses = data
            for (let i of data) {
                classroomList.insertAdjacentHTML('beforeend', `
                    <option value="${i.id}">${i.name} ${i.classRoom.name}</option>`
                )
            }
            teacherLoadStudent()
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}

function teacherLoadStudent() {
    let courseId = document.getElementById("teacherClassroomsList").value

    let teacherTable = document.querySelector("#teacherTable tbody")
    console.log("load stud " + courseId)
    teacherTable.innerHTML = ''

    fetch(`/api/teacher/studentsByCourse/${courseId}`,
        {
            method: 'GET',
        })
        .then(response => response.json())
        .then(data => {
            for (i of data) {

                let rowSpan = "2"
                if (i.examCourse)
                    rowSpan = "3"

                teacherTable.insertAdjacentHTML('beforeend',
                    `
                    <tr>
                        <td rowspan=${rowSpan} >${i.studentFirstName} ${i.studentLastName}</td>
                        <td>${i.studentMarksSem1.map(student => student.value + "<small> (pe " + student.date + ")</small>").join(", <br>")}</td>
                        <td>${i.studentMarksSem2.map(student => student.value + "<small> (pe " + student.date + ")</small>").join(", <br>")}</td>
                        <td rowspan=${rowSpan}>${i.studentAbsences.map(student => student.date + " " + student.justified).join(",<br>")}</td>
                        <td rowspan=${rowSpan}></td>
                        <td rowspan=${rowSpan}>
                          <button class="changeButton" onClick="teacherOpenMarkModal(${i.student_id},${courseId})">
                            <span class="material-icons">
                                add
                            </span>
                          </button>
                          <button class="changeButton" onClick="teacherOpenAbsenceModal(${i.student_id},${courseId})"> 
                            <span class="material-icons">
                                report_gmailerrorred
                            </span>                                               
                          </button>
                        </td>
                    </tr>`)
                if (i.examCourse) {
                    teacherTable.insertAdjacentHTML('beforeend', `
                        <tr>                    
                            <td><span>Teza ${i.examMarkSem1.value} <small>(pe ${i.examMarkSem1.date})</small> </span></td> 
                            <td><span>Teza ${i.examMarkSem2.value} <small>(pe ${i.examMarkSem2.date})</small> </span></td> 
                        </tr>
                    `)
                }
                teacherTable.insertAdjacentHTML('beforeend', `
                    <tr>
                        <td>Medie: ${i.averageSem1}</td>
                        <td>Medie: ${i.averageSem2}</td>
                    </tr>`)
            }
            teacherLoadClassroom()
        }).catch((error) => {
        console.error('Error:', error);
    });
}

let markStudentId
let markCourseId

function teacherOpenMarkModal(studentId, courseId) {
    markStudentId = studentId
    markCourseId = courseId
    openModal("markModal")
}

function teacherOpenAbsenceModal(studentId, courseId) {
    markStudentId = studentId
    markCourseId = courseId
    openModal("absenceModal")
}

function teacherAddMark() {
    let markValue = document.getElementById("markValue").value
    let markData = document.getElementById("markDate").value
    let examMark = document.getElementById("examGrade").checked
    fetch('/api/teacher/createMark',
        {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'student_id': markStudentId,
                'course_id': markCourseId,
                'value': "" + markValue,
                'date': "" + markData,
                examMark: "" + examMark,
            }),
        })
        .then(response => {
            if (response.status === 200) {
                showAlert('Mark created successfully!', 'success', 'Success!')
                closeTeacherModal()
                resetMarkModal()
                teacherGetCourses()
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

function teacherAddAbsence() {
    let absenceData = document.getElementById("absenceDate").value

    fetch('/api/teacher/createAbsence',
        {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'student_id': markStudentId,
                'course_id': markCourseId,
                'date': "" + absenceData,
            }),
        })
        .then(response => {
            if (response.status === 200) {
                showAlert('Absence created successfully!', 'success', 'Success!')
                closeAbsenceModal()
                resetAbsenceModal()
                teacherGetCourses()
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

function closeTeacherModal() {
    document.getElementById("createMarkModal").style.display = "none"
}

function closeAbsenceModal() {
    document.getElementById("createAbsenceModal").style.display = "none"
}

function teacherGetClassroom() {

    let table = document.querySelector("#teacherOwnClassroomsList")

    fetch('/api/teacher/studentsByClassroom',
        {
            method: 'GET',
        })
        .then(response => response.json())
        .then(data => {
            table.innerHTML = ""
            for (i of data) {
                table.insertAdjacentHTML('beforeend', `
                  <option value="${i.studentId}">${i.studentName}</option>
                `)
            }
        }).catch((error) => {
        console.error('Error:', error);
    });
}

function resetMarkModal() {
    document.getElementById("examGrade").checked = false
    document.getElementById("markValue").value = ""
    document.getElementById("markDate").value = ""
}

function resetAbsenceModal() {
    document.getElementById("absenceDate").value = ""
}

function teacherLoadClassroom() {

    table = document.querySelector("#myClassroomTable tbody")
    let studentId = document.querySelector("#teacherOwnClassroomsList").value

    fetch(`/api/student/marksById/${studentId}`,
        {
            method: 'GET',
        })
        .then(response => response.json())
        .then(data => {
            table.innerHTML = ""
            for (i of data) {
                let col = "2"

                if(i.examCourse)
                    col = "3"

                table.insertAdjacentHTML('beforeend', `
                    <tr style="border-top: 1px solid">
                        <td rowspan=${col}>${i.name}</td>
                        <td>${i.marksSem1.map(mark => mark.value +" la "+ mark.date).join(",<br>")}</td>
                        <td>${i.marksSem2.map(mark => mark.value +" la "+ mark.date).join(",<br>")}</td>
                        <td rowspan=${col}>${i.absencesSem1}</td>
                        <td rowspan=${col}>${i.absencesSem2}</td>
                    </tr>`)
                     if(i.examCourse){
                         table.insertAdjacentHTML('beforeend', `
                            <tr>
                                <td>Teza ${i.examMarkSem1.value} (pe  ${i.examMarkSem1.date})</td>
                                <td>Teza ${i.examMarkSem2.value} (pe  ${i.examMarkSem2.date})</td>
                            </tr>
                           `)
                     }
                    table.insertAdjacentHTML('beforeend', `
                    <tr style="border-bottom: 1px solid">
                        <td>Medie ${i.averageSem1}</td>
                        <td>Medie ${i.averageSem2}</td>
                    </tr>
                `)
            }
        }).catch((error) => {
        console.error('Error:', error);
    });

}