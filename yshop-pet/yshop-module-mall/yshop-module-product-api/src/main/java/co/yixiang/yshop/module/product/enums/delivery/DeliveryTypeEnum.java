package co.yixiang.yshop.module.product.enums.delivery;

import co.yixiang.yshop.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 配送方式枚举
 *
 * @author yshop
 */
@Getter
@AllArgsConstructor
public enum DeliveryTypeEnum implements ArrayValuable<Integer> {

    // TODO yshop：英文单词，需要再想下；
    EXPRESS(1, "快递发货"),
    USER(2, "用户自提"),;

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(DeliveryTypeEnum::getMode).toArray(Integer[]::new);

    /**
     * 配送方式
     */
    private final Integer mode;
    /**
     * 状态名
     */
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
