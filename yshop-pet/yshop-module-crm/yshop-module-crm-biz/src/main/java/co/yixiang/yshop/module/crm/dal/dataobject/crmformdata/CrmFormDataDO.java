package co.yixiang.yshop.module.crm.dal.dataobject.crmformdata;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;

/**
 * crm表单自定义数据 DO
 *
 * @author yshop
 */
@TableName("yshop_crm_form_data")
@KeySequence("yshop_crm_form_data_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmFormDataDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 模型
     */
    private String formModule;
    /**
     * 目标数据id
     */
    private Long formDataId;
    /**
     * 目标数据json
     */
    private String formDataJson;

}