package com.cg.estate_tracker.service;

import com.cg.estate_tracker.dtos.RentDTO;
import com.cg.estate_tracker.dtos.ResponseDTO;
import com.cg.estate_tracker.model.User;

public interface IRentLogService {
    public ResponseDTO addRentLog(RentDTO rentDTO);

    public ResponseDTO viewRentLog(User user, Long propertyId);
}
