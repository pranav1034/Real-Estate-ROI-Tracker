package com.cg.estate_tracker.controller;

import com.cg.estate_tracker.model.User;
import com.cg.estate_tracker.repository.UserRepository;
import com.cg.estate_tracker.service.IReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/report")
@Slf4j
public class ReportController {

    @Autowired
    private IReportService reportService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user")
    public ResponseEntity<byte[]> getUserReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        User user = getCurrentUser();
        byte[] pdf = reportService.generateUserReport(user, date);
        return buildPdfResponse(pdf, "user_report.pdf");
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<byte[]> getPropertyReport(
            @PathVariable Long propertyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        User user = getCurrentUser();
        byte[] pdf = reportService.generatePropertyReport(user, propertyId, date);
        return buildPdfResponse(pdf, "property_report_" + propertyId + ".pdf");
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Authenticated user not found in database.");
        }
        return user;
    }

    private ResponseEntity<byte[]> buildPdfResponse(byte[] pdfContent, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(filename).build());
        headers.setContentLength(pdfContent.length);
        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }
}
