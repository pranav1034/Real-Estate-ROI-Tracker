package com.cg.estate_tracker.repository;

import com.cg.estate_tracker.model.RentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<RentLog,Long> {

    List<RentLog> findAllByPropertyId(Long propertyId);


}
