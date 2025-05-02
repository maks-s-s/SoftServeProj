package com.softserve.academy.controller;

import com.softserve.academy.repository.CustomerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignController {
    @GetMapping("/")
    public String signInHome() {
        return "signIn";
    }

    @GetMapping("/SignUp")
    public String signUp() {
        return "signUp";
    }

    @GetMapping("/home")
    public String goHome(Model model, HttpSession session) {
        model.addAttribute("name", session.getAttribute("name"));
        return "home";
    }
}