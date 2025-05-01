package com.cg.estate_tracker.service;

import com.cg.estate_tracker.dtos.ResetPasswordDTO;
import com.cg.estate_tracker.dtos.ResponseDTO;
import com.cg.estate_tracker.dtos.UserDTO;
import com.cg.estate_tracker.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IUserService {
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody UserDTO request);

    public ResponseEntity<ResponseDTO> loginUser(@RequestBody UserDTO request);

    public ResponseEntity<User> getUserById(@PathVariable Long id);

    public List<User> getAllUsers();

    public ResponseEntity<String> deleteUser(@PathVariable Long id);

    public ResponseEntity<ResponseDTO> forgotPassword(@RequestBody UserDTO request);

    public ResponseEntity<ResponseDTO> resetPassword(@RequestBody ResetPasswordDTO request);


}
