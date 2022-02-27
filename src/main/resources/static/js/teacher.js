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
    span2.onclick = function (f) {
        resetAbsenceModal()
        createAbsenceModal.style.display = "none";
    }
})

let teacherCourses

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
                    <option value="${i.id}">${i.name}, clasa ${i.classroomName}</option>`
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
                        <td>${i.studentFirstName} ${i.studentLastName}</td>
                        <td>${i.studentMarksSem1.map(student => `<span class="tag"> ${student.value} <small> (pe ${student.date})</small></span>`).join(" ")}
                       
                             ${specialGrade(i.examCourse, i.examMarkSem1)}
                             ${averagePrint(i.averageSem1)}                       
                        </td>
                        <td>${i.studentMarksSem2.map(student => `<span class="tag"> ${student.value} <small> (pe ${student.date})</small></span>`).join(" ")}
                             ${specialGrade(i.examCourse, i.examMarkSem2)}
                             ${averagePrint(i.averageSem2)} 
                        </td>
                                     
                        <td>${i.studentAbsencesSem1.map(student => absence(student)).join(" ")}
                        </td>
                        <td>${i.studentAbsencesSem2.map(student => absence(student)).join(" ")}
                        </td>
                                                
                        <td>
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
                    showAlert(data.message, 'error', 'Eroare!')
                )
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
                    showAlert(data.message, 'error', 'Eroare!')
                )
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

    fetch(`/api/teacher/marksById/${studentId}`,
        {
            method: 'GET',
        })
        .then(response => response.json())
        .then(data => {
            table.innerHTML = ""
            for (i of data) {
                table.insertAdjacentHTML('beforeend', `
                    <tr>
                        <td>${i.name}</td>
                        
                        <td>${i.marksSem1.map(mark => `<span class="tag"> ${mark.value} (pe ${mark.date}</span>`).join(" ")}
                             ${specialGrade(i.examCourse, i.examMarkSem1)}
                             ${averagePrint(i.averageSem1)}               
                        </td>
                        
                        <td>${i.marksSem2.map(mark => `<span class="tag"> ${mark.value} (pe ${mark.date}</span>`).join(" ")}
                            ${specialGrade(i.examCourse, i.examMarkSem2)}
                            ${averagePrint(i.averageSem2)}
                        </td>
                        
                        <td>${i.absencesSem1.map(absencesSem1 => absence(absencesSem1)).join(" ")}
                        </td>
                        <td>${i.absencesSem2.map(absencesSem2 => absence(absencesSem2)).join(" ")}
                        </td>
                    </tr>`)
            }
        }).catch((error) => {
        console.error('Error:', error);
    });
}
function specialGrade(active, examMark){
    if(active === true && examMark.value != null){
        return `<br> <span class="tag"> Teza ${examMark.value} (pe  ${examMark.date})</span>`
    }
    return ''
}
function averagePrint(average){
    if(average == 0)
        return '<span class="tag"> Nu exita note </span>'
    return `<br> <span class="tag"> Medie ${average} </span>`
}

function openJustifiedModal(absenceId){

    Swal.fire({
        title: 'Absenta',
        text: "Motiezi absenta?",
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Da',
        cancelButtonText:'Nu'
    }).then((result) => {
        if (result.isConfirmed) {
            justifyAbsence(absenceId)
        }
    })
}

function justifyAbsence(absenceId){
    fetch(`/api/teacher/justify/${absenceId}`,
        {
            method: 'POST',
        })
    .then(response =>{
        if(response.status === 200) {

            Swal.fire(
                'Absenta motivata',
                'Absenta a fost motivata cu succes',
                'success'
            )
            teacherGetCourses()
            teacherGetClassroom()
        }
    }).catch((error) => {
    console.error('Error:', error);
    });
}

function absence(absence){
    if(absence.justified === true){
        return `<span class = "tag absence-justified"> ${absence.date} motivata</span>`
    }
    return `<span class = "tag absence-nejustified" onclick="openJustifiedModal(absence.id)"> ${absence.date} nemotivata</span>`
}