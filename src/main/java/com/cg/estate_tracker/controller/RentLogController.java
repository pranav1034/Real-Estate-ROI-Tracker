package com.cg.estate_tracker.controller;

import com.cg.estate_tracker.dtos.RentDTO;
import com.cg.estate_tracker.dtos.ResponseDTO;
import com.cg.estate_tracker.model.Property;
import com.cg.estate_tracker.model.User;
import com.cg.estate_tracker.repository.PropertyRepository;
import com.cg.estate_tracker.repository.UserRepository;
import com.cg.estate_tracker.service.RentLogServiceImpl;
import com.cg.estate_tracker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user/rent-log")
public class RentLogController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PropertyRepository propertyRepository;

    @Autowired
    RentLogServiceImpl rentLogService;

    @PostMapping("/add")
    public ResponseEntity<ResponseDTO> addRentLog(@RequestHeader("Authorization") String authHeader, @RequestBody RentDTO rent){

        // is user authenticated
        String token = "";
        String email = "";
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            if(!jwtUtil.validateToken(token)) {
                return new ResponseEntity<>(new ResponseDTO("Login Expired,login again",null),HttpStatus.REQUEST_TIMEOUT);
            }
                email = jwtUtil.extractEmail(token);
        }

        // Does property exist or belongs to the user or not
        User user = userRepository.findByEmail(email);
        Optional<Property> propertyOpt = propertyRepository.findById(rent.getPropertyID());

        if (propertyOpt.isEmpty()) {
            return new ResponseEntity<>(new ResponseDTO("Property not found.",null),HttpStatus.NOT_FOUND);
        }

        boolean isUserProperty = user.getProperties().stream()
                .anyMatch(p -> p.getId() == rent.getPropertyID());

        if (!isUserProperty) {
            return new ResponseEntity<>(new ResponseDTO("You do not own this property.",null),HttpStatus.FORBIDDEN);
        }

        // adding rent log for the property

        ResponseDTO responseDTO = rentLogService.addRent(rent);


        ResponseDTO response = new ResponseDTO("Rent added successfully.",rent);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

}
