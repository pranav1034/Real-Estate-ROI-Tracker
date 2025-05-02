package com.cg.estate_tracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {
    private String propertyTitle;
    private double purchasePrice;
    private double currentValue;
    private double totalRentReceived;
    private double totalExpenses;
    private double roi;
    private double rentalYield;
}
