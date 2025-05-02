package com.cg.estate_tracker.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RentDTO {

    @NotNull(message = "Property ID is required")
    private long propertyID;

    @NotNull(message = "Date received is required")
    @PastOrPresent(message = "Date received cannot be in the future")
    private LocalDate dateReceived;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private double amount;

}
