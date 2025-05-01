
package com.cg.estate_tracker.service;

import com.cg.estate_tracker.dtos.PropertyDTO;
import com.cg.estate_tracker.dtos.ResponseDTO;
import com.cg.estate_tracker.model.Property;
import com.cg.estate_tracker.model.User;
import com.cg.estate_tracker.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PropertyServiceImpl implements IPropertyService {

    @Autowired
    PropertyRepository propertyRepository;

    public ResponseEntity<ResponseDTO> addProperty(PropertyDTO property, User user){
        Property newProperty = new Property();
        newProperty.setTitle(property.getTitle());
        newProperty.setUser(user);
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
        return new ResponseEntity<>(new ResponseDTO("New Property Added !",newProperty), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ResponseDTO> deleteProperty(Property property,User user,Long id) {

        if(!property.getUser().getId().equals(user.getId())){
            return new ResponseEntity<>(new ResponseDTO("User not authenticated",null),HttpStatus.UNAUTHORIZED);
        }
        else{
            propertyRepository.delete(property);
            return new ResponseEntity<>(new ResponseDTO("Property deleted successfully",null),HttpStatus.NO_CONTENT);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> updateProperty(Property property,PropertyDTO dto, User user, Long id) {
        if(!property.getUser().getId().equals(user.getId())){
            return new ResponseEntity<>(new ResponseDTO("User not authenticated",null),HttpStatus.UNAUTHORIZED);
        }

        property.setTitle(dto.getTitle());
        property.setLocation(dto.getLocation());
        property.setSize(dto.getSize());
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
        return new ResponseEntity<>(new ResponseDTO("Property Updated !",property), HttpStatus.OK);
    }



}
