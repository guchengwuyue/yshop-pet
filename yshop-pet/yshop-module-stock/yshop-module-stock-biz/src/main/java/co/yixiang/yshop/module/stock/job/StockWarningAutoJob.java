package co.yixiang.yshop.module.stock.job;

import co.yixiang.yshop.framework.quartz.core.handler.JobHandler;
import co.yixiang.yshop.framework.tenant.core.job.TenantJob;
import co.yixiang.yshop.module.product.dal.dataobject.storeproduct.StoreProductDO;
import co.yixiang.yshop.module.product.dal.dataobject.storeproductattrvalue.StoreProductAttrValueDO;
import co.yixiang.yshop.module.product.dal.mysql.storeproduct.StoreProductMapper;
import co.yixiang.yshop.module.product.dal.mysql.storeproductattrvalue.StoreProductAttrValueMapper;
import co.yixiang.yshop.module.system.service.notify.NotifySendService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 库存告警通知
 */
@Component
@Slf4j
public class StockWarningAutoJob implements JobHandler {
    @Resource
    private StoreProductMapper storeProductMapper;
    @Resource
    private StoreProductAttrValueMapper storeProductAttrValueMapper;
    @Resource
    private NotifySendService notifySendService;


    @Override
    @TenantJob
    public String execute(String param) {
        autoSend();

        return "ok";
    }

    /**
     * 库存告警发送消息
     */
    public void autoSend() {

        storeProductMapper.selectList(new LambdaQueryWrapper<StoreProductDO>()
                .ne(StoreProductDO::getInventoryWarningEnable,1), new ResultHandler<StoreProductDO>() {
            @Override
            public void handleResult(ResultContext<? extends StoreProductDO> resultContext) {
                StoreProductDO storeProductDO = resultContext.getResultObject();
                List<StoreProductAttrValueDO> storeProductAttrValueDOList = storeProductAttrValueMapper
                        .selectList(new LambdaQueryWrapper<StoreProductAttrValueDO>()
                        .eq(StoreProductAttrValueDO::getProductId, storeProductDO.getId())
                        .le(StoreProductAttrValueDO::getStock, storeProductDO.getInventoryWarningNumber()));
                //组合商品属性值
                String sku = storeProductAttrValueDOList.stream().map(StoreProductAttrValueDO::getSku)
                        .collect(Collectors.joining("、"));

                Map<String, Object> templateParams = Map.of("name", "商品名为:"+ storeProductDO.getStoreName() + "，规格：" + sku + "，库存告警");
                notifySendService.sendSingleNotifyToAdmin(Long.valueOf(storeProductDO.getCreator()),
                        "stock_warning", templateParams);

            }
        });
    }




}
