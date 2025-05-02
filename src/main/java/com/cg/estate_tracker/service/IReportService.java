package com.cg.estate_tracker.service;

import com.cg.estate_tracker.model.User;

import java.time.LocalDate;

public interface IReportService {
    byte[] generateUserReport(User user, LocalDate date);
}
