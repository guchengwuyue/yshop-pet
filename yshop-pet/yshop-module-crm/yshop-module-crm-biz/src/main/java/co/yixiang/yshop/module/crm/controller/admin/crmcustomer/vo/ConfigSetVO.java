package co.yixiang.yshop.module.crm.controller.admin.crmcustomer.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "管理后台 - 客户规则配置 ConfigSetVO")
@Data
@Builder
public class ConfigSetVO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 未成交多少天
     */
    private Integer noDealDay;

    /**
     * 已经成交多少天
     */
    private Integer dealDay;

    /**
     * 未跟进多少天
     */
    private Integer noFollowDay;
    /**
     * 提前多少天
     */
    private Integer frontDay;
    /**
     * 拥有客户数量
     */
    private Integer haveNum;

}
