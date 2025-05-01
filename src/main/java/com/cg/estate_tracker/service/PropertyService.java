package com.cg.estate_tracker.service;

import com.cg.estate_tracker.dtos.PropertyDTO;
import com.cg.estate_tracker.dtos.ResponseDTO;
import com.cg.estate_tracker.model.User;
import org.springframework.http.ResponseEntity;

public interface PropertyService {
    public ResponseEntity<ResponseDTO> addProperty(PropertyDTO property, User user);
}
