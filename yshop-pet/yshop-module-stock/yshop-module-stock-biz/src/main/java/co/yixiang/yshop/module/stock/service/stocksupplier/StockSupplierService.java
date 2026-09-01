package co.yixiang.yshop.module.stock.service.stocksupplier;

import java.util.*;
import jakarta.validation.*;
import co.yixiang.yshop.module.stock.controller.admin.stocksupplier.vo.*;
import co.yixiang.yshop.module.stock.dal.dataobject.stocksupplier.StockSupplierDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.pojo.PageParam;

/**
 * 供应商 Service 接口
 *
 * @author yshop
 */
public interface StockSupplierService {

    /**
     * 创建供应商
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSupplier(@Valid StockSupplierSaveReqVO createReqVO);

    /**
     * 更新供应商
     *
     * @param updateReqVO 更新信息
     */
    void updateSupplier(@Valid StockSupplierSaveReqVO updateReqVO);

    /**
     * 删除供应商
     *
     * @param id 编号
     */
    void deleteSupplier(Long id);

    /**
     * 获得供应商
     *
     * @param id 编号
     * @return 供应商
     */
    StockSupplierDO getSupplier(Long id);

    /**
     * 获得供应商分页
     *
     * @param pageReqVO 分页查询
     * @return 供应商分页
     */
    PageResult<StockSupplierDO> getSupplierPage(StockSupplierPageReqVO pageReqVO);

}