package com.softserve.academy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {

    @RequestMapping("/error")
    public String handleError(Model model) {
        Integer statusCode = (Integer) model.asMap().get("javax.servlet.error.status_code");

        model.addAttribute("errorCode", statusCode);

        return "error";
    }
}
