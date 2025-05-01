package com.cg.estate_tracker.service;

import com.cg.estate_tracker.dtos.ResponseDTO;
import com.cg.estate_tracker.dtos.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface IUserService {
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody UserDTO request);

    public ResponseEntity<ResponseDTO> loginUser(@RequestBody UserDTO request);


}
