package com.ixpert.isampleapp1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping(value = {"/", "/home"})
    public String showHome(){
        return "home";
    }


}
