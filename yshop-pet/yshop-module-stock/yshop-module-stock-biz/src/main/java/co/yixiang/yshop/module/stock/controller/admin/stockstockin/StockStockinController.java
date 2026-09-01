package co.yixiang.yshop.module.stock.controller.admin.stockstockin;

import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.module.stock.controller.admin.stockstockin.vo.StockStockinPageReqVO;
import co.yixiang.yshop.module.stock.controller.admin.stockstockin.vo.StockStockinRespVO;
import co.yixiang.yshop.module.stock.controller.admin.stockstockin.vo.StockStockinSaveReqVO;
import co.yixiang.yshop.module.stock.dal.dataobject.stockstockin.StockStockinDO;
import co.yixiang.yshop.module.stock.service.stockstockin.StockStockinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 商品入库")
@RestController
@RequestMapping("/stock/stockin")
@Validated
public class StockStockinController {

    @Resource
    private StockStockinService stockinService;

    @PostMapping("/create")
    @Operation(summary = "创建商品入库")
    @PreAuthorize("@ss.hasPermission('stock:stockin:create')")
    public CommonResult<Long> createStockin(@Valid @RequestBody StockStockinSaveReqVO createReqVO) {
        return success(stockinService.createStockin(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新商品入库")
    @PreAuthorize("@ss.hasPermission('stock:stockin:update')")
    public CommonResult<Boolean> updateStockin(@Valid @RequestBody StockStockinSaveReqVO updateReqVO) {
        stockinService.updateStockin(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除商品入库")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('stock:stockin:delete')")
    public CommonResult<Boolean> deleteStockin(@RequestParam("id") Long id) {
        stockinService.deleteStockin(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得商品入库")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('stock:stockin:query')")
    public CommonResult<StockStockinRespVO> getStockin(@RequestParam("id") Long id) {
        return success(stockinService.getStockin(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获得商品入库分页")
    @PreAuthorize("@ss.hasPermission('stock:stockin:query')")
    public CommonResult<PageResult<StockStockinRespVO>> getStockinPage(@Valid StockStockinPageReqVO pageReqVO) {
        return success(stockinService.getStockinPage(pageReqVO));
    }

    @GetMapping("/audit")
    @Operation(summary = "获得商品入库审核")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Boolean> auditStockin(@RequestParam("id") Long id, @RequestParam("type") String type) {
        stockinService.auditStockin(id, type);
        return success(true);
    }


}