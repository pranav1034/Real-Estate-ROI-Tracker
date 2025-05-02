package com.cg.estate_tracker.repository;

import com.cg.estate_tracker.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property,Long> {
    Optional<Property> findById(Long Id);

    List<Property> findByUserId(Long id);
}
