package com.BillSystem.Appticals.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Component;
@Component
public class DaoCommon {
	
	 @Autowired
	    private JdbcTemplate jdbcTemplate;
	 
	 @Autowired
	 private CustomLogger logger;
	    
	    // Execute Stored Procedure
	  public List<Map<String, Object>> executeStoredProcedure(String procedureName, Object... params) {
        try {
            // Exact same as Node.js
            String placeholders = String.join(",", Collections.nCopies(params.length, "?"));
            logger.log("DaoCommon params: " + Arrays.toString(params));
            
            String sql = "CALL " + procedureName + "(" + placeholders + ")";
            logger.log("DaoCommon SQL: " + sql);
            
            // Direct SQL call - bypass Spring's parameter mapping
            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, params);
            
            logger.log("DaoCommon Success: Returned " + result.size() + " records");
            return result;
            
        } catch (Exception err) {
            logger.log("DaoCommon Error: " + err.getMessage());
            throw new RuntimeException("Error executing stored procedure: " + err.getMessage());
        }
    }
	    
	    // Execute Database Action (Procedure/Query/Function)
	    public List<Map<String, Object>> executeDbAction(String actionType, String actionName, Object... params) {
	        switch (actionType.toLowerCase()) {
	            case "procedure":
	                return executeStoredProcedure(actionName, params);
	                
	            case "query":
	                return jdbcTemplate.queryForList(actionName);
	                
	            case "function":
	                String sql = "SELECT " + actionName + "(" + 
	                    String.join(",", Collections.nCopies(params.length, "?")) + ")";
	                return jdbcTemplate.queryForList(sql, params);
	                
	            default:
	                throw new IllegalArgumentException("Invalid action type: " + actionType);
	        }
	    }
	    
	    private List<Map<String, Object>> extractResultList(Map<String, Object> result) {
	        // Extract result from stored procedure output
	        for (Object value : result.values()) {
	            if (value instanceof List) {
	                return (List<Map<String, Object>>) value;
	            }
	        }
	        return new ArrayList<>();
	    }


}
