package com.cg.estate_tracker.service;

import com.cg.estate_tracker.dtos.ResponseDTO;
import com.cg.estate_tracker.dtos.UserDTO;
import com.cg.estate_tracker.model.User;
import com.cg.estate_tracker.repository.UserRepository;
import com.cg.estate_tracker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public ResponseEntity<ResponseDTO> registerUser(@RequestBody UserDTO request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            ResponseDTO response = new ResponseDTO("user already exists", HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        mailService.sendMail(request.getEmail(), "Registration Successful", "Welcome to the Estate Tracker System!");

        ResponseDTO response = new ResponseDTO("User registered successfully", HttpStatus.CREATED);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<ResponseDTO> loginUser(@RequestBody UserDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception e) {
            e.printStackTrace();
            ResponseDTO response = new ResponseDTO("Invalid Credentials", HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        final UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(request.getEmail());
        final String token = jwtUtil.generateToken(userDetails);

        ResponseDTO response = new ResponseDTO(token, HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

