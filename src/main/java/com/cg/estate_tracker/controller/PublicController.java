package com.cg.estate_tracker.controller;

import com.cg.estate_tracker.dtos.LoginDTO;
import com.cg.estate_tracker.dtos.RegisterDTO;
import com.cg.estate_tracker.dtos.ResponseDTO;
import com.cg.estate_tracker.service.IUserService;
import com.cg.estate_tracker.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/auth")
public class PublicController {

    @Autowired
    private IUserService service;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@Valid @RequestBody RegisterDTO request){
        return service.registerUser(request);
    }
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> loginUser(@Valid @RequestBody LoginDTO request){
        return service.loginUser(request);
    }
}
