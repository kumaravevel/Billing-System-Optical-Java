package com.BillSystem.Appticals.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BillSystem.Appticals.helpers.CustomLogger;
import com.BillSystem.Appticals.service.Frameservice;
@RestController
@RequestMapping("/api/frames")
@CrossOrigin(origins = "*")
public class FrameController {
	
	@Autowired
	private Frameservice  service;
	
	@Autowired
	private CustomLogger logger;
	
	
	@PostMapping("/search")
	   public ResponseEntity<?> searchFrames(@RequestBody Map<String, Object> searchRequest) {
        logger.log("FrameController: POST Search request received - " + searchRequest);
        
        try {
            String searchTerm = (String) searchRequest.get("search");
            
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                    Map.of("error", "Search term is required")
                );
            }
            
            List<Map<String, Object>> frames = service.serachFrameBName(searchTerm.trim());
            logger.log("FrameController: POST Search successful, returning " + frames.size() + " frames");
            return ResponseEntity.ok(frames);
            
        } catch (RuntimeException e) {
            logger.log("FrameController: POST Search failed - " + e.getMessage());
            return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
            );
        } catch (Exception e) {
            logger.log("FrameController: Internal server error during POST search - " + e.getMessage());
            return ResponseEntity.internalServerError().body(
                Map.of("error", "Internal server error")
            );
        }
    }
	
	
    @PostMapping("/searchlens")
    public ResponseEntity<?> searchLenses(@RequestBody Map<String, Object> searchRequest) {
        logger.log("LensController: POST Search request received - " + searchRequest);
        
        try {
            String searchTerm = (String) searchRequest.get("search");
            
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                    Map.of("error", "Search term is required")
                );
            }
            
            List<Map<String, Object>> lenses = service.searchLensesByName(searchTerm.trim());
            logger.log("LensController: POST Search successful, returning " + lenses.size() + " lenses");
            return ResponseEntity.ok(lenses);
            
        } catch (RuntimeException e) {
            logger.log("LensController: POST Search failed - " + e.getMessage());
            return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
            );
        } catch (Exception e) {
            logger.log("LensController: Internal server error during POST search - " + e.getMessage());
            return ResponseEntity.internalServerError().body(
                Map.of("error", "Internal server error")
            );
        }
    }
    
    
  

}
