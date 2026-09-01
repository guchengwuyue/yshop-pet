package co.yixiang.yshop.module.stock.service.stockiodetail;

import cn.hutool.core.util.StrUtil;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.stock.controller.admin.stockchecklist.vo.StockCheckListRespVO;
import co.yixiang.yshop.module.stock.controller.admin.stockiodetail.vo.StockIoDetailPageReqVO;
import co.yixiang.yshop.module.stock.controller.admin.stockiodetail.vo.StockIoDetailRespVO;
import co.yixiang.yshop.module.stock.controller.admin.stockstockinlist.vo.StockStockinListRespVO;
import co.yixiang.yshop.module.stock.controller.admin.stockstockoutlist.vo.StockStockoutListRespVO;
import co.yixiang.yshop.module.stock.dal.mysql.stockchecklist.StockCheckListMapper;
import co.yixiang.yshop.module.stock.dal.mysql.stockstockinlist.StockStockinListMapper;
import co.yixiang.yshop.module.stock.dal.mysql.stockstockoutlist.StockStockoutListMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品出入盘库明细 Service 实现类
 */
@Service
@Validated
public class StockIoDetailServiceImpl implements StockIoDetailService {

    @Resource
    private StockStockinListMapper stockinListMapper;
    @Resource
    private StockStockoutListMapper stockoutListMapper;
    @Resource
    private StockCheckListMapper stockCheckListMapper;

    @Override
    public PageResult<StockIoDetailRespVO> getStockIoDetailPage(StockIoDetailPageReqVO pageReqVO) {
        List<StockIoDetailRespVO> all = new ArrayList<>();

        // 入库明细
        List<StockStockinListRespVO> inList = stockinListMapper.selectListByGoodsId(pageReqVO.getStockGoodsId());
        for (StockStockinListRespVO item : inList) {
            StockIoDetailRespVO vo = new StockIoDetailRespVO();
            vo.setRowKey("in-" + item.getId());
            vo.setId(item.getId());
            vo.setIoType("入库");
            vo.setDocnum(item.getDocNumber());
            vo.setStockGoodsId(item.getStockGoodsId());
            vo.setStockGoodsValueId(item.getStockGoodsValueId());
            vo.setGoodsCode(item.getGoodsCode());
            vo.setStockGoodsName(item.getStockGoodsName());
            vo.setStockGoodsValueName(item.getStockGoodsValueName());
            vo.setUnitName(item.getUnitName());
            vo.setUnitPrice(item.getInboundPrice());
            vo.setStockinNums(item.getStockinNums());
            vo.setStockinAmount(item.getAmount());
            vo.setStockOutNums(null);
            vo.setStockOutAmount(null);
            vo.setBookNums(null);
            vo.setActualNums(null);
            vo.setDiffNums(null);
            vo.setIoTime(item.getInboundTime());
            all.add(vo);
        }

        // 出库明细
        List<StockStockoutListRespVO> outList = stockoutListMapper.selectListByGoodsId(pageReqVO.getStockGoodsId());
        for (StockStockoutListRespVO item : outList) {
            StockIoDetailRespVO vo = new StockIoDetailRespVO();
            vo.setRowKey("out-" + item.getId());
            vo.setId(item.getId());
            vo.setIoType("出库");
            vo.setDocnum(item.getDocNumber());
            vo.setStockGoodsId(item.getStockGoodsId());
            vo.setStockGoodsValueId(item.getStockGoodsValueId());
            vo.setGoodsCode(item.getGoodsCode());
            vo.setStockGoodsName(item.getStockGoodsName());
            vo.setStockGoodsValueName(item.getStockGoodsValueName());
            vo.setUnitName(item.getUnitName());
            vo.setUnitPrice(item.getOutboundPrice());
            vo.setStockinNums(null);
            vo.setStockinAmount(null);
            vo.setStockOutNums(item.getStockOutNums());
            vo.setStockOutAmount(item.getAmount());
            vo.setBookNums(null);
            vo.setActualNums(null);
            vo.setDiffNums(null);
            vo.setIoTime(item.getOutboundTime());
            all.add(vo);
        }

        // 盘库明细
        List<StockCheckListRespVO> checkList = stockCheckListMapper.selectListByGoodsId(pageReqVO.getStockGoodsId());
        for (StockCheckListRespVO item : checkList) {
            StockIoDetailRespVO vo = new StockIoDetailRespVO();
            vo.setRowKey("check-" + item.getId());
            vo.setId(item.getId());
            vo.setIoType("盘库");
            vo.setDocnum(item.getDocnum());
            vo.setStockGoodsId(item.getStockGoodsId());
            vo.setStockGoodsValueId(item.getStockGoodsValueId());
            vo.setGoodsCode(item.getGoodsCode());
            vo.setStockGoodsName(item.getStockGoodsName());
            vo.setStockGoodsValueName(item.getStockGoodsValueName());
            vo.setUnitName(item.getUnitName());
            vo.setUnitPrice(null);
            vo.setStockinNums(null);
            vo.setStockinAmount(null);
            vo.setStockOutNums(null);
            vo.setStockOutAmount(null);
            vo.setBookNums(item.getBookNums());
            vo.setActualNums(item.getActualNums());
            vo.setDiffNums(item.getDiffNums());
            vo.setIoTime(item.getCheckTime());
            all.add(vo);
        }

        // 关键字过滤
        String keyword = StrUtil.trim(pageReqVO.getKeyword());
        if (StrUtil.isNotEmpty(keyword)) {
            all = all.stream().filter(item -> containsKeyword(item, keyword)).collect(Collectors.toList());
        }

        // 出入盘时间倒序
        all.sort(Comparator.comparing(StockIoDetailRespVO::getIoTime,
                Comparator.nullsLast(Comparator.reverseOrder())));

        long total = all.size();
        int pageNo = pageReqVO.getPageNo();
        int pageSize = pageReqVO.getPageSize();
        int fromIndex = Math.max((pageNo - 1) * pageSize, 0);
        if (fromIndex >= total) {
            return new PageResult<>(new ArrayList<>(), total);
        }
        int toIndex = (int) Math.min(fromIndex + pageSize, total);
        return new PageResult<>(all.subList(fromIndex, toIndex), total);
    }

    private boolean containsKeyword(StockIoDetailRespVO item, String keyword) {
        return StrUtil.containsIgnoreCase(StrUtil.nullToEmpty(item.getGoodsCode()), keyword)
                || StrUtil.containsIgnoreCase(StrUtil.nullToEmpty(item.getStockGoodsName()), keyword)
                || StrUtil.containsIgnoreCase(StrUtil.nullToEmpty(item.getStockGoodsValueName()), keyword)
                || StrUtil.containsIgnoreCase(StrUtil.nullToEmpty(item.getIoType()), keyword)
                || StrUtil.containsIgnoreCase(StrUtil.nullToEmpty(item.getDocnum()), keyword);
    }

}
