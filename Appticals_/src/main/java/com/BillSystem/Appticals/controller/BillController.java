package com.BillSystem.Appticals.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BillSystem.Appticals.entity.BillRecord;
import com.BillSystem.Appticals.helpers.CustomLogger;
import com.BillSystem.Appticals.service.BillRecordService;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin(origins = "*")
public class BillController {
	
	 @Autowired
	    private BillRecordService service;
	    
	    @Autowired
	    private CustomLogger logger;
	    
	    @PostMapping
	    public ResponseEntity<?> createBill(@RequestBody Map<String, Object> billData) {
	        logger.log("BillController: Create bill request received");
	        try {
	            Map<String, Object> result = service.createBill(billData);
	            logger.log("BillController: Bill created successfully - Number: " + result.get("billNumber"));
	            return ResponseEntity.ok(result);
	        } catch (RuntimeException e) {
	            logger.log("BillController: Create bill failed - " + e.getMessage());
	            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
	        } catch (Exception e) {
	            logger.log("BillController: Internal server error during bill creation - " + e.getMessage());
	            return ResponseEntity.internalServerError().body(Map.of("error", "Internal server error"));
	        }
	    }
	    
	    @PostMapping("/{billId}/print")
	    public ResponseEntity<?> printBill(@PathVariable Long billId) {
	        logger.log("BillController: Print request for bill - " + billId);
	        try {
	            boolean success = service.printBill(billId);
	            if (success) {
	                logger.log("BillController: Bill printed successfully - " + billId);
	                return ResponseEntity.ok(Map.of("message", "Bill printed successfully"));
	            } else {
	                logger.log("BillController: Print failed - " + billId);
	                return ResponseEntity.badRequest().body(Map.of("error", "Print failed"));
	            }
	        } catch (Exception e) {
	            logger.log("BillController: Print error - " + e.getMessage());
	            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
	        }
	    }
	    
	    @PostMapping("/print/test")
	    public ResponseEntity<?> testPrinter() {
	        logger.log("BillController: Test printer request");
	        try {
	            boolean success = service.testPrinter();
	            if (success) {
	                return ResponseEntity.ok(Map.of("message", "Test print successful"));
	            } else {
	                return ResponseEntity.badRequest().body(Map.of("error", "Test print failed"));
	            }
	        } catch (Exception e) {
	            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
	        }
	    }
	    
	    @GetMapping
	    public ResponseEntity<?> getAllBills() {
	        logger.log("BillController: Get all bills request");
	        try {
	            List<Map<String, Object>> bills = service.getAllBills();
	            logger.log("BillController: Returned " + bills.size() + " bills");
	            return ResponseEntity.ok(bills);
	        } catch (Exception e) {
	            logger.log("BillController: Get all bills failed - " + e.getMessage());
	            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
	        }
	    }
	    
	

}
