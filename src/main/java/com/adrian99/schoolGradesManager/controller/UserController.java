package com.adrian99.schoolGradesManager.controller;

import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
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

    @PostMapping("/api/createUser")
    public User createUser(@RequestBody User user){
        return userService.save(user);
    }
}