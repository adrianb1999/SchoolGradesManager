package com.adrian99.schoolGradesManager.controller;

import com.adrian99.schoolGradesManager.model.Classroom;
import com.adrian99.schoolGradesManager.model.Course;
import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.service.ClassroomService;
import com.adrian99.schoolGradesManager.service.CourseService;
import com.adrian99.schoolGradesManager.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CourseController {

    private final CourseService courseService;
    private final ClassroomService classroomService;
    private final UserService userService;

    public CourseController(CourseService courseService, ClassroomService classroomService, UserService userService) {
        this.courseService = courseService;
        this.classroomService = classroomService;
        this.userService = userService;
    }

    @PostMapping("/api/createCourse")
    public Course createCourse(@RequestBody Map<String, String> info){
        Course course = new Course();
        Classroom classroom = classroomService.findById(Long.parseLong(info.get("classroom_id")));
        User teacher = userService.findById(Long.parseLong(info.get("teacher_id")));

        course.setTeacher(teacher);
        course.setClassRoom(classroom);
        course.setName(info.get("name"));

        return courseService.save(course);
    }
}
