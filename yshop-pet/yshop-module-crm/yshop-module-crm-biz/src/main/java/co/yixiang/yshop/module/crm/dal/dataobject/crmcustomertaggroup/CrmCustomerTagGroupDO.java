package co.yixiang.yshop.module.crm.dal.dataobject.crmcustomertaggroup;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * 客户标签分组 DO
 *
 * @author yshop
 */
@TableName("yshop_crm_customer_tag_group")
@KeySequence("yshop_crm_customer_tag_group_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmCustomerTagGroupDO extends BaseDO {

    /**
     * 用户ID
     */
    @TableId
    private Long id;
    /**
     * 名称
     */
    private String groupName;
    /**
     * 排序
     */
    private Integer sort;

}