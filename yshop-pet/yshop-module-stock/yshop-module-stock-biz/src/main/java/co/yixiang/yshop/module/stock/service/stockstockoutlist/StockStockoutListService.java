package co.yixiang.yshop.module.stock.service.stockstockoutlist;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.stock.controller.admin.stockstockoutlist.vo.StockStockoutListPageReqVO;
import co.yixiang.yshop.module.stock.controller.admin.stockstockoutlist.vo.StockStockoutListRespVO;

/**
 * 商品出库详情 Service 接口
 *
 * @author yshop
 */
public interface StockStockoutListService {


    /**
     * 获得商品出库详情分页
     *
     * @param pageReqVO 分页查询
     * @return 商品出库详情分页
     */
    PageResult<StockStockoutListRespVO> getStockoutListPage(StockStockoutListPageReqVO pageReqVO);

}
