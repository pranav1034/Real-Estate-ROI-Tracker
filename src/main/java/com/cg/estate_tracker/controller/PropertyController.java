package com.cg.estate_tracker.controller;
import com.cg.estate_tracker.dtos.PropertyDTO;
import com.cg.estate_tracker.dtos.ResponseDTO;
import com.cg.estate_tracker.model.Property;
import com.cg.estate_tracker.model.User;
import com.cg.estate_tracker.repository.PropertyRepository;
import com.cg.estate_tracker.repository.UserRepository;
import com.cg.estate_tracker.service.PropertyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user/property")
public class PropertyController {

    private final UserRepository userRepository;

    @Autowired
    PropertyServiceImpl propertyService;
    @Autowired
    private PropertyRepository propertyRepository;

    public PropertyController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseDTO> addNewProperty(@RequestBody PropertyDTO dto){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userRepository.findByEmail(email);

            if(user != null){
                return propertyService.addProperty(dto,user);
            }

            else{
                return new ResponseEntity<>(new ResponseDTO("User not found",null), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ResponseDTO("Error adding property",null),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteProperty(@PathVariable Long id){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userRepository.findByEmail(email);
            Optional<Property> obj = propertyRepository.findById(id);

            if(obj.isPresent()){
                return propertyService.deleteProperty(obj.get(),user,id);
            }
            else{
                return new ResponseEntity<>(new ResponseDTO("Property not found",null),HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ResponseDTO("Error deleting property",null),HttpStatus.BAD_REQUEST);
        }
    }
}