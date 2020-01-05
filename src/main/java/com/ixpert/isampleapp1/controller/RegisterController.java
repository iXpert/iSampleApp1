package com.ixpert.isampleapp1.controller;


import com.ixpert.isampleapp1.model.User;
import com.ixpert.isampleapp1.service.EmailService;
import com.ixpert.isampleapp1.service.UserService;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

@Controller
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


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView showRegisterPage(ModelAndView modelAndView, User user){
        System.out.println("--------------------- RegisterController.showRegister --------------------");

        modelAndView.addObject("user", user);
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView processRegistrationForm(ModelAndView modelAndView, @Valid User user, BindingResult bindingResult, HttpServletRequest request){

        User userExists = userService.findByEmail(user.getEmail());

        System.out.println("\n -----------------------------------------");
        System.out.println("\n  Checked email: "+user.getEmail());
        System.out.println(userExists);
        System.out.println("\n -----------------------------------------");


        if (userExists != null){
            modelAndView.addObject("alreadyRegisteredMessage","There is already a user registered with this email!");
            modelAndView.setViewName("register");
            bindingResult.reject("email");
        }

        if (bindingResult.hasErrors()){
            modelAndView.setViewName("register");
        } else {
            // new user
            user.setEnabled(false);
            // generate a random confirmation token
            user.setConfirmationToken(UUID.randomUUID().toString());

            // save user
            userService.saveUser(user);

            String appUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();

            SimpleMailMessage registrationEmail = new SimpleMailMessage();
            registrationEmail.setTo(user.getEmail());
            registrationEmail.setSubject("Registration Email at iXpert Technologies");
            registrationEmail.setText("Dear "+user.getFirstName()+", \n"+"To confirm your email address, please click on the link below:\n"+appUrl+"/confirm?token="+user.getConfirmationToken());
            registrationEmail.setFrom("info@ixpert.org");

            emailService.sendMail(registrationEmail);

            modelAndView.addObject("confirmationMessage","A confirmation email has been sent to "+user.getEmail());
           // modelAndView.setViewName("register");
            modelAndView.setViewName("successRegistration");
        }

       return modelAndView; 
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public ModelAndView confirmRegistration(ModelAndView modelAndView, @RequestParam("token") String token){

        User user = userService.findByConfirmationToken(token);

        if (user == null){ // no similar tokens were found in the DB
             modelAndView.addObject("invalidToken","This is an invalid confirmation link");
        } else {
            modelAndView.addObject("confirmationToken",user.getConfirmationToken());
        }

        modelAndView.setViewName("confirm");
        return modelAndView;
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public ModelAndView confirmRegistration(ModelAndView modelAndView, BindingResult bindingResult, @RequestParam Map<String, String> requestParams, RedirectAttributes redir){

        System.out.println("\n --------------------- RegisterController.confirmRegistration POST --------------------");

        modelAndView.setViewName("confirm");

        Zxcvbn passwordCheck = new Zxcvbn();
        Strength strength = passwordCheck.measure(requestParams.get("password"));

        if (strength.getScore() < 3){
            bindingResult.reject("password");
            redir.addFlashAttribute("errorMessage","Your password is too weak. Choose a stronger one.");
            modelAndView.setViewName("redirect:confirm?token="+requestParams.get("token"));
            System.out.println("\n  Token: "+requestParams.get("token"));
            return modelAndView;
        }

        // find the user associated with the token
        User user = userService.findByConfirmationToken(requestParams.get("token"));

        // set new password
        user.setPassword(bCryptPasswordEncoder.encode(requestParams.get("password")));

        // set user to enabled
        user.setEnabled(true);

        // save user
        userService.saveUser(user);

        modelAndView.addObject("successMessage","Your password has been set!");
        return modelAndView;

    }



}
