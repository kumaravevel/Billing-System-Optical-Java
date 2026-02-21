package com.BillSystem.Appticals.repostory;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.BillSystem.Appticals.entity.BillRecord;
import com.BillSystem.Appticals.helpers.CaseConverterHelper;
import com.BillSystem.Appticals.helpers.CustomLogger;
import com.BillSystem.Appticals.helpers.DaoCommon;

@Repository
public class Framerespo {
	
	@Autowired
	private DaoCommon daocommon;
	
	@Autowired
	private CustomLogger logger;
	
	@Autowired
	private CaseConverterHelper helper;
	
	
	public List<Map<String, Object>>serachFrameByName(String serachTerm){
		logger.log("FrameRepository: Searching frames with term"+serachTerm);
		try {
			List<Map<String, Object>>result=daocommon.executeStoredProcedure("search_frames", serachTerm);
			 logger.log("FrameRepository: Search found " + result.size() + " frames");
			 return helper.toCamelCase(result);
			
		}
		catch (Exception e) {
	        logger.log("FrameRepository: Error searching frames - " + e.getMessage());
            throw new RuntimeException("Error searching frames: " + e.getMessage());
		}
	}
	
	
	
    // Search lenses by name using SP
    public List<Map<String, Object>> searchLensesByName(String searchTerm) {
        logger.log("LensRepository: Searching lenses with term - " + searchTerm);
        try {
            List<Map<String, Object>> result = daocommon.executeStoredProcedure("search_lenses",searchTerm);
            logger.log("LensRepository: Search found " + result.size() + " lenses");
            return helper.toCamelCase(result);
        } catch (Exception e) {
            logger.log("LensRepository: Search error - " + e.getMessage());
            throw new RuntimeException("Error searching lenses: " + e.getMessage());
        }
    }
	
    
     
    

}
