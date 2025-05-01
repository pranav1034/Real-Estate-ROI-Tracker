package com.cg.estate_tracker.controller;

import com.cg.estate_tracker.dtos.ResponseDTO;
import com.cg.estate_tracker.dtos.UserDTO;
import com.cg.estate_tracker.repository.UserRepository;
import com.cg.estate_tracker.service.IUserService;
import com.cg.estate_tracker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/auth")
public class PublicController {

    @Autowired
    private IUserService service;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody UserDTO request){
        return service.registerUser(request);
    }
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> loginUser(@RequestBody UserDTO request){
        return service.loginUser(request);
    }
}
