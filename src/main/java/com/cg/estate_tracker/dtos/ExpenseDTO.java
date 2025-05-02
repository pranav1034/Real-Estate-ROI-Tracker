package com.cg.estate_tracker.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


@Data
@AllArgsConstructor
public class ExpenseDTO {

    @NotNull(message = "Property ID is required")
    private Long propertyId;

    @NotNull(message = "Expense date is required")
    @PastOrPresent(message = "Expense date cannot be in the future")
    private LocalDate expenseDate;

    @NotNull(message = "Expense amount is required")
    @Positive(message = "Expense amount must be greater than zero")
    private Double expenseAmount;

    @NotBlank(message = "Expense details are required")
    @Size(max = 255, message = "Expense details must not exceed 255 characters")
    private String expenseDetails;

}
