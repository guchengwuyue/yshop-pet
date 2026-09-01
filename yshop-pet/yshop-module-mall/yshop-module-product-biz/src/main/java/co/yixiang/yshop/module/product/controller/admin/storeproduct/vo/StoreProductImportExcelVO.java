package co.yixiang.yshop.module.product.controller.admin.storeproduct.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 商品 Excel 导入 VO（统一单规格 specType=0）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = false)
public class StoreProductImportExcelVO {

    @ExcelProperty("商品名称")
    private String storeName;

    @ExcelProperty("单位名")
    private String unitName;

    @ExcelProperty("商品简介")
    private String storeInfo;

    @ExcelProperty("商品图片")
    private String image;

    /** 单规格参数：售价 */
    @ExcelProperty("售价")
    private Double price;

    /** 单规格参数：成本价 */
    @ExcelProperty("成本价")
    private Double cost;

    /** 单规格参数：原价 */
    @ExcelProperty("原价")
    private Double otPrice;

    @ExcelProperty("库存警告启用")
    private Integer inventoryWarningEnable;

    @ExcelProperty("库存警告数量")
    private Integer inventoryWarningNumber;

}
