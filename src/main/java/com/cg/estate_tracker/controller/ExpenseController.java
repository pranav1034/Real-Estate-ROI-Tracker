package com.cg.estate_tracker.controller;

import com.cg.estate_tracker.dtos.ExpenseDTO;
import com.cg.estate_tracker.dtos.ResponseDTO;
import com.cg.estate_tracker.model.User;
import com.cg.estate_tracker.repository.UserRepository;
import com.cg.estate_tracker.service.AuthenticationService;
import com.cg.estate_tracker.service.IExpenseService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user/expense")
@Slf4j
public class ExpenseController {

    @Autowired
    IExpenseService expenseService;

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/add")
    public ResponseEntity<ResponseDTO> addExpense(@Valid @RequestBody ExpenseDTO expenseDTO){

        log.info("Received request to add expense for property ID: {}", expenseDTO.getPropertyId());

        String email = authenticationService.getAuthenticatedUserEmail();
        User user = authenticationService.getAuthenticatedUser();

        if (user == null){
            log.warn("User not found with email: {}", email);
            return new ResponseEntity<>(new ResponseDTO("User not found with email: " + email , null), HttpStatus.NOT_FOUND);
        }

        ResponseDTO responseDTO = expenseService.addExpense(user,expenseDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/view/{propertyId}")
    public ResponseEntity<ResponseDTO> viewExpense(@PathVariable Long propertyId){

        log.info("Received request to view expense for propertyId: {}", propertyId);

        String email = authenticationService.getAuthenticatedUserEmail();
        User user = authenticationService.getAuthenticatedUser();
        if (user == null){
            log.warn("User not found with email: {}", email);
            return new ResponseEntity<>(new ResponseDTO("User not found with email: " + email, null), HttpStatus.NOT_FOUND);
        }

        ResponseDTO responseDTO = expenseService.viewExpense(user,propertyId);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
