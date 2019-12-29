package com.ixpert.isampleapp1.controller;

import com.ixpert.isampleapp1.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;


@Controller
@RequestMapping(value = "/login")
public class LoginController {

    @GetMapping
    public ModelAndView showLogin(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @PostMapping
    public ModelAndView performLogin(@Valid User user, BindingResult bindingResult){
        System.out.println("--------------------- LoginController.performLogin --------------------");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("successLogin");
        return modelAndView;
    }


}
