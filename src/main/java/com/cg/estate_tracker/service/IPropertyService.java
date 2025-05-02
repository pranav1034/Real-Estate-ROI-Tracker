package com.cg.estate_tracker.service;

import com.cg.estate_tracker.dtos.PropertyDTO;
import com.cg.estate_tracker.dtos.ResponseDTO;
import com.cg.estate_tracker.model.Property;
import com.cg.estate_tracker.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface IPropertyService {
    public ResponseEntity<ResponseDTO> addProperty(PropertyDTO property, User user);
    public ResponseEntity<ResponseDTO> deleteProperty(Property property,User user, @PathVariable Long id);
    public ResponseEntity<ResponseDTO> updateProperty(Property property,PropertyDTO dto,User user, @PathVariable Long id);
    public ResponseEntity<ResponseDTO> viewProperty(@PathVariable Long id,User user);
}
