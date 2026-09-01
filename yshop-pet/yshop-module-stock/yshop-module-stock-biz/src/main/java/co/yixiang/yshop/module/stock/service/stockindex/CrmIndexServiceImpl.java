package co.yixiang.yshop.module.stock.service.stockindex;

import cn.hutool.core.date.DateUtil;
import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.framework.security.core.util.SecurityFrameworkUtils;
import co.yixiang.yshop.module.crm.dal.mysql.crmcustomer.CrmCustomerMapper;
import co.yixiang.yshop.module.product.dal.mysql.category.ProductCategoryMapper;
import co.yixiang.yshop.module.product.dal.mysql.storeproduct.StoreProductMapper;
import co.yixiang.yshop.module.product.dal.mysql.storeproductrule.StoreProductRuleMapper;
import co.yixiang.yshop.module.stock.controller.admin.stockindex.vo.BrieCountVO;
import co.yixiang.yshop.module.stock.controller.admin.stockindex.vo.CrmIndexRespVO;
import co.yixiang.yshop.module.stock.dal.dataobject.stockstockin.StockStockinDO;
import co.yixiang.yshop.module.stock.dal.dataobject.stockstockout.StockStockoutDO;
import co.yixiang.yshop.module.stock.dal.mysql.stockstockin.StockStockinMapper;
import co.yixiang.yshop.module.stock.dal.mysql.stockstockinlist.StockStockinListMapper;
import co.yixiang.yshop.module.stock.dal.mysql.stockstockout.StockStockoutMapper;
import co.yixiang.yshop.module.stock.dal.mysql.stocksupplier.StockSupplierMapper;
import co.yixiang.yshop.module.system.dal.mysql.user.AdminUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商机 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class CrmIndexServiceImpl implements CrmIndexService {

    @Resource
    private StockStockinMapper stockStockinMapper;
    @Resource
    private StockStockoutMapper stockStockoutMapper;
    @Resource
    private StockStockinListMapper stockStockinListMapper;
    @Resource
    private StoreProductMapper storeProductMapper;
    @Resource
    private CrmCustomerMapper crmCustomerMapper;
    @Resource
    private StockSupplierMapper stockSupplierMapper;
    @Resource
    private AdminUserMapper adminUserMapper;
    @Resource
    private ProductCategoryMapper productCategoryMapper;
    @Resource
    private StoreProductRuleMapper storeProductRuleMapper;




    @Override
    public CrmIndexRespVO getIndexCount() {
        Date todayStart = DateUtil.beginOfDay(new Date());
        Date todayEnd = DateUtil.endOfDay(new Date());
        //入库待审核
        Long stockinCount = stockStockinMapper.selectCount(new LambdaQueryWrapper<StockStockinDO>()
                .eq(StockStockinDO::getIsAudit, ShopCommonEnum.IS_STATUS_0.getValue()));
        //出库待审核
        Long stockoutCount = stockStockoutMapper.selectCount(new LambdaQueryWrapper<StockStockoutDO>()
                .eq(StockStockoutDO::getIsAudit, ShopCommonEnum.IS_STATUS_0.getValue()));
        //今日入库数量
        Integer todayInboundCount = stockStockinMapper.selectSum(new LambdaQueryWrapper<StockStockinDO>()
                .between(StockStockinDO::getInboundTime,todayStart,todayEnd));

        //今日出库数量
        Integer todayOutboundCount = stockStockoutMapper.selectSum(new LambdaQueryWrapper<StockStockoutDO>()
                .between(StockStockoutDO::getOutboundTime,todayStart,todayEnd));
        //商品数量
        Long productCount = storeProductMapper.selectCount();
        //客户数量
        Long customerCount = crmCustomerMapper.selectCount();
        //供应商数量
        Long supplierCount = stockSupplierMapper.selectCount();
        //用户数量
        Long userCount = adminUserMapper.selectCount();
        //商品分类数量
        Long productCategoryCount = productCategoryMapper.selectCount();
        //商品规则数量
        Long productRuleCount = storeProductRuleMapper.selectCount();



        BrieCountVO brieCountVO = BrieCountVO.builder()
                .count03(todayInboundCount)
                .count08(todayOutboundCount)
                .count01(productCount)
                .count05(customerCount)
                .count06(supplierCount)
                .count07(userCount)
                .count02(productCategoryCount)
                .count04(productRuleCount)
                .build();
        return CrmIndexRespVO.builder()
                .todoCount(stockinCount)
                .doneCount(stockoutCount)
                .brieCountVO(brieCountVO)
                .build();
    }


}