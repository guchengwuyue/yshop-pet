package co.yixiang.yshop.module.stock.controller.admin.stockiodetail;

import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.stock.controller.admin.stockiodetail.vo.StockIoDetailPageReqVO;
import co.yixiang.yshop.module.stock.controller.admin.stockiodetail.vo.StockIoDetailRespVO;
import co.yixiang.yshop.module.stock.service.stockiodetail.StockIoDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 商品出入盘库明细")
@RestController
@RequestMapping("/stock/stock-io-detail")
@Validated
public class StockIoDetailController {

    @Resource
    private StockIoDetailService stockIoDetailService;

    @GetMapping("/page")
    @Operation(summary = "获得商品出入盘库明细分页（入库+出库+盘库合并，按出入盘时间倒序）")
    public CommonResult<PageResult<StockIoDetailRespVO>> getStockIoDetailPage(@Valid StockIoDetailPageReqVO pageReqVO) {
        return success(stockIoDetailService.getStockIoDetailPage(pageReqVO));
    }

}
