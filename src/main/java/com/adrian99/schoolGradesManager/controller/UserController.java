package com.adrian99.schoolGradesManager.controller;

import com.adrian99.schoolGradesManager.exception.ApiRequestException;
import com.adrian99.schoolGradesManager.model.Classroom;
import com.adrian99.schoolGradesManager.model.Course;
import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.service.ClassroomService;
import com.adrian99.schoolGradesManager.service.CourseService;
import com.adrian99.schoolGradesManager.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private final ClassroomService classroomService;
    private final UserService userService;
    private final CourseService courseService;

    public UserController(ClassroomService classroomService, UserService userService, CourseService courseService) {
        this.classroomService = classroomService;
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping("/api/admin/allTeachers")
    public List<Map<String, Object>> findAllTeachers(){
        return userService.findAllTeachers();
    }

    @GetMapping("/api/admin/allStudents")
    public List<Map<String,Object>> findAllStudents(){
        return userService.findAllStudents();
    }

    @PostMapping("/api/admin/createTeacher")
    public User createTeacher(@RequestBody Map<String, String> info, Principal principal){

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

    @PostMapping("/api/admin/createTeachers")
    public List<User> createTeachers(@RequestBody List<User> teachers){
        return (List<User>) userService.saveAll(teachers);
    }

    @PutMapping("/api/admin/editTeacher/{teacherId}")
    public User editTeacher(@RequestBody Map<String, String> info,@PathVariable Long teacherId){

        User user = userService.findById(teacherId);

        user.setEmail(info.get("email"));
        user.setUsername(info.get("username"));
        user.setFirstName(info.get("firstName"));
        user.setLastName(info.get("lastName"));

        return userService.save(user);
    }

    @PostMapping("/api/admin/createStudent")
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

    @PostMapping("api/admin/createStudents")
    public User createStudents(@RequestBody List<Map<String, String>> infos){


        for(Map<String,String> info : infos) {
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
        }
        return null;
    }

    @PutMapping ("/api/admin/editStudent/{studentId}")
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

    @GetMapping("/api/teacher/studentsByCourse/{courseId}")
    public List<Map<String, Object>> getStudentsByCourseId(@PathVariable Long courseId){
        Course course = courseService.findById(courseId);
        if(course == null)
            throw new ApiRequestException("Course is invalid");

        return userService.findStudentsByCourse(course);
    }

    @GetMapping("/api/teacher/studentsByClassroom")
    public List<Map<String, Object>> getStudentByClassroomId(Principal principal){

        User teacher = userService.findByUsername(principal.getName());

        if(teacher == null)
            throw new ApiRequestException("User is not a teacher");

        Classroom classroom = classroomService.findClassroomByClassmasterId(teacher);

        if(classroom == null)
            throw new ApiRequestException("Teacher is not a classmaster!");

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
}