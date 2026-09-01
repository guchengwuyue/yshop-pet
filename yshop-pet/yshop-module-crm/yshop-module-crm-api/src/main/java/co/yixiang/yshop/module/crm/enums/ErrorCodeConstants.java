package co.yixiang.yshop.module.crm.enums;

import co.yixiang.yshop.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants {
    ErrorCode CUSTOMER_NOT_EXISTS = new ErrorCode(200000, "客户不存在");
    ErrorCode CUSTOMER_TAG_GROUP_NOT_EXISTS = new ErrorCode(200018, "客户标签分组不存在");
    ErrorCode FORM_DATA_NOT_EXISTS = new ErrorCode(200024, "crm表单自定义数据不存在");

}
