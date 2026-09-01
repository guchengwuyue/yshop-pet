package co.yixiang.yshop.module.stock.service.stockstockout;

import java.util.*;
import jakarta.validation.*;
import co.yixiang.yshop.module.stock.controller.admin.stockstockout.vo.*;
import co.yixiang.yshop.framework.common.pojo.PageResult;

/**
 * 商品出库 Service 接口
 *
 * @author yshop
 */
public interface StockStockoutService {

    /**
     * 创建商品出库
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createStockout(@Valid StockStockoutSaveReqVO createReqVO);

    /**
     * 更新商品出库
     *
     * @param updateReqVO 更新信息
     */
    void updateStockout(@Valid StockStockoutSaveReqVO updateReqVO);

    /**
     * 删除商品出库
     *
     * @param id 编号
     */
    void deleteStockout(Long id);

    /**
     * 获得商品出库
     *
     * @param id 编号
     * @return 商品出库
     */
    StockStockoutRespVO getStockout(Long id);

    /**
     * 获得商品出库分页
     *
     * @param pageReqVO 分页查询
     * @return 商品出库分页
     */
    PageResult<StockStockoutRespVO> getStockoutPage(StockStockoutPageReqVO pageReqVO);

    /**
     * 审核商品出库
     *
     * @param id 编号
     * @param type 类型 audit 审核 unaudit 反审核
     */
    void auditStockout(Long id, String type);

}
