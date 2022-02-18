package com.adrian99.schoolGradesManager.controller;

import com.adrian99.schoolGradesManager.model.Absence;
import com.adrian99.schoolGradesManager.model.Course;
import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.service.AbsenceService;
import com.adrian99.schoolGradesManager.service.CourseService;
import com.adrian99.schoolGradesManager.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
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

    @PostMapping("api/createAbsence")
    public Absence createAbsence(@RequestBody Map<String, String> info){
        Absence absence = new Absence();

        User student = userService.findById(Long.parseLong(info.get("student_id")));
        Course course = courseService.findById(Long.parseLong(info.get("course_id")));

        absence.setStudent(student);
        absence.setCourse(course);
        absence.setJustified(false);
        absence.setDate(LocalDate.now());

        return absenceService.save(absence);
    }
}
