package co.yixiang.yshop.module.stock.enums;

import co.yixiang.yshop.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants {
    ErrorCode SUPPLIER_NOT_EXISTS = new ErrorCode(200026, "供应商不存在");
    ErrorCode STOCKIN_NOT_EXISTS = new ErrorCode(200027, "商品入库不存在");
    ErrorCode STOCKIN_LIST_NOT_EXISTS = new ErrorCode(200028, "商品入库详情不存在");
    ErrorCode STOCKCUR_NOT_EXISTS = new ErrorCode(200029, "商品库存不存在");
    ErrorCode STOCKOUT_LIST_NOT_EXISTS = new ErrorCode(200030, "商品出库详情不存在");
    ErrorCode STOCKOUT_NOT_EXISTS = new ErrorCode(200031, "商品出库不存在");
    ErrorCode STOCKCHECK_NOT_EXISTS = new ErrorCode(200032, "商品盘库不存在");
}
