package tech.piis.ex_service.job;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tech.piis.common.core.lang.UUID;
import tech.piis.common.enums.UserStatus;
import tech.piis.common.exception.BaseException;
import tech.piis.common.utils.DateUtils;
import tech.piis.common.utils.http.HttpClientUtils;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.utils.SecurityUtils;
import tech.piis.modules.common.domain.DeptExternal;
import tech.piis.modules.common.domain.DeptResponse;
import tech.piis.modules.common.domain.UserExternal;
import tech.piis.modules.common.domain.UserResponse;
import tech.piis.modules.system.domain.*;
import tech.piis.modules.system.mapper.*;

import java.util.*;

import static tech.piis.common.constant.HttpStatus.SUCCESS;
import static tech.piis.common.constant.UserConstants.*;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.ex_service.job
 * User: Tuzki
 * Date: 2020/10/8
 * Time: 16:32
 * Description:数据港同步部门、岗位、员工数据
 * <p>
 * TODO
 * 人员状态 删除？
 */
@Slf4j
@Component("SyncData")
@Transactional
public class SyncData {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysUserDeptMapper userDeptMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private SysPostHistoryMapper postHistoryMapper;

    @Value("${data.dept.url}")
    private String GET_DEPT_URL;

    @Value("${data.dept.chg_url}")
    private String GET_DEPT_CHG_URL;

    @Value("${data.user.url}")
    private String GET_USER_URL;

    @Value("${data.dept.chg_url}")
    private String GET_USER_CHG_URL;

    @Value("${data.Authorization}")
    private String Authorization;

    /**
     * 同步数据
     *
     * @return
     */
    public void sync() {
        try {
            syncDept();
            syncUser();
        } catch (Exception e) {
            log.error("#####SyncData##### e = {}", e);
        }
    }

    /**
     * 从数据港同步部门数据
     */
    public void syncDept() throws Exception {
        String deptData = "{\n" +
                "  \"code\": 200,\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1804000000\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"大数据部\",\n" +
                "      \"orgFullNm\": \"大数据部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1804000000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1805000000\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"战略规划部\",\n" +
                "      \"orgFullNm\": \"战略规划部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1805000000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1806000000\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"人力资源部\",\n" +
                "      \"orgFullNm\": \"人力资源部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1806000000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1807000000\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"综合管理部\",\n" +
                "      \"orgFullNm\": \"综合管理部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1807000000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1808000000\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"财务管理部\",\n" +
                "      \"orgFullNm\": \"财务管理部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1808000000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1809000000\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"党群工作部\",\n" +
                "      \"orgFullNm\": \"党群工作部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1809000000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1810000000\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"风险管理部\",\n" +
                "      \"orgFullNm\": \"风险管理部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1810000000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1899000000\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"外包项目组\",\n" +
                "      \"orgFullNm\": \"外包项目组\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1899000000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1800000000\",\n" +
                "      \"upGrpOrgId\": \"0000000000\",\n" +
                "      \"orgShtNm\": \"光大科技\",\n" +
                "      \"orgFullNm\": \"光大科技有限公司\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1800000001\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"光大云缴费\",\n" +
                "      \"orgFullNm\": \"光大云缴费科技有限公司\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1800000001\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1801000001\",\n" +
                "      \"upGrpOrgId\": \"1800000001\",\n" +
                "      \"orgShtNm\": \"综合管理部\",\n" +
                "      \"orgFullNm\": \"综合管理部\",\n" +
                "      \"orgStatCd\": \"2\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1800000001|1801000001\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1802000001\",\n" +
                "      \"upGrpOrgId\": \"1800000001\",\n" +
                "      \"orgShtNm\": \"市场拓展部\",\n" +
                "      \"orgFullNm\": \"市场拓展部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1800000001|1802000001\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1803000001\",\n" +
                "      \"upGrpOrgId\": \"1800000001\",\n" +
                "      \"orgShtNm\": \"投资及机构管理部\",\n" +
                "      \"orgFullNm\": \"投资及机构管理部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1800000001|1803000001\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1804000001\",\n" +
                "      \"upGrpOrgId\": \"1800000001\",\n" +
                "      \"orgShtNm\": \"产品与平台支持部\",\n" +
                "      \"orgFullNm\": \"产品与平台支持部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1800000001|1804000001\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1801000000\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"总经理室\",\n" +
                "      \"orgFullNm\": \"总经理室\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1801000000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1802000000\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"产品研发部\",\n" +
                "      \"orgFullNm\": \"产品研发部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1802000000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1803000000\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"智能云计算部\",\n" +
                "      \"orgFullNm\": \"智能云计算部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1803000000\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";
        String syncDeptUrl;
        if (!GET_DEPT_CHG_URL.equals("NULL")) {
            syncDeptUrl = GET_DEPT_CHG_URL + DateUtils.dateTimeNow("yyyyMMdd");
        } else {
            syncDeptUrl = GET_DEPT_URL;
        }
        log.info("================================同步部门数据START================================ get dept url = {}", syncDeptUrl);
        DeptResponse deptResponse = JSONObject.parseObject(HttpClientUtils.doGet(GET_DEPT_URL, Authorization), DeptResponse.class);
//        DeptResponse deptResponse = JSONObject.parseObject(deptData, DeptResponse.class);
        if (null != deptResponse) {
            if (deptResponse.getCode() == SUCCESS) {
                syncDept(deptResponse.getData());
            } else {
                log.error("同步部门数据失败！ code = {}, msg = {}", deptResponse.getCode(), deptResponse.getMessage());
                throw new BaseException("同步部门数据失败！code = " + deptResponse.getCode() + " msg = " + deptResponse.getMessage());
            }
        } else {
            log.error("同步部门数据失败！ deptResponse is null");
            throw new BaseException("同步部门数据失败！ deptResponse is null");
        }
        log.info("================================同步部门数据END================================");
    }


    /**
     * 从数据港同步用户数据
     */
    public void syncUser() throws Exception {
        String userData = "{\"code\":200,\"msg\":\"\",\"data\":[{\"grpEmpUnifId\":\"wulj\",\"empNm\":\"吴**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"0100100000\",\"dutyStatCd\":\"1\",\"empEml\":\"wul****************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"01006991\",\"empNm\":\"李*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0120000000\",\"dutyStatCd\":\"1\",\"empEml\":\"lif*****************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"01009959\",\"empNm\":\"黄**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"138********\",\"belgGrpDeptId\":\"0120000000\",\"dutyStatCd\":\"1\",\"empEml\":\"hua*******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"01008976\",\"empNm\":\"向**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0120000000\",\"dutyStatCd\":\"1\",\"empEml\":\"xia*******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"01009970\",\"empNm\":\"项**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0120000000\",\"dutyStatCd\":\"1\",\"empEml\":\"xia*******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08860059\",\"empNm\":\"蓝**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"136********\",\"belgGrpDeptId\":\"0811073907\",\"dutyStatCd\":\"1\",\"empEml\":null,\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08860060\",\"empNm\":\"陈**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"136********\",\"belgGrpDeptId\":\"0811073907\",\"dutyStatCd\":\"1\",\"empEml\":null,\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"01009333\",\"empNm\":\"廖**\",\"genderCd\":\"1\",\"workTel\":\"289*****\",\"moblNum\":\"693*****\",\"belgGrpDeptId\":\"0810000005\",\"dutyStatCd\":\"1\",\"empEml\":\"lia******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08000096\",\"empNm\":\"张**\",\"genderCd\":\"1\",\"workTel\":\"286*****\",\"moblNum\":\"977*****\",\"belgGrpDeptId\":\"0810000005\",\"dutyStatCd\":\"1\",\"empEml\":\"zha*******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08000111\",\"empNm\":\"张*\",\"genderCd\":\"1\",\"workTel\":\"286*****\",\"moblNum\":\"901*****\",\"belgGrpDeptId\":\"0810000005\",\"dutyStatCd\":\"1\",\"empEml\":\"zha******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08000132\",\"empNm\":\"李**\",\"genderCd\":\"1\",\"workTel\":\"282*****\",\"moblNum\":null,\"belgGrpDeptId\":\"0810000005\",\"dutyStatCd\":\"1\",\"empEml\":\"lir****************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08000055\",\"empNm\":\"陈**\",\"genderCd\":\"2\",\"workTel\":\"286*****\",\"moblNum\":null,\"belgGrpDeptId\":\"0810000005\",\"dutyStatCd\":\"1\",\"empEml\":\"che******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08000062\",\"empNm\":\"叶**\",\"genderCd\":\"2\",\"workTel\":\"286*****\",\"moblNum\":null,\"belgGrpDeptId\":\"0810000005\",\"dutyStatCd\":\"1\",\"empEml\":\"yex****************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08000058\",\"empNm\":\"萧**\",\"genderCd\":\"2\",\"workTel\":\"286*****\",\"moblNum\":null,\"belgGrpDeptId\":\"0810000005\",\"dutyStatCd\":\"1\",\"empEml\":\"xia******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08000125\",\"empNm\":\"黄**\",\"genderCd\":\"1\",\"workTel\":\"286*****\",\"moblNum\":null,\"belgGrpDeptId\":\"0810000006\",\"dutyStatCd\":\"1\",\"empEml\":\"hua*******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08001163\",\"empNm\":\"汪**\",\"genderCd\":\"2\",\"workTel\":\"282*****\",\"moblNum\":\"611*****\",\"belgGrpDeptId\":\"0810000006\",\"dutyStatCd\":\"1\",\"empEml\":\"wan******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"10012736\",\"empNm\":\"续**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"138********\",\"belgGrpDeptId\":\"1000000001\",\"dutyStatCd\":\"1\",\"empEml\":\"无\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"10012836\",\"empNm\":\"姜**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"137********\",\"belgGrpDeptId\":\"1000000001\",\"dutyStatCd\":\"1\",\"empEml\":\"无\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"10012676\",\"empNm\":\"赵**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"1000000001\",\"dutyStatCd\":\"1\",\"empEml\":\"无\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"10012986\",\"empNm\":\"刘**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"1000000001\",\"dutyStatCd\":\"1\",\"empEml\":\"950************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"10022968\",\"empNm\":\"谢*\",\"genderCd\":null,\"workTel\":null,\"moblNum\":\"138********\",\"belgGrpDeptId\":\"0119900000\",\"dutyStatCd\":\"1\",\"empEml\":\"cyi***********\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"10022916\",\"empNm\":\"王*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"152********\",\"belgGrpDeptId\":\"1000000002\",\"dutyStatCd\":\"1\",\"empEml\":\"无\",\"currPostn\":\"测试岗位\"},{\"grpEmpUnifId\":\"10032876\",\"empNm\":\"刘**\",\"genderCd\":null,\"workTel\":null,\"moblNum\":\"136********\",\"belgGrpDeptId\":\"0119900000\",\"dutyStatCd\":\"1\",\"empEml\":\"lgh*************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"10032926\",\"empNm\":\"杨*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"1000000003\",\"dutyStatCd\":\"1\",\"empEml\":\"yan******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"10058568\",\"empNm\":\"张**\",\"genderCd\":null,\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0119900000\",\"dutyStatCd\":\"1\",\"empEml\":\"zha**********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"10052708\",\"empNm\":\"李**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"136********\",\"belgGrpDeptId\":\"1000000004\",\"dutyStatCd\":\"1\",\"empEml\":\"136****************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"10050302\",\"empNm\":\"许*\",\"genderCd\":null,\"workTel\":null,\"moblNum\":\"135********\",\"belgGrpDeptId\":\"0119900000\",\"dutyStatCd\":\"1\",\"empEml\":\"295*************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"10051359\",\"empNm\":\"杨**\",\"genderCd\":null,\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"0119900000\",\"dutyStatCd\":\"1\",\"empEml\":\"517************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08100911\",\"empNm\":\"殷**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"0800001001\",\"dutyStatCd\":\"1\",\"empEml\":\"lia************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"01008955\",\"empNm\":\"蔡**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"135********\",\"belgGrpDeptId\":\"0115000000\",\"dutyStatCd\":\"1\",\"empEml\":\"cai*****************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101631\",\"empNm\":\"赵**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"0800001001\",\"dutyStatCd\":\"1\",\"empEml\":\"zeh**********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101726\",\"empNm\":\"王*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"0800001001\",\"dutyStatCd\":\"1\",\"empEml\":\"Ou.*******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08100688\",\"empNm\":\"潘*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"0800001002\",\"dutyStatCd\":\"1\",\"empEml\":\"hon********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08100855\",\"empNm\":\"季**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"136********\",\"belgGrpDeptId\":\"0800001002\",\"dutyStatCd\":\"1\",\"empEml\":\"lin*******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101378\",\"empNm\":\"崔*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"136********\",\"belgGrpDeptId\":\"0800001002\",\"dutyStatCd\":\"1\",\"empEml\":\"Dan*******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101614\",\"empNm\":\"顾*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"199********\",\"belgGrpDeptId\":\"0800001002\",\"dutyStatCd\":\"1\",\"empEml\":\"896*************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101670\",\"empNm\":\"桂**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"136********\",\"belgGrpDeptId\":\"0800001002\",\"dutyStatCd\":\"1\",\"empEml\":\"tut***************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08100850\",\"empNm\":\"刘**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"135********\",\"belgGrpDeptId\":\"0800001002\",\"dutyStatCd\":\"1\",\"empEml\":\"wei**********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101661\",\"empNm\":\"徐*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"0800001002\",\"dutyStatCd\":\"1\",\"empEml\":\"Jia******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101676\",\"empNm\":\"刘*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"137********\",\"belgGrpDeptId\":\"0800001003\",\"dutyStatCd\":\"1\",\"empEml\":\"pen********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101658\",\"empNm\":\"蒋*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"189********\",\"belgGrpDeptId\":\"0800001003\",\"dutyStatCd\":\"1\",\"empEml\":\"Jin**********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08100750\",\"empNm\":\"徐**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"136********\",\"belgGrpDeptId\":\"0800001003\",\"dutyStatCd\":\"1\",\"empEml\":\"sha*********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"02715250\",\"empNm\":\"刘**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"159********\",\"belgGrpDeptId\":\"0800001003\",\"dutyStatCd\":\"1\",\"empEml\":\"Jih*********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101228\",\"empNm\":\"李*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"136********\",\"belgGrpDeptId\":\"0800001003\",\"dutyStatCd\":\"1\",\"empEml\":\"nan******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101537\",\"empNm\":\"林**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"150********\",\"belgGrpDeptId\":\"0800001003\",\"dutyStatCd\":\"1\",\"empEml\":\"Sip*********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101731\",\"empNm\":\"刘*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0800001003\",\"dutyStatCd\":\"1\",\"empEml\":\"Men********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101056\",\"empNm\":\"郑**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"136********\",\"belgGrpDeptId\":\"0800001006\",\"dutyStatCd\":\"1\",\"empEml\":\"pen*************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101203\",\"empNm\":\"刘*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"183********\",\"belgGrpDeptId\":\"0800001006\",\"dutyStatCd\":\"1\",\"empEml\":\"Yi.******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101695\",\"empNm\":\"王**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"138********\",\"belgGrpDeptId\":\"0800001006\",\"dutyStatCd\":\"1\",\"empEml\":\"Ziz************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101170\",\"empNm\":\"杨**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"137********\",\"belgGrpDeptId\":\"0800001006\",\"dutyStatCd\":\"1\",\"empEml\":\"Dai************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08100759\",\"empNm\":\"陈*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"138********\",\"belgGrpDeptId\":\"0800001006\",\"dutyStatCd\":\"1\",\"empEml\":\"bin********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101464\",\"empNm\":\"毕**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"135********\",\"belgGrpDeptId\":\"0800001006\",\"dutyStatCd\":\"1\",\"empEml\":\"553************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101692\",\"empNm\":\"石**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"137********\",\"belgGrpDeptId\":\"0800001006\",\"dutyStatCd\":\"1\",\"empEml\":\"115**************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101499\",\"empNm\":\"傅*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0800001007\",\"dutyStatCd\":\"1\",\"empEml\":\"Fen*******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101532\",\"empNm\":\"于*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"138********\",\"belgGrpDeptId\":\"0800001007\",\"dutyStatCd\":\"1\",\"empEml\":\"Wei******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101597\",\"empNm\":\"邹*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"0800001007\",\"dutyStatCd\":\"1\",\"empEml\":\"Tin********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101616\",\"empNm\":\"卢**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"185********\",\"belgGrpDeptId\":\"0800001007\",\"dutyStatCd\":\"1\",\"empEml\":\"Xun*********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101188\",\"empNm\":\"丛*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"0800001007\",\"dutyStatCd\":\"1\",\"empEml\":\"Yu.*******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101341\",\"empNm\":\"金*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"158********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"zhe*********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101484\",\"empNm\":\"施*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"136********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"che********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101594\",\"empNm\":\"胡**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"156********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"yin***********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101432\",\"empNm\":\"田*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"151********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"qin*********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101433\",\"empNm\":\"郭**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"zhe**********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101343\",\"empNm\":\"张**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"134********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"tin**************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101368\",\"empNm\":\"景**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"134********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"xue************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101340\",\"empNm\":\"艾*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"138********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"yu.*****************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101556\",\"empNm\":\"B*******************\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"138********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"Bry******************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101430\",\"empNm\":\"张*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"lei*********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101434\",\"empNm\":\"孙*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"yun*******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101589\",\"empNm\":\"刘*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"shu********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101590\",\"empNm\":\"周**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"shu*****************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101342\",\"empNm\":\"安*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"185********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"ren******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101396\",\"empNm\":\"吕**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"zha*********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101568\",\"empNm\":\"马**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"136********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"xia*********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101584\",\"empNm\":\"唐*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"159********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"zhi********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101663\",\"empNm\":\"黄*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"134********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"rui*********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101500\",\"empNm\":\"邓*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"138********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"rui********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101501\",\"empNm\":\"郑*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"156********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"wei*********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101505\",\"empNm\":\"郑*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"189********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"xi.********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101567\",\"empNm\":\"邹*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"158********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"ya.******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101581\",\"empNm\":\"周**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"187********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"she**************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101706\",\"empNm\":\"赵*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"158********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"Xue********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101601\",\"empNm\":\"贾*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"185********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"ke.******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101659\",\"empNm\":\"刘**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"188********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"hon*************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101668\",\"empNm\":\"傅**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"183********\",\"belgGrpDeptId\":\"0800001008\",\"dutyStatCd\":\"1\",\"empEml\":\"han********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101516\",\"empNm\":\"唐**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"138********\",\"belgGrpDeptId\":\"0800001009\",\"dutyStatCd\":\"1\",\"empEml\":\"Jia**********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101536\",\"empNm\":\"王**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"135********\",\"belgGrpDeptId\":\"0800001009\",\"dutyStatCd\":\"1\",\"empEml\":\"yiz**********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101513\",\"empNm\":\"范**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"138********\",\"belgGrpDeptId\":\"0800001009\",\"dutyStatCd\":\"1\",\"empEml\":\"wen**********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101557\",\"empNm\":\"张**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"134********\",\"belgGrpDeptId\":\"0800001009\",\"dutyStatCd\":\"1\",\"empEml\":\"nia**************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101618\",\"empNm\":\"张**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0800001009\",\"dutyStatCd\":\"1\",\"empEml\":\"kex***********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101514\",\"empNm\":\"张*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"185********\",\"belgGrpDeptId\":\"0800001009\",\"dutyStatCd\":\"1\",\"empEml\":\"qi.********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101613\",\"empNm\":\"侯*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"182********\",\"belgGrpDeptId\":\"0800001009\",\"dutyStatCd\":\"1\",\"empEml\":\"che*********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101680\",\"empNm\":\"毛*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"132********\",\"belgGrpDeptId\":\"0800001009\",\"dutyStatCd\":\"1\",\"empEml\":\"Zhe*******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101734\",\"empNm\":\"魏**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"138********\",\"belgGrpDeptId\":\"0800001009\",\"dutyStatCd\":\"1\",\"empEml\":\"Zho************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101534\",\"empNm\":\"郭*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"136********\",\"belgGrpDeptId\":\"0800001009\",\"dutyStatCd\":\"1\",\"empEml\":\"xue*******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101410\",\"empNm\":\"李*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0800001010\",\"dutyStatCd\":\"1\",\"empEml\":\"yua*******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101691\",\"empNm\":\"李**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"151********\",\"belgGrpDeptId\":\"0800001011\",\"dutyStatCd\":\"1\",\"empEml\":\"Hen*********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101729\",\"empNm\":\"岳**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"0800001011\",\"dutyStatCd\":\"1\",\"empEml\":\"Cai***********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101719\",\"empNm\":\"李*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0800001011\",\"dutyStatCd\":\"1\",\"empEml\":\"Bin******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101702\",\"empNm\":\"寿*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"0800001011\",\"dutyStatCd\":\"1\",\"empEml\":\"Shi********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101711\",\"empNm\":\"吴**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"861**********\",\"belgGrpDeptId\":\"0800001012\",\"dutyStatCd\":\"1\",\"empEml\":\"Jun**********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101730\",\"empNm\":\"杨**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"0800001012\",\"dutyStatCd\":\"1\",\"empEml\":\"Hai************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101491\",\"empNm\":\"贾*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"189********\",\"belgGrpDeptId\":\"0800001013\",\"dutyStatCd\":\"1\",\"empEml\":\"Nin********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101357\",\"empNm\":\"桂**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"137********\",\"belgGrpDeptId\":\"0800001013\",\"dutyStatCd\":\"1\",\"empEml\":\"Cha***************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101585\",\"empNm\":\"田*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"152********\",\"belgGrpDeptId\":\"0800001013\",\"dutyStatCd\":\"1\",\"empEml\":\"Men*********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101509\",\"empNm\":\"王**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"133********\",\"belgGrpDeptId\":\"0800001014\",\"dutyStatCd\":\"1\",\"empEml\":\"yuq***********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101083\",\"empNm\":\"段**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"138********\",\"belgGrpDeptId\":\"0800001014\",\"dutyStatCd\":\"1\",\"empEml\":\"Jin*************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101511\",\"empNm\":\"马**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"138********\",\"belgGrpDeptId\":\"0800001014\",\"dutyStatCd\":\"1\",\"empEml\":\"jin**********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101573\",\"empNm\":\"陈**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"158********\",\"belgGrpDeptId\":\"0800001015\",\"dutyStatCd\":\"1\",\"empEml\":\"Wei************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08100925\",\"empNm\":\"张**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"133********\",\"belgGrpDeptId\":\"0800001015\",\"dutyStatCd\":\"1\",\"empEml\":\"jin*************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101508\",\"empNm\":\"张*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"136********\",\"belgGrpDeptId\":\"0800001015\",\"dutyStatCd\":\"1\",\"empEml\":\"Che***********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101525\",\"empNm\":\"刘**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"135********\",\"belgGrpDeptId\":\"0800001015\",\"dutyStatCd\":\"1\",\"empEml\":\"Yix*********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101641\",\"empNm\":\"庞*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"138********\",\"belgGrpDeptId\":\"0800001015\",\"dutyStatCd\":\"1\",\"empEml\":\"Sui********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101645\",\"empNm\":\"张*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0800001015\",\"dutyStatCd\":\"1\",\"empEml\":\"Qun*********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101564\",\"empNm\":\"汪*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"182********\",\"belgGrpDeptId\":\"0800001015\",\"dutyStatCd\":\"1\",\"empEml\":\"Pei********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101615\",\"empNm\":\"郭**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"137********\",\"belgGrpDeptId\":\"0800001015\",\"dutyStatCd\":\"1\",\"empEml\":\"Wei*********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101541\",\"empNm\":\"黄**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"155********\",\"belgGrpDeptId\":\"0800001015\",\"dutyStatCd\":\"1\",\"empEml\":\"Zhe************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101642\",\"empNm\":\"张**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"177********\",\"belgGrpDeptId\":\"0800001015\",\"dutyStatCd\":\"1\",\"empEml\":\"Lul************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08100454\",\"empNm\":\"马*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"0800001016\",\"dutyStatCd\":\"1\",\"empEml\":\"wei******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08100752\",\"empNm\":\"李**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"0800001016\",\"dutyStatCd\":\"1\",\"empEml\":\"jin**********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08100912\",\"empNm\":\"孙**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"0800001016\",\"dutyStatCd\":\"1\",\"empEml\":\"lin**********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101031\",\"empNm\":\"王*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"138********\",\"belgGrpDeptId\":\"0800001016\",\"dutyStatCd\":\"1\",\"empEml\":\"le.*******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08100829\",\"empNm\":\"何**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0800001016\",\"dutyStatCd\":\"1\",\"empEml\":\"jia**********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101309\",\"empNm\":\"李**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"151********\",\"belgGrpDeptId\":\"0800001016\",\"dutyStatCd\":\"1\",\"empEml\":\"zib*******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101327\",\"empNm\":\"栗**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"189********\",\"belgGrpDeptId\":\"0800001016\",\"dutyStatCd\":\"1\",\"empEml\":\"xue********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101220\",\"empNm\":\"赵**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0800001016\",\"dutyStatCd\":\"1\",\"empEml\":\"Hai***********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101334\",\"empNm\":\"宋*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"138********\",\"belgGrpDeptId\":\"0800001016\",\"dutyStatCd\":\"1\",\"empEml\":\"fan*********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101482\",\"empNm\":\"张**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"185********\",\"belgGrpDeptId\":\"0800001016\",\"dutyStatCd\":\"1\",\"empEml\":\"zha**************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101539\",\"empNm\":\"郭**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"0800001016\",\"dutyStatCd\":\"1\",\"empEml\":\"mei***********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101679\",\"empNm\":\"赵**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"159********\",\"belgGrpDeptId\":\"0800001016\",\"dutyStatCd\":\"1\",\"empEml\":\"Xue********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08100766\",\"empNm\":\"李*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0800001016\",\"dutyStatCd\":\"1\",\"empEml\":\"zhe******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101598\",\"empNm\":\"唐**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"151********\",\"belgGrpDeptId\":\"0800001017\",\"dutyStatCd\":\"1\",\"empEml\":\"Jie************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101436\",\"empNm\":\"张**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"134********\",\"belgGrpDeptId\":\"0800001017\",\"dutyStatCd\":\"1\",\"empEml\":\"xia************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101673\",\"empNm\":\"孙**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"0800001017\",\"dutyStatCd\":\"1\",\"empEml\":\"Qin***********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101599\",\"empNm\":\"吴**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"132********\",\"belgGrpDeptId\":\"0800001018\",\"dutyStatCd\":\"1\",\"empEml\":\"fra*************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101450\",\"empNm\":\"吴**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"0800001019\",\"dutyStatCd\":\"1\",\"empEml\":\"May******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101544\",\"empNm\":\"曾*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0800001020\",\"dutyStatCd\":\"1\",\"empEml\":\"Yan*********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101624\",\"empNm\":\"李**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"177********\",\"belgGrpDeptId\":\"0800001020\",\"dutyStatCd\":\"1\",\"empEml\":\"Xin*********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101437\",\"empNm\":\"李**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"138********\",\"belgGrpDeptId\":\"0800001022\",\"dutyStatCd\":\"1\",\"empEml\":\"Zhi**********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101686\",\"empNm\":\"姜**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0800001022\",\"dutyStatCd\":\"1\",\"empEml\":\"tia***************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101632\",\"empNm\":\"李*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"lij***********\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101268\",\"empNm\":\"朱*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"zhu**************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101391\",\"empNm\":\"郑*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"138********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"zhe******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101289\",\"empNm\":\"张**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"138********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"zha***************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101627\",\"empNm\":\"马**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"mad************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101275\",\"empNm\":\"梁**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"135********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"lia***************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101313\",\"empNm\":\"杨*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"135********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"yan****************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101363\",\"empNm\":\"单**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"159********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"sha**************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101364\",\"empNm\":\"王**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"151********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"wan**************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101374\",\"empNm\":\"徐**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"xul************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101385\",\"empNm\":\"邓**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"132********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"den**************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101389\",\"empNm\":\"刘**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"159********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"liu*************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101390\",\"empNm\":\"魏*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"138********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"wei**************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101407\",\"empNm\":\"隗**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"150********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"wei*************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101503\",\"empNm\":\"刘*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"135********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"liu***************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101582\",\"empNm\":\"姚**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"137********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"yao*************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101669\",\"empNm\":\"冉**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"138********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"ran*************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101672\",\"empNm\":\"吴*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"wub************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101291\",\"empNm\":\"张*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"zha****************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101292\",\"empNm\":\"杨*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"yan****************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101294\",\"empNm\":\"郭**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"152********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"guo*************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101321\",\"empNm\":\"邓**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"den**************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101347\",\"empNm\":\"赵*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"150********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"zha****************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101352\",\"empNm\":\"于*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"150********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"yuy*************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101372\",\"empNm\":\"赵**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"180********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"zha**************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101617\",\"empNm\":\"冯**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"188********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"fen**************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101635\",\"empNm\":\"郝**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"187********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"hao*************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101288\",\"empNm\":\"孟*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"177********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"men***************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101354\",\"empNm\":\"呙*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"151********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"guo***************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101388\",\"empNm\":\"杜**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"187********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"duc****************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101504\",\"empNm\":\"刘*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"152********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"liu**************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101690\",\"empNm\":\"杨*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"185********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"yan***************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101654\",\"empNm\":\"熊*\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"153********\",\"belgGrpDeptId\":\"0800001023\",\"dutyStatCd\":\"1\",\"empEml\":\"xio***************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101565\",\"empNm\":\"赵**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"180********\",\"belgGrpDeptId\":\"0800001025\",\"dutyStatCd\":\"1\",\"empEml\":\"Hui************************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101143\",\"empNm\":\"温**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0800001025\",\"dutyStatCd\":\"1\",\"empEml\":\"Lin********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08100686\",\"empNm\":\"李**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"150********\",\"belgGrpDeptId\":\"0800001025\",\"dutyStatCd\":\"1\",\"empEml\":\"lon**********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101052\",\"empNm\":\"黄**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"137********\",\"belgGrpDeptId\":\"0800001025\",\"dutyStatCd\":\"1\",\"empEml\":\"sui************************\",\"currPostn\":null},{\"grpEmpUnifId\":\"08100883\",\"empNm\":\"陈*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"139********\",\"belgGrpDeptId\":\"0800001025\",\"dutyStatCd\":\"1\",\"empEml\":\"yan********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101242\",\"empNm\":\"曹*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"0800001025\",\"dutyStatCd\":\"1\",\"empEml\":\"Yu.******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08100691\",\"empNm\":\"黄*\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"130********\",\"belgGrpDeptId\":\"0800001025\",\"dutyStatCd\":\"1\",\"empEml\":\"can*********************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"08101460\",\"empNm\":\"陈**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"181********\",\"belgGrpDeptId\":\"0800001025\",\"dutyStatCd\":\"1\",\"empEml\":\"wen***********************\",\"currPostn\":\"测试岗位22\"},{\"grpEmpUnifId\":\"01008976\",\"empNm\":\"向**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"1800000000\",\"dutyStatCd\":\"1\",\"empEml\":\"xia*******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"01009959\",\"empNm\":\"黄**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"138********\",\"belgGrpDeptId\":\"0120000000\",\"dutyStatCd\":\"1\",\"empEml\":\"hua*******************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"02756746\",\"empNm\":\"吴**\",\"genderCd\":\"2\",\"workTel\":null,\"moblNum\":\"135********\",\"belgGrpDeptId\":\"0120000000\",\"dutyStatCd\":\"1\",\"empEml\":\"wub****************\",\"currPostn\":\"***\"},{\"grpEmpUnifId\":\"18000591\",\"empNm\":\"谷**\",\"genderCd\":\"1\",\"workTel\":null,\"moblNum\":\"186********\",\"belgGrpDeptId\":\"1812000000\",\"dutyStatCd\":\"1\",\"empEml\":null,\"currPostn\":\"***\"}]}";
        String userData1 = "{\n" +
                "  \"code\": 200,\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"grpEmpUnifId\": \"18000110\",\n" +
                "      \"empNm\": \"吴**\",\n" +
                "      \"genderCd\": \"1\",\n" +
                "      \"workTel\": null,\n" +
                "      \"moblNum\": \"***\",\n" +
                "      \"belgGrpDeptId\": \"1806000000\",\n" +
                "      \"dutyStatCd\": \"1\",\n" +
                "      \"empEml\": \"***\",\n" +
                "      \"currPostn\": \"员工\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpEmpUnifId\": \"18000167\",\n" +
                "      \"empNm\": \"王**\",\n" +
                "      \"genderCd\": \"2\",\n" +
                "      \"workTel\": null,\n" +
                "      \"moblNum\": \"***\",\n" +
                "      \"belgGrpDeptId\": \"1806000000\",\n" +
                "      \"dutyStatCd\": \"1\",\n" +
                "      \"empEml\": \"***\",\n" +
                "      \"currPostn\": \"员工\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpEmpUnifId\": \"18000181\",\n" +
                "      \"empNm\": \"周**\",\n" +
                "      \"genderCd\": \"2\",\n" +
                "      \"workTel\": null,\n" +
                "      \"moblNum\": \"***\",\n" +
                "      \"belgGrpDeptId\": \"1806000000\",\n" +
                "      \"dutyStatCd\": \"1\",\n" +
                "      \"empEml\": \"***\",\n" +
                "      \"currPostn\": \"经理\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpEmpUnifId\": \"18000103\",\n" +
                "      \"empNm\": \"赵**\",\n" +
                "      \"genderCd\": \"2\",\n" +
                "      \"workTel\": null,\n" +
                "      \"moblNum\": \"***\",\n" +
                "      \"belgGrpDeptId\": \"1806000000\",\n" +
                "      \"dutyStatCd\": \"1\",\n" +
                "      \"empEml\": \"***\",\n" +
                "      \"currPostn\": \"部门总经理\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpEmpUnifId\": \"18000364\",\n" +
                "      \"empNm\": \"陈*\",\n" +
                "      \"genderCd\": \"2\",\n" +
                "      \"workTel\": null,\n" +
                "      \"moblNum\": \"***\",\n" +
                "      \"belgGrpDeptId\": \"1806000000\",\n" +
                "      \"dutyStatCd\": \"1\",\n" +
                "      \"empEml\": \"***\",\n" +
                "      \"currPostn\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpEmpUnifId\": \"18000252\",\n" +
                "      \"empNm\": \"李*\",\n" +
                "      \"genderCd\": \"1\",\n" +
                "      \"workTel\": null,\n" +
                "      \"moblNum\": \"***\",\n" +
                "      \"belgGrpDeptId\": \"1806000000\",\n" +
                "      \"dutyStatCd\": \"1\",\n" +
                "      \"empEml\": \"***\",\n" +
                "      \"currPostn\": \"经理\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";


        String syncUserUrl;
        if (!GET_USER_CHG_URL.equals("NULL")) {
            syncUserUrl = GET_USER_CHG_URL + DateUtils.dateTimeNow("yyyyMMdd");
        } else {
            syncUserUrl = GET_USER_URL;
        }
        log.info("================================同步用户数据START================================ ger user url = {}", syncUserUrl);
        UserResponse userResponse = JSONObject.parseObject(HttpClientUtils.doGet(GET_USER_URL, Authorization), UserResponse.class);
//        UserResponse userResponse = JSONObject.parseObject(userData, UserResponse.class);
        if (null != userResponse) {
            if (userResponse.getCode() == SUCCESS) {
                syncUser(userResponse.getData());
            } else {
                log.error("同步用户数据失败！ code = {}, msg = {}", userResponse.getCode(), userResponse.getMessage());
                throw new BaseException("同步用户数据失败！code = " + userResponse.getCode() + " msg = " + userResponse.getMessage());
            }
        } else {
            log.error("同步用户数据失败！ userResponse is null!");
            throw new BaseException("同步用户数据失败！ userResponse is null!");
        }
        log.info("================================同步用户数据END================================");
    }

    /**
     * 同步用户数据
     *
     * @param userExternalList
     */
    public void syncUser(List<UserExternal> userExternalList) {
        /** 示例数据
         * {
         *       "grpEmpUnifId": "18000110",
         *       "empNm": "吴**",
         *       "genderCd": "1",
         *       "workTel": null,
         *       "moblNum": "***",
         *       "belgGrpDeptId": "1806000000",
         *       "dutyStatCd": "1",
         *       "empEml": "***",
         *       "currPostn": "员工"
         *     }
         */
        if (!CollectionUtils.isEmpty(userExternalList)) {
            //用户表
            List<SysUser> users = new ArrayList<>(userExternalList.size());
            //用户-部门关系表
            List<SysUserDept> userDepts = new ArrayList<>(userExternalList.size());
            //岗位表
            List<SysPost> posts = new ArrayList<>(userExternalList.size());
            //用户-岗位关系表
            List<SysUserPost> userPosts = new ArrayList<>(userExternalList.size());
            //历史岗位表
            List<SysPostHistory> postHistories = new ArrayList<>(userExternalList.size());

            //存放临时岗位名称
            Set<String> tempPosts = new HashSet<>();

            //删除用户列表
            Map<String, List<SysUser>> delUserMap = new HashMap<>();
            userExternalList.forEach(userExternal -> {
                SysUser user = new SysUser();
                String userId = userExternal.getGrpEmpUnifId();
                user.setUserId(userId);
                user.setUserName(userExternal.getEmpNm());
                user.setEmail(userExternal.getEmpEml());
                user.setPhonenumber(userExternal.getMoblNum());
                user.setPassword(SecurityUtils.encryptPassword(user.getUserId()));
                String gender = userExternal.getGenderCd();

                /**
                 * 数据港性别代码
                 * 未说明	0
                 * 男	1
                 * 女	2
                 * 未知	3
                 */
                if (!StringUtils.isEmpty(gender)) {

                    switch (gender) {
                        case "1":
                            gender = SEX_MAN;
                            break;
                        case "2":
                            gender = SEX_WOMAN;
                            break;
                        default:
                            gender = SEX_UNKNOWN;
                            break;
                    }
                    user.setSex(gender);
                }

                /**
                 * 数据港人员在职状态代码
                 * 主动离职	0
                 * 在职	1
                 * 退休	2
                 * 系统内调动	3  -> 虚拟节点
                 * 死亡	4
                 * 辞退	5
                 * 其它	6
                 * 兼职	7
                 * 借调	8
                 */
                String status = userExternal.getDutyStatCd();
                String delFlag;
                if (status.equals("1") || status.equals("3") || status.equals("7") || status.equals("8")) {
                    status = UserStatus.OK.getCode();
                    delFlag = UserStatus.OK.getCode();
                } else {
                    status = UserStatus.DISABLE.getCode();
                    delFlag = UserStatus.DELETED.getCode();
                }

                user.setStatus(status);
                user.setDelFlag(delFlag);
                BizUtils.setCreatedTimeOperation(SysUser.class, user);
                BizUtils.setUpdatedTimeOperation(SysUser.class, user);

                if (UserStatus.DELETED.getCode().equals(delFlag)) {
                    if (delUserMap.containsKey(userId)) {
                        delUserMap.get(userId).add(user);
                    } else {
                        List<SysUser> temp = new ArrayList<>();
                        temp.add(user);
                        delUserMap.put(userId, temp);
                    }
                }

                SysUserDept userDept = new SysUserDept();
                userDept.setUserId(userId);
                userDept.setDeptId(userExternal.getBelgGrpDeptId());
                BizUtils.setCreatedTimeOperation(SysUserDept.class, userDept);
                BizUtils.setUpdatedTimeOperation(SysUserDept.class, userDept);

                SysPost post = new SysPost();
                SysUserPost userPost = new SysUserPost();
                String currPostn = userExternal.getCurrPostn();
                //判断数据库中是否存在当前岗位，null不存在
                SysPost temp = postMapper.checkPostNameUnique(currPostn);
                String postId;
                if (null != temp) {
                    postId = temp.getPostId();
                } else {
                    postId = UUID.fastUUID().toString().substring(6) + System.currentTimeMillis();
                }

                if (null == temp && !tempPosts.contains(currPostn)) {
                    //防止脏数据
                    if (!StringUtils.isEmpty(currPostn)) {
                        log.info("##同步数据### userId = {}, currPost = {}", userId, currPostn);
                        post.setPostId(postId);
                        post.setPostName(currPostn);
                        post.setStatus(UserStatus.OK.getCode());
                        BizUtils.setCreatedTimeOperation(SysPost.class, post);
                        BizUtils.setUpdatedTimeOperation(SysPost.class, post);
                        posts.add(post);
                    }
                }
                tempPosts.add(currPostn);
                BizUtils.setCreatedTimeOperation(SysUserPost.class, userPost);
                BizUtils.setUpdatedTimeOperation(SysUserPost.class, userPost);

                SysPostHistory postHistory = new SysPostHistory();
                postHistory.setUserId(userId)
                        .setPostName(currPostn)
                        .setCreatedTime(DateUtils.getNowDate());
                userPost.setUserId(userId);
                userPost.setPostId(postId);
                if (!StringUtils.isEmpty(currPostn)) {
                    userPosts.add(userPost);
                }

                QueryWrapper<SysPostHistory> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id", userId);
                queryWrapper.eq("post_name", currPostn);
                if (CollectionUtils.isEmpty(postHistoryMapper.selectList(queryWrapper))) {
                    if (!StringUtils.isEmpty(currPostn)) {
                        postHistories.add(postHistory);
                    }
                }
                users.add(user);
                userDepts.add(userDept);
            });

            Iterator<Map.Entry<String, List<SysUser>>> it = delUserMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, List<SysUser>> entry = it.next();
                List<SysUser> value = entry.getValue();
                value.forEach(var -> {
                    if (!UserStatus.DELETED.getCode().equals(var.getDelFlag())) {
                        it.remove();
                    }
                });
            }

            log.info("####同步数据### userCount = {}, userDelCount = {}, postCount = {}, postHistoryCount = {}, userDeptCount = {}, userPostCount = {}", users.size(), delUserMap.size(), posts.size(), postHistories.size(), userDepts.size(), userPosts.size());
            if (!CollectionUtils.isEmpty(users)) {
                userMapper.insertBatch(users);
            }

            /**
             * 待验证
             */
            List<String> userIdList = new ArrayList<>(delUserMap.keySet());
            if (!CollectionUtils.isEmpty(userIdList)) {
                userMapper.updateUserDelStatusBatch(userIdList);
            }

            if (!CollectionUtils.isEmpty(posts)) {
                postMapper.insertPostBatch(posts);
            }

            /**
             * 待验证
             */
            if (!CollectionUtils.isEmpty(postHistories)) {
                postHistoryMapper.insertPostHistoryBatch(postHistories);
            }

            //先删除原有的用户部门关系
            if (!CollectionUtils.isEmpty(userDepts)) {
                userDeptMapper.delUserDeptBatch(userDepts);
                userDeptMapper.batchUserDept(userDepts);
            }
            //先删除原有的用户岗位关系
            if (!CollectionUtils.isEmpty(userPosts)) {
                userPostMapper.delUserPostBatch(userPosts);
                userPostMapper.batchUserPost(userPosts);
            }
        }

    }

    /**
     * 同步部门数据
     */
    public void syncDept(List<DeptExternal> deptExternalList) {
        /** 示例数据
         * {
         *       "grpOrgId": "1804000000",
         *       "upGrpOrgId": "1800000000",
         *       "orgShtNm": "大数据部",
         *       "orgFullNm": "大数据部",
         *       "orgStatCd": "1",
         *       "orgIdHrcyStr": "0000000000|1800000000|1804000000"
         *     },
         */
        if (!CollectionUtils.isEmpty(deptExternalList)) {
            List<SysDept> deptList = new ArrayList<>(deptExternalList.size());
            deptExternalList.forEach(deptExternal -> {
                SysDept dept = new SysDept();
                dept.setDeptId(deptExternal.getGrpOrgId());
                //设置根节点
                if (deptExternal.getGrpOrgId().equals("0000000000")) {
                    dept.setParentId("-1");
                } else {
                    dept.setParentId(deptExternal.getUpGrpOrgId());
                }
                dept.setDeptName(deptExternal.getOrgFullNm());
                String status = deptExternal.getOrgStatCd();
                if ("1".equals(status) || "5".equals(status)) {
                    dept.setStatus(UserStatus.OK.getCode());
                } else {
                    dept.setStatus(UserStatus.DISABLE.getCode());
                    dept.setDelFlag(UserStatus.DELETED.getCode());
                }
                //设置祖先节点
                String ancestors = deptExternal.getOrgIdHrcyStr();
                StringBuilder newAncestors = new StringBuilder();
                ancestors = ancestors.replace("|", ",").replace(deptExternal.getGrpOrgId(), "");
                String[] parentIds = ancestors.split(",");
                Collections.reverse(Arrays.asList(parentIds));
                for (String parentId : parentIds) {
                    newAncestors.append(parentId).append(",");
                }
                newAncestors = new StringBuilder(newAncestors.substring(0, newAncestors.lastIndexOf(",")));
                dept.setAncestors(newAncestors.toString());
                dept.setLeaf(deptMapper.hasChildByDeptId(dept.getDeptId()) <= 0);
                BizUtils.setCreatedTimeOperation(SysDept.class, dept);
                BizUtils.setUpdatedTimeOperation(SysDept.class, dept);
                deptList.add(dept);
            });
            deptMapper.insertDeptBatch(deptList);
        }
    }
}
