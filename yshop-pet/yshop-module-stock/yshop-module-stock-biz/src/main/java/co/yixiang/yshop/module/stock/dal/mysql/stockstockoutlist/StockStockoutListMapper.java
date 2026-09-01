package co.yixiang.yshop.module.stock.dal.mysql.stockstockoutlist;

import java.util.List;

import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.module.crm.dal.dataobject.crmcustomer.CrmCustomerDO;
import co.yixiang.yshop.module.product.dal.dataobject.storeproduct.StoreProductDO;
import co.yixiang.yshop.module.product.dal.dataobject.storeproductattrvalue.StoreProductAttrValueDO;
import co.yixiang.yshop.module.stock.controller.admin.stockstockoutlist.vo.StockStockoutListPageReqVO;
import co.yixiang.yshop.module.stock.controller.admin.stockstockoutlist.vo.StockStockoutListRespVO;
import co.yixiang.yshop.module.stock.dal.dataobject.stockstockin.StockStockinDO;
import co.yixiang.yshop.module.stock.dal.dataobject.stockstockout.StockStockoutDO;
import co.yixiang.yshop.module.stock.dal.dataobject.stockstockoutlist.StockStockoutListDO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品出库详情 Mapper
 *
 * @author yshop
 */
@Mapper
public interface StockStockoutListMapper extends BaseMapperX<StockStockoutListDO> {

    default PageResult<StockStockoutListRespVO> selectPage(StockStockoutListPageReqVO reqVO) {
        return selectJoinPage(reqVO, StockStockoutListRespVO.class, new MPJLambdaWrapper<StockStockoutListDO>()
                .selectAll(StockStockoutListDO.class)
                .selectAs(StoreProductDO::getStoreName, StockStockoutListRespVO::getStockGoodsName)
                .selectAs(StoreProductDO::getUnitName, StockStockoutListRespVO::getUnitName)
                .selectAs(StoreProductAttrValueDO::getGoodCode, StockStockoutListRespVO::getGoodsCode)
                .selectAs(StoreProductAttrValueDO::getSku, StockStockoutListRespVO::getStockGoodsValueName)
                .leftJoin(StoreProductDO.class, StoreProductDO::getId, StockStockoutListDO::getStockGoodsId)
                .leftJoin(StoreProductAttrValueDO.class, StoreProductAttrValueDO::getId, StockStockoutListDO::getStockGoodsValueId)
                .eqIfExists(StockStockoutListDO::getStockStockoutId, reqVO.getStockStockoutId())
                .orderByDesc(StockStockoutListDO::getId)
        );
    }

    default PageResult<StockStockoutListRespVO> selectPage2(StockStockoutListPageReqVO reqVO) {
        return selectJoinPage(reqVO, StockStockoutListRespVO.class, new MPJLambdaWrapper<StockStockoutListDO>()
                .selectAll(StockStockoutListDO.class)
                .selectAs(StoreProductDO::getStoreName, StockStockoutListRespVO::getStockGoodsName)
                .selectAs(StoreProductDO::getUnitName, StockStockoutListRespVO::getUnitName)
                .selectAs(StoreProductAttrValueDO::getGoodCode, StockStockoutListRespVO::getGoodsCode)
                .selectAs(StoreProductAttrValueDO::getSku, StockStockoutListRespVO::getStockGoodsValueName)
                .selectAs(StockStockoutDO::getDocnum, StockStockoutListRespVO::getDocNumber)
                .selectAs(StockStockoutDO::getOutboundTime, StockStockoutListRespVO::getOutboundTime)
                .selectAs(CrmCustomerDO::getName, StockStockoutListRespVO::getCustomerName)
                .leftJoin(StoreProductDO.class, StoreProductDO::getId, StockStockoutListDO::getStockGoodsId)
                .leftJoin(StoreProductAttrValueDO.class, StoreProductAttrValueDO::getId, StockStockoutListDO::getStockGoodsValueId)
                .leftJoin(StockStockoutDO.class, StockStockoutDO::getId, StockStockoutListDO::getStockStockoutId)
                .leftJoin(CrmCustomerDO.class, CrmCustomerDO::getId, StockStockoutDO::getStockCustomerId)
                .likeIfExists(StockStockoutDO::getDocnum, reqVO.getDocNumber())
                .likeIfExists(CrmCustomerDO::getName, reqVO.getCustomerName())
                .between(reqVO.getOutboundTime() != null, StockStockoutDO::getOutboundTime,
                        reqVO.getOutboundTime() != null && reqVO.getOutboundTime().length > 1
                                ? reqVO.getOutboundTime()[0] : null,
                        reqVO.getOutboundTime() != null && reqVO.getOutboundTime().length > 1
                                ? reqVO.getOutboundTime()[1] : null)
                .likeIfExists(StoreProductAttrValueDO::getGoodCode, reqVO.getGoodsCode())
                .likeIfExists(StoreProductDO::getStoreName, reqVO.getStockGoodsName())
                .orderByDesc(StockStockoutListDO::getId)
        );
    }

    /**
     * 按商品ID查询出库明细（含出库时间、商品信息）
     */
    default List<StockStockoutListRespVO> selectListByGoodsId(Long stockGoodsId) {
        return selectJoinList(StockStockoutListRespVO.class, new MPJLambdaWrapper<StockStockoutListDO>()
                .selectAll(StockStockoutListDO.class)
                .selectAs(StoreProductDO::getStoreName, StockStockoutListRespVO::getStockGoodsName)
                .selectAs(StoreProductDO::getUnitName, StockStockoutListRespVO::getUnitName)
                .selectAs(StoreProductAttrValueDO::getGoodCode, StockStockoutListRespVO::getGoodsCode)
                .selectAs(StoreProductAttrValueDO::getSku, StockStockoutListRespVO::getStockGoodsValueName)
                .selectAs(StockStockoutDO::getOutboundTime, StockStockoutListRespVO::getOutboundTime)
                .selectAs(StockStockoutDO::getDocnum, StockStockoutListRespVO::getDocNumber)
                .leftJoin(StoreProductDO.class, StoreProductDO::getId, StockStockoutListDO::getStockGoodsId)
                .leftJoin(StoreProductAttrValueDO.class, StoreProductAttrValueDO::getId, StockStockoutListDO::getStockGoodsValueId)
                .leftJoin(StockStockoutDO.class, StockStockoutDO::getId, StockStockoutListDO::getStockStockoutId)
                .eq(StockStockoutListDO::getStockGoodsId, stockGoodsId)
                .eq(StockStockoutDO::getIsAudit, ShopCommonEnum.IS_STATUS_1.getValue())
                .orderByDesc(StockStockoutDO::getOutboundTime)
        );
    }

}
