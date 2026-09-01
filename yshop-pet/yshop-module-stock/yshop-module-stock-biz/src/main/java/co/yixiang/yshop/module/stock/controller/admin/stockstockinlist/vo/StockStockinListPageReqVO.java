package co.yixiang.yshop.module.stock.controller.admin.stockstockinlist.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static co.yixiang.yshop.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 商品入库详情分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StockStockinListPageReqVO extends PageParam {

    @Schema(description = "入库主表ID", example = "18551")
    private Long stockStockinId;

    @Schema(description = "商品ID", example = "26720")
    private Long stockGoodsId;

    @Schema(description = "商品规格id", example = "1049")
    private Long stockGoodsValueId;

    @Schema(description = "入库数量")
    private Integer stockinNums;

    @Schema(description = "入库价格", example = "20071")
    private BigDecimal inboundPrice;

    @Schema(description = "入库金额")
    private BigDecimal amount;

    @Schema(description = "入库备注", example = "随便")
    private String stockinGoodsRemark;


    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "单据编号", example = "15767")
    private String docNumber;

    @Schema(description = "供应商名称", example = "供应商名称")
    private String supplierName;


    @Schema(description = "入库时间")
    private LocalDateTime[] inboundTime;

    @Schema(description = "商品名称", example = "商品名称")
    private String stockGoodsName;

    @Schema(description = "商品编码", example = "商品编码")
    private String goodsCode;

}