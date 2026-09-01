package co.yixiang.yshop.module.stock.service.stockiodetail;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.stock.controller.admin.stockiodetail.vo.StockIoDetailPageReqVO;
import co.yixiang.yshop.module.stock.controller.admin.stockiodetail.vo.StockIoDetailRespVO;

/**
 * 商品出入盘库明细 Service 接口
 */
public interface StockIoDetailService {

    /**
     * 分页查询商品出入盘库明细（入库+出库+盘库合并，按业务时间倒序）
     */
    PageResult<StockIoDetailRespVO> getStockIoDetailPage(StockIoDetailPageReqVO pageReqVO);

}
