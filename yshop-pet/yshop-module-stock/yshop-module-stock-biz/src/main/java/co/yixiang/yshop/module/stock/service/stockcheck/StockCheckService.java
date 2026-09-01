package co.yixiang.yshop.module.stock.service.stockcheck;

import jakarta.validation.Valid;
import co.yixiang.yshop.module.stock.controller.admin.stockcheck.vo.*;
import co.yixiang.yshop.framework.common.pojo.PageResult;

/**
 * 商品盘库 Service 接口
 *
 * @author yshop
 */
public interface StockCheckService {

    /**
     * 创建商品盘库
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createStockCheck(@Valid StockCheckSaveReqVO createReqVO);

    /**
     * 更新商品盘库
     *
     * @param updateReqVO 更新信息
     */
    void updateStockCheck(@Valid StockCheckSaveReqVO updateReqVO);

    /**
     * 删除商品盘库
     *
     * @param id 编号
     */
    void deleteStockCheck(Long id);

    /**
     * 获得商品盘库
     *
     * @param id 编号
     * @return 商品盘库
     */
    StockCheckRespVO getStockCheck(Long id);

    /**
     * 获得商品盘库分页
     *
     * @param pageReqVO 分页查询
     * @return 商品盘库分页
     */
    PageResult<StockCheckRespVO> getStockCheckPage(StockCheckPageReqVO pageReqVO);

    /**
     * 审核/反审核商品盘库
     *
     * @param id   编号
     * @param type audit 审核 / unaudit 反审核
     */
    void auditStockCheck(Long id, String type);

}
