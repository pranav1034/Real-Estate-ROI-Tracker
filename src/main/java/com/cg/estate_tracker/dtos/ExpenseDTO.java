package com.cg.estate_tracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


@Data
@AllArgsConstructor
public class ExpenseDTO {
    private Long propertyId;
    private LocalDate expenseDate;
    private Double expenseAmount;
    private String expenseDetails;

}
