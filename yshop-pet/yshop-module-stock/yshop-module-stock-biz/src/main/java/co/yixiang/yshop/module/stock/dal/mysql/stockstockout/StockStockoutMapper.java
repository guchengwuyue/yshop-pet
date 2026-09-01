package co.yixiang.yshop.module.stock.dal.mysql.stockstockout;

import java.util.*;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.module.crm.dal.dataobject.crmcustomer.CrmCustomerDO;
import co.yixiang.yshop.module.stock.controller.admin.stockstockin.vo.StockStockinRespVO;
import co.yixiang.yshop.module.stock.dal.dataobject.stockstockin.StockStockinDO;
import co.yixiang.yshop.module.stock.dal.dataobject.stockstockout.StockStockoutDO;
import co.yixiang.yshop.module.stock.dal.dataobject.stocksupplier.StockSupplierDO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;
import co.yixiang.yshop.module.stock.controller.admin.stockstockout.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 商品出库 Mapper
 *
 * @author yshop
 */
@Mapper
public interface StockStockoutMapper extends BaseMapperX<StockStockoutDO> {

    default PageResult<StockStockoutRespVO> selectPage(StockStockoutPageReqVO reqVO) {

        return selectJoinPage(reqVO, StockStockoutRespVO.class, new MPJLambdaWrapper<StockStockoutDO>()
                .selectAll(StockStockoutDO.class)
                .selectAs(CrmCustomerDO::getName, StockStockoutRespVO::getStockCustomerName)
                .leftJoin(CrmCustomerDO.class,CrmCustomerDO::getId, StockStockoutDO::getStockCustomerId)
                .likeIfExists(StockStockoutDO::getDocnum, reqVO.getDocnum())
                .likeIfExists(CrmCustomerDO::getName, reqVO.getStockCustomerName())
                .eqIfExists(StockStockoutDO::getIsAudit, reqVO.getIsAudit())
                .between( reqVO.getOutboundTime() != null,StockStockoutDO::getOutboundTime,
                        reqVO.getOutboundTime() != null && reqVO.getOutboundTime().length > 1
                                ? reqVO.getOutboundTime()[0] : null,
                        reqVO.getOutboundTime() != null && reqVO.getOutboundTime().length > 1
                                ? reqVO.getOutboundTime()[1] : null)
                .orderByDesc(StockStockoutDO::getId)
        );

    }

    @Select("select IFNULL(sum(stock_out_total_nums),0) from yshop_stock_stockout ${ew.customSqlSegment}")
    Integer selectSum(@Param(Constants.WRAPPER) Wrapper<StockStockoutDO> wrapper);

}