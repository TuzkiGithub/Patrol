package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import tech.piis.common.enums.FileEnum;
import tech.piis.common.utils.DateUtils;
import tech.piis.common.utils.IdUtils;
import tech.piis.common.utils.StringUtils;
import tech.piis.framework.config.PIISConfig;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.utils.file.FileUploadUtils;
import tech.piis.modules.core.domain.po.*;
import tech.piis.modules.core.domain.vo.PlanCompanyCountVO;
import tech.piis.modules.core.mapper.*;
import tech.piis.modules.core.service.IInspectionPlanService;

import java.util.*;

import static tech.piis.common.constant.OperationConstants.*;

/**
 * 巡视计划 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-09-14
 */
@Service
public class InspectionPlanServiceImpl implements IInspectionPlanService {
    @Autowired
    private InspectionPlanMapper planMapper;

    @Autowired
    private InspectionGroupMapper groupMapper;

    @Autowired
    private InspectionGroupMemberMapper groupMemberMapper;

    @Autowired
    private InspectionUnitsMapper unitsMapper;

    @Autowired
    private PiisDocumentMapper documentMapper;

    @Value("${piis.profile}")
    private String baseFileUrl;


    /**
     * 查询巡视计划列表
     *
     * @param inspectionPlanPO
     * @return
     */
    @Override
    public List<InspectionPlanPO> selectPlanList(InspectionPlanPO inspectionPlanPO) {
        return planMapper.selectPlanList(inspectionPlanPO);
    }

    /**
     * 新增巡视计划
     *
     * @param inspectionPlanPO
     * @return
     */
    @Transactional
    @Override
    public int savePlan(InspectionPlanPO inspectionPlanPO) throws Exception {
        //巡视计划主键生成
        String planId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
        inspectionPlanPO.setPlanId(planId);
        List<InspectionGroupPO> groupList = inspectionPlanPO.getInspectionGroupList();

        groupList.forEach(group -> group.setPlanId(planId));
        //新增巡视组
        saveGroups(inspectionPlanPO.getInspectionGroupList());
        //更新文件表
        updateFile(inspectionPlanPO.getDocuments(), planId);
        //新增计划
        BizUtils.setCreatedOperation(InspectionPlanPO.class, inspectionPlanPO);
        return planMapper.insert(inspectionPlanPO.setInspectionGroupList(null));

    }


    /**
     * 修改巡视计划
     *
     * @param inspectionPlanPO
     * @return
     */
    @Override
    @Transactional
    public int editPlan(InspectionPlanPO inspectionPlanPO) throws Exception {
        List<InspectionGroupPO> groupList = inspectionPlanPO.getInspectionGroupList();
        if (!CollectionUtils.isEmpty(groupList)) {
            groupList.forEach(group -> {
                group.setPlanId(inspectionPlanPO.getPlanId());
                int operationType = group.getOperationType();
                switch (operationType) {
                    case INSERT: {
                        List<InspectionGroupPO> groups = new ArrayList<>();
                        groups.add(group);
                        saveGroups(groups);
                        break;
                    }
                    case UPDATE: {
                        //删除关联巡视组组员、被巡视单位
                        delGroupRelation(group);
                        //新增关联巡视组组员、被巡视单位
                        List<InspectionGroupPO> groups = new ArrayList<>();
                        groups.add(group);
                        saveGroupRelation(groups, false);
                        //更新巡视组信息
                        groupMapper.updateById(group.setInspectionGroupMemberList(null).setInspectionUnitsList(null).setOperationType(null));
                        break;
                    }
                    case DELETE: {
                        delGroup(group);
                        break;
                    }
                }
            });
        }
        updateFile(inspectionPlanPO.getDocuments(), inspectionPlanPO.getPlanId());
        return planMapper.updateById(inspectionPlanPO.setInspectionGroupList(null));
    }

    /**
     * documentList 文件列表
     * planId  巡视计划ID
     *
     * @param documentList
     */
    private void updateFile(List<PiisDocumentPO> documentList, String planId) {
        if (!CollectionUtils.isEmpty(documentList)) {
            documentList.forEach(document -> {
                Integer operationType = document.getOperationType();
                if (null != operationType) {
                    switch (operationType) {
                        case INSERT: {
                            //将业务字段赋予文件表
                            document.setFileDictId(FileEnum.WORK_PREPARED_OTHER_FILE.getCode())
                                    .setObjectId(planId);
                            documentMapper.updateById(document);
                            break;
                        }
                        case DELETE: {
                            //删除服务器上文件以及文件表数据
                            documentMapper.deleteById(document.getPiisDocId());
                            String filePath = document.getFilePath();
                            if (!StringUtils.isEmpty(filePath)) {
                                FileUploadUtils.deleteServerFile(filePath.replace(filePath, baseFileUrl));
                            }
                            break;
                        }
                    }
                }
            });
        }
    }


    /**
     * 批量新增巡视组
     *
     * @param groupList
     */
    private void saveGroups(List<InspectionGroupPO> groupList) {
        saveGroupRelation(groupList, true);
        groupMapper.insertBatch(groupList);
    }

    /**
     * 批量新增被巡视单位
     */
    public void saveUnits(List<InspectionUnitsPO> unitsList) {
        if (!CollectionUtils.isEmpty(unitsList)) {
            unitsMapper.insertBatch(unitsList);
        }
    }


    /**
     * 批量新增巡视组组员、被巡视单位
     *
     * @param groupList 巡视组列表
     * @param isSave    是否是新增巡视计划
     */
    private void saveGroupRelation(List<InspectionGroupPO> groupList, boolean isSave) {
        if (!CollectionUtils.isEmpty(groupList)) {
            for (int i = 0; i < groupList.size(); i++) {
                InspectionGroupPO group = groupList.get(i);
                if (isSave) {
                    String groupId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6) + i;
                    group.setGroupId(groupId);
                }
                //新增巡视组成员
                List<InspectionGroupMemberPO> groupMemberList = group.getInspectionGroupMemberList();
                if (!CollectionUtils.isEmpty(groupMemberList)) {
                    groupMemberList.forEach(groupMember -> {
                        groupMember.setPlanId(group.getPlanId());
                        groupMember.setGroupId(group.getGroupId());
                    });
                    saveGroupMembers(groupMemberList);
                }
                //新增被巡视单位
                List<InspectionUnitsPO> unitsList = group.getInspectionUnitsList();
                if (!CollectionUtils.isEmpty(unitsList)) {
                    unitsList.forEach(units -> {
                        units.setPlanId(group.getPlanId());
                        units.setGroupId(group.getGroupId());
                    });
                    saveUnits(unitsList);
                }
            }
        }
    }


    /**
     * 批量新增巡视组组员
     */
    @Transactional
    public void saveGroupMembers(List<InspectionGroupMemberPO> groupMemberList) {
        if (!CollectionUtils.isEmpty(groupMemberList)) {
            groupMemberMapper.insertBatch(groupMemberList);
        }
    }

    /**
     * 根据ID删除巡视计划
     *
     * @param planIds
     * @return
     * @desc 循环删除效率低，向工时妥协
     */
    @Override
    @Transactional
    public int delPlanByIds(String[] planIds) {
        List<String> planIdList = Arrays.asList(planIds);
        if (!CollectionUtils.isEmpty(planIdList)) {
            for (String planId : planIdList) {
                Map<String, Object> params = new HashMap<>();
                params.put("PLAN_ID", planId);
                //删除关联巡视组
                groupMapper.deleteByMap(params);
                //删除关联巡视组组员
                groupMemberMapper.deleteByMap(params);
                //删除关联被巡视单位
                unitsMapper.deleteByMap(params);
            }
        }
        return planMapper.deleteBatchIds(planIdList);
    }

    /**
     * 查询总记录数
     *
     * @return
     */
    @Override
    public int selectCount() {
        return planMapper.selectCount(null);
    }

    /**
     * 统计公司巡察次数
     *
     * @return
     */
    @Override
    public List<PlanCompanyCountVO> selectCountByCompany() {
        return this.planMapper.selectPlanCompanyTotal();
    }

    /**
     * 删除巡视组
     *
     * @param group
     */
    @Transactional
    public void delGroup(InspectionGroupPO group) {
        Map<String, Object> params = new HashMap<>();
        params.put("GROUP_ID", group.getGroupId());
        //删除巡视组
        groupMapper.deleteByMap(params);
        //删除关联巡视组组员、被巡视单位
        delGroupRelation(group);
    }

    /**
     * 删除巡视组组员和被巡视单位信息
     */
    @Transactional
    public void delGroupRelation(InspectionGroupPO group) {
        Map<String, Object> params = new HashMap<>();
        params.put("GROUP_ID", group.getGroupId());
        //删除关联巡视组组员
        groupMemberMapper.deleteByMap(params);
        //删除关联被巡视单位
        unitsMapper.deleteByMap(params);
    }
}
