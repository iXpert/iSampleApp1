package com.ixpert.isampleapp1.service;


import com.ixpert.isampleapp1.model.User;
import com.ixpert.isampleapp1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//@Service("userService")
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User findByConfirmationToken(String confirmationToken){
        return userRepository.findByConfirmationToken(confirmationToken);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

}
