package co.yixiang.yshop.module.stock.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hupeng
 * 审核类型枚举
 */
@Getter
@AllArgsConstructor
public enum AuditTypesEnum {
    AUDIT("audit","审核"),
    UNAUDIT("unaudit","反审核");

    private String value;
    private String desc;
}
