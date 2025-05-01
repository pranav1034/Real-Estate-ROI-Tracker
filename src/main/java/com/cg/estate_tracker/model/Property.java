package com.cg.estate_tracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private String title;

    private String location;

    @Column(nullable = false)
    private double size;

    @Column(nullable = false)
    private double purchasePrice;

    @Column(nullable = false)
    private double currentMarketValue;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<RentLog> rentLogs = new ArrayList<>();

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<Expense> expenses = new ArrayList<>();
}
