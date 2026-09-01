package co.yixiang.yshop.module.stock.service.stockstockoutlist;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.stock.controller.admin.stockstockoutlist.vo.StockStockoutListPageReqVO;
import co.yixiang.yshop.module.stock.controller.admin.stockstockoutlist.vo.StockStockoutListRespVO;
import co.yixiang.yshop.module.stock.dal.mysql.stockstockoutlist.StockStockoutListMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 商品出库详情 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class StockStockoutListServiceImpl implements StockStockoutListService {

    @Resource
    private StockStockoutListMapper stockoutListMapper;


    @Override
    public PageResult<StockStockoutListRespVO> getStockoutListPage(StockStockoutListPageReqVO pageReqVO) {
        return stockoutListMapper.selectPage2(pageReqVO);
    }

}
