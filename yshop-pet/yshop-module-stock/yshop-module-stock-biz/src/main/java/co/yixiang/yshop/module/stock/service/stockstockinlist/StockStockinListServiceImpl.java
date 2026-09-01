package co.yixiang.yshop.module.stock.service.stockstockinlist;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.stock.controller.admin.stockstockinlist.vo.StockStockinListPageReqVO;
import co.yixiang.yshop.module.stock.controller.admin.stockstockinlist.vo.StockStockinListRespVO;
import co.yixiang.yshop.module.stock.dal.mysql.stockstockinlist.StockStockinListMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 商品入库详情 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class StockStockinListServiceImpl implements StockStockinListService {

    @Resource
    private StockStockinListMapper stockinListMapper;

    @Override
    public PageResult<StockStockinListRespVO> getStockinListPage(StockStockinListPageReqVO pageReqVO) {
        return stockinListMapper.selectPage2(pageReqVO);
    }

}