package co.yixiang.yshop.module.stock.service.stockcheck;

import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.module.product.dal.mysql.storeproduct.StoreProductMapper;
import co.yixiang.yshop.module.product.dal.mysql.storeproductattrvalue.StoreProductAttrValueMapper;
import co.yixiang.yshop.module.stock.controller.admin.stockcheck.vo.StockCheckPageReqVO;
import co.yixiang.yshop.module.stock.controller.admin.stockcheck.vo.StockCheckRespVO;
import co.yixiang.yshop.module.stock.controller.admin.stockcheck.vo.StockCheckSaveReqVO;
import co.yixiang.yshop.module.stock.controller.admin.stockchecklist.vo.StockCheckListPageReqVO;
import co.yixiang.yshop.module.stock.controller.admin.stockchecklist.vo.StockCheckListRespVO;
import co.yixiang.yshop.module.stock.controller.admin.stockchecklist.vo.StockCheckListSaveReqVO;
import co.yixiang.yshop.module.stock.dal.dataobject.stockcheck.StockCheckDO;
import co.yixiang.yshop.module.stock.dal.dataobject.stockchecklist.StockCheckListDO;
import co.yixiang.yshop.module.stock.dal.mysql.stockcheck.StockCheckMapper;
import co.yixiang.yshop.module.stock.dal.mysql.stockchecklist.StockCheckListMapper;
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
import static co.yixiang.yshop.module.stock.enums.ErrorCodeConstants.STOCKCHECK_NOT_EXISTS;

/**
 * 商品盘库 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class StockCheckServiceImpl implements StockCheckService {

    @Resource
    private StockCheckMapper stockCheckMapper;
    @Resource
    private StockCheckListMapper stockCheckListMapper;
    @Resource
    private StoreProductAttrValueMapper storeProductAttrValueMapper;
    @Resource
    private StoreProductMapper storeProductMapper;
    @Resource
    private NotifySendService notifySendService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = "STOCK_CHECK", subType = "创建商品盘库", bizNo = "{{#stockcheck.id}}",
            success = "创建了盘库商品【{{#stockcheck.docnum}}】")
    public Long createStockCheck(StockCheckSaveReqVO createReqVO) {
        StockCheckDO stockCheck = BeanUtils.toBean(createReqVO, StockCheckDO.class);
        stockCheck.setDocnum(getDocnum());
        if (stockCheck.getIsAudit() == null) {
            stockCheck.setIsAudit(ShopCommonEnum.IS_STATUS_0.getValue());
        }
        stockCheckMapper.insert(stockCheck);
        if (createReqVO.getCheckLists() != null) {
            for (StockCheckListSaveReqVO checkList : createReqVO.getCheckLists()) {
                checkList.setStockStockcheckId(stockCheck.getId());
                stockCheckListMapper.insert(BeanUtils.toBean(checkList, StockCheckListDO.class));
            }
        }
        //审核提醒
        Map<String, Object> templateParams = Map.of("name", "单据号:"+ stockCheck.getDocnum());
        notifySendService.sendSingleNotifyToAdmin(Long.valueOf(stockCheck.getCreator()),
                "stockcheck_audit", templateParams);

        LogRecordContext.putVariable("stockcheck", stockCheck);
        return stockCheck.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = "STOCK_CHECK", subType = "更新商品盘库", bizNo = "{{#updateReqVO.id}}",
            success = "更新了盘库商品【{{#updateReqVO.docnum}}】")
    public void updateStockCheck(StockCheckSaveReqVO updateReqVO) {
        validateStockCheckExists(updateReqVO.getId());
        StockCheckDO updateObj = BeanUtils.toBean(updateReqVO, StockCheckDO.class);
        stockCheckMapper.updateById(updateObj);
        stockCheckListMapper.delete(new LambdaQueryWrapper<StockCheckListDO>()
                .eq(StockCheckListDO::getStockStockcheckId, updateReqVO.getId()));
        if (updateReqVO.getCheckLists() != null) {
            for (StockCheckListSaveReqVO checkList : updateReqVO.getCheckLists()) {
                checkList.setStockStockcheckId(updateReqVO.getId());
                stockCheckListMapper.insert(BeanUtils.toBean(checkList, StockCheckListDO.class));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = "STOCK_CHECK", subType = "删除商品盘库", bizNo = "{{#id}}",
            success = "删除了盘库商品【{{#docnum}}】")
    public void deleteStockCheck(Long id) {
        StockCheckDO stockCheck =  validateStockCheckExists(id);
        stockCheckMapper.deleteById(id);
        stockCheckListMapper.delete(new LambdaQueryWrapper<StockCheckListDO>()
                .eq(StockCheckListDO::getStockStockcheckId, id));

        LogRecordContext.putVariable("docnum", stockCheck.getDocnum());
    }

    private StockCheckDO validateStockCheckExists(Long id) {
        StockCheckDO stockCheck = stockCheckMapper.selectById(id);
        if (stockCheck == null) {
            throw exception(STOCKCHECK_NOT_EXISTS);
        }
        return stockCheck;
    }

    @Override
    public StockCheckRespVO getStockCheck(Long id) {
        StockCheckDO stockCheckDO = validateStockCheckExists(id);
        StockCheckRespVO respVO = BeanUtils.toBean(stockCheckDO, StockCheckRespVO.class);
        StockCheckListPageReqVO listPageReqVO = new StockCheckListPageReqVO();
        listPageReqVO.setStockStockcheckId(id);
        listPageReqVO.setPageSize(50);
        PageResult<StockCheckListRespVO> listPage = stockCheckListMapper.selectPage(listPageReqVO);
        respVO.setStockStockcheckListList(listPage.getList());
        return respVO;
    }

    @Override
    public PageResult<StockCheckRespVO> getStockCheckPage(StockCheckPageReqVO pageReqVO) {
        PageResult<StockCheckRespVO> pageResult = stockCheckMapper.selectPage(pageReqVO);
        pageResult.getList().forEach(item -> {
            StockCheckListPageReqVO listPageReqVO = new StockCheckListPageReqVO();
            listPageReqVO.setStockStockcheckId(item.getId());
            listPageReqVO.setPageSize(50);
            PageResult<StockCheckListRespVO> listPage = stockCheckListMapper.selectPage(listPageReqVO);
            item.setStockStockcheckListList(listPage.getList());
        });
        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = "STOCK_CHECK", subType = "审核/反审核商品盘库", bizNo = "{{#id}}",
            success = "审核/反审核了盘库商品【{{#docnum}}】")
    public void auditStockCheck(Long id, String auditStatus) {
        StockCheckDO stockCheck = validateStockCheckExists(id);
        List<StockCheckListDO> checkLists = stockCheckListMapper.selectList(new LambdaQueryWrapper<StockCheckListDO>()
                .eq(StockCheckListDO::getStockStockcheckId, id));
        if (AuditTypesEnum.AUDIT.getValue().equals(auditStatus)) {
            stockCheck.setIsAudit(ShopCommonEnum.IS_STATUS_1.getValue());
            stockCheck.setAuditTime(LocalDateTime.now());
            stockCheckMapper.updateById(stockCheck);
            // 审核：按盈亏调整库存（正数加库存，负数减库存）
            for (StockCheckListDO item : checkLists) {
                applyDiff(item, false);
            }
        } else if (AuditTypesEnum.UNAUDIT.getValue().equals(auditStatus)) {
            stockCheck.setIsAudit(ShopCommonEnum.IS_STATUS_0.getValue());
            stockCheck.setAuditTime(null);
            stockCheckMapper.updateById(stockCheck);
            // 反审核：反向回滚
            for (StockCheckListDO item : checkLists) {
                applyDiff(item, true);
            }
        }

        LogRecordContext.putVariable("docnum", stockCheck.getDocnum());
    }

    /**
     * 按盈亏调整库存
     *
     * @param item    明细
     * @param reverse true 表示反审核回滚
     */
    private void applyDiff(StockCheckListDO item, boolean reverse) {
        Integer diff = item.getDiffNums();
        if (diff == null || diff == 0) {
            return;
        }
        int adjust = reverse ? -diff : diff;
        if (adjust > 0) {
            storeProductAttrValueMapper.incStockDecSales(adjust, item.getStockGoodsValueId());
            storeProductMapper.incStockDecSales(adjust, item.getStockGoodsId());
        } else if (adjust < 0) {
            int abs = Math.abs(adjust);
            storeProductAttrValueMapper.decStockIncSales(abs, item.getStockGoodsValueId());
            storeProductMapper.decStockIncSales(abs, item.getStockGoodsId());
        }
    }

    /**
     * 获取单号
     * 格式：PKD+yyyyMMddHHmmss
     */
    private String getDocnum() {
        return "PKD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

}
