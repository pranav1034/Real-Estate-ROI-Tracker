package com.cg.estate_tracker.dtos;

import com.cg.estate_tracker.model.Expense;
import com.cg.estate_tracker.model.RentLog;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PropertyDTO {
    @NotBlank(message = "Title must not be blank")
    private String title;

    @NotBlank(message = "Location must not be blank")
    private String location;

    @Positive(message = "Size must be a positive value")
    private double size;

    @PositiveOrZero(message = "Purchase price must be zero or positive")
    private double purchasePrice;

    @PositiveOrZero(message = "Current market value must be zero or positive")
    private double currentMarketValue;

    @NotNull(message = "Purchase date must not be null")
    private LocalDate purchaseDate;

//    @NotNull(message = "Rent logs list must not be null")
//    @Size(min = 0, message = "Rent logs list must not be null")
    private List<RentLog> rentLogs;

//    @NotNull(message = "Expenses list must not be null")
//    @Size(min = 0, message = "Expenses list must not be null")
    private List<Expense> expenses;
}
