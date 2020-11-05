package tech.piis.modules.managment.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.common.constant.ManagmentConstants;
import tech.piis.modules.managment.domain.FullPartOrg;
import tech.piis.modules.managment.mapper.FullPartOrgMapper;
import tech.piis.modules.managment.mapper.OrganizationMapper;
import tech.piis.modules.managment.service.OrganizationService;
import tech.piis.modules.system.domain.SysDept;
import tech.piis.modules.system.mapper.SysDeptMapper;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName : IorganizationServiceImpl
 * Package : tech.piis.modules.managment.service.impl
 * Description :
 *  机构  service业务层处理
 * @author : chenhui@xvco.com
 */
@Slf4j
@Service
@Transactional
public class IOrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private FullPartOrgMapper fullPartOrgMapper;

    @Override
    public String getInspectionOrganWholeName(String organId) {
        String piisWholeName = organizationMapper.getInspectionOrganWholeName(organId);
        return piisWholeName;
    }

    @Override
    public Object getPageInfo(String organId) {
        int sonOrganCount = 0;
        int organCount = 0;
        JSONObject returnJsonObject = new JSONObject();// 对应机构概览展示页面的的记录行
        JSONObject jsonObject = null;
        JSONObject sonJsonObject = null;
        int sumNum = 0 ; // 统计子公司巡察人数之和
        int sonSumNum = 0 ; // 统计子公司下一级直属子公司巡察人数之和

        // 根据传入的机构号查询对应机构
        SysDept organ = sysDeptMapper.selectDeptById(organId);

        // 获取公司巡察机构全称
        String wholeName = organizationMapper.getInspectionOrganWholeName(organId);
        jsonObject.put(ManagmentConstants.PIIS_WHOLE_NAME, wholeName);

        // 获取公司专职、兼职人数
        Map<String, Object> returnMap = getFullPartNumByOrganId(organId);

        // 获取公司下一级直属机构列表
        List<SysDept> sysDeptList = sysDeptMapper.selectChildrenDeptById(organId);
        for (SysDept dept : sysDeptList) {
            sonJsonObject = new JSONObject();

            // 获取子公司巡察机构全称
            wholeName = organizationMapper.getInspectionOrganWholeName(dept.getDeptId());
            sonJsonObject.put(ManagmentConstants.PIIS_WHOLE_NAME, wholeName);

            // 获取子公司专职、兼职人数 ，以及专职兼职人数之和sum
            FullPartOrg fullPartObj = fullPartOrgMapper.getFullPartNumByOrganId(dept.getDeptId());
            sonJsonObject.put(ManagmentConstants.FULL_NUM,fullPartObj.getFullNumber());
            sonJsonObject.put(ManagmentConstants.PART_NUM,fullPartObj.getFullNumber());
            int exact = Math.addExact(fullPartObj.getFullNumber(), fullPartObj.getPartNumber());
            sumNum += exact;

            // 判断初始传入的机构是否为集团总部，如果是，则执行下列流程
            if (null == organ.getParentId()){
                // 获取子公司的直属公司
                List<SysDept> grandSonCompany = sysDeptMapper.selectChildrenDeptById(dept.getDeptId());

                // 获取子公司的直属下级公司巡察人数
                for ( SysDept sysDept : grandSonCompany) {

                    // 获取子公司的直属下级公司专职、兼职人数 以及专职兼职人数之和sum
                    Map<String, Object> retuenMap = getFullPartNumByOrganId(sysDept.getDeptId());
                    retuenMap.get(ManagmentConstants.FULL_PART_SUM);
//                    sonSumNum += addExact;
                    // 子公司的直属下级公司巡察机构数
                    organCount++;
                }
                // 获取子公司的直属下级巡察队伍人数和
                sonJsonObject.put("tempnum", sonSumNum);
                // 获取子公司的直属下级巡察机构数
                sonJsonObject.put("organCount", organCount);
            }
            returnJsonObject.put(dept.getDeptName(), sonJsonObject);
            // 获取子公司巡查机构数
            sonOrganCount++;
        }
        jsonObject.put("sonOrganCount", sonOrganCount);
        jsonObject.put("sumNum", sumNum);
        returnJsonObject.put(organ.getDeptName(), jsonObject);
        return null;
    }

    private Map<String, Object> getFullPartNumByOrganId(String organId) {

        Map<String,Object> resultMap = new HashMap<>();

        FullPartOrg fullPartOrg = fullPartOrgMapper.getFullPartNumByOrganId(organId);
        resultMap.put(ManagmentConstants.FULL_NUM,fullPartOrg.getFullNumber());
        resultMap.put(ManagmentConstants.PART_NUM,fullPartOrg.getPartNumber());

        int sum = Math.addExact(fullPartOrg.getFullNumber(), fullPartOrg.getPartNumber());
        resultMap.put(ManagmentConstants.FULL_PART_SUM, sum);
        return resultMap;
    }

}
