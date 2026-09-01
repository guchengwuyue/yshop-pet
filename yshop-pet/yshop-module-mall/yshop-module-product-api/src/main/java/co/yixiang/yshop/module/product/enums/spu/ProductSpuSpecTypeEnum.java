package co.yixiang.yshop.module.product.enums.spu;

import co.yixiang.yshop.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 商品 SPU 规格类型
 *
 * @author yshop
 */
@Getter
@AllArgsConstructor
public enum ProductSpuSpecTypeEnum implements ArrayValuable<Integer> {

    RECYCLE(1, "统一规格"),
    DISABLE(2, "多规格");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(ProductSpuSpecTypeEnum::getType).toArray(Integer[]::new);

    /**
     * 规格类型
     */
    private final Integer type;
    /**
     * 规格名称
     */
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
