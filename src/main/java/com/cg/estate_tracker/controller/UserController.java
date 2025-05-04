package com.cg.estate_tracker.controller;

import com.cg.estate_tracker.dtos.ForgotPasswordDTO;
import com.cg.estate_tracker.dtos.ResetPasswordDTO;
import com.cg.estate_tracker.dtos.ResponseDTO;
import com.cg.estate_tracker.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService service;

    @PostMapping("/forgotPassword")
    public ResponseEntity<ResponseDTO> forgotPassword(@Valid @RequestBody ForgotPasswordDTO request) {
        return service.forgotPassword(request);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ResponseDTO> resetPassword(@Valid @RequestBody ResetPasswordDTO request){
        return service.resetPassword(request);
    }
}
