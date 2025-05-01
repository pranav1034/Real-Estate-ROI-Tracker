package com.cg.estate_tracker.service;

import com.cg.estate_tracker.dtos.RentDTO;
import com.cg.estate_tracker.dtos.ResponseDTO;

public interface IRentLogService {
    public ResponseDTO addRentLog(RentDTO rentDTO);
}
