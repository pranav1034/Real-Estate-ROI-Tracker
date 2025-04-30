package com.cg.estate_tracker.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name; // e.g., "ROLE_ADMIN", "ROLE_USER"

}
