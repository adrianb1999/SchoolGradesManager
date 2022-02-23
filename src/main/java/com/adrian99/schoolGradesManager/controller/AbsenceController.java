package com.adrian99.schoolGradesManager.controller;

import com.adrian99.schoolGradesManager.exception.ApiRequestException;
import com.adrian99.schoolGradesManager.model.Absence;
import com.adrian99.schoolGradesManager.model.Course;
import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.service.AbsenceService;
import com.adrian99.schoolGradesManager.service.CourseService;
import com.adrian99.schoolGradesManager.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class AbsenceController {

    private final AbsenceService absenceService;
    private final UserService userService;
    private final CourseService courseService;

    public AbsenceController(AbsenceService absenceService, UserService userService, CourseService courseService) {
        this.absenceService = absenceService;
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping("/api/student/absences/{courseId}")
    public List<Map<String, Object>> getStudentAbsences(@PathVariable Long courseId, Principal principal){
        User student = userService.findByUsername(principal.getName());
        Course course = courseService.findById(courseId);

        if(!student.getRoles().contains("ROLE_STUDENT"))
            throw new ApiRequestException("The user is not a student!");

        if(course == null)
            throw new ApiRequestException("The course is invalid!");

        return userService.findAbsencesByStudentAndCourse(student,course);
    }

    @PostMapping("/api/teacher/createAbsence")
    public Absence createAbsence(@RequestBody Map<String, String> info){
        Absence absence = new Absence();

        User student = userService.findById(Long.parseLong(info.get("student_id")));
        Course course = courseService.findById(Long.parseLong(info.get("course_id")));

        absence.setStudent(student);
        absence.setCourse(course);
        absence.setJustified(false);
        absence.setDate(LocalDate.parse(info.get("date")));

        return absenceService.save(absence);
    }
}
