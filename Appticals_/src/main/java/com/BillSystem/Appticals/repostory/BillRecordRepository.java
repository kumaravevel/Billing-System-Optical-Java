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
public class BillRecordRepository {
	
	@Autowired
    private DaoCommon daocommon;
    
    @Autowired
    private CaseConverterHelper helper;
    
    @Autowired
    private CustomLogger logger;
    
    public Map<String, Object> createBill(Map<String, Object> billData) {
        logger.log("BillRecordRepository: Creating new bill for " + billData.get("customerName"));
        try {
            List<Map<String, Object>> result = daocommon.executeStoredProcedure("create_bill",
                billData.get("customerName"),
                billData.get("customerPhone"),
                billData.get("customerAddress"),
                billData.get("age"),                   
                billData.get("frameId"),
                billData.get("frameQuantity"),
                billData.get("frameUnitPrice"),
                billData.get("lensId"),
                billData.get("lensQuantity"),
                billData.get("lensUnitPrice"),
                billData.get("fittingCharges"),
                billData.get("otherCharges"),
                billData.get("discountAmount"),
                billData.get("paymentMode"),
                billData.get("amountPaid")
            );
            
            logger.log("BillRecordRepository: Bill created successfully");
            return helper.toCamelCase(result.get(0));
            
        } catch (Exception e) {
            logger.log("BillRecordRepository: Error creating bill - " + e.getMessage());
            throw new RuntimeException("Error creating bill: " + e.getMessage());
        }
    }
    
    public List<Map<String, Object>> findAll() {
        try {
            List<Map<String, Object>> result = daocommon.executeStoredProcedure("get_all_bills");
            List<Map<String, Object>> bills = helper.toCamelCase(result);
            logger.log("BillRecordRepository: Found " + bills.size() + " bills");
            return bills;
        } catch (Exception e) {
            logger.log("BillRecordRepository: Error getting all bills - " + e.getMessage());
            throw new RuntimeException("Error getting bills: " + e.getMessage());
        }
    }

    
    public Map<String, Object> findById(Long id) {
        try {
            List<Map<String, Object>> result = daocommon.executeStoredProcedure("get_bill_by_id", id);
            if (result.isEmpty()) {
                return null;
            }
            Map<String, Object> billMap = helper.toCamelCase(result.get(0));
            logger.log("BillRecordRepository: Found bill - " + billMap.get("billNumber"));
            return billMap;
        } catch (Exception e) {
            logger.log("BillRecordRepository: Error getting bill by id - " + e.getMessage());
            throw new RuntimeException("Error getting bill: " + e.getMessage());
        }
    }
    
    private List<BillRecord> convertToBillRecordList(List<Map<String, Object>> result) {
        return helper.toCamelCase(result);
    }
    
    private BillRecord convertToBillRecord(Map<String, Object> map) {
        return helper.toCamelCase(map);
    }

}
