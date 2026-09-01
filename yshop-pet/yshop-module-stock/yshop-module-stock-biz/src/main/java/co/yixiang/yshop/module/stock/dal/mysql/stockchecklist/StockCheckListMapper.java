package co.yixiang.yshop.module.stock.dal.mysql.stockchecklist;

import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.module.product.dal.dataobject.storeproduct.StoreProductDO;
import co.yixiang.yshop.module.product.dal.dataobject.storeproductattrvalue.StoreProductAttrValueDO;
import co.yixiang.yshop.module.stock.controller.admin.stockchecklist.vo.StockCheckListPageReqVO;
import co.yixiang.yshop.module.stock.controller.admin.stockchecklist.vo.StockCheckListRespVO;
import co.yixiang.yshop.module.stock.dal.dataobject.stockcheck.StockCheckDO;
import co.yixiang.yshop.module.stock.dal.dataobject.stockchecklist.StockCheckListDO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商品盘库详情 Mapper
 *
 * @author yshop
 */
@Mapper
public interface StockCheckListMapper extends BaseMapperX<StockCheckListDO> {

    default PageResult<StockCheckListRespVO> selectPage(StockCheckListPageReqVO reqVO) {
        return selectJoinPage(reqVO, StockCheckListRespVO.class, new MPJLambdaWrapper<StockCheckListDO>()
                .selectAll(StockCheckListDO.class)
                .selectAs(StoreProductDO::getStoreName, StockCheckListRespVO::getStockGoodsName)
                .selectAs(StoreProductDO::getUnitName, StockCheckListRespVO::getUnitName)
                .selectAs(StoreProductAttrValueDO::getGoodCode, StockCheckListRespVO::getGoodsCode)
                .selectAs(StoreProductAttrValueDO::getSku, StockCheckListRespVO::getStockGoodsValueName)
                .leftJoin(StoreProductDO.class, StoreProductDO::getId, StockCheckListDO::getStockGoodsId)
                .leftJoin(StoreProductAttrValueDO.class, StoreProductAttrValueDO::getId, StockCheckListDO::getStockGoodsValueId)
                .eqIfExists(StockCheckListDO::getStockStockcheckId, reqVO.getStockStockcheckId())
                .orderByDesc(StockCheckListDO::getId)
        );
    }

    /**
     * 按商品ID查询已审核盘库明细（含盘库时间、单据编号、商品信息）
     */
    default List<StockCheckListRespVO> selectListByGoodsId(Long stockGoodsId) {
        return selectJoinList(StockCheckListRespVO.class, new MPJLambdaWrapper<StockCheckListDO>()
                .selectAll(StockCheckListDO.class)
                .selectAs(StoreProductDO::getStoreName, StockCheckListRespVO::getStockGoodsName)
                .selectAs(StoreProductDO::getUnitName, StockCheckListRespVO::getUnitName)
                .selectAs(StoreProductAttrValueDO::getGoodCode, StockCheckListRespVO::getGoodsCode)
                .selectAs(StoreProductAttrValueDO::getSku, StockCheckListRespVO::getStockGoodsValueName)
                .selectAs(StockCheckDO::getCheckTime, StockCheckListRespVO::getCheckTime)
                .selectAs(StockCheckDO::getDocnum, StockCheckListRespVO::getDocnum)
                .leftJoin(StoreProductDO.class, StoreProductDO::getId, StockCheckListDO::getStockGoodsId)
                .leftJoin(StoreProductAttrValueDO.class, StoreProductAttrValueDO::getId, StockCheckListDO::getStockGoodsValueId)
                .leftJoin(StockCheckDO.class, StockCheckDO::getId, StockCheckListDO::getStockStockcheckId)
                .eq(StockCheckListDO::getStockGoodsId, stockGoodsId)
                .eq(StockCheckDO::getIsAudit, ShopCommonEnum.IS_STATUS_1.getValue())
                .orderByDesc(StockCheckDO::getCheckTime)
        );
    }

}
