package com.adrian99.schoolGradesManager.controller;

import com.adrian99.schoolGradesManager.exception.ApiRequestException;
import com.adrian99.schoolGradesManager.model.Course;
import com.adrian99.schoolGradesManager.model.Mark;
import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.service.CourseService;
import com.adrian99.schoolGradesManager.service.MarkService;
import com.adrian99.schoolGradesManager.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class MarkController {

    private final MarkService markService;
    private final UserService userService;
    private final CourseService courseService;

    public MarkController(MarkService markService, UserService userService, CourseService courseService) {
        this.markService = markService;
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping("/api/student/marks/{courseId}")
    public List<Map<String, Object>> getStudentMarks(@PathVariable Long courseId, Principal principal){

        User student = userService.findByUsername(principal.getName());
        Course course = courseService.findById(courseId);

        if(!student.getRoles().contains("ROLE_STUDENT"))
            throw new ApiRequestException("The user is not a student!");

        if(course == null)
            throw new ApiRequestException("The course is invalid!");

        return userService.findMarksByStudentAndCourse(student, course);
    }

    @GetMapping("/api/student/marksById/{studentId}")
    public List<Map<String, Object>> getAllMarksOfStudent(@PathVariable Long studentId){
        User student = userService.findById(studentId);

        if(!student.getRoles().contains("ROLE_STUDENT"))
            throw new ApiRequestException("The user is not a student!");

        return userService.findMarksByStudent(student);
    }

    @PostMapping("/api/teacher/createMark")
    public Mark createMark(@RequestBody Map<String, String> info){

        Mark mark = new Mark();
        User student = userService.findById(Long.parseLong(info.get("student_id")));
        Course course = courseService.findById(Long.parseLong(info.get("course_id")));

        mark.setStudent(student);
        mark.setCourse(course);
        mark.setExamMark(Boolean.valueOf(info.get("examMark")));
        mark.setValue(Integer.parseInt(info.get("value")));
        mark.setDate(LocalDate.parse(info.get("date")));

        return markService.save(mark);
    }

}
