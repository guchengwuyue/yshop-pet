package co.yixiang.yshop.module.crm.log;

import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.stereotype.Service;

/**
 * 日志记录
 */
@Service
public class LogRecordService {

    @LogRecord(type = "CRM_CLUE", subType = "跟进线索", bizNo = "{{#typesId}}",
            success = "跟进了线索")
    public void recordClueLog(Long typesId) {}

    @LogRecord(type = "CRM_CUSTOMER", subType = "跟进客户", bizNo = "{{#typesId}}",
            success = "跟进了客户")
    public void recordCustomerLog(Long typesId) {}

    @LogRecord(type = "CRM_BUSINESS", subType = "跟进商机", bizNo = "{{#typesId}}",
            success = "跟进了商机")
    public void recordBusinessLog(Long typesId) {}

    /**
     * 导入线索日志
     * @param id
     * @param cluesName
     */
    @LogRecord(type = "CRM_CLUE", subType = "导入线索", bizNo = "{{#id}}",
            success = "导入了线索【{{#cluesName}}】")
    public void importCluesLog(Long id,String cluesName){}


    /**
     * 此方法但存未了记录日志
     * @param cluesName
     * @param oldOwnerUserId
     * @param newOwnerUserId
     */
    @LogRecord(type = "CRM_CLUE", subType = "转移线索", bizNo = "{{#id}}",
            success = "将线索【{{#cluesName}}】的负责人从【{getAdminUserName{#oldOwnerUserId}}】转移到【{getAdminUserName{#newOwnerUserId}}】")
    public void transferCluesLog(Long id,String cluesName,Long oldOwnerUserId,Long newOwnerUserId) {}


    @LogRecord(type = "CRM_CUSTOMER", subType = "导入客户", bizNo = "{{#id}}",
            success = "导入了客户【{{#customerName}}】")
    public void importCustomerLog(Long id,String customerName){}

    @LogRecord(type = "CRM_CUSTOMER", subType = "回收客户", bizNo = "{{#id}}",
            success = "将客户【{{#customerName}}】的负责人从【{getAdminUserName{#oldOwnerUserId}}】回收到【{getAdminUserName{#newOwnerUserId}}】")
    public void recoverCustomerLog(Long id,String customerName,Long oldOwnerUserId,Long newOwnerUserId) {}

    @LogRecord(type = "CRM_CUSTOMER", subType = "转移客户", bizNo = "{{#id}}",
            success = "将客户【{{#customerName}}】的负责人从【{getAdminUserName{#oldOwnerUserId}}】转移到【{getAdminUserName{#newOwnerUserId}}】")
    public void transferCustomerLog(Long id,String customerName,Long oldOwnerUserId,Long newOwnerUserId) {}

}
