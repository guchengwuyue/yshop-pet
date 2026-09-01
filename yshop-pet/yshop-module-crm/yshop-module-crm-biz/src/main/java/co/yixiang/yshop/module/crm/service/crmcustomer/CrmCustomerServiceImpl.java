package co.yixiang.yshop.module.crm.service.crmcustomer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.framework.common.enums.UserTypeEnum;
import co.yixiang.yshop.framework.common.exception.ErrorCode;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.framework.redis.util.redis.RedisUtil;
import co.yixiang.yshop.framework.security.core.util.SecurityFrameworkUtils;
import co.yixiang.yshop.module.crm.controller.admin.crmcustomer.vo.*;
import co.yixiang.yshop.module.crm.dal.dataobject.crmcustomer.CrmCustomerDO;
import co.yixiang.yshop.module.crm.dal.mysql.crmcustomer.CrmCustomerMapper;
import co.yixiang.yshop.module.crm.enums.CustomerTypesEnum;
import co.yixiang.yshop.module.crm.enums.RelationEnum;
import co.yixiang.yshop.module.crm.enums.TeamRoleEnum;
import co.yixiang.yshop.module.crm.enums.TypesEnum;
import co.yixiang.yshop.module.crm.log.LogRecordService;;
import co.yixiang.yshop.module.system.api.user.AdminUserApi;
import co.yixiang.yshop.module.system.controller.admin.user.vo.user.UserImportRespVO;
import co.yixiang.yshop.module.system.dal.dataobject.dict.DictDataDO;
import co.yixiang.yshop.module.system.dal.dataobject.user.AdminUserDO;
import co.yixiang.yshop.module.system.dal.mysql.user.AdminUserMapper;
import co.yixiang.yshop.module.system.service.dict.DictDataService;
import co.yixiang.yshop.module.system.service.mail.MailSendService;
import co.yixiang.yshop.module.system.service.sms.SmsSendService;
import co.yixiang.yshop.module.system.service.user.AdminUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.crm.enums.ErrorCodeConstants.CUSTOMER_NOT_EXISTS;
import static java.lang.Math.toRadians;

/**
 * 客户 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class CrmCustomerServiceImpl implements CrmCustomerService {

    @Resource
    private CrmCustomerMapper customerMapper;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private AdminUserMapper userMapper;
    @Resource
    private AdminUserService adminUserService;
    @Resource
    private SmsSendService smsSendService;
    @Resource
    private MailSendService mailSendService;
    @Resource
    private DictDataService dictDataService;
    @Resource
    private LogRecordService logRecordService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = "CRM_CUSTOMER", subType = "创建客户", bizNo = "{{#customer.id}}",
            success = "创建了客户【{{#customer.name}}】")
    public Long createCustomer(CrmCustomerSaveReqVO createReqVO) {

        limitCustomer(SecurityFrameworkUtils.getLoginUserId());
        // 插入
        CrmCustomerDO customer = BeanUtils.toBean(createReqVO, CrmCustomerDO.class);
        customer.setCollectTime(LocalDateTime.now());
        customer.setFollowTime(LocalDateTime.now());
        customer.setOwnerUserId(SecurityFrameworkUtils.getLoginUserId());
        customerMapper.insert(customer);

        //插入日志
        LogRecordContext.putVariable("customer", customer);
        //crmOperatelogService.createLog("添加客户",customer.getId(),0L,0L);
        // 返回
        return customer.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = "CRM_CUSTOMER", subType = "更新客户", bizNo = "{{#updateReqVO.id}}",
            success = "创建了客户【{{#updateReqVO.name}}】")
    public void updateCustomer(CrmCustomerSaveReqVO updateReqVO) {
        // 校验存在
        validateCustomerExists(updateReqVO.getId());
        // 更新
        CrmCustomerDO updateObj = BeanUtils.toBean(updateReqVO, CrmCustomerDO.class);
        customerMapper.updateById(updateObj);

        //插入日志
        //crmOperatelogService.createLog("修改客户",updateReqVO.getId(),0L,0L);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = "CRM_CUSTOMER", subType = "删除客户", bizNo = "{{#id}}",
            success = "删除了客户【{{#customerName}}】")
    public void deleteCustomer(Long id) {
        // 校验存在
        CrmCustomerDO customer = validateCustomerExists(id);

        // 删除
        customerMapper.deleteById(id);

        LogRecordContext.putVariable("customerName", customer.getName());

    }



    private CrmCustomerDO validateCustomerExists(Long id) {
        CrmCustomerDO customer = customerMapper.selectById(id);
        if (customer == null) {
            throw exception(CUSTOMER_NOT_EXISTS);
        }
        return customer;
    }

    @Override
    public CrmCustomerRespVO getCustomer(Long id) {
        CrmCustomerDO customerDO = customerMapper.selectById(id);
        CrmCustomerRespVO customerRespVO = BeanUtils.toBean(customerDO, CrmCustomerRespVO.class);
        //返回拥有者名称
        if(customerDO.getOwnerUserId() != null && customerDO.getOwnerUserId() > 0){
            AdminUserDO adminUserDO = adminUserService.getUser(customerDO.getOwnerUserId());
            customerRespVO.setOwnUserName(adminUserDO.getUsername());
        }

        return customerRespVO;
    }

    @Override
    public PageResult<CrmCustomerDO> getCustomerPage(CrmCustomerPageReqVO pageReqVO) {
        List<Long> ids = new ArrayList<>();
        Long adminId = SecurityFrameworkUtils.getLoginUserId();
        if(CustomerTypesEnum.OPEN.getValue().equals(pageReqVO.getType())){
            ids.add(0L);
        }else{
            if(RelationEnum.MY.getValue().equals(pageReqVO.getRelation())){
                ids.add(adminId);
            }else if(RelationEnum.SUB.getValue().equals(pageReqVO.getRelation())){
                ids = adminUserApi.getUserListBySubordinateIds(adminId);
            }
        }
        return customerMapper.selectPage(pageReqVO,ids);
    }


    @Override
    public PageResult<CrmCustomerRespVO> getCustomerPage2(CrmCustomerPageReqVO pageReqVO) {
        List<Long> ids = new ArrayList<>();
        Long adminId = SecurityFrameworkUtils.getLoginUserId();
        if(CustomerTypesEnum.OPEN.getValue().equals(pageReqVO.getType())){
            ids.add(0L);
        }else{
            if(RelationEnum.MY.getValue().equals(pageReqVO.getRelation())){
                ids.add(adminId);
            }else if(RelationEnum.SUB.getValue().equals(pageReqVO.getRelation())){
                ids = adminUserApi.getUserListBySubordinateIds(adminId);
            }else if(RelationEnum.ALL.getValue().equals(pageReqVO.getRelation())){
                //这里要还要查询团队成员数据
                ids.add(adminId);
                ids.addAll(adminUserApi.getUserListBySubordinateIds(adminId));
            }
        }
        PageResult<CrmCustomerRespVO> result = customerMapper.selectPage2(pageReqVO,ids);

        for (CrmCustomerRespVO respVO : result.getList()){
            //all-所有权限(拥有者) edit-读写权限 read-只读权限"
            String permission = "all";
            if(StrUtil.isNotEmpty(respVO.getRoUserId())){
                List<String> roUsers = StrUtil.split(respVO.getRoUserId(),",");
                if(roUsers.contains(String.valueOf(adminId))){
                    permission = "read";
                }
            }
            if(StrUtil.isNotEmpty(respVO.getRwUserId())){
                List<String> rwUsers = StrUtil.split(respVO.getRwUserId(),",");
                if(rwUsers.contains(String.valueOf(adminId))){
                    permission = "edit"; //他只是有修改与跟进、添加相关联系人权限而且 其他比如放入公海等是不可以的
                }
            }
            respVO.setPermission(permission);
            DictDataDO dictDataDO = dictDataService.getDictData("customer_source",respVO.getSource().toString());
            respVO.setSourceName(dictDataDO.getLabel());
        }

        return result;
    }

    @Override
    public List<NearbyCustomerRespVO> getNearbyCustomer(NearbyCustomerVO nearbyCustomerVO) {
        List<Long> ids = new ArrayList<>();
        Long adminId = SecurityFrameworkUtils.getLoginUserId();
        if(RelationEnum.MY.getValue().equals(nearbyCustomerVO.getRelation())){
            ids.add(adminId);
        }else if(RelationEnum.SUB.getValue().equals(nearbyCustomerVO.getRelation())){
            ids = adminUserApi.getUserListBySubordinateIds(adminId);
        }
        //根据经纬度与距离范围计算最大经纬度最小经纬度
        //距离转换成纬度的值
        Double latDegrees = doLatDegress(nearbyCustomerVO.getDistance().doubleValue());
        //距离转换成经度的值
        Double lngDegree = doLngDegress(latDegrees, nearbyCustomerVO.getLat());
        //经度最小值
        Double lonMin = nearbyCustomerVO.getLng() - lngDegree;
        //经度最大值
        Double lonMax = nearbyCustomerVO.getLng() + lngDegree;
        //纬度最小值
        Double latMin = nearbyCustomerVO.getLat() - latDegrees;
        //纬度最大值
        Double latMax = nearbyCustomerVO.getLat() + latDegrees;

        List<CrmCustomerDO> list = customerMapper.selectList( new LambdaQueryWrapper<CrmCustomerDO>()
                .in(!ids.isEmpty(),CrmCustomerDO::getOwnerUserId,ids)
                .between(CrmCustomerDO::getLng,lonMin,lonMax)
                .between(CrmCustomerDO::getLat,latMin,latMax)
                .like(StrUtil.isNotBlank(nearbyCustomerVO.getName()), CrmCustomerDO::getName,nearbyCustomerVO.getName()));
        List<NearbyCustomerRespVO> customerRespVOS = BeanUtils.toBean(list, NearbyCustomerRespVO.class);
        for (NearbyCustomerRespVO VO : customerRespVOS) {
            //计算距离
            double distance = haversineDistance(VO.getLat(), VO.getLng(), nearbyCustomerVO.getLat(), nearbyCustomerVO.getLng());
            VO.setDistance(new BigDecimal(distance).setScale(2, RoundingMode.HALF_UP).doubleValue()+"");
            //获取拥有者名称
            AdminUserDO adminUserDO = userMapper.selectById(VO.getOwnerUserId());
            if(adminUserDO != null){
                VO.setOwnUserName(adminUserDO.getNickname());
            }else{
                VO.setOwnUserName("没有拥有者,公海客户");
            }
            //获取跟进状态
            //获取字典值
            DictDataDO dictDataDO = dictDataService.getDictData("follow_status", VO.getFollowStatus().toString());
            VO.setFollowStatusStr(dictDataDO.getLabel());
        }

        return customerRespVOS;
    }

    // 计算两地之间的距离（公里）
    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        // 将经纬度转换为弧度
        double latRad1 = toRadians(lat1);
        double lonRad1 = toRadians(lon1);
        double latRad2 = toRadians(lat2);
        double lonRad2 = toRadians(lon2);

        // Haversine公式
        double dlon = lonRad2 - lonRad1;
        double dlat = latRad2 - latRad1;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(latRad1) * Math.cos(latRad2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double R = 6371; // 地球平均半径，单位为公里
        return R * c; // 返回距离，单位为公里
    }

    /**
     * @return  java.lang.Double
     * @description 距离转换成经度
     * @params [latDegrees 距离转换成纬度后的值, latitude 纬度]
     */
    private  Double doLngDegress(Double latDegrees,Double latitude) {
        double lngDegree = latDegrees / Math.cos(latitude * Math.PI / 180);
        return lngDegree;
    }


    /**
     * @return  java.lang.Double
     * @description 距离转换成纬度
     * @params [distance 距离]
     */
    private  Double doLatDegress(Double distance) {
        double latDegrees = distance/6365;
        // 转换弧度
        latDegrees = latDegrees * (180/Math.PI);
        return latDegrees;
    }


    @Override
    public CustomerImportRespVO importList(List<CrmCustomerImportVO> importUsers, String deptId, String adminIds,
                                           Integer averageType) {
        CustomerImportRespVO respVO = CustomerImportRespVO.builder().createNames(new ArrayList<>())
                .failureNames(new LinkedHashMap<>()).build();
        //处理客户归属方案
        //如果只选择了机构没有选择用户直接获取当前部门下的用户
        List<Long> userList = new  ArrayList<>();
        //是否是线索池导入
        Boolean isOpen = false;
        if(StrUtil.isBlank(deptId) && StrUtil.isBlank(adminIds) && ObjectUtil.isNull(averageType)){
            isOpen = true;
        }
        if(!isOpen){
            if(StrUtil.isNotEmpty(deptId) && StrUtil.isEmpty(adminIds)){
                Set<Long> deptIds = adminUserService.getDeptCondition(Long.valueOf(deptId));
                List<AdminUserDO> adminUserDOS =  userMapper.selectList(new LambdaQueryWrapperX<AdminUserDO>()
                        .in(AdminUserDO::getDeptId,deptIds));
                userList = adminUserDOS.stream().map(AdminUserDO::getId).collect(Collectors.toList());
                if(userList.isEmpty()){
                    throw exception(new ErrorCode(202409080,"当前部门下没有用户，清重新选择"));
                }
            }else{
                userList = StrUtil.split(adminIds,",").stream().map(Long::parseLong).toList();
            }
        }


        int i = 1;
        int j = 0;
        int adminCount = userList.size();
        for (CrmCustomerImportVO importVO : importUsers){
            if(StrUtil.isEmpty(importVO.getName())){
                respVO.getFailureNames().put("第" + i + "行:", "客户名称为空，已经被过滤掉");
                continue;
            }
            Long count = customerMapper.selectCount(new LambdaQueryWrapper<CrmCustomerDO>()
                    .eq(CrmCustomerDO::getName,importVO.getName()));
            if(count > 0){
                respVO.getFailureNames().put("第" + i + "行:", "客户名称已经存在，已经被过滤掉");
                continue;
            }
            CrmCustomerDO customer = BeanUtils.toBean(importVO, CrmCustomerDO.class);
            Long adminId = 0L;
            if(!isOpen){
                if(ShopCommonEnum.AVG_1.getValue().equals(averageType)){
                    //平均分配
                    adminId = userList.get(j);
                    if(j == (adminCount - 1)){
                        j = 0;
                    }else{
                        j++;
                    }
                }else{
                    //随机分配
                    Random random = new Random();
                    int num = random.nextInt(adminCount);
                    adminId = userList.get(num);
                }
            }

            customer.setOwnerUserId(adminId);
            customerMapper.insert(customer);
            //处理日志
            logRecordService.importCustomerLog(customer.getId(), customer.getName());
            respVO.getCreateNames().add("第" + i + "行:导入成功客户名->" + importVO.getName());
            i++;
        }
        return respVO;
    }




    private void limitCustomer(Long uid){
        Integer customerNum = RedisUtil.get("customerNum");
        if(customerNum == null || customerNum == 0){
            return;
        }
        Long count = customerMapper.selectCount(new LambdaQueryWrapper<CrmCustomerDO>()
                .eq(CrmCustomerDO::getOwnerUserId,uid));
        if(NumberUtil.compare(count,customerNum) > 0){
            throw exception(new ErrorCode(202502130,"员工id:"+uid+"超出拥有客户限制"));
        }
    }
}