package com.adrian99.schoolGradesManager.controller;

import com.adrian99.schoolGradesManager.exception.ApiRequestException;
import com.adrian99.schoolGradesManager.model.Classroom;
import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.service.ClassroomService;
import com.adrian99.schoolGradesManager.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private final ClassroomService classroomService;
    private final UserService userService;

    public UserController(ClassroomService classroomService, UserService userService) {
        this.classroomService = classroomService;
        this.userService = userService;
    }

    @GetMapping("/api/teachers")
    public List<Map<String, Object>> findAllTeachers(){
        return userService.findAllTeachers();
    }

    @GetMapping("api/students")
    public List<Map<String,Object>> findAllStudents(){
        return userService.findAllStudents();
    }

    @PostMapping("/api/createTeacher")
    public User createTeacher(@RequestBody Map<String, String> info){

        User user = new User();

        user.setActive(true);
        user.setEmail(info.get("email"));
        user.setUsername(info.get("username"));
        user.setPassword("pass");
        user.setFirstName(info.get("firstName"));
        user.setLastName(info.get("lastName"));
        user.setRoles("ROLE_TEACHER");

        return userService.save(user);
    }
    @PutMapping("api/createTeacher/{teacherId}")
    public User editTeacher(@RequestBody Map<String, String> info,@PathVariable Long teacherId){

        User user = userService.findById(teacherId);

        user.setEmail(info.get("email"));
        user.setUsername(info.get("username"));
        user.setFirstName(info.get("firstName"));
        user.setLastName(info.get("lastName"));

        return userService.save(user);
    }

    @PostMapping("api/createStudent")
    public User createStudent(@RequestBody Map<String, String> info){

        User user = new User();

        user.setActive(true);
        user.setEmail(info.get("email"));
        user.setUsername(info.get("username"));
        user.setPassword("pass");
        user.setFirstName(info.get("firstName"));
        user.setLastName(info.get("lastName"));
        user.setRoles("ROLE_STUDENT");

        User createdUser = userService.save(user);

        Classroom classroom = classroomService.findById(Long.parseLong(info.get("classroom_id")));
        classroom.getStudents().add(createdUser);
        classroomService.save(classroom);

        return createdUser;
    }

    @PutMapping ("api/createStudent/{studentId}")
    public User editStudent(@RequestBody Map<String, String> info, @PathVariable Long studentId){

        User user = userService.findById(studentId);

        Classroom oldClassroom = classroomService.findClassroomByUserId(user);

        user.setEmail(info.get("email"));
        user.setUsername(info.get("username"));
        user.setFirstName(info.get("firstName"));
        user.setLastName(info.get("lastName"));

        User createdUser = userService.save(user);

        oldClassroom.getStudents().remove(user);

        Classroom classroom = classroomService.findById(Long.parseLong(info.get("classroom_id")));
        classroom.getStudents().add(createdUser);
        classroomService.save(oldClassroom);
        classroomService.save(classroom);

        return createdUser;
    }

    @GetMapping("api/students/{classroomId}")
    public List<Map<String, Object>> getStudentsByClassroomId(@PathVariable Long classroomId){
        Classroom classroom = classroomService.findById(classroomId);
        if(classroom == null)
            throw new ApiRequestException("Classroom is invalid");

        return userService.findStudentsByClassroom(classroom);
    }

    @GetMapping("/createAdmin")
    public void createAdminAccount(){
        User admin = new User("admin",
                "admin",
                "ROLE_ADMIN",
                "",
                "",
                "",
                true
        );
        userService.save(admin);
    }

    @DeleteMapping("api/user/{userId}")
    public void deleteUser(@PathVariable Long userId){
        //TODO NOT COMPLETED!
        userService.deleteById(userId);
    }
}