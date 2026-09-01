package co.yixiang.yshop.module.crm.dal.dataobject.crmform;

import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 表单DO
 *
 * @author yshop
 */
@TableName("yshop_crm_form")
@KeySequence("yshop_crm_form_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmFormDO extends BaseDO {

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
     * 模型json明细
     */
    private String formGroupJson;

}