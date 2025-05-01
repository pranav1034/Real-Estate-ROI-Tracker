package com.cg.estate_tracker.repository;

import com.cg.estate_tracker.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property,Long> {
}
