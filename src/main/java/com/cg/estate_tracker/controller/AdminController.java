package com.cg.estate_tracker.controller;

import com.cg.estate_tracker.model.User;
import com.cg.estate_tracker.repository.UserRepository;
import com.cg.estate_tracker.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IUserService service;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return service.getUserById(id);
    }

    @GetMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        return service.deleteUser(id);
    }
}
