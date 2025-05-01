package com.cg.estate_tracker.service;

import com.cg.estate_tracker.dtos.PropertyDto;
//import com.cg.estate_tracker.dtos.Response;
import com.cg.estate_tracker.dtos.ResponseDTO;
import com.cg.estate_tracker.model.Property;
import com.cg.estate_tracker.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropertyServiceImpl {

    @Autowired
    PropertyRepository propertyRepository;

    public ResponseDTO addProperty(PropertyDto property){
        Property newProperty = new Property();
        newProperty.setTitle(property.getTitle());
        newProperty.setLocation(property.getLocation());
        newProperty.setSize(property.getSize());
        newProperty.setPurchasePrice(property.getPurchasePrice());
        newProperty.setCurrentMarketValue(property.getCurrentMarketValue());
        if (property.getRentLogs() != null) {
            property.getRentLogs().forEach(r -> r.setProperty(newProperty));
            property.setRentLogs(property.getRentLogs());
        }

        if (property.getExpenses() != null) {
            property.getExpenses().forEach(e -> e.setProperty(newProperty));
            property.setExpenses(property.getExpenses());
        }

        propertyRepository.save(newProperty);
        return new ResponseDTO("New Property Added !",newProperty);
    }
}
