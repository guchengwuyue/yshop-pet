package co.yixiang.yshop.module.stock.dal.mysql.stocksupplier;

import java.util.*;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.module.stock.dal.dataobject.stocksupplier.StockSupplierDO;
import org.apache.ibatis.annotations.Mapper;
import co.yixiang.yshop.module.stock.controller.admin.stocksupplier.vo.*;

/**
 * 供应商 Mapper
 *
 * @author yshop
 */
@Mapper
public interface StockSupplierMapper extends BaseMapperX<StockSupplierDO> {

    default PageResult<StockSupplierDO> selectPage(StockSupplierPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<StockSupplierDO>()
                .likeIfPresent(StockSupplierDO::getName, reqVO.getName())
                .likeIfPresent(StockSupplierDO::getShortName, reqVO.getShortName())
                .eqIfPresent(StockSupplierDO::getCode, reqVO.getCode())
                .eqIfPresent(StockSupplierDO::getShipAddress, reqVO.getShipAddress())
                .eqIfPresent(StockSupplierDO::getAddress, reqVO.getAddress())
                .eqIfPresent(StockSupplierDO::getContacts, reqVO.getContacts())
                .eqIfPresent(StockSupplierDO::getTelphone, reqVO.getTelphone())
                .eqIfPresent(StockSupplierDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(StockSupplierDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(StockSupplierDO::getId));
    }

}