package com.BillSystem.Appticals.service;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BillSystem.Appticals.helpers.CustomLogger;
import com.BillSystem.Appticals.repostory.Framerespo;

@Service
public class Frameservice{
	@Autowired
	private Framerespo respo;
	
	
	@Autowired
	private CustomLogger logger;
	
	public List<Map<String, Object>>serachFrameBName(String searchTerm){
	    logger.log("FrameService: Validating search term - " + searchTerm);
	    if(searchTerm==null || searchTerm.trim().length()<2) {
            logger.log("FrameService: Search term validation failed - minimum 2 characters required");
            throw new RuntimeException("Search term must be at least 2 characters long");
	    }
	    
        logger.log("FrameService: Calling repository to search frames");
        return respo.serachFrameByName(searchTerm.trim());
	}
	
	// Search lenses by name
    public List<Map<String, Object>> searchLensesByName(String searchTerm) {
        logger.log("LensService: Validating search term - " + searchTerm);
        if (searchTerm == null || searchTerm.trim().length() < 2) {
            logger.log("LensService: Search term validation failed - minimum 2 characters required");
            throw new RuntimeException("Search term must be at least 2 characters long");
        }
        
        logger.log("LensService: Calling repository to search lenses");
        return respo.searchLensesByName(searchTerm.trim());
    }
    
   
	
	
}
