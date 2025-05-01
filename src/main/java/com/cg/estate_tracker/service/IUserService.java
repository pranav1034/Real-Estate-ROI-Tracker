package com.cg.estate_tracker.service;

import com.cg.estate_tracker.dtos.*;
import com.cg.estate_tracker.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IUserService {
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody RegisterDTO request);

    public ResponseEntity<ResponseDTO> loginUser(@RequestBody LoginDTO request);

    public ResponseEntity<User> getUserById(@PathVariable Long id);

    public List<User> getAllUsers();

    public ResponseEntity<String> deleteUser(@PathVariable Long id);

    public ResponseEntity<ResponseDTO> forgotPassword(@RequestBody ForgotPasswordDTO request);

    public ResponseEntity<ResponseDTO> resetPassword(@RequestBody ResetPasswordDTO request);


}
