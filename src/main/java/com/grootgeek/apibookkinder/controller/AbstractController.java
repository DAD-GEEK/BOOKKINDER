package com.grootgeek.apibookkinder.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AbstractController {

    @Value("${index.mensaje}")
    private String message;

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("message", message);
        return "index";
    }

}
