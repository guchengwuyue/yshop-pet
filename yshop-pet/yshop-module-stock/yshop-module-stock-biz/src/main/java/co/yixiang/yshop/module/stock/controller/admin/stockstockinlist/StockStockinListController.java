package co.yixiang.yshop.module.stock.controller.admin.stockstockinlist;

import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.stock.controller.admin.stockstockinlist.vo.StockStockinListPageReqVO;
import co.yixiang.yshop.module.stock.controller.admin.stockstockinlist.vo.StockStockinListRespVO;
import co.yixiang.yshop.module.stock.service.stockstockinlist.StockStockinListService;
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

@Tag(name = "管理后台 - 商品入库详情")
@RestController
@RequestMapping("/stock/stockin-list")
@Validated
public class StockStockinListController {

    @Resource
    private StockStockinListService stockinListService;




    @GetMapping("/page")
    @Operation(summary = "获得商品入库详情分页")
    @PreAuthorize("@ss.hasPermission('stock:stockin-list:query')")
    public CommonResult<PageResult<StockStockinListRespVO>> getStockinListPage(@Valid StockStockinListPageReqVO pageReqVO) {
        return success(stockinListService.getStockinListPage(pageReqVO));
    }


}