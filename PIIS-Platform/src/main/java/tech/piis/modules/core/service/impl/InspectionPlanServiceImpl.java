package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.piis.common.enums.FileEnum;
import tech.piis.common.utils.DateUtils;
import tech.piis.common.utils.IdUtils;
import tech.piis.common.utils.StringUtils;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.utils.file.FileUploadUtils;
import tech.piis.modules.core.domain.po.*;
import tech.piis.modules.core.domain.vo.PlanCompanyCountVO;
import tech.piis.modules.core.mapper.*;
import tech.piis.modules.core.service.IInspectionPlanService;

import java.util.*;
import java.util.stream.Collectors;

import static tech.piis.common.constant.OperationConstants.*;

/**
 * 巡视计划 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-09-14
 */
@Slf4j
@Service
@Transactional
public class InspectionPlanServiceImpl implements IInspectionPlanService {
    @Autowired
    private InspectionPlanMapper planMapper;

    @Autowired
    private InspectionGroupMapper groupMapper;

    @Autowired
    private InspectionGroupMemberMapper groupMemberMapper;

    @Autowired
    private InspectionGroupMemberUnitsMapper groupMemberUnitsMapper;

    @Autowired
    private InspectionUnitsMapper unitsMapper;

    @Autowired
    private InspectionTalkOutlineMapper talkOutlineMapper;

    @Autowired
    private PiisDocumentMapper documentMapper;

    @Value("${piis.profile}")
    private String baseFileUrl;

    @Value("${piis.serverAddr}")
    private String serverAddr;


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
     * 查询总记录数
     *
     * @return
     */
    @Override
    public int selectCount(InspectionPlanPO plan) {
        QueryWrapper<InspectionPlanPO> queryWrapper = new QueryWrapper<>();
        if (null != plan) {
            if (!StringUtils.isEmpty(plan.getPlanName())) {
                queryWrapper.like("PLAN_NAME", plan.getPlanName());
            }
            if (!StringUtils.isEmpty(plan.getPlanCompanyId())) {
                queryWrapper.eq("PLAN_COMPANY_ID", plan.getPlanCompanyId());
            }
        }
        return planMapper.selectCount(queryWrapper);
    }

    /**
     * 统计公司巡察次数
     *
     * @return
     */
    @Override
    public List<PlanCompanyCountVO> selectCountByCompany(InspectionPlanPO plan) {
        return this.planMapper.selectPlanCompanyTotal(plan);
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

        //新增默认谈话纲要分类
        saveTalkOutline(planId);
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
                Integer operationType = group.getOperationType();
                if (null != operationType) {
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
                } else {
                    log.warn("documents operationType is null! group = {}", group);
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
                                FileUploadUtils.deleteServerFile(filePath.replace(serverAddr + "/upload", baseFileUrl));
                            }
                            break;
                        }
                    }
                } else {
                    log.warn("documents operationType is null! documents = {}", document);
                }
            });
        }
    }


    /**
     * 新增默认谈话纲要
     *
     * @param planId 巡视ID
     */
    private void saveTalkOutline(String planId) {
        List<InspectionTalkOutlinePO> talkOutlineList = new ArrayList<>();
        talkOutlineList.add(new InspectionTalkOutlinePO()
                .setPlanId(planId)
                .setTalkClassification("落实党的路线方针政策和党中央重大决策部署监督重点情况")
                .setTalkClassificationId(-1L));

        talkOutlineList.add(new InspectionTalkOutlinePO()
                .setPlanId(planId)
                .setTalkClassification("落实全面从严治党战略部署监督重点情况")
                .setTalkClassificationId(-1L));

        talkOutlineList.add(new InspectionTalkOutlinePO()
                .setPlanId(planId)
                .setTalkClassification("落实新时代党的组织路线监督重点情况")
                .setTalkClassificationId(-1L));

        talkOutlineList.add(new InspectionTalkOutlinePO()
                .setPlanId(planId)
                .setTalkClassification("落实巡视、 主题教育、审计整改情况")
                .setTalkClassificationId(-1L));

        for (InspectionTalkOutlinePO talkOutline : talkOutlineList) {
            BizUtils.setCreatedOperation(InspectionTalkOutlinePO.class, talkOutline);
            talkOutlineMapper.insert(talkOutline);
        }
    }


    /**
     * 批量新增巡视组
     *
     * @param groupList
     */
    private void saveGroups(List<InspectionGroupPO> groupList) {
        saveGroupRelation(groupList, true);
        if (!CollectionUtils.isEmpty(groupList)) {
            for (InspectionGroupPO var : groupList) {
                BizUtils.setCreatedOperation(InspectionGroupPO.class, var);
            }
        }
        groupMapper.insertBatch(groupList);
    }

    /**
     * 批量新增巡视组组员
     */
    public void saveGroupMembers(List<InspectionGroupMemberPO> groupMemberList) {
        if (!CollectionUtils.isEmpty(groupMemberList)) {
            for (InspectionGroupMemberPO var : groupMemberList) {
                BizUtils.setCreatedOperation(InspectionGroupMemberPO.class, var);
            }
            groupMemberMapper.insertBatch(groupMemberList);
        }
    }

    /**
     * 批量新增被巡视单位
     */
    public void saveUnits(List<InspectionUnitsPO> unitsList) {
        if (!CollectionUtils.isEmpty(unitsList)) {
            for (InspectionUnitsPO var : unitsList) {
                BizUtils.setCreatedOperation(InspectionUnitsPO.class, var);
            }
            unitsMapper.insertBatch(unitsList);
        }
    }

    /**
     * 批量新增被巡视单位-组员关系
     */
    public void saveGroupMemberUnits(List<InspectionGroupMemberUnitsPO> groupMemberUnitsList) {
        if (!CollectionUtils.isEmpty(groupMemberUnitsList)) {
            groupMemberUnitsList = groupMemberUnitsList.stream().filter(var -> null == var.getGroupMemberUnitsId()).collect(Collectors.toList());
            groupMemberUnitsMapper.insertGroupMemberUnitsBatch(groupMemberUnitsList);
        }
    }

    /**
     * 批量新增/修改巡视组组员、被巡视单位、巡视组组员-被巡视单位关系
     *
     * @param groupList 巡视组列表
     * @param isSave    是否是新增
     */
    private void saveGroupRelation(List<InspectionGroupPO> groupList, boolean isSave) {
        if (!CollectionUtils.isEmpty(groupList)) {
            for (int i = 0; i < groupList.size(); i++) {
                InspectionGroupPO group = groupList.get(i);
                if (isSave) {
                    String groupId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6) + i;
                    group.setGroupId(groupId);
                }
                String planId = group.getPlanId();
                String groupId = group.getGroupId();

                //批量新增巡视组成员
                List<InspectionGroupMemberPO> groupMemberList = group.getInspectionGroupMemberList();
                if (!CollectionUtils.isEmpty(groupMemberList)) {
                    groupMemberList.forEach(groupMember -> {
                        groupMember.setGroupId(groupId);
                        groupMember.setPlanId(planId);
                    });
                    saveGroupMembers(groupMemberList);
                }

                List<InspectionGroupMemberUnitsPO> groupMemberUnitsList = new ArrayList<>();
                //批量新增被巡视单位
                List<InspectionUnitsPO> unitsList = group.getInspectionUnitsList();
                if (!CollectionUtils.isEmpty(unitsList)) {
                    List<Long> groupMemberIds = new ArrayList<>();
                    unitsList.forEach(units -> {
                        units.setPlanId(planId);
                        units.setGroupId(groupId);
                        List<InspectionGroupMemberPO> groupMembers = units.getGroupMemberList();
                        BizUtils.setCreatedOperation(InspectionUnitsPO.class, units);
                        unitsMapper.insert(units.setGroupMemberList(null));
                        Long unitsId = units.getUnitsId();
                        if (!CollectionUtils.isEmpty(groupMembers)) {
                            for (InspectionGroupMemberPO groupMember : groupMembers) {
                                for (InspectionGroupMemberPO aGroupMemberList : groupMemberList) {
                                    //设置组员ID字段
                                    if (Objects.equals(groupMember.getMemberId(), aGroupMemberList.getMemberId())) {
                                        groupMemberIds.add(aGroupMemberList.getGroupMemberId());
                                        break;
                                    }
                                }
                            }
                        }

                        groupMemberIds.forEach(var -> {
                            InspectionGroupMemberUnitsPO groupMemberUnits = new InspectionGroupMemberUnitsPO();
                            groupMemberUnits.setUnitsId(unitsId);
                            groupMemberUnits.setGroupMemberId(var);
                            groupMemberUnits.setGroupId(groupId);
                            groupMemberUnitsList.add(groupMemberUnits);
                        });

                    });
                }

                //批量新增巡视组组员-被巡视单位关系
                saveGroupMemberUnits(groupMemberUnitsList);
            }
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
     * 删除巡视组组员和被巡视单位信息以及其中关系
     */
    public void delGroupRelation(InspectionGroupPO group) {
        Map<String, Object> params = new HashMap<>();
        params.put("GROUP_ID", group.getGroupId());
        //删除关联巡视组组员
        groupMemberMapper.deleteByMap(params);
        //删除关联被巡视单位
        unitsMapper.deleteByMap(params);
        //删除组员和被巡视单位关系
        groupMemberUnitsMapper.deleteByMap(params);
    }
}
