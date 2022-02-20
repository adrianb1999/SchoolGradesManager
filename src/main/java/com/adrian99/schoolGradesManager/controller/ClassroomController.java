package com.adrian99.schoolGradesManager.controller;

import com.adrian99.schoolGradesManager.model.Classroom;
import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.service.ClassroomService;
import com.adrian99.schoolGradesManager.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ClassroomController {

    private final ClassroomService classroomService;
    private final UserService userService;

    public ClassroomController(ClassroomService classroomService, UserService userService) {
        this.classroomService = classroomService;
        this.userService = userService;
    }

    @GetMapping("api/classrooms")
    public List<Classroom> getClassrooms(){
        return (List<Classroom>) classroomService.findAll();
    }

    @PostMapping("/api/createClassroom")
    public Classroom createClassroom(@RequestBody Map<String, String> info){

        Classroom classroom = new Classroom();

        User teacher = userService.findById(Long.parseLong(info.get("teacher_id")));

        classroom.setName(info.get("name"));

        classroom.setClassMaster(teacher);

        return classroomService.save(classroom);
    }

    @PutMapping("/api/createClassroom/{classroomId}")
    public Classroom updateClassroom(@RequestBody Map<String, String> info, @PathVariable Long classroomId){

        Classroom classroom = classroomService.findById(classroomId);

        User teacher = userService.findById(Long.parseLong(info.get("teacher_id")));

        classroom.setName(info.get("name"));

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
