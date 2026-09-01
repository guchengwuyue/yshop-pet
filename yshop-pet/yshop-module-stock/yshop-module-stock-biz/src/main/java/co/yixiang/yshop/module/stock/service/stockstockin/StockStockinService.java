package co.yixiang.yshop.module.stock.service.stockstockin;

import java.util.*;
import jakarta.validation.*;
import co.yixiang.yshop.module.stock.controller.admin.stockstockin.vo.*;
import co.yixiang.yshop.module.stock.dal.dataobject.stockstockin.StockStockinDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.pojo.PageParam;

/**
 * 商品入库 Service 接口
 *
 * @author yshop
 */
public interface StockStockinService {

    /**
     * 创建商品入库
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createStockin(@Valid StockStockinSaveReqVO createReqVO);

    /**
     * 更新商品入库
     *
     * @param updateReqVO 更新信息
     */
    void updateStockin(@Valid StockStockinSaveReqVO updateReqVO);

    /**
     * 删除商品入库
     *
     * @param id 编号
     */
    void deleteStockin(Long id);

    /**
     * 获得商品入库
     *
     * @param id 编号
     * @return 商品入库
     */
    StockStockinRespVO getStockin(Long id);

    /**
     * 获得商品入库分页
     *
     * @param pageReqVO 分页查询
     * @return 商品入库分页
     */
    PageResult<StockStockinRespVO> getStockinPage(StockStockinPageReqVO pageReqVO);

    /**
     * 审核商品入库
     *
     * @param id 编号
     * @param type 类型 audit 审核 unaudit 反审核
     */
    void auditStockin(Long id, String type);

}