package co.yixiang.yshop.module.stock.dal.mysql.stockstockinlist;

import java.util.*;

import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.module.crm.dal.dataobject.crmcustomer.CrmCustomerDO;
import co.yixiang.yshop.module.product.dal.dataobject.storeproduct.StoreProductDO;
import co.yixiang.yshop.module.product.dal.dataobject.storeproductattrvalue.StoreProductAttrValueDO;
import co.yixiang.yshop.module.stock.controller.admin.stockstockin.vo.StockStockinRespVO;
import co.yixiang.yshop.module.stock.dal.dataobject.stockstockin.StockStockinDO;
import co.yixiang.yshop.module.stock.dal.dataobject.stockstockinlist.StockStockinListDO;
import co.yixiang.yshop.module.stock.dal.dataobject.stocksupplier.StockSupplierDO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;
import co.yixiang.yshop.module.stock.controller.admin.stockstockinlist.vo.*;

/**
 * 商品入库详情 Mapper
 *
 * @author yshop
 */
@Mapper
public interface StockStockinListMapper extends BaseMapperX<StockStockinListDO> {

    default PageResult<StockStockinListRespVO> selectPage(StockStockinListPageReqVO reqVO) {
        return selectJoinPage(reqVO, StockStockinListRespVO.class, new MPJLambdaWrapper<StockStockinListDO>()
                .selectAll(StockStockinListDO.class)
                .selectAs(StoreProductDO::getStoreName, StockStockinListRespVO::getStockGoodsName)
                .selectAs(StoreProductDO::getUnitName, StockStockinListRespVO::getUnitName)
                .selectAs(StoreProductAttrValueDO::getGoodCode, StockStockinListRespVO::getGoodsCode)
                .selectAs(StoreProductAttrValueDO::getSku, StockStockinListRespVO::getStockGoodsValueName)
                .leftJoin(StoreProductDO.class,StoreProductDO::getId, StockStockinListDO::getStockGoodsId)
                .leftJoin(StoreProductAttrValueDO.class,StoreProductAttrValueDO::getId, StockStockinListDO::getStockGoodsValueId)
                .eqIfExists(StockStockinListDO::getStockStockinId, reqVO.getStockStockinId())
                .orderByDesc(StockStockinListDO::getId)
        );

    }

    default PageResult<StockStockinListRespVO> selectPage2(StockStockinListPageReqVO reqVO) {
        return selectJoinPage(reqVO, StockStockinListRespVO.class, new MPJLambdaWrapper<StockStockinListDO>()
                .selectAll(StockStockinListDO.class)
                .selectAs(StoreProductDO::getStoreName, StockStockinListRespVO::getStockGoodsName)
                .selectAs(StoreProductDO::getUnitName, StockStockinListRespVO::getUnitName)
                .selectAs(StoreProductAttrValueDO::getGoodCode, StockStockinListRespVO::getGoodsCode)
                .selectAs(StoreProductAttrValueDO::getSku, StockStockinListRespVO::getStockGoodsValueName)
                .selectAs(StockStockinDO::getDocnum, StockStockinListRespVO::getDocNumber)
                .selectAs(StockStockinDO::getInboundTime, StockStockinListRespVO::getInboundTime)
                .selectAs(StockSupplierDO::getName, StockStockinListRespVO::getSupplierName)
                .leftJoin(StoreProductDO.class,StoreProductDO::getId, StockStockinListDO::getStockGoodsId)
                .leftJoin(StoreProductAttrValueDO.class,StoreProductAttrValueDO::getId, StockStockinListDO::getStockGoodsValueId)
                .leftJoin(StockStockinDO.class,StockStockinDO::getId, StockStockinListDO::getStockStockinId)
                .leftJoin(StockSupplierDO.class,StockSupplierDO::getId, StockStockinDO::getStockSupplierId)
                .likeIfExists(StockStockinDO::getDocnum, reqVO.getDocNumber())
                .likeIfExists(StockSupplierDO::getName, reqVO.getSupplierName())
                .between( reqVO.getInboundTime() != null,StockStockinDO::getInboundTime,
                        reqVO.getInboundTime() != null && reqVO.getInboundTime().length > 1
                                ? reqVO.getInboundTime()[0] : null,
                        reqVO.getInboundTime() != null && reqVO.getInboundTime().length > 1
                                ? reqVO.getInboundTime()[1] : null)
                .likeIfExists(StoreProductAttrValueDO::getGoodCode, reqVO.getGoodsCode())
                .likeIfExists(StoreProductDO::getStoreName, reqVO.getStockGoodsName())
                .orderByDesc(StockStockinListDO::getId)
        );

    }

    /**
     * 按商品ID查询入库明细（含入库时间、商品信息）
     */
    default List<StockStockinListRespVO> selectListByGoodsId(Long stockGoodsId) {
        return selectJoinList(StockStockinListRespVO.class, new MPJLambdaWrapper<StockStockinListDO>()
                .selectAll(StockStockinListDO.class)
                .selectAs(StoreProductDO::getStoreName, StockStockinListRespVO::getStockGoodsName)
                .selectAs(StoreProductDO::getUnitName, StockStockinListRespVO::getUnitName)
                .selectAs(StoreProductAttrValueDO::getGoodCode, StockStockinListRespVO::getGoodsCode)
                .selectAs(StoreProductAttrValueDO::getSku, StockStockinListRespVO::getStockGoodsValueName)
                .selectAs(StockStockinDO::getInboundTime, StockStockinListRespVO::getInboundTime)
                .selectAs(StockStockinDO::getDocnum, StockStockinListRespVO::getDocNumber)
                .leftJoin(StoreProductDO.class, StoreProductDO::getId, StockStockinListDO::getStockGoodsId)
                .leftJoin(StoreProductAttrValueDO.class, StoreProductAttrValueDO::getId, StockStockinListDO::getStockGoodsValueId)
                .leftJoin(StockStockinDO.class, StockStockinDO::getId, StockStockinListDO::getStockStockinId)
                .eq(StockStockinListDO::getStockGoodsId, stockGoodsId)
                .eq(StockStockinDO::getIsAudit, ShopCommonEnum.IS_STATUS_1.getValue())
                .orderByDesc(StockStockinDO::getInboundTime)
        );
    }

}