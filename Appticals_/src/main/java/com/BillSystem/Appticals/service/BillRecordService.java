package com.BillSystem.Appticals.service;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.Sides;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BillSystem.Appticals.entity.BillRecord;
import com.BillSystem.Appticals.helpers.CustomLogger;
import com.BillSystem.Appticals.repostory.BillRecordRepository;

@Service
public class BillRecordService {
	
	@Autowired
    private BillRecordRepository respo;
    
    @Autowired
    private CustomLogger logger;
    
    public Map<String, Object> createBill(Map<String, Object> billData) {
        logger.log("BillRecordService: Creating new bill for customer - " + billData.get("customerName"));
        validateBillData(billData);
        setDefaultValues(billData);
        
        //  Ensure age is set (default to 0 if not provided)
        if (!billData.containsKey("age") || billData.get("age") == null) {
            billData.put("age", 0);
        }
        
        // Create bill first
        Map<String, Object> result = respo.createBill(billData);
        
        // AUTO PRINT AFTER CREATION
        try {
            Long billId = ((Number) result.get("id")).longValue();
            logger.log("BillRecordService: Attempting auto print for bill ID: " + billId);
            printBill(billId);
            logger.log("BillRecordService:  Bill auto-printed successfully - ID: " + billId);
        } catch (Exception e) {
            logger.log("BillRecordService: ❌ Auto-print failed but bill created - " + e.getMessage());
        }
        
        return result;
    }
    
    public List<Map<String, Object>> getAllBills() {
        logger.log("BillRecordService: Getting all bills");
        try {
            List<Map<String, Object>> bills = respo.findAll();
            logger.log("BillRecordService: Retrieved " + bills.size() + " bills");
            return bills;
        } catch (Exception e) {
            logger.log("BillRecordService: Error getting all bills - " + e.getMessage());
            throw new RuntimeException("Error getting bills: " + e.getMessage());
        }
    }
    
    public Map<String, Object> getBillById(Long billId) {
        logger.log("BillRecordService: Getting bill by ID: " + billId);
        try {
            Map<String, Object> bill = respo.findById(billId);
            if (bill == null) {
                throw new RuntimeException("Bill not found with ID: " + billId);
            }
            logger.log("BillRecordService: Bill retrieved - " + bill.get("billNumber"));
            return bill;
        } catch (Exception e) {
            logger.log("BillRecordService: Error getting bill by id - " + e.getMessage());
            throw new RuntimeException("Error getting bill: " + e.getMessage());
        }
    }
    public boolean printBill(Long billId) {
        try {
            // Get bill details from repository as Map
            Map<String, Object> bill = respo.findById(billId);
            if (bill == null) {
                logger.log("BillRecordService: Bill not found for printing - ID: " + billId);
                return false;
            }
            
            // Format bill content from Ma
            String billContent = formatBillContent(bill);
            
            // Use Java Print Service API
            return printWithJavaPrintService(billContent, billId);
            
        } catch (Exception e) {
            logger.log("BillRecordService: Print failed for bill " + billId + " - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    private boolean printWithJavaPrintService(String billContent, Long billId) {
        try {
            logger.log("BillRecordService: Starting Java Print Service for bill: " + billId);
            
            // 1. Get all available printers
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            logger.log("BillRecordService: Found " + printServices.length + " printers");
            
            // 2. Display available printers (for debugging)
            for (PrintService service : printServices) {
                logger.log("BillRecordService: Available Printer: " + service.getName());
            }
            
            // 3. Find HP printer (case-insensitive search)
            PrintService hpPrinter = findHPPrinter(printServices);
            
            if (hpPrinter == null) {
                logger.log("BillRecordService: No HP printer found, using default printer");
                hpPrinter = PrintServiceLookup.lookupDefaultPrintService();
            }
            
            if (hpPrinter == null) {
                logger.log("BillRecordService: No printers available at all!");
                return false;
            }
            
            logger.log("BillRecordService: Selected Printer: " + hpPrinter.getName());
            
            // 4. Create print job
            DocPrintJob printJob = hpPrinter.createPrintJob();
            
            // 5. Prepare print attributes
            HashPrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
            attributes.add(MediaSizeName.NA_LETTER); // Paper size
            attributes.add(new Copies(1));           // Number of copies
            attributes.add(Sides.ONE_SIDED);         // Single sided
            
            // 6. Create printable document
            byte[] contentBytes = billContent.getBytes();
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            Doc doc = new SimpleDoc(new ByteArrayInputStream(contentBytes), flavor, null);
            
            // 7. Add print job listener to track progress
            printJob.addPrintJobListener(new PrintJobAdapter() {
                @Override
                public void printJobCompleted(PrintJobEvent pje) {
                    logger.log("BillRecordService: Print job completed successfully for bill: " + billId);
                }
                
                @Override
                public void printJobFailed(PrintJobEvent pje) {
                    logger.log("BillRecordService: Print job failed for bill: " + billId);
                }
            });
            
            // 8. Send to printer
            logger.log("BillRecordService: Sending print job to printer...");
            printJob.print(doc, attributes);
            
            logger.log("BillRecordService: Print job submitted successfully for bill: " + billId);
            return true;
            
        } catch (PrintException e) {
            logger.log("BillRecordService: PrintException for bill " + billId + " - " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            logger.log("BillRecordService: General exception for bill " + billId + " - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    private PrintService findHPPrinter(PrintService[] printServices) {
        // Common HP printer name patterns
        String[] hpPatterns = {
            "hp", "HP", "deskjet", "Deskjet", "DESKJET",
            "laserjet", "Laserjet", "LASERJET", 
            "officejet", "Officejet", "OFFICEJET",
            "pavilion", "Pavilion", "PAVILION"
        };
        
        for (PrintService service : printServices) {
            String printerName = service.getName().toLowerCase();
            for (String pattern : hpPatterns) {
                if (printerName.contains(pattern.toLowerCase())) {
                    return service;
                }
            }
        }
        return null;
    }
    
    


    
    public boolean testPrinter() {  
        try {
            String testContent = 
                "\n\n=== TEST PRINT ===\n" +
                "Optical Shop Printer\n" +
                "Date: " + java.time.LocalDate.now() + "\n" +
                "Time: " + java.time.LocalTime.now() + "\n" +
                "Status: ✅ WORKING\n" +
                "=====================\n\n\n";
            
            FileOutputStream printer = new FileOutputStream("COM3");
            printer.write(testContent.getBytes());
            printer.close();
            
            logger.log("BillRecordService: Test print successful");
            return true;
        } catch (Exception e) {
            logger.log("BillRecordService: Test print failed - " + e.getMessage());
            return false;
        }
    }
    
    private void validateBillData(Map<String, Object> billData) {
        // Existing validations...
        if (billData.get("customerName") == null || ((String) billData.get("customerName")).trim().isEmpty()) {
            throw new RuntimeException("Customer name is required");
        }
        if (billData.get("customerPhone") == null || ((String) billData.get("customerPhone")).trim().isEmpty()) {
            throw new RuntimeException("Customer phone is required");
        }
        
  
        if (billData.containsKey("age") && billData.get("age") != null) {
            try {
                int age = ((Number) billData.get("age")).intValue();
                if (age < 0 || age > 150) {
                    throw new RuntimeException("Age must be between 0 and 150");
                }
            } catch (ClassCastException e) {
                throw new RuntimeException("Age must be a valid number");
            }
        }
    }
    
    private void setDefaultValues(Map<String, Object> billData) {
        // Existing defaults...
        if (!billData.containsKey("customerAddress") || billData.get("customerAddress") == null) {
            billData.put("customerAddress", "");
        }
        if (!billData.containsKey("age") || billData.get("age") == null) {
            billData.put("age", 0); // ✅ Default age
        }
        if (!billData.containsKey("frameQuantity") || billData.get("frameQuantity") == null) {
            billData.put("frameQuantity", 1);
        }
        if (!billData.containsKey("lensQuantity") || billData.get("lensQuantity") == null) {
            billData.put("lensQuantity", 1);
        }
        if (!billData.containsKey("fittingCharges") || billData.get("fittingCharges") == null) {
            billData.put("fittingCharges", 0.0);
        }
        if (!billData.containsKey("otherCharges") || billData.get("otherCharges") == null) {
            billData.put("otherCharges", 0.0);
        }
        if (!billData.containsKey("discountAmount") || billData.get("discountAmount") == null) {
            billData.put("discountAmount", 0.0);
        }
        if (!billData.containsKey("paymentMode") || billData.get("paymentMode") == null) {
            billData.put("paymentMode", "CASH");
        }
        if (!billData.containsKey("amountPaid") || billData.get("amountPaid") == null) {
            billData.put("amountPaid", 0.0);
        }
    }
    
       private String formatBillContent(Map<String, Object> bill) {
        StringBuilder content = new StringBuilder();
        
        content.append("\n\n");
        content.append("      === Vision Eye Care Hospital  ===\n");
        content.append("         BILL RECEIPT\n");
        content.append("==============================\n\n");
        content.append("Bill No: ").append(bill.get("billNumber")).append("\n");
        content.append("Date: ").append(bill.get("billDate")).append("\n");
        content.append("-----------------------------\n\n");
        content.append("Customer: ").append(bill.get("customerName")).append("\n");
        content.append("Phone: ").append(bill.get("customerPhone")).append("\n");
        
        if (bill.get("age") != null && ((Number) bill.get("age")).intValue() > 0) {
            content.append("Age: ").append(bill.get("age")).append(" years\n");
        }
        
        if (bill.get("customerAddress") != null && !bill.get("customerAddress").toString().isEmpty()) {
            content.append("Address: ").append(bill.get("customerAddress")).append("\n");
        }
        
        content.append("-----------------------------\n\n");
        content.append("Frame Qty: ").append(bill.get("frameQuantity")).append("\n");
        content.append("Frame Price: ₹").append(bill.get("frameUnitPrice")).append("\n");
        content.append("Lens Qty: ").append(bill.get("lensQuantity")).append("\n");
        content.append("Lens Price: ₹").append(bill.get("lensUnitPrice")).append("\n");
        content.append("-----------------------------\n");
        
        if (((Number) bill.get("fittingCharges")).doubleValue() > 0) {
            content.append("Fitting: ₹").append(bill.get("fittingCharges")).append("\n");
        }
        
        if (((Number) bill.get("otherCharges")).doubleValue() > 0) {
            content.append("Other: ₹").append(bill.get("otherCharges")).append("\n");
        }
        
        if (((Number) bill.get("discountAmount")).doubleValue() > 0) {
            content.append("Discount: -₹").append(bill.get("discountAmount")).append("\n");
        }
        
        content.append("-----------------------------\n");
        content.append("Sub Total: ₹").append(bill.get("subtotal")).append("\n");
        content.append("Tax: ₹").append(bill.get("taxAmount")).append("\n");
        content.append("GRAND TOTAL: ₹").append(bill.get("grandTotal")).append("\n");
        content.append("Paid: ₹").append(bill.get("amountPaid")).append("\n");
        
        double balance = ((Number) bill.get("grandTotal")).doubleValue() - 
                        ((Number) bill.get("amountPaid")).doubleValue();
        if (balance > 0) {
            content.append("Balance: ₹").append(String.format("%.2f", balance)).append("\n");
        }
        
        content.append("==============================\n");
        content.append("Payment Mode: ").append(bill.get("paymentMode")).append("\n");
        content.append("Status: ").append(bill.get("paymentStatus")).append("\n");
        content.append("==============================\n");
        content.append("     THANK YOU VISIT AGAIN!\n");
        content.append("==============================\n\n\n\n");
        
        return content.toString();
    }

}
