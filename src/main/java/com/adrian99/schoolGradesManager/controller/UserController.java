package com.adrian99.schoolGradesManager.controller;

import com.adrian99.schoolGradesManager.email.EmailSender;
import com.adrian99.schoolGradesManager.exception.ApiRequestException;
import com.adrian99.schoolGradesManager.model.Classroom;
import com.adrian99.schoolGradesManager.model.Course;
import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.service.ClassroomService;
import com.adrian99.schoolGradesManager.service.CourseService;
import com.adrian99.schoolGradesManager.service.UserService;
import com.adrian99.schoolGradesManager.service.VerificationTokenService;
import com.adrian99.schoolGradesManager.token.TokenType;
import com.adrian99.schoolGradesManager.token.VerificationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.adrian99.schoolGradesManager.token.TokenType.PASSWORD_RESET;

@RestController
public class UserController {

    private final PasswordEncoder passwordEncoder;

    private final ClassroomService classroomService;
    private final UserService userService;
    private final CourseService courseService;
    private final EmailSender emailSender;
    private final VerificationTokenService verificationTokenService;

    public UserController(PasswordEncoder passwordEncoder, ClassroomService classroomService, UserService userService, CourseService courseService, EmailSender emailSender, VerificationTokenService verificationTokenService) {
        this.passwordEncoder = passwordEncoder;
        this.classroomService = classroomService;
        this.userService = userService;
        this.courseService = courseService;
        this.emailSender = emailSender;
        this.verificationTokenService = verificationTokenService;
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
    public User createTeacher(@RequestBody Map<String, String> info) throws MessagingException {

        User user = new User();
        String password = userService.passwordGenerator(20);

        user.setActive(true);
        user.setEmail(info.get("email"));
        user.setUsername(info.get("username"));
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(info.get("firstName"));
        user.setLastName(info.get("lastName"));
        user.setRoles("ROLE_TEACHER");

        String token = userService.generateToken(user);

        emailSender.sendPasswordEmail(user.getEmail(), user.getUsername(), user.getPassword(), token, TokenType.ACCOUNT_ACTIVATION);

        return userService.save(user);
    }

    @PostMapping("/api/admin/createTeachers")
    public List<User> createTeachers(@RequestBody List<Map<String,String>> teachers) throws MessagingException {

        String password;

        for(Map<String,String> teacher : teachers) {
            User user = new User();

            password = userService.passwordGenerator(20);

            user.setActive(true);
            user.setEmail(teacher.get("email"));
            user.setUsername(teacher.get("username"));
            user.setPassword(passwordEncoder.encode(password));
            user.setFirstName(teacher.get("firstName"));
            user.setLastName(teacher.get("lastName"));
            user.setRoles("ROLE_TEACHER");

            User createdUser = userService.save(user);

            String token = userService.generateToken(createdUser);

            emailSender.sendPasswordEmail(user.getEmail(), user.getUsername(), user.getPassword(), token, TokenType.ACCOUNT_ACTIVATION);

        }

        return null;
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
    public User createStudent(@RequestBody Map<String, String> info) throws MessagingException {

        User user = new User();

        String password = userService.passwordGenerator(20);

        user.setActive(true);
        user.setEmail(info.get("email"));
        user.setUsername(info.get("username"));
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(info.get("firstName"));
        user.setLastName(info.get("lastName"));
        user.setRoles("ROLE_STUDENT");

        User createdUser = userService.save(user);

        String token = userService.generateToken(createdUser);

        emailSender.sendPasswordEmail(user.getEmail(), user.getUsername(), user.getPassword(), token, TokenType.ACCOUNT_ACTIVATION);

        Classroom classroom = classroomService.findById(Long.parseLong(info.get("classroom_id")));
        classroom.getStudents().add(createdUser);
        classroomService.save(classroom);

        return createdUser;
    }

    @PostMapping("api/admin/createStudents")
    public User createStudents(@RequestBody List<Map<String, String>> students) throws MessagingException {

        String password;

        for(Map<String,String> student : students) {
            User user = new User();

            password = userService.passwordGenerator(20);

            user.setActive(true);
            user.setEmail(student.get("email"));
            user.setUsername(student.get("username"));
            user.setPassword(passwordEncoder.encode(password));
            user.setFirstName(student.get("firstName"));
            user.setLastName(student.get("lastName"));
            user.setRoles("ROLE_STUDENT");

            User createdUser = userService.save(user);

            String token = userService.generateToken(createdUser);

            emailSender.sendPasswordEmail(user.getEmail(), user.getUsername(), user.getPassword(), token, TokenType.ACCOUNT_ACTIVATION);


            Classroom classroom = classroomService.findById(Long.parseLong(student.get("classroom_id")));
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

    @PutMapping("/api/user/updateInfo")
    public User saveOrUpdate(@RequestBody Map<String, String> userInfo, Principal principal) {

        if(userInfo.get("password") == null)
            throw new ApiRequestException("Password is required!");

        User updateUser = userService.findByUsername(principal.getName());

        if(!passwordEncoder.matches(userInfo.get("password"), updateUser.getPassword()))
            throw new ApiRequestException("Password incorrect");

        if(!userService.isEmailValid(userInfo.get("email")))
            throw new ApiRequestException("Email is invalid");

        if (userInfo.get("email") != null) {
            if (userInfo.get("email").isEmpty())
                throw new ApiRequestException("Email cannot be null!");

            if (Objects.equals(updateUser.getEmail(), userInfo.get("email"))) {
                throw new ApiRequestException("The email cannot be the same!");
            }

            if (userService.findByEmail(userInfo.get("email")) != null)
                throw new ApiRequestException("Email already used!");

            updateUser.setEmail(userInfo.get("email"));
        }

        if (userInfo.get("newPassword") != null) {
            if (userInfo.get("newPassword").isEmpty())
                throw new ApiRequestException("Password cannot be empty");
            if(Objects.equals(updateUser.getPassword(), userInfo.get("newPassword")))
                throw new ApiRequestException("The password cannot be the same!");

            updateUser.setPassword(passwordEncoder.encode(userInfo.get("newPassword")));
        }
        return userService.save(updateUser);
    }

    @PostMapping("/api/user/resetPasswordLink")
    public void sendResetToken(@RequestBody Map<String, String> userInfo) throws MessagingException {

        String email = userInfo.get("email");

        if(email == null)
            throw new ApiRequestException("Email is null!");

        if(!userService.isEmailValid(email))
            throw new ApiRequestException("Email is invalid!");

        User currentUser = userService.findByEmail(userInfo.get("email"));

        if(currentUser == null)
            throw new ApiRequestException("The email doesn't exist!");

        if(!currentUser.getActive())
            throw new ApiRequestException("The account is not activated!");

        String token = userService.generateToken(currentUser);

        emailSender.sendPasswordEmail(currentUser.getEmail(), currentUser.getUsername(), currentUser.getPassword(),  token, PASSWORD_RESET);
    }

    @PostMapping("/api/user/passwordReset")
    public void resetPassword(@RequestBody User user,
                              @RequestParam(name = "token") String token) {
        VerificationToken currentToken = verificationTokenService.isTokenValid(token);

        User currentUser = currentToken.getUser();
        currentUser.setPassword(user.getPassword());

        userService.save(currentUser);

        verificationTokenService.deleteById(currentToken.getId());
    }
}