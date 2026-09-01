package co.yixiang.yshop.module.stock.service.stockstockinlist;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.stock.controller.admin.stockstockinlist.vo.StockStockinListPageReqVO;
import co.yixiang.yshop.module.stock.controller.admin.stockstockinlist.vo.StockStockinListRespVO;

/**
 * 商品入库详情 Service 接口
 *
 * @author yshop
 */
public interface StockStockinListService {

    /**
     * 获得商品入库详情分页
     *
     * @param pageReqVO 分页查询
     * @return 商品入库详情分页
     */
    PageResult<StockStockinListRespVO> getStockinListPage(StockStockinListPageReqVO pageReqVO);

}