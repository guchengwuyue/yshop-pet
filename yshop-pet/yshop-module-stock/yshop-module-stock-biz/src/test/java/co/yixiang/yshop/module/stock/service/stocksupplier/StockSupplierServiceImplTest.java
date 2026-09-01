package co.yixiang.yshop.module.stock.service.stocksupplier;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.annotation.Resource;

import co.yixiang.yshop.framework.test.core.ut.BaseDbUnitTest;

import co.yixiang.yshop.module.stock.controller.admin.stocksupplier.vo.*;
import co.yixiang.yshop.module.stock.dal.dataobject.stocksupplier.StockSupplierDO;
import co.yixiang.yshop.module.stock.dal.mysql.stocksupplier.StockSupplierMapper;
import co.yixiang.yshop.framework.common.pojo.PageResult;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static co.yixiang.yshop.module.stock.enums.ErrorCodeConstants.*;
import static co.yixiang.yshop.framework.test.core.util.AssertUtils.*;
import static co.yixiang.yshop.framework.test.core.util.RandomUtils.*;
import static co.yixiang.yshop.framework.common.util.date.LocalDateTimeUtils.*;
import static co.yixiang.yshop.framework.common.util.object.ObjectUtils.*;
import static co.yixiang.yshop.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * {@link StockSupplierServiceImpl} 的单元测试类
 *
 * @author yshop
 */
@Import(StockSupplierServiceImpl.class)
public class StockSupplierServiceImplTest extends BaseDbUnitTest {

    @Resource
    private StockSupplierServiceImpl supplierService;

    @Resource
    private StockSupplierMapper supplierMapper;

    @Test
    public void testCreateSupplier_success() {
        // 准备参数
        StockSupplierSaveReqVO createReqVO = randomPojo(StockSupplierSaveReqVO.class).setId(null);

        // 调用
        Long supplierId = supplierService.createSupplier(createReqVO);
        // 断言
        assertNotNull(supplierId);
        // 校验记录的属性是否正确
        StockSupplierDO supplier = supplierMapper.selectById(supplierId);
        assertPojoEquals(createReqVO, supplier, "id");
    }

    @Test
    public void testUpdateSupplier_success() {
        // mock 数据
        StockSupplierDO dbSupplier = randomPojo(StockSupplierDO.class);
        supplierMapper.insert(dbSupplier);// @Sql: 先插入出一条存在的数据
        // 准备参数
        StockSupplierSaveReqVO updateReqVO = randomPojo(StockSupplierSaveReqVO.class, o -> {
            o.setId(dbSupplier.getId()); // 设置更新的 ID
        });

        // 调用
        supplierService.updateSupplier(updateReqVO);
        // 校验是否更新正确
        StockSupplierDO supplier = supplierMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, supplier);
    }

    @Test
    public void testUpdateSupplier_notExists() {
        // 准备参数
        StockSupplierSaveReqVO updateReqVO = randomPojo(StockSupplierSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> supplierService.updateSupplier(updateReqVO), SUPPLIER_NOT_EXISTS);
    }

    @Test
    public void testDeleteSupplier_success() {
        // mock 数据
        StockSupplierDO dbSupplier = randomPojo(StockSupplierDO.class);
        supplierMapper.insert(dbSupplier);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbSupplier.getId();

        // 调用
        supplierService.deleteSupplier(id);
       // 校验数据不存在了
       assertNull(supplierMapper.selectById(id));
    }

    @Test
    public void testDeleteSupplier_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> supplierService.deleteSupplier(id), SUPPLIER_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetSupplierPage() {
       // mock 数据
       StockSupplierDO dbSupplier = randomPojo(StockSupplierDO.class, o -> { // 等会查询到
           o.setName(null);
           o.setShortName(null);
           o.setCode(null);
           o.setShipAddress(null);
           o.setAddress(null);
           o.setContacts(null);
           o.setTelphone(null);
           o.setRemark(null);
           o.setCreateTime(null);
       });
       supplierMapper.insert(dbSupplier);
       // 测试 name 不匹配
       supplierMapper.insert(cloneIgnoreId(dbSupplier, o -> o.setName(null)));
       // 测试 shortName 不匹配
       supplierMapper.insert(cloneIgnoreId(dbSupplier, o -> o.setShortName(null)));
       // 测试 code 不匹配
       supplierMapper.insert(cloneIgnoreId(dbSupplier, o -> o.setCode(null)));
       // 测试 shipAddress 不匹配
       supplierMapper.insert(cloneIgnoreId(dbSupplier, o -> o.setShipAddress(null)));
       // 测试 address 不匹配
       supplierMapper.insert(cloneIgnoreId(dbSupplier, o -> o.setAddress(null)));
       // 测试 contacts 不匹配
       supplierMapper.insert(cloneIgnoreId(dbSupplier, o -> o.setContacts(null)));
       // 测试 telphone 不匹配
       supplierMapper.insert(cloneIgnoreId(dbSupplier, o -> o.setTelphone(null)));
       // 测试 remark 不匹配
       supplierMapper.insert(cloneIgnoreId(dbSupplier, o -> o.setRemark(null)));
       // 测试 createTime 不匹配
       supplierMapper.insert(cloneIgnoreId(dbSupplier, o -> o.setCreateTime(null)));
       // 准备参数
       StockSupplierPageReqVO reqVO = new StockSupplierPageReqVO();
       reqVO.setName(null);
       reqVO.setShortName(null);
       reqVO.setCode(null);
       reqVO.setShipAddress(null);
       reqVO.setAddress(null);
       reqVO.setContacts(null);
       reqVO.setTelphone(null);
       reqVO.setRemark(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<StockSupplierDO> pageResult = supplierService.getSupplierPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbSupplier, pageResult.getList().get(0));
    }

}