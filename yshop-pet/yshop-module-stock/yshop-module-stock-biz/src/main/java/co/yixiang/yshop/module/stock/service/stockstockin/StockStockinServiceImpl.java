package co.yixiang.yshop.module.stock.service.stockstockin;

import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.module.product.dal.mysql.storeproduct.StoreProductMapper;
import co.yixiang.yshop.module.product.dal.mysql.storeproductattrvalue.StoreProductAttrValueMapper;
import co.yixiang.yshop.module.stock.controller.admin.stockstockinlist.vo.StockStockinListPageReqVO;
import co.yixiang.yshop.module.stock.controller.admin.stockstockinlist.vo.StockStockinListRespVO;
import co.yixiang.yshop.module.stock.controller.admin.stockstockinlist.vo.StockStockinListSaveReqVO;
import co.yixiang.yshop.module.stock.dal.dataobject.stockstockinlist.StockStockinListDO;
import co.yixiang.yshop.module.stock.dal.mysql.stockstockinlist.StockStockinListMapper;
import co.yixiang.yshop.module.stock.dal.mysql.stocksupplier.StockSupplierMapper;
import co.yixiang.yshop.module.stock.enums.AuditTypesEnum;
import co.yixiang.yshop.module.system.service.notify.NotifySendService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import co.yixiang.yshop.module.stock.controller.admin.stockstockin.vo.*;
import co.yixiang.yshop.module.stock.dal.dataobject.stockstockin.StockStockinDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;

import co.yixiang.yshop.module.stock.dal.mysql.stockstockin.StockStockinMapper;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.stock.enums.ErrorCodeConstants.*;

/**
 * 商品入库 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class StockStockinServiceImpl implements StockStockinService {

    @Resource
    private StockStockinMapper stockinMapper;
    @Resource
    private StockStockinListMapper stockinListMapper;
    @Resource
    private StockSupplierMapper stockSupplierMapper;
    @Resource
    private StoreProductAttrValueMapper storeProductAttrValueMapper;
    @Resource
    private StoreProductMapper storeProductMapper;
    @Resource
    private NotifySendService notifySendService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = "STOCK_IN", subType = "创建商品入库", bizNo = "{{#stockin.id}}",
            success = "创建了入库商品【{{#stockin.docnum}}】")
    public Long createStockin(StockStockinSaveReqVO createReqVO) {
        // 插入
        StockStockinDO stockin = BeanUtils.toBean(createReqVO, StockStockinDO.class);
        stockin.setDocnum(getDocnum());
        stockinMapper.insert(stockin);
        //插入商品入库明细
        for (StockStockinListSaveReqVO stockinList : createReqVO.getStockinLists()) {
            stockinList.setStockStockinId(stockin.getId());
            stockinListMapper.insert(BeanUtils.toBean(stockinList, StockStockinListDO.class));
        }
        //审核提醒
        Map<String, Object> templateParams = Map.of("name", "单据号:"+ stockin.getDocnum());
        notifySendService.sendSingleNotifyToAdmin(Long.valueOf(stockin.getCreator()),
                "stockin_audit", templateParams);

        //插入日志
        LogRecordContext.putVariable("stockin", stockin);
        // 返回
        return stockin.getId();
    }

    @Override
    @LogRecord(type = "STOCK_IN", subType = "更新商品入库", bizNo = "{{#updateReqVO.id}}",
            success = "更新了入库商品【{{#updateReqVO.docnum}}】")
    public void updateStockin(StockStockinSaveReqVO updateReqVO) {
        // 校验存在
        validateStockinExists(updateReqVO.getId());
        // 更新
        StockStockinDO updateObj = BeanUtils.toBean(updateReqVO, StockStockinDO.class);
        stockinMapper.updateById(updateObj);
        // 更新商品入库明细
        stockinListMapper.delete(new LambdaQueryWrapper<StockStockinListDO>()
                .eq(StockStockinListDO::getStockStockinId, updateReqVO.getId()));
        for (StockStockinListSaveReqVO stockinList : updateReqVO.getStockinLists()) {
            stockinList.setStockStockinId(updateReqVO.getId());
            stockinListMapper.insert(BeanUtils.toBean(stockinList, StockStockinListDO.class));
        }
    }

    @Override
    @LogRecord(type = "STOCK_IN", subType = "删除商品入库", bizNo = "{{#id}}",
            success = "删除了入库商品【{{#docnum}}】")
    public void deleteStockin(Long id) {
        // 校验存在
        StockStockinDO stockin = validateStockinExists(id);
        // 删除
        stockinMapper.deleteById(id);
        stockinListMapper.delete(new LambdaQueryWrapper<StockStockinListDO>()
                .eq(StockStockinListDO::getStockStockinId, id));

        LogRecordContext.putVariable("docnum", stockin.getDocnum());
    }

    private StockStockinDO validateStockinExists(Long id) {
        StockStockinDO stockin = stockinMapper.selectById(id);
        if (stockin == null) {
            throw exception(STOCKIN_NOT_EXISTS);
        }
        return stockin;
    }

    @Override
    public StockStockinRespVO getStockin(Long id) {
        StockStockinDO stockStockinDO = stockinMapper.selectById(id);
        StockStockinRespVO stockStockinRespVO = BeanUtils.toBean(stockStockinDO, StockStockinRespVO.class);
        stockStockinRespVO.setStockSupplierName(stockSupplierMapper.selectById(stockStockinDO.getStockSupplierId()).getName());
        StockStockinListPageReqVO stockStockinListPageReqVO = new StockStockinListPageReqVO();
        stockStockinListPageReqVO.setStockStockinId(id);
        stockStockinListPageReqVO.setPageSize(50);
        PageResult<StockStockinListRespVO> stockinListPage = stockinListMapper
                .selectPage(stockStockinListPageReqVO);
        stockStockinRespVO.setStockStockinListList(stockinListPage.getList());
        return stockStockinRespVO;
    }

    @Override
    public PageResult<StockStockinRespVO> getStockinPage(StockStockinPageReqVO pageReqVO) {
        PageResult<StockStockinRespVO> pageResult = stockinMapper.selectPage(pageReqVO);
        //设置商品明细
        pageResult.getList().forEach(item -> {
            StockStockinListPageReqVO stockStockinListPageReqVO = new StockStockinListPageReqVO();
            stockStockinListPageReqVO.setStockStockinId(item.getId());
            stockStockinListPageReqVO.setPageSize(50);
            PageResult<StockStockinListRespVO> stockinListPage = stockinListMapper
                    .selectPage(stockStockinListPageReqVO);
            item.setStockStockinListList(stockinListPage.getList());
        });

        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = "STOCK_IN", subType = "审核/反审核商品入库", bizNo = "{{#id}}",
            success = "审核/反审核了入库商品【{{#docnum}}】")
    public void auditStockin(Long id, String auditStatus) {
        // 校验存在
        StockStockinDO stockin = validateStockinExists(id);
        //获取入库明细
        List<StockStockinListDO> stockinList = stockinListMapper.selectList(new LambdaQueryWrapper<StockStockinListDO>()
                .eq(StockStockinListDO::getStockStockinId, id));
        //审核，增加库存
        if(AuditTypesEnum.AUDIT.getValue().equals(auditStatus)){
            stockin.setIsAudit(ShopCommonEnum.IS_STATUS_1.getValue());
            stockin.setAuditTime(LocalDateTime.now());
            stockinMapper.updateById(stockin);
            //增加库存

            for (StockStockinListDO stockinListDO : stockinList) {
                //增加库存
                storeProductAttrValueMapper.incStockDecSales(stockinListDO.getStockinNums(), stockinListDO.getStockGoodsValueId());
                storeProductMapper.incStockDecSales(stockinListDO.getStockinNums(), stockinListDO.getStockGoodsId());
            }
        } else if (AuditTypesEnum.UNAUDIT.getValue().equals(auditStatus)) {
            //反审核，去掉库存
            stockin.setIsAudit(ShopCommonEnum.IS_STATUS_0.getValue());
            stockin.setAuditTime(null);
            stockinMapper.updateById(stockin);
            //减少库存
            for (StockStockinListDO stockinListDO : stockinList) {
                //减少库存
                storeProductAttrValueMapper.decStockIncSales(stockinListDO.getStockinNums(), stockinListDO.getStockGoodsValueId());
                storeProductMapper.decStockIncSales(stockinListDO.getStockinNums(), stockinListDO.getStockGoodsId());
            }
        }
        LogRecordContext.putVariable("docnum", stockin.getDocnum());

    }


    /**
     * 获取单号
     * 格式：GHD+yyyyMMddHHmmss
     * @return
     */
    private String getDocnum() {
        return "GHD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

}