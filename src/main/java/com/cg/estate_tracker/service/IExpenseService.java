package com.cg.estate_tracker.service;

import com.cg.estate_tracker.dtos.ExpenseDTO;
import com.cg.estate_tracker.dtos.ResponseDTO;
import com.cg.estate_tracker.model.User;

public interface IExpenseService {
    ResponseDTO addExpense(User user, ExpenseDTO expenseDTO);
    ResponseDTO viewExpense(User user,Long propertyId);
}
