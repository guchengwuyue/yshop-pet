package co.yixiang.yshop.module.stock.service.stockstockout;

import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.module.crm.dal.dataobject.crmcustomer.CrmCustomerDO;
import co.yixiang.yshop.module.crm.dal.mysql.crmcustomer.CrmCustomerMapper;
import co.yixiang.yshop.module.product.dal.mysql.storeproduct.StoreProductMapper;
import co.yixiang.yshop.module.product.dal.mysql.storeproductattrvalue.StoreProductAttrValueMapper;
import co.yixiang.yshop.module.stock.controller.admin.stockstockout.vo.StockStockoutPageReqVO;
import co.yixiang.yshop.module.stock.controller.admin.stockstockout.vo.StockStockoutRespVO;
import co.yixiang.yshop.module.stock.controller.admin.stockstockout.vo.StockStockoutSaveReqVO;
import co.yixiang.yshop.module.stock.controller.admin.stockstockoutlist.vo.StockStockoutListPageReqVO;
import co.yixiang.yshop.module.stock.controller.admin.stockstockoutlist.vo.StockStockoutListRespVO;
import co.yixiang.yshop.module.stock.controller.admin.stockstockoutlist.vo.StockStockoutListSaveReqVO;
import co.yixiang.yshop.module.stock.dal.dataobject.stockstockout.StockStockoutDO;
import co.yixiang.yshop.module.stock.dal.dataobject.stockstockoutlist.StockStockoutListDO;
import co.yixiang.yshop.module.stock.dal.mysql.stockstockout.StockStockoutMapper;
import co.yixiang.yshop.module.stock.dal.mysql.stockstockoutlist.StockStockoutListMapper;
import co.yixiang.yshop.module.stock.enums.AuditTypesEnum;
import co.yixiang.yshop.module.system.service.notify.NotifySendService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.stock.enums.ErrorCodeConstants.STOCKOUT_NOT_EXISTS;

/**
 * 商品出库 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class StockStockoutServiceImpl implements StockStockoutService {

    @Resource
    private StockStockoutMapper stockoutMapper;
    @Resource
    private StockStockoutListMapper stockoutListMapper;
    @Resource
    private CrmCustomerMapper crmCustomerMapper;
    @Resource
    private StoreProductAttrValueMapper storeProductAttrValueMapper;
    @Resource
    private StoreProductMapper storeProductMapper;
    @Resource
    private NotifySendService notifySendService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = "STOCK_OUT", subType = "创建商品出库", bizNo = "{{#stockout.id}}",
            success = "创建了出库商品【{{#stockout.docnum}}】")
    public Long createStockout(StockStockoutSaveReqVO createReqVO) {
        StockStockoutDO stockout = BeanUtils.toBean(createReqVO, StockStockoutDO.class);
        stockout.setDocnum(getDocnum());
        stockoutMapper.insert(stockout);
        if (createReqVO.getStockoutLists() != null) {
            for (StockStockoutListSaveReqVO stockoutList : createReqVO.getStockoutLists()) {
                stockoutList.setStockStockoutId(stockout.getId());
                stockoutListMapper.insert(BeanUtils.toBean(stockoutList, StockStockoutListDO.class));
            }
        }
        //审核提醒
        Map<String, Object> templateParams = Map.of("name", "单据号:"+ stockout.getDocnum());
        notifySendService.sendSingleNotifyToAdmin(Long.valueOf(stockout.getCreator()),
                "stockout_audit", templateParams);

        LogRecordContext.putVariable("stockout", stockout);
        return stockout.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = "STOCK_OUT", subType = "更新商品出库", bizNo = "{{#updateReqVO.id}}",
            success = "更新了出库商品【{{#updateReqVO.docnum}}】")
    public void updateStockout(StockStockoutSaveReqVO updateReqVO) {
        validateStockoutExists(updateReqVO.getId());
        StockStockoutDO updateObj = BeanUtils.toBean(updateReqVO, StockStockoutDO.class);
        stockoutMapper.updateById(updateObj);
        stockoutListMapper.delete(new LambdaQueryWrapper<StockStockoutListDO>()
                .eq(StockStockoutListDO::getStockStockoutId, updateReqVO.getId()));
        if (updateReqVO.getStockoutLists() != null) {
            for (StockStockoutListSaveReqVO stockoutList : updateReqVO.getStockoutLists()) {
                stockoutList.setStockStockoutId(updateReqVO.getId());
                stockoutListMapper.insert(BeanUtils.toBean(stockoutList, StockStockoutListDO.class));
            }
        }
    }

    @Override
    @LogRecord(type = "STOCK_IN", subType = "删除商品入库", bizNo = "{{#id}}",
            success = "删除了入库商品【{{#docnum}}】")
    public void deleteStockout(Long id) {
        StockStockoutDO stockout = validateStockoutExists(id);

        stockoutMapper.deleteById(id);
        stockoutListMapper.delete(new LambdaQueryWrapper<StockStockoutListDO>()
                .eq(StockStockoutListDO::getStockStockoutId, id));

        LogRecordContext.putVariable("docnum", stockout.getDocnum());
    }

    private StockStockoutDO validateStockoutExists(Long id) {
        StockStockoutDO stockout = stockoutMapper.selectById(id);
        if (stockout == null) {
            throw exception(STOCKOUT_NOT_EXISTS);
        }
        return stockout;
    }

    @Override
    public StockStockoutRespVO getStockout(Long id) {
        StockStockoutDO stockStockoutDO = validateStockoutExists(id);
        StockStockoutRespVO stockStockoutRespVO = BeanUtils.toBean(stockStockoutDO, StockStockoutRespVO.class);
        if (stockStockoutDO.getStockCustomerId() != null) {
            CrmCustomerDO customer = crmCustomerMapper.selectById(stockStockoutDO.getStockCustomerId());
            if (customer != null) {
                stockStockoutRespVO.setStockCustomerName(customer.getName());
            }
        }
        StockStockoutListPageReqVO stockStockoutListPageReqVO = new StockStockoutListPageReqVO();
        stockStockoutListPageReqVO.setStockStockoutId(id);
        stockStockoutListPageReqVO.setPageSize(50);
        PageResult<StockStockoutListRespVO> stockoutListPage = stockoutListMapper.selectPage(stockStockoutListPageReqVO);
        stockStockoutRespVO.setStockStockoutListList(stockoutListPage.getList());
        return stockStockoutRespVO;
    }

    @Override
    public PageResult<StockStockoutRespVO> getStockoutPage(StockStockoutPageReqVO pageReqVO) {
        PageResult<StockStockoutRespVO> result = stockoutMapper.selectPage(pageReqVO);
        //PageResult<StockStockoutRespVO> result = BeanUtils.toBean(pageResult, StockStockoutRespVO.class);
        result.getList().forEach(item -> {
//            if (item.getStockCustomerId() != null) {
//                CrmCustomerDO customer = crmCustomerMapper.selectById(item.getStockCustomerId());
//                if (customer != null) {
//                    item.setStockCustomerName(customer.getName());
//                }
//            }
            StockStockoutListPageReqVO stockStockoutListPageReqVO = new StockStockoutListPageReqVO();
            stockStockoutListPageReqVO.setStockStockoutId(item.getId());
            stockStockoutListPageReqVO.setPageSize(50);
            PageResult<StockStockoutListRespVO> stockoutListPage = stockoutListMapper.selectPage(stockStockoutListPageReqVO);
            item.setStockStockoutListList(stockoutListPage.getList());
        });
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = "STOCK_OUT", subType = "审核/反审核商品出库", bizNo = "{{#id}}",
            success = "审核/反审核了出库商品【{{#docnum}}】")
    public void auditStockout(Long id, String auditStatus) {
        StockStockoutDO stockout = validateStockoutExists(id);
        List<StockStockoutListDO> stockoutList = stockoutListMapper.selectList(new LambdaQueryWrapper<StockStockoutListDO>()
                .eq(StockStockoutListDO::getStockStockoutId, id));
        // 审核：扣减库存
        if (AuditTypesEnum.AUDIT.getValue().equals(auditStatus)) {
            stockout.setIsAudit(ShopCommonEnum.IS_STATUS_1.getValue());
            stockout.setAuditTime(LocalDateTime.now());
            stockoutMapper.updateById(stockout);
            for (StockStockoutListDO stockoutListDO : stockoutList) {
                storeProductAttrValueMapper.decStockIncSales(stockoutListDO.getStockOutNums(), stockoutListDO.getStockGoodsValueId());
                storeProductMapper.decStockIncSales(stockoutListDO.getStockOutNums(), stockoutListDO.getStockGoodsId());
            }
        } else if (AuditTypesEnum.UNAUDIT.getValue().equals(auditStatus)) {
            // 反审核：回增库存
            stockout.setIsAudit(ShopCommonEnum.IS_STATUS_0.getValue());
            stockout.setAuditTime(null);
            stockoutMapper.updateById(stockout);
            for (StockStockoutListDO stockoutListDO : stockoutList) {
                storeProductAttrValueMapper.incStockDecSales(stockoutListDO.getStockOutNums(), stockoutListDO.getStockGoodsValueId());
                storeProductMapper.incStockDecSales(stockoutListDO.getStockOutNums(), stockoutListDO.getStockGoodsId());
            }
        }

        LogRecordContext.putVariable("docnum", stockout.getDocnum());
    }

    /**
     * 获取单号
     * 格式：CKD+yyyyMMddHHmmss
     */
    private String getDocnum() {
        return "CKD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

}
