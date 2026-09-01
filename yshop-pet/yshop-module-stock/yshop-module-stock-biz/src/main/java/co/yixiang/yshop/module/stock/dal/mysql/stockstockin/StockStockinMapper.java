package co.yixiang.yshop.module.stock.dal.mysql.stockstockin;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.module.crm.dal.dataobject.crmcustomer.CrmCustomerDO;
import co.yixiang.yshop.module.stock.controller.admin.stockstockin.vo.StockStockinPageReqVO;
import co.yixiang.yshop.module.stock.controller.admin.stockstockin.vo.StockStockinRespVO;
import co.yixiang.yshop.module.stock.dal.dataobject.stockstockin.StockStockinDO;
import co.yixiang.yshop.module.stock.dal.dataobject.stocksupplier.StockSupplierDO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * 商品入库 Mapper
 *
 * @author yshop
 */
@Mapper
public interface StockStockinMapper extends BaseMapperX<StockStockinDO> {

    default PageResult<StockStockinRespVO> selectPage(StockStockinPageReqVO reqVO) {
        return selectJoinPage(reqVO, StockStockinRespVO.class, new MPJLambdaWrapper<StockStockinDO>()
                        .selectAll(StockStockinDO.class)
                        .selectAs(StockSupplierDO::getName, StockStockinRespVO::getStockSupplierName)
                        .leftJoin(StockSupplierDO.class,StockSupplierDO::getId, StockStockinDO::getStockSupplierId)
                        .likeIfExists(StockStockinDO::getDocnum, reqVO.getDocnum())
                        .likeIfExists(StockSupplierDO::getName, reqVO.getStockSupplierName())
                        .eqIfExists(StockStockinDO::getIsAudit, reqVO.getIsAudit())
                        .between( reqVO.getInboundTime() != null,StockStockinDO::getInboundTime,
                                reqVO.getInboundTime() != null && reqVO.getInboundTime().length > 1
                                        ? reqVO.getInboundTime()[0] : null,
                                reqVO.getInboundTime() != null && reqVO.getInboundTime().length > 1
                                        ? reqVO.getInboundTime()[1] : null)
                        .orderByDesc(StockStockinDO::getId)
        );

    }

    @Select("select IFNULL(sum(stockin_total_nums),0) from yshop_stock_stockin ${ew.customSqlSegment}")
    Integer selectSum(@Param(Constants.WRAPPER) Wrapper<StockStockinDO> wrapper);

}