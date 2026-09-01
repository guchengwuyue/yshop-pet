package co.yixiang.yshop.module.stock.dal.dataobject.stocksupplier;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * 供应商 DO
 *
 * @author yshop
 */
@TableName("yshop_stock_supplier")
@KeySequence("yshop_stock_supplier_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockSupplierDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 短名
     */
    private String shortName;
    /**
     * 代码
     */
    private String code;
    /**
     * 发货地址
     */
    private String shipAddress;
    /**
     * 联系地址
     */
    private String address;
    /**
     * 联系人
     */
    private String contacts;
    /**
     * 联系电话
     */
    private String telphone;
    /**
     * 备注
     */
    private String remark;

}