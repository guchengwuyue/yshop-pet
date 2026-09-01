package co.yixiang.yshop.module.stock.dal.mysql.stockcheck;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.module.stock.controller.admin.stockcheck.vo.StockCheckPageReqVO;
import co.yixiang.yshop.module.stock.controller.admin.stockcheck.vo.StockCheckRespVO;
import co.yixiang.yshop.module.stock.dal.dataobject.stockcheck.StockCheckDO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品盘库 Mapper
 *
 * @author yshop
 */
@Mapper
public interface StockCheckMapper extends BaseMapperX<StockCheckDO> {

    default PageResult<StockCheckRespVO> selectPage(StockCheckPageReqVO reqVO) {
        return selectJoinPage(reqVO, StockCheckRespVO.class, new MPJLambdaWrapper<StockCheckDO>()
                .selectAll(StockCheckDO.class)
                .likeIfExists(StockCheckDO::getDocnum, reqVO.getDocnum())
                .eqIfExists(StockCheckDO::getIsAudit, reqVO.getIsAudit())
                .between(reqVO.getCheckTime() != null, StockCheckDO::getCheckTime,
                        reqVO.getCheckTime() != null && reqVO.getCheckTime().length > 1
                                ? reqVO.getCheckTime()[0] : null,
                        reqVO.getCheckTime() != null && reqVO.getCheckTime().length > 1
                                ? reqVO.getCheckTime()[1] : null)
                .orderByDesc(StockCheckDO::getId)
        );
    }

}
