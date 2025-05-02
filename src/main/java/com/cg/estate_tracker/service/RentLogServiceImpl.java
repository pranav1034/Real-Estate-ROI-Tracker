package com.cg.estate_tracker.service;

import com.cg.estate_tracker.dtos.RentDTO;
import com.cg.estate_tracker.dtos.ResponseDTO;
import com.cg.estate_tracker.model.Property;
import com.cg.estate_tracker.model.RentLog;
import com.cg.estate_tracker.model.User;
import com.cg.estate_tracker.repository.PropertyRepository;
import com.cg.estate_tracker.repository.RentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
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

    @Override
    public ResponseDTO addRentLog(User user, RentDTO rentDTO) {
        Long propertyId = rentDTO.getPropertyID();
        log.info("Adding rent log for property ID: {}", propertyId);

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> {
                    log.error("Property not found: {}", propertyId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found");
                });

        boolean isUserProperty = user.getProperties().stream()
                .anyMatch(p -> p.getId().equals(propertyId));
        if (!isUserProperty) {
            throw new AccessDeniedException("You do not own this property.");
        }

        RentLog rentLog = new RentLog();
        rentLog.setProperty(property);
        rentLog.setAmount(rentDTO.getAmount());
        rentLog.setDateReceived(rentDTO.getDateReceived());

        rentRepository.save(rentLog);
        log.info("Rent log saved for property '{}'", property.getTitle());

        return new ResponseDTO("Rent added successfully.", rentDTO);
    }


    @Override
    public ResponseDTO viewRentLog(User user, Long propertyId) {
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
        return new ResponseDTO("All rent logs of property have been fetched successfully", rentLogs);
    }
}
