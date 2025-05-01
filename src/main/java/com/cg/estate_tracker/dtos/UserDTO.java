package com.cg.estate_tracker.dtos;

import com.cg.estate_tracker.model.Property;
import com.cg.estate_tracker.model.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserDTO {
    String email;
    String password;
    String name;
    Set<Role> roles = new HashSet<>();

    List<Property> properties;

    public UserDTO(String email){
        this.email = email;
    }
}
