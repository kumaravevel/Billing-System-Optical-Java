package com.BillSystem.Appticals.helpers;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
@Component
public class CaseConverterHelper {
	public <T> T toCamelCase(Object obj) {
        if (obj == null) {
            return null;
        }
        
        if (obj instanceof Map) {
            Map<String, Object> result = new LinkedHashMap<>();
            ((Map<?, ?>) obj).forEach((key, value) -> {
                String camelCaseKey = toCamelCaseString(key.toString());
                result.put(camelCaseKey, toCamelCase(value));
            });
            return (T) result;
        }
        
        if (obj instanceof List) {
            List<Object> result = new ArrayList<>();
            for (Object item : (List<?>) obj) {
                result.add(toCamelCase(item));
            }
            return (T) result;
        }
        
        // Handle Date formatting
        if (obj instanceof Date) {
            return (T) formatDate((Date) obj);
        }
        
        return (T) obj;
    }
    
    private String toCamelCaseString(String snakeCase) {
        if (snakeCase == null) return null;
        
        StringBuilder result = new StringBuilder();
        boolean nextUpperCase = false;
        
        for (int i = 0; i < snakeCase.length(); i++) {
            char currentChar = snakeCase.charAt(i);
            
            if (currentChar == '_') {
                nextUpperCase = true;
            } else {
                if (nextUpperCase) {
                    result.append(Character.toUpperCase(currentChar));
                    nextUpperCase = false;
                } else {
                    result.append(currentChar);
                }
            }
        }
        
        return result.toString();
    }
    
    // Convert Camel Case to Snake Case
    public <T> T toSnakeCase(Object input) {
        if (input == null) {
            return null;
        }
        
        if (input instanceof Map) {
            Map<String, Object> result = new LinkedHashMap<>();
            ((Map<?, ?>) input).forEach((key, value) -> {
                String snakeCaseKey = toSnakeCaseString(key.toString());
                result.put(snakeCaseKey, toSnakeCase(value));
            });
            return (T) result;
        }
        
        if (input instanceof List) {
            List<Object> result = new ArrayList<>();
            for (Object item : (List<?>) input) {
                if (item instanceof String) {
                    result.add(toSnakeCaseString(item.toString()));
                } else {
                    result.add(toSnakeCase(item));
                }
            }
            return (T) result;
        }
        
        if (input instanceof String) {
            return (T) toSnakeCaseString(input.toString());
        }
        
        return (T) input;
    }
    
    private String toSnakeCaseString(String camelCase) {
        if (camelCase == null) return null;
        
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
    
    // Date formatting method
    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }


}
