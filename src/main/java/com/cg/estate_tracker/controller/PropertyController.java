package com.cg.estate_tracker.controller;

import com.cg.estate_tracker.dtos.PropertyDto;
import com.cg.estate_tracker.dtos.ResponseDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/property")
public class PropertyController {
    public void addNewProperty(PropertyDto dto){
    }
}
