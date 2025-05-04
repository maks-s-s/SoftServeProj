package com.softserve.academy.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, HttpServletResponse response, Model model) {
        Integer statusCode = response.getStatus();

        System.out.println("Error Code: " + statusCode);

        if (statusCode == 0) {
            statusCode = 500;
        }

        model.addAttribute("errorCode", statusCode);

        return "errors/error";
    }
}
