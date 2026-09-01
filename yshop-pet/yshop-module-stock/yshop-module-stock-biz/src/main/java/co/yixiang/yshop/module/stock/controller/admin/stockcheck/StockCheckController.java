package co.yixiang.yshop.module.stock.controller.admin.stockcheck;

import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.stock.controller.admin.stockcheck.vo.StockCheckPageReqVO;
import co.yixiang.yshop.module.stock.controller.admin.stockcheck.vo.StockCheckRespVO;
import co.yixiang.yshop.module.stock.controller.admin.stockcheck.vo.StockCheckSaveReqVO;
import co.yixiang.yshop.module.stock.service.stockcheck.StockCheckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 商品盘库")
@RestController
@RequestMapping("/stock/stockcheck")
@Validated
public class StockCheckController {

    @Resource
    private StockCheckService stockCheckService;

    @PostMapping("/create")
    @Operation(summary = "创建商品盘库")
    @PreAuthorize("@ss.hasPermission('stock:stockcheck:create')")
    public CommonResult<Long> createStockCheck(@Valid @RequestBody StockCheckSaveReqVO createReqVO) {
        return success(stockCheckService.createStockCheck(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新商品盘库")
    @PreAuthorize("@ss.hasPermission('stock:stockcheck:update')")
    public CommonResult<Boolean> updateStockCheck(@Valid @RequestBody StockCheckSaveReqVO updateReqVO) {
        stockCheckService.updateStockCheck(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除商品盘库")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('stock:stockcheck:delete')")
    public CommonResult<Boolean> deleteStockCheck(@RequestParam("id") Long id) {
        stockCheckService.deleteStockCheck(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得商品盘库")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('stock:stockcheck:query')")
    public CommonResult<StockCheckRespVO> getStockCheck(@RequestParam("id") Long id) {
        return success(stockCheckService.getStockCheck(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获得商品盘库分页")
    @PreAuthorize("@ss.hasPermission('stock:stockcheck:query')")
    public CommonResult<PageResult<StockCheckRespVO>> getStockCheckPage(@Valid StockCheckPageReqVO pageReqVO) {
        return success(stockCheckService.getStockCheckPage(pageReqVO));
    }

    @GetMapping("/audit")
    @Operation(summary = "商品盘库审核/反审核")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Boolean> auditStockCheck(@RequestParam("id") Long id, @RequestParam("type") String type) {
        stockCheckService.auditStockCheck(id, type);
        return success(true);
    }

}
