document.addEventListener("DOMContentLoaded", function () {
    studentGetMarkAndAbsences()
})

function studentGetMarkAndAbsences() {

    table = document.querySelector("#marksTable tbody")

    fetch(`/api/student/marksByStudent`,
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
                        <td>${i.marksSem1.map(mark => `<span class="tag"> ${mark.value} la ${mark.date} </span>`).join(" ")}
                             ${specialGrade(i.examCourse, i.examMarkSem1)}
                            <br> <span class="tag"> Medie ${i.averageSem1} </span>                      
                        </td>
                        <td>${i.marksSem2.map(mark => `<span class="tag"> ${mark.value} la ${mark.date} </span>`).join(" ")}
                            ${specialGrade(i.examCourse, i.examMarkSem2)}
                           <br> <span class="tag"> Medie ${i.averageSem2} </span>    
                        </td>
                        <td>${i.absencesSem1.map(absencesSem1 => absence(absencesSem1)).join(" ")}</td>
                        <td>${i.absencesSem2.map(absencesSem2 => absence(absencesSem2)).join(" ")}</td>
                     
                    </tr>`)
            }
        }).catch((error) => {
        console.error('Error:', error);
    });
}
function specialGrade(active, examMark){
    if(active === true){
        return `<br><span class="tag">Teza ${examMark.value} (pe  ${examMark.date})</span>`
    }
    return ''
}
function absence(absence){
    if(absence.justified == true){
        return `<span class = "tag absence-justified"> ${absence.date} motivata</span>`
    }
    return `<span class = "tag absence-nejustified"> ${absence.date} nemotivata</span>`
}
