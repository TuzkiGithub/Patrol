package tech.piis.modules.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.piis.common.utils.DateUtils;
import tech.piis.common.utils.IdUtils;
import tech.piis.modules.core.domain.po.InspectionGroupPO;
import tech.piis.modules.core.domain.po.InspectionPlanPO;
import tech.piis.modules.core.mapper.*;
import tech.piis.modules.core.service.IInspectionPlanService;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
    public int savePlan(InspectionPlanPO inspectionPlanPO) {
        //plan主键生成
        String planId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0,6);

        //新增巡视组
        saveGroups(inspectionPlanPO.getInspectionGroupList());

        //新增巡视组成员
        groupMemberMapper.insert(null);

        //新增被巡视单位
        unitsMapper.insert(null);

        inspectionPlanPO.setPlanId(planId);
        return planMapper.insert(inspectionPlanPO);

    }

    /**
     * 批量新增巡视组
     * @param groupList
     */
    private void saveGroups(List<InspectionGroupPO> groupList){
        if(!CollectionUtils.isEmpty(groupList)){
            for (int i = 0; i < groupList.size(); i++) {
                InspectionGroupPO group = groupList.get(i);
                String groupId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0,6) + i;
                group.setGroupId(groupId);
            }
            groupMapper.insertBatch(groupList);
        }
    }


    /**
     * 修改巡视计划
     *
     * @param inspectionPlanPO
     * @return
     */
    @Override
    public int editPlan(InspectionPlanPO inspectionPlanPO) {
        return 0;
    }


    /**
     * 根据ID删除巡视计划
     *
     * @param planIds
     * @return
     */
    @Override
    public int delPlanByIds(String[] planIds) {
        return planMapper.deleteBatchIds(Arrays.asList(planIds));
    }
}
