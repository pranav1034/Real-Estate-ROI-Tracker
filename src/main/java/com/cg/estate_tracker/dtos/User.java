package com.cg.estate_tracker.dtos;

import com.cg.estate_tracker.model.Property;
import com.cg.estate_tracker.model.Role;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class User {
    String email;
    String password;
    String name;
    Set<Role> roles;
    List<Property> properties;
}
