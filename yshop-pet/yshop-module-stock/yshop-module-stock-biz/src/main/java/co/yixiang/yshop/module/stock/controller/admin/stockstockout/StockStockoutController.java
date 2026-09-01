package co.yixiang.yshop.module.stock.controller.admin.stockstockout;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.*;
import jakarta.servlet.http.*;
import java.util.*;
import java.io.IOException;

import co.yixiang.yshop.framework.common.pojo.PageParam;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.pojo.CommonResult;
import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;

import co.yixiang.yshop.framework.excel.core.util.ExcelUtils;

import co.yixiang.yshop.framework.apilog.core.annotation.ApiAccessLog;
import static co.yixiang.yshop.framework.apilog.core.enums.OperateTypeEnum.*;

import co.yixiang.yshop.module.stock.controller.admin.stockstockout.vo.*;
import co.yixiang.yshop.module.stock.service.stockstockout.StockStockoutService;

@Tag(name = "管理后台 - 商品出库")
@RestController
@RequestMapping("/stock/stockout")
@Validated
public class StockStockoutController {

    @Resource
    private StockStockoutService stockoutService;

    @PostMapping("/create")
    @Operation(summary = "创建商品出库")
    @PreAuthorize("@ss.hasPermission('stock:stockout:create')")
    public CommonResult<Long> createStockout(@Valid @RequestBody StockStockoutSaveReqVO createReqVO) {
        return success(stockoutService.createStockout(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新商品出库")
    @PreAuthorize("@ss.hasPermission('stock:stockout:update')")
    public CommonResult<Boolean> updateStockout(@Valid @RequestBody StockStockoutSaveReqVO updateReqVO) {
        stockoutService.updateStockout(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除商品出库")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('stock:stockout:delete')")
    public CommonResult<Boolean> deleteStockout(@RequestParam("id") Long id) {
        stockoutService.deleteStockout(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得商品出库")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('stock:stockout:query')")
    public CommonResult<StockStockoutRespVO> getStockout(@RequestParam("id") Long id) {
        return success(stockoutService.getStockout(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获得商品出库分页")
    @PreAuthorize("@ss.hasPermission('stock:stockout:query')")
    public CommonResult<PageResult<StockStockoutRespVO>> getStockoutPage(@Valid StockStockoutPageReqVO pageReqVO) {
        return success(stockoutService.getStockoutPage(pageReqVO));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出商品出库 Excel")
    @PreAuthorize("@ss.hasPermission('stock:stockout:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportStockoutExcel(@Valid StockStockoutPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<StockStockoutRespVO> list = stockoutService.getStockoutPage(pageReqVO).getList();
        ExcelUtils.write(response, "商品出库.xls", "数据", StockStockoutRespVO.class, list);
    }

    @GetMapping("/audit")
    @Operation(summary = "商品出库审核/反审核")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Boolean> auditStockout(@RequestParam("id") Long id, @RequestParam("type") String type) {
        stockoutService.auditStockout(id, type);
        return success(true);
    }

}
