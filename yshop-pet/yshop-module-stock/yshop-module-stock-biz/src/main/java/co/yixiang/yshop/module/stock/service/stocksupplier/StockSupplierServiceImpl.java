package co.yixiang.yshop.module.stock.service.stocksupplier;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import co.yixiang.yshop.module.stock.controller.admin.stocksupplier.vo.*;
import co.yixiang.yshop.module.stock.dal.dataobject.stocksupplier.StockSupplierDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;

import co.yixiang.yshop.module.stock.dal.mysql.stocksupplier.StockSupplierMapper;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.stock.enums.ErrorCodeConstants.*;

/**
 * 供应商 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class StockSupplierServiceImpl implements StockSupplierService {

    @Resource
    private StockSupplierMapper supplierMapper;

    @Override
    public Long createSupplier(StockSupplierSaveReqVO createReqVO) {
        // 插入
        StockSupplierDO supplier = BeanUtils.toBean(createReqVO, StockSupplierDO.class);
        supplierMapper.insert(supplier);
        // 返回
        return supplier.getId();
    }

    @Override
    public void updateSupplier(StockSupplierSaveReqVO updateReqVO) {
        // 校验存在
        validateSupplierExists(updateReqVO.getId());
        // 更新
        StockSupplierDO updateObj = BeanUtils.toBean(updateReqVO, StockSupplierDO.class);
        supplierMapper.updateById(updateObj);
    }

    @Override
    public void deleteSupplier(Long id) {
        // 校验存在
        validateSupplierExists(id);
        // 删除
        supplierMapper.deleteById(id);
    }

    private void validateSupplierExists(Long id) {
        if (supplierMapper.selectById(id) == null) {
            throw exception(SUPPLIER_NOT_EXISTS);
        }
    }

    @Override
    public StockSupplierDO getSupplier(Long id) {
        return supplierMapper.selectById(id);
    }

    @Override
    public PageResult<StockSupplierDO> getSupplierPage(StockSupplierPageReqVO pageReqVO) {
        return supplierMapper.selectPage(pageReqVO);
    }

}