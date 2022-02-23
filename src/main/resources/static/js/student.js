document.addEventListener("DOMContentLoaded", function () {
    studentGetCourse()
    document.querySelector("#createMarkForm").onsubmit = function () {
        teacherAddMark()
        return false
    }
})

let courses

function injectInSelectCourse(){
    let form = document.getElementById("studentCourseList")
    form.innerHTML = ''
    for (course of courses) {
        form.insertAdjacentHTML('beforeend',
            `<option value="${course.courseId}">${course.courseName} - ${course.teacherFullName}</option>`)
    }
    studentGetMarkAndAbsences()
}
function studentGetMarkAndAbsences(){
    studentGetMark()
    studentGetAbsence()
}

function studentGetCourse(){
        fetch(`/api/student/courses`,
        {
            method: 'GET',
        })
        .then(response => response.json())
        .then(data => {
            courses = data
            injectInSelectCourse()
        }).catch((error) => {
        console.error('Error:', error);
    });
}

function studentGetMark(){

    let currentCourseId = document.getElementById("studentCourseList").value

    let markTable = document.querySelector("#marksTable tbody")

    fetch(`/api/student/marks/${currentCourseId}`,
        {
            method: 'GET',
        })
        .then(response => response.json())
        .then(data => {
            markTable.innerHTML = ''
            for(i of data){
                markTable.insertAdjacentHTML('beforeend',
                    `
                    <tr>
                        <td>${i.value}</td>
                        <td>${i.date}</td>
                    </tr>
                `)
            }
        }).catch((error) => {
        console.error('Error:', error);
    });
}

function studentGetAbsence(){
    let currentCourseId = document.getElementById("studentCourseList").value

    let absenceTable = document.querySelector("#absencesTable tbody")

    fetch(`/api/student/absences/${currentCourseId}`,
        {
            method: 'GET',
        })
        .then(response => response.json())
        .then(data => {
            absenceTable.innerHTML = ''
            for(i of data){
                absenceTable.insertAdjacentHTML('beforeend',
                    `
                    <tr>
                        <td>${i.date}</td>
                        <td>${i.justified}</td>
                    </tr>
                `)
            }
        }).catch((error) => {
        console.error('Error:', error);
    });
}