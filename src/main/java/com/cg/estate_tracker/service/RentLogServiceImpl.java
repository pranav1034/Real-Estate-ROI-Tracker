package com.cg.estate_tracker.service;

import com.cg.estate_tracker.dtos.RentDTO;
import com.cg.estate_tracker.dtos.ResponseDTO;
import com.cg.estate_tracker.model.Property;
import com.cg.estate_tracker.model.RentLog;
import com.cg.estate_tracker.repository.PropertyRepository;
import com.cg.estate_tracker.repository.RentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RentLogServiceImpl {

    @Autowired
    RentRepository rentRepository;

    @Autowired
    PropertyRepository propertyRepository;

    public ResponseDTO addRent(RentDTO rentDTO){
        RentLog rentLog = new RentLog();
        rentLog.setAmount(rentDTO.getAmount());
        rentLog.setDateReceived(rentDTO.getDateReceived());

        Optional<Property> property = propertyRepository.findById(rentDTO.getPropertyID());
        if(property.isPresent()){
            Property p = property.get();
            rentLog.setProperty(p);
        }

        rentRepository.save(rentLog);
        ResponseDTO response = new ResponseDTO("Rent added successfully.",rentDTO);
        return response;
    }
}
