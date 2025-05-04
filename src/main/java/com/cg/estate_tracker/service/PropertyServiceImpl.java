
package com.cg.estate_tracker.service;

import com.cg.estate_tracker.dtos.PropertyDTO;
import com.cg.estate_tracker.dtos.ResponseDTO;
import com.cg.estate_tracker.model.Property;
import com.cg.estate_tracker.model.User;
import com.cg.estate_tracker.repository.PropertyRepository;
import com.cg.estate_tracker.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class PropertyServiceImpl implements IPropertyService {

    @Autowired
    PropertyRepository propertyRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<ResponseDTO> addProperty(PropertyDTO property, User user){
        Property newProperty = new Property();
        newProperty.setTitle(property.getTitle());
        newProperty.setUser(user);
        newProperty.setLocation(property.getLocation());
        newProperty.setPurchaseDate(property.getPurchaseDate());
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
        user.getProperties().add(newProperty);
        userRepository.save(user);
        log.info("Property added successfully:");
        return new ResponseEntity<>(new ResponseDTO("New Property Added !",property), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ResponseDTO> deleteProperty(Property property,User user,Long id) {

        if(!property.getUser().getId().equals(user.getId())){
            log.warn("User not authenticated for property ID: {}", id);
            return new ResponseEntity<>(new ResponseDTO("User not authenticated",null),HttpStatus.UNAUTHORIZED);
        }
        else{
            propertyRepository.delete(property);
            log.info("Property deleted successfully: {}", property);
            return new ResponseEntity<>(new ResponseDTO("Property deleted successfully",null),HttpStatus.NO_CONTENT);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> updateProperty(Property property,PropertyDTO dto, User user, Long id) {
        if(!property.getUser().getId().equals(user.getId())){
            log.warn("User not authenticated for property ID: {}", id);
            return new ResponseEntity<>(new ResponseDTO("User not authenticated",null),HttpStatus.UNAUTHORIZED);
        }

        property.setTitle(dto.getTitle());
        property.setLocation(dto.getLocation());
        property.setSize(dto.getSize());
        property.setPurchaseDate(dto.getPurchaseDate());
        property.setPurchasePrice(dto.getPurchasePrice());
        property.setCurrentMarketValue(dto.getCurrentMarketValue());

        if (dto.getRentLogs() != null) {
            dto.getRentLogs().forEach(r -> r.setProperty(property));
            property.setRentLogs(property.getRentLogs());
        }

        if (dto.getExpenses() != null) {
            dto.getExpenses().forEach(e -> e.setProperty(property));
            property.setExpenses(property.getExpenses());
        }
        propertyRepository.save(property);
        log.info("Property updated successfully: {}", property);
        return new ResponseEntity<>(new ResponseDTO("Property Updated !",property), HttpStatus.OK);
    }

    public ResponseEntity<ResponseDTO> viewProperty(Long id,User user){
        List<Property> propertyList = user.getProperties();
        Property property = null;

        for(Property p : propertyList){
            if(id.equals(p.getId())){
                property = p;
                break;
            }
        }

        if(property == null) return new ResponseEntity<>(new ResponseDTO("Property not found !",null),HttpStatus.NOT_FOUND);

        log.info("Property fetched successfully: {}", property);
        return new ResponseEntity<>(new ResponseDTO("Property fetched !",property),HttpStatus.OK);

    }

}
