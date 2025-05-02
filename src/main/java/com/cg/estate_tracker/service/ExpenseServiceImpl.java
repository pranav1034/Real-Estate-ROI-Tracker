package com.cg.estate_tracker.service;

import com.cg.estate_tracker.dtos.ExpenseDTO;
import com.cg.estate_tracker.dtos.ResponseDTO;
import com.cg.estate_tracker.model.Expense;
import com.cg.estate_tracker.model.Property;
import com.cg.estate_tracker.model.RentLog;
import com.cg.estate_tracker.model.User;
import com.cg.estate_tracker.repository.ExpenseRepository;
import com.cg.estate_tracker.repository.PropertyRepository;
import com.cg.estate_tracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ExpenseServiceImpl implements IExpenseService {

    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PropertyRepository propertyRepository;

    @Override
    public ResponseDTO addExpense(User user, ExpenseDTO expenseDTO) {

        log.info("Adding expense for property ID: {}", expenseDTO.getPropertyId());

        Optional<Property> propertyExist = propertyRepository.findById(expenseDTO.getPropertyId());

        if(propertyExist.isEmpty())
        {
            log.error("Property not found: {}", expenseDTO.getPropertyId());
            return new ResponseDTO("Property doesn't exist",null);
        }

        Property property = propertyExist.get();

        boolean isUserProperty = user.getProperties().stream()
                .anyMatch(p -> p.getId().equals(expenseDTO.getPropertyId()));

        if(!isUserProperty){
            log.warn("Unauthorized access attempt by user: {} for property: {}", user.getEmail(), expenseDTO.getPropertyId());
            return new ResponseDTO("Property doesn't belong to user",null);
        }

        Expense expense = new Expense();
        expense.setProperty(property);
        expense.setAmount(expenseDTO.getExpenseAmount());
        expense.setExpenseDetails(expenseDTO.getExpenseDetails());
        expense.setExpenseDate(expenseDTO.getExpenseDate());

        expenseRepository.save(expense);
        log.info("Expense saved for property '{}'", property.getTitle());

        return new ResponseDTO("Property expense added successfully.",expenseDTO);
    }

    @Override
    public ResponseDTO viewExpense(User user, Long propertyId) {
        log.info("Fetching expenses for propertyId: {} by user: {}", propertyId, user.getEmail());

        Optional<Property> propertyExist = propertyRepository.findById(propertyId);

        if(propertyExist.isEmpty())
        {
            log.error("Property not found: {}", propertyId);
            return new ResponseDTO("Property doesn't exist",null);
        }

        Property property = propertyExist.get();

        if (!property.getUser().getId().equals(user.getId())) {
            log.warn("Unauthorized access attempt by user: {} for property: {}", user.getEmail(), propertyId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this property.");
        }
        List<Expense> expenses = expenseRepository.findAllByPropertyId(propertyId);

        log.info("Fetched {} expenses for propertyId: {}", expenses.size(), propertyId);
        return new ResponseDTO("All expenses of property have been fetched successfully", expenses);
    }
}
