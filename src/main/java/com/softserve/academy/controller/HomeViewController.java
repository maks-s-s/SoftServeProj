package com.softserve.academy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeViewController {
    @GetMapping("/buy")
    public String buyPage() {
        System.out.println("buy psge");
        return "Buy";
    }
}
