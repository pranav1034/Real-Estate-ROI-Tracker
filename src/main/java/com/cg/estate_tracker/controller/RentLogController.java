package com.cg.estate_tracker.controller;

import com.cg.estate_tracker.dtos.RentDTO;
import com.cg.estate_tracker.dtos.ResponseDTO;
import com.cg.estate_tracker.model.Property;
import com.cg.estate_tracker.model.User;
import com.cg.estate_tracker.repository.PropertyRepository;
import com.cg.estate_tracker.repository.UserRepository;
import com.cg.estate_tracker.service.IRentLogService;
import com.cg.estate_tracker.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/user/rent-log")
@Slf4j
public class RentLogController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PropertyRepository propertyRepository;

    @Autowired
    IRentLogService rentLogService;

    @PostMapping("/add")
    public ResponseEntity<ResponseDTO> addRentLog(@RequestBody RentDTO rent){

        log.info("Received request to add rent log for property ID: {}", rent.getPropertyID());

        // is user authenticated
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        // Does property exist or belongs to the user or not
        Optional<Property> propertyOpt = propertyRepository.findById(rent.getPropertyID());

        if (propertyOpt.isEmpty()) {
            log.error("Property with ID {} not found.", rent.getPropertyID());
            return new ResponseEntity<>(new ResponseDTO("Property not found.",null),HttpStatus.NOT_FOUND);
        }

        boolean isUserProperty = user.getProperties().stream()
                .anyMatch(p -> p.getId() == rent.getPropertyID());

        if (!isUserProperty) {
            log.warn("User {} attempted to access property ID {} which they do not own.", user.getEmail(), rent.getPropertyID());
            return new ResponseEntity<>(new ResponseDTO("You do not own this property.",null),HttpStatus.FORBIDDEN);
        }

        // adding rent log for the property
        ResponseDTO responseDTO = rentLogService.addRentLog(rent);
        log.info("Rent log added successfully for property ID {}.", rent.getPropertyID());
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/view/{propertyId}")
    public ResponseEntity<ResponseDTO> viewRentLogs(@PathVariable Long propertyId){
        log.info("Received request to view rent logs for propertyId: {}", propertyId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);

        ResponseDTO responseDTO = rentLogService.viewRentLog(user,propertyId);
        log.info("Successfully fetched rent logs for propertyId: {}", propertyId);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

}
