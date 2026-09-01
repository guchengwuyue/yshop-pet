package co.yixiang.yshop.module.stock.controller.admin.stockindex.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BrieCountVO {

    @Schema(description = "商品数量")
    private Long count01;


    @Schema(description = "分类数量")
    private Long count02;

    @Schema(description = "昨日新增线索")
    private Long count002;


    @Schema(description = "新增入库")
    private Integer count03;

    @Schema(description = "昨日新增客户")
    private Long count003;

    @Schema(description = "较昨日比率")
    private String per003;

    @Schema(description = "规格数量")
    private Long count04;


    @Schema(description = "客户数量")
    private Long count05;

    @Schema(description = "昨日新增合同")
    private Long count005;

    @Schema(description = "较昨日比率")
    private String per005;

    @Schema(description = "供应商数量")
    private Long count06;


    @Schema(description = "用户数量")
    private Long count07;


    @Schema(description = "出库数量")
    private Integer count08;

    @Schema(description = "昨日新增联系人")
    private Long count008;


    @Schema(description = "客户成交量")
    private Long count09;


    @Schema(description = "通知数")
    private Long count13;



}