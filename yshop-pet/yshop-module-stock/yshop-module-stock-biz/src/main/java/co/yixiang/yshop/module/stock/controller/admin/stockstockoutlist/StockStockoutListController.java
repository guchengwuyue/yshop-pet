package co.yixiang.yshop.module.stock.controller.admin.stockstockoutlist;

import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.stock.controller.admin.stockstockoutlist.vo.StockStockoutListPageReqVO;
import co.yixiang.yshop.module.stock.controller.admin.stockstockoutlist.vo.StockStockoutListRespVO;
import co.yixiang.yshop.module.stock.service.stockstockoutlist.StockStockoutListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 商品出库详情")
@RestController
@RequestMapping("/stock/stockout-list")
@Validated
public class StockStockoutListController {

    @Resource
    private StockStockoutListService stockoutListService;


    @GetMapping("/page")
    @Operation(summary = "获得商品出库详情分页")
    @PreAuthorize("@ss.hasPermission('stock:stockout-list:query')")
    public CommonResult<PageResult<StockStockoutListRespVO>> getStockoutListPage(@Valid StockStockoutListPageReqVO pageReqVO) {
        return success(stockoutListService.getStockoutListPage(pageReqVO));
    }



}