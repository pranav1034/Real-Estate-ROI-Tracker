package com.cg.estate_tracker.controller;

import com.cg.estate_tracker.dtos.RentDTO;
import com.cg.estate_tracker.dtos.ResponseDTO;
import com.cg.estate_tracker.model.User;
import com.cg.estate_tracker.repository.UserRepository;
import com.cg.estate_tracker.service.AuthenticationService;
import com.cg.estate_tracker.service.IRentLogService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
    AuthenticationService authenticationService;

    @Autowired
    IRentLogService rentLogService;

    @PostMapping("/add")
    public ResponseEntity<ResponseDTO> addRentLog(@Valid @RequestBody RentDTO rent) {
        log.info("Received request to add rent log for property ID: {}", rent.getPropertyID());

        String email = authenticationService.getAuthenticatedUserEmail();
        User user = authenticationService.getAuthenticatedUser();
        if (user == null){
            return new ResponseEntity<>(new ResponseDTO("User not found with email: " + email, null), HttpStatus.NOT_FOUND);
        }

        try {
            ResponseDTO responseDTO = rentLogService.addRentLog(user, rent);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new ResponseDTO(e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (AccessDeniedException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(new ResponseDTO(e.getMessage(), null), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            log.error("Unexpected error occurred: ", e);
            return new ResponseEntity<>(new ResponseDTO("Internal Server Error", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/view/{propertyId}")
    public ResponseEntity<ResponseDTO> viewRentLogs(@PathVariable Long propertyId) {
        log.info("Received request to view rent logs for propertyId: {}", propertyId);

        String email = authenticationService.getAuthenticatedUserEmail();
        User user = authenticationService.getAuthenticatedUser();
        if (user == null){
            return new ResponseEntity<>(new ResponseDTO("User not found with email: " + email, null), HttpStatus.NOT_FOUND);
        }

        try {
            ResponseDTO responseDTO = rentLogService.viewRentLog(user, propertyId);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ResponseDTO(e.getReason(), null), e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO("Internal Server Error", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
