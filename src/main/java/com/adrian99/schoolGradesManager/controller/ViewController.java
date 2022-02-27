package com.adrian99.schoolGradesManager.controller;

import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class ViewController {
    private final UserService userService;

    public ViewController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping({"/","/index.html","/index","/login.html"})
    public String indexPage(){
        return "index";
    }

    @RequestMapping("/user.html")
    public String userPage(Model model, Principal principal){
        User user = userService.findByUsername(principal.getName());

        model.addAttribute("userType",user.getRoles());
        model.addAttribute("userInfo",user);

        return "user";
    }
}
