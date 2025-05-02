package com.cg.estate_tracker.service;

import com.cg.estate_tracker.dtos.*;
import com.cg.estate_tracker.model.Role;
import com.cg.estate_tracker.model.User;
import com.cg.estate_tracker.repository.UserRepository;
import com.cg.estate_tracker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

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

    public ResponseEntity<ResponseDTO> registerUser(@RequestBody RegisterDTO request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            ResponseDTO response = new ResponseDTO("user already exists", HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setProperties(new ArrayList<>());
        userRepository.save(user);

        mailService.sendMail(request.getEmail(), "Registration Successful", "Welcome to the Estate Tracker System!");

        ResponseDTO response = new ResponseDTO("User registered successfully", HttpStatus.CREATED);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<ResponseDTO> loginUser(@RequestBody LoginDTO request) {
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

    public ResponseEntity<User> getUserById(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email);

        if (currentUser.getRole() != Role.ADMIN) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public List<User> getAllUsers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // email = username
        User currentUser = userRepository.findByEmail(email);

        if (currentUser.getRole() != Role.ADMIN) {
            throw new RuntimeException("Access denied. Admins only.");
        }
        return userRepository.findAll();
    }

    public ResponseEntity<String> deleteUser(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email);

        if (currentUser.getRole() != Role.ADMIN) {
            return new ResponseEntity<>("Access denied. Admins only.", HttpStatus.FORBIDDEN);
        }

        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        userRepository.delete(user);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    public ResponseEntity<ResponseDTO> forgotPassword(@RequestBody ForgotPasswordDTO request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            ResponseDTO response = new ResponseDTO("User not found", HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        String otp = String.valueOf(Math.round(Math.random() * 9000 + 1000));
        user.setOtp(otp);
        userRepository.save(user);

        mailService.sendMail(request.getEmail(),"Password Reset OTP", "Your OTP is: " + otp);
        ResponseDTO response = new ResponseDTO("OTP sent to your email", HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ResponseDTO> resetPassword(@RequestBody ResetPasswordDTO request){
        User user = userRepository.findByOtp(request.getOtp());
        if (user == null) {
            ResponseDTO response = new ResponseDTO("User not found", HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if (!user.getOtp().equals(request.getOtp())) {
            ResponseDTO response = new ResponseDTO("Invalid OTP", HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setOtp(null);
        userRepository.save(user);

        mailService.sendMail(user.getEmail(),"Password Reset Successful", "Your password has been reset successfully!");

        ResponseDTO response = new ResponseDTO("Password reset successfully", HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

