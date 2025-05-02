package com.cg.estate_tracker.dtos;

import com.cg.estate_tracker.model.Expense;
import com.cg.estate_tracker.model.RentLog;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PropertyDTO {
    String title;
    String location;
    double size;
    double purchasePrice;
    double currentMarketValue;
    private LocalDate purchaseDate;
    List<RentLog> rentLogs;
    List<Expense> expenses;
}
