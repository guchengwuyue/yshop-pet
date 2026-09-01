package co.yixiang.yshop.module.crm.controller.admin.crmcustomertaggroup.vo;

import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

/**
 * 客户标签分组明细 DO
 *
 * @author yshop
 */

@Data
@Builder
public class CrmCustomerTagGroupDetailVO {

    /**
     * 用户ID
     */
    @TableId
    private Long id;
    /**
     * 名称
     */
    private String name;

    /**
     * 标签ID
     */
    private String tagId;
    /**
     * 排序
     */
    private Integer sort;

    private Long groupId;

}