package com.cg.estate_tracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RentDTO {
    private long propertyID;

    private LocalDate dateReceived;

    private double amount;

}
