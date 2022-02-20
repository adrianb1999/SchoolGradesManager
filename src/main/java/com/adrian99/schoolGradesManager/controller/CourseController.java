package com.adrian99.schoolGradesManager.controller;

import com.adrian99.schoolGradesManager.exception.ApiRequestException;
import com.adrian99.schoolGradesManager.model.Classroom;
import com.adrian99.schoolGradesManager.model.Course;
import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.service.ClassroomService;
import com.adrian99.schoolGradesManager.service.CourseService;
import com.adrian99.schoolGradesManager.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.sql.rowset.Predicate;
import java.security.Principal;
import java.util.List;
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

    @GetMapping("api/allCourses")
    public List<Course> allCourses(){
        return (List<Course>) courseService.findAll();
    }

    @GetMapping("api/courses")
    public List<Course> allCourse(Principal principal){
        User teacher = userService.findByUsername(principal.getName());
        if(!teacher.getRoles().contains("ROLE_TEACHER"))
            throw new ApiRequestException("User is not a teacher!");

        return courseService.allCoursesByTeacher(teacher);
    }

    @PostMapping("/api/courses")
    public Course createCourse(@RequestBody Map<String, String> info){
        Course course = new Course();
        Classroom classroom = classroomService.findById(Long.parseLong(info.get("classroom_id")));
        User teacher = userService.findById(Long.parseLong(info.get("teacher_id")));

        course.setTeacher(teacher);
        course.setClassRoom(classroom);
        course.setName(info.get("name"));

        return courseService.save(course);
    }
    @PutMapping("/api/courses/{courseId}")
    public Course updateCourse(@RequestBody Map<String, String> info, @PathVariable Long courseId){

        Course course = courseService.findById(courseId);

        Classroom classroom = classroomService.findById(Long.parseLong(info.get("classroom_id")));
        User teacher = userService.findById(Long.parseLong(info.get("teacher_id")));
        course.setTeacher(teacher);
        course.setClassRoom(classroom);
        course.setName(info.get("name"));

        return courseService.save(course);
    }

}
