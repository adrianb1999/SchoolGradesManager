package com.adrian99.schoolGradesManager.controller;

import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.service.UserService;
import com.adrian99.schoolGradesManager.service.VerificationTokenService;
import com.adrian99.schoolGradesManager.token.TokenState;
import com.adrian99.schoolGradesManager.token.TokenType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class ViewController {
    private final UserService userService;
    private final VerificationTokenService verificationTokenService;

    public ViewController(UserService userService, VerificationTokenService verificationTokenService) {
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
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

    @RequestMapping("/passwordResetForm.html")
    public String passwordResetForm(@RequestParam(name = "token") String token, Model model) {

        TokenState tokenState = verificationTokenService.isTokenValidHtml(token, TokenType.PASSWORD_RESET);

        if (!tokenState.equals(TokenState.VALID)) {
            model.addAttribute("TokenState", tokenState);
            return "errorPage";
        }
        return "passwordResetForm";
    }

    @RequestMapping("/registrationConfirm.html")
    public String confirmAccount(@RequestParam(name = "token") String token, Model model) {

        TokenState tokenState = verificationTokenService.isTokenValidHtml(token, TokenType.ACCOUNT_ACTIVATION);

        if (!tokenState.equals(TokenState.VALID)) {
            model.addAttribute("TokenState", tokenState);
            return "errorPage";
        }
        model.addAttribute("TokenState", "Token is valid!");
        return "index";
    }

    @RequestMapping("/resetPassword.html")
    public String resetPasswordPage() {
        return "resetPassword";
    }
}
