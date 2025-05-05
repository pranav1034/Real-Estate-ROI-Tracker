package com.cg.estate_tracker.service;

import com.cg.estate_tracker.model.Property;
import com.cg.estate_tracker.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class PropertyValueScheduler {

    @Autowired
    private PropertyRepository propertyRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void increaseValueAfterEachMonthSincePurchase() {

        LocalDate today = LocalDate.now();
        List<Property> properties = propertyRepository.findAll();

        for (Property property : properties) {
            LocalDate purchaseDate = property.getPurchaseDate();

            // Check if at least 1 month has passed
            long monthsPassed = ChronoUnit.MONTHS.between(
                    purchaseDate.withDayOfMonth(1),
                    today.withDayOfMonth(1)
            );

            boolean isSameDayOfMonth = purchaseDate.getDayOfMonth() == today.getDayOfMonth();

            if (monthsPassed >= 1 && isSameDayOfMonth) {
                double updatedValue = property.getCurrentMarketValue() * 1.01;
                property.setCurrentMarketValue(updatedValue);
            }
        }

        propertyRepository.saveAll(properties);
    }
}

