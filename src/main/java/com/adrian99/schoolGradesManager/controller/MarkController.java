package com.adrian99.schoolGradesManager.controller;

import com.adrian99.schoolGradesManager.model.Course;
import com.adrian99.schoolGradesManager.model.Mark;
import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.service.CourseService;
import com.adrian99.schoolGradesManager.service.MarkService;
import com.adrian99.schoolGradesManager.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
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

    @PostMapping("api/createMark")
    public Mark createMark(@RequestBody Map<String, String> info){

        Mark mark = new Mark();
        User student = userService.findById(Long.parseLong(info.get("student_id")));
        Course course = courseService.findById(Long.parseLong(info.get("course_id")));

        mark.setStudent(student);
        mark.setCourse(course);
        mark.setType(info.get("type"));
        mark.setValue(Integer.parseInt(info.get("value")));
        mark.setDate(LocalDate.now());

        return markService.save(mark);
    }

}
