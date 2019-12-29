package com.ixpert.isampleapp1.controller;


import com.ixpert.isampleapp1.model.User;
import com.ixpert.isampleapp1.service.EmailService;
import com.ixpert.isampleapp1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/register")
public class RegisterController {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private EmailService emailService;
    private UserService userService;

    @Autowired
    public RegisterController(BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService, EmailService emailService){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
        this.userService = userService;
    }



    @GetMapping
    public ModelAndView showRegister(ModelAndView modelAndView, User user){
        System.out.println("--------------------- RegisterController.showRegister --------------------");

        modelAndView.addObject("user", user);
        modelAndView.setViewName("register");
        return modelAndView;
    }





}
