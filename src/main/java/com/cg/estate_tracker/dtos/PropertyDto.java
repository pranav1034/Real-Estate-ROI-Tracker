package com.cg.estate_tracker.dtos;

import com.cg.estate_tracker.model.Expense;
import com.cg.estate_tracker.model.RentLog;
import lombok.Data;
import java.util.List;

@Data
public class PropertyDto {
    User user;
    String title;
    String location;
    double size;
    double purchasePrice;
    double currentMarketValue;
    List<RentLog> rentLogs;
    List<Expense> expenses;
}
