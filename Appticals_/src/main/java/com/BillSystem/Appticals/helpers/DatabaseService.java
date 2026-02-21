package com.BillSystem.Appticals.helpers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseService {
	 @Autowired
	    private DaoCommon daoCommon;
	    
	    @Autowired
	    private CaseConverterHelper caseConverter;
	    
	    // Execute procedure and return camelCase result
	    public List<Map<String, Object>> executeProcedureWithCamelCase(String procedureName, Object... params) {
	        List<Map<String, Object>> result = daoCommon.executeStoredProcedure(procedureName, params);
	        return caseConverter.toCamelCase(result);
	    }
	    
	    // Execute procedure with snake_case parameters
	    public List<Map<String, Object>> executeProcedureWithSnakeParams(String procedureName, Object... params) {
	        Object[] snakeCaseParams = caseConverter.toSnakeCase(params);
	        return daoCommon.executeStoredProcedure(procedureName, snakeCaseParams);
	    }

}
