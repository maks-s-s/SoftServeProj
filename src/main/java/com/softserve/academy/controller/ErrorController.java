package com.softserve.academy.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletResponse response, Model model) {
        Integer statusCode = response.getStatus();

        System.out.println("Error Code: " + statusCode);

        if (statusCode == 0) {
            statusCode = 500;
        }

        model.addAttribute("errorCode", statusCode);

        return "errors/error";
    }

    @RequestMapping("/error-403")
    public String handleError403(Model model) {
        model.addAttribute("errorCode", 403);
        return "errors/error";
    }
}
