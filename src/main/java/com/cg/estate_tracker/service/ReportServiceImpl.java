package com.cg.estate_tracker.service;

import com.cg.estate_tracker.model.Expense;
import com.cg.estate_tracker.model.Property;
import com.cg.estate_tracker.model.RentLog;
import com.cg.estate_tracker.model.User;
import com.cg.estate_tracker.repository.PropertyRepository;
import com.cg.estate_tracker.repository.UserRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@Slf4j
public class ReportServiceImpl implements IReportService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public byte[] generateUserReport(User user, LocalDate date) {
        List<Property> properties = propertyRepository.findByUserId(user.getId());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Estate Tracker Report").setBold().setFontSize(18));
        document.add(new Paragraph("Generated For: " + user.getName()));
        document.add(new Paragraph("Date: " + date.toString()));
        document.add(new Paragraph("\n"));

        for (Property property : properties) {
            document.add(new Paragraph("Property: " + property.getTitle()).setBold());
            document.add(new Paragraph("Location: " + property.getLocation()));
            document.add(new Paragraph("Size: " + property.getSize() + " sq ft"));
            document.add(new Paragraph("Purchase Date: " + property.getPurchaseDate()));
            document.add(new Paragraph("Purchase Price: ₹" + property.getPurchasePrice()));

            double appreciationRate = 0.01;  // 1% per month
            long monthsBetweenPurchaseAndDate = Period.between(property.getPurchaseDate(), date).getMonths();
            double estimatedMarketValue = property.getPurchasePrice() * Math.pow(1 + appreciationRate, monthsBetweenPurchaseAndDate);

            String formattedEstimatedMarketValue = String.format("%.2f", estimatedMarketValue);
            document.add(new Paragraph("Estimated Market Value (as of " + date + "): ₹" + formattedEstimatedMarketValue));

            LocalDate firstRentLogDate = property.getRentLogs().stream()
                    .map(RentLog::getDateReceived)
                    .min(LocalDate::compareTo)
                    .orElse(property.getPurchaseDate());

            long monthsRent = Period.between(firstRentLogDate, date).getMonths();
            double totalRent = property.getRentLogs().stream()
                    .filter(r -> !r.getDateReceived().isAfter(date))
                    .mapToDouble(rentLog -> rentLog.getAmount() * monthsRent)
                    .sum();

            double monthlyRent = property.getRentLogs().stream()
                    .filter(r -> !r.getDateReceived().isAfter(date))
                    .mapToDouble(RentLog::getAmount)
                    .findFirst()
                    .orElse(0.0);

// Calculate annual rent as 12 * monthly rent
            double annualRent = monthlyRent * 12;

// Rental yield = (Annual Rent / Estimated Market Value) * 100
            double rentalYield = (annualRent / estimatedMarketValue) * 100;

            double totalExpense = property.getExpenses().stream()
                    .filter(e -> !e.getExpenseDate().isAfter(date))
                    .mapToDouble(Expense::getAmount)
                    .sum();

            double gains = (estimatedMarketValue - property.getPurchasePrice()) + totalRent;

            double roi = (gains - totalExpense) / property.getPurchasePrice() * 100;

//            double rentalYield = (totalRent / property.getCurrentMarketValue()) * 100;

            Table metrics = new Table(2);
            metrics.addCell("Total Rent Received");
            metrics.addCell("₹" + totalRent);
            metrics.addCell("Total Expenses");
            metrics.addCell("₹" + totalExpense);
            metrics.addCell("ROI (%)");
            metrics.addCell(String.format("%.2f%%", roi));
            metrics.addCell("Rental Yield (%)");
            metrics.addCell(String.format("%.2f%%", rentalYield));
            document.add(metrics);

            document.add(new Paragraph("\n"));
        }

        document.close();
        return out.toByteArray();
    }
}
