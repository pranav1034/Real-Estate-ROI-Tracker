package com.cg.estate_tracker.service;

import com.cg.estate_tracker.dtos.RentDTO;
import com.cg.estate_tracker.dtos.ResponseDTO;
import com.cg.estate_tracker.model.Property;
import com.cg.estate_tracker.model.RentLog;
import com.cg.estate_tracker.model.User;
import com.cg.estate_tracker.repository.PropertyRepository;
import com.cg.estate_tracker.repository.RentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RentLogServiceImpl implements IRentLogService {

    @Autowired
    RentRepository rentRepository;

    @Autowired
    PropertyRepository propertyRepository;

    public ResponseDTO addRentLog(RentDTO rentDTO){
        log.info("Adding new rent log for propertyId: {}", rentDTO.getPropertyID());
        RentLog rentLog = new RentLog();
        rentLog.setAmount(rentDTO.getAmount());
        rentLog.setDateReceived(rentDTO.getDateReceived());

        Optional<Property> property = propertyRepository.findById(rentDTO.getPropertyID());
        if (property.isPresent()) {
            Property p = property.get();
            rentLog.setProperty(p);
            log.debug("Property found: {}", p.getTitle());
        } else {
            log.warn("Property with ID {} not found", rentDTO.getPropertyID());
        }

        rentRepository.save(rentLog);
        log.info("Rent log saved successfully");
        ResponseDTO response = new ResponseDTO("Rent added successfully.",rentDTO);
        return response;
    }

    @Override
    public ResponseDTO viewRentLog(User user,Long propertyId) {

        log.info("Fetching rent logs for propertyId: {} by user: {}", propertyId, user.getEmail());

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> {
                    log.error("Property not found: {}", propertyId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found");
                });

        if (!property.getUser().getId().equals(user.getId())) {
            log.warn("Unauthorized access attempt by user: {} for property: {}", user.getEmail(), propertyId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this property.");
        }

        List<RentLog> rentLogs = rentRepository.findAllByPropertyId(propertyId);
        log.info("Fetched {} rent logs for propertyId: {}", rentLogs.size(), propertyId);
        return new ResponseDTO("All rent logs of property has been fetched successfully",rentLogs);
    }
}
