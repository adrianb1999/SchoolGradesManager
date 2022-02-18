package com.adrian99.schoolGradesManager.controller;

import com.adrian99.schoolGradesManager.model.Classroom;
import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.service.ClassroomService;
import com.adrian99.schoolGradesManager.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ClassroomController {

    private final ClassroomService classroomService;
    private final UserService userService;

    public ClassroomController(ClassroomService classroomService, UserService userService) {
        this.classroomService = classroomService;
        this.userService = userService;
    }

    @PostMapping("/api/createClassroom")
    public Classroom createClassroom(@RequestBody Map<String, String> info){

        Classroom classroom = new Classroom();

        classroom.setName(info.get("name"));
        User teacher = userService.findById(Long.parseLong(info.get("teacher_id")));
        classroom.setClassMaster(teacher);

        return classroomService.save(classroom);
    }

    @PostMapping("/api/addStudentToClassroom")
    public Classroom addStudent(@RequestBody Map<String, String> info) {

        Classroom classroom = classroomService.findById(Long.parseLong(info.get("classroom_id")));
        User student = userService.findById(Long.parseLong(info.get("student_id")));

        classroom.getStudents().add(student);

        return classroomService.save(classroom);
    }
}
