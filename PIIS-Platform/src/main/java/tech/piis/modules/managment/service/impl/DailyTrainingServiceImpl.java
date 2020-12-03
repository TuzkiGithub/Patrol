package tech.piis.modules.managment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.piis.common.exception.BaseException;
import tech.piis.common.utils.DateUtils;
import tech.piis.common.utils.IdUtils;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.utils.SecurityUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.mapper.PiisDocumentMapper;
import tech.piis.modules.managment.domain.po.DailyTrainingClassPO;
import tech.piis.modules.managment.domain.po.DailyTrainingMemberPO;
import tech.piis.modules.managment.domain.po.DailyTrainingPO;
import tech.piis.modules.managment.mapper.DailyTrainingClassMapper;
import tech.piis.modules.managment.mapper.DailyTrainingMapper;
import tech.piis.modules.managment.mapper.DailyTrainingMemberMapper;
import tech.piis.modules.managment.service.IDailyTrainingService;
import tech.piis.modules.system.domain.SysUser;
import tech.piis.modules.system.mapper.SysDeptMapper;

import javax.validation.constraints.NotEmpty;
import java.util.*;

import static tech.piis.common.constant.OperationConstants.*;

/**
 * ClassName : IDailyTrainingServiceImpl
 * Package : tech.piis.modules.managment.service.impl
 * Description :
 *
 * @author : chenhui@xvco.com
 */
@Service
@Slf4j
@Transactional
public class DailyTrainingServiceImpl implements IDailyTrainingService {

    @Autowired
    private DailyTrainingMapper dailyTrainingMapper;

    @Autowired
    private DailyTrainingClassMapper dailyTrainingClassMapper;

    @Autowired
    private DailyTrainingMemberMapper dailyTrainingMemberMapper;

    @Autowired
    private PiisDocumentMapper documentMapper;

    @Value("${piis.profile}")
    private String baseFileUrl;

    @Value("${piis.serverAddr}")
    private String serverAddr;

    /**
     * 新增日常培训
     */
    @Override
    public int saveDailyTraining(DailyTrainingPO dailyTrainingPO) throws BaseException {

        String dailyId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
        dailyTrainingPO.setDailyId(dailyId);

        List<DailyTrainingMemberPO> memberPOS = dailyTrainingPO.getMemberPOS();
        List<DailyTrainingClassPO> classPOS = dailyTrainingPO.getClassPOS();

        memberPOS.forEach(dailyTrainingMemberPO -> {
            dailyTrainingMemberPO.setDailyId(dailyId);
            BizUtils.setCreatedOperation(DailyTrainingMemberPO.class, dailyTrainingMemberPO);
            BizUtils.setCreatedTimeOperation(DailyTrainingMemberPO.class, dailyTrainingMemberPO);
//            dailyTrainingMemberMapper.insert(dailyTrainingMemberPO);
        });
        dailyTrainingMemberMapper.insertBatch(memberPOS);

        for (DailyTrainingClassPO classPO : classPOS) {
            classPO.setDailyId(dailyId);
            String classId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
            classPO.setClassId(classId);
            BizUtils.setCreatedOperation(DailyTrainingClassPO.class, classPO);
            BizUtils.setCreatedTimeOperation(DailyTrainingClassPO.class, classPO);
            List<String> memberList = classPO.getMemberList();
            if (!CollectionUtils.isEmpty(memberList)){
                String members = "";
                String[] memberArray = new String[memberList.size()];
                memberList.toArray(memberArray);
                if (memberArray.length >= 1) {
                    for (int i = 0; i < memberArray.length; i++) {
                        members = members + memberArray[i];
                        if (i < memberArray.length - 1) {
                            members += ",";
                        }
                    }
                }
                classPO.setMembers(members);
            }

            List<PiisDocumentPO> piisDocumentPOS = classPO.getPiisDocumentPOS();
            for (PiisDocumentPO piisDocumentPO : piisDocumentPOS) {
                piisDocumentPO.setObjectId(classId);
                documentMapper.updateById(piisDocumentPO);
            }
        }
        dailyTrainingClassMapper.insertBatch(classPOS);
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 发起人编号
        dailyTrainingPO.setInitiatorId(user.getUserId());
        // 发起人名称
        dailyTrainingPO.setInitiator(user.getUserName());
        return dailyTrainingMapper.insert(dailyTrainingPO);
    }


    /**
     * 修改日常培训
     *
     * @param dailyTrainingPO
     * @return
     */
    @Override
    public int updateDailyTraining(DailyTrainingPO dailyTrainingPO) throws BaseException {
        Integer[] memberDelete = dailyTrainingPO.getMemberDelete();
        if (memberDelete != null && memberDelete.length != 0) {
            List<Integer> list = Arrays.asList(memberDelete);
            dailyTrainingMemberMapper.deleteBatchIds(list);
        }
        Integer[] classDelete = dailyTrainingPO.getClassDelete();
        if (classDelete != null && classDelete.length != 0) {
            List<Integer> list = Arrays.asList(classDelete);
            List<DailyTrainingClassPO> classPOS = dailyTrainingClassMapper.selectBatchIds(list);

            // 删除课程相关课件
            deleClassFile(classPOS);
            // 删除课程表
            dailyTrainingClassMapper.deleteBatchIds(list);
        }

        List<DailyTrainingMemberPO> memberPOS = dailyTrainingPO.getMemberPOS();
        if (!CollectionUtils.isEmpty(memberPOS)) {
            // 根据操作类型修改参与培训人员信息
            editTrainingMemberInfo(dailyTrainingPO);
        }
        List<DailyTrainingClassPO> classPOS = dailyTrainingPO.getClassPOS();
        if (!CollectionUtils.isEmpty(classPOS)) {
            // 根据操作类型修改参与培训课程信息
            editTrainingClassInfo(dailyTrainingPO);
        }
        // 修改日常培训信息
        return dailyTrainingMapper.updateById(dailyTrainingPO);
    }


    /**
     * 删除日常培训
     *
     * @param id
     * @return
     */
    @Override
    public int delDailyTrainingById(String id) throws BaseException {
        // 由日常培训编号 关联所有相关培训课程
        QueryWrapper<DailyTrainingClassPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DAILY_ID", id);
        List<DailyTrainingClassPO> classPOS = dailyTrainingClassMapper.selectList(queryWrapper);
        // 删除日常培训相关课程的关联课件文件
        deleClassFile(classPOS);

        Map<String, Object> param = new HashMap<>();
        param.put("DAILY_ID", id);

        dailyTrainingClassMapper.deleteByMap(param);
        // 由日常培训编号 关联所有相关培训人员
        dailyTrainingMemberMapper.deleteByMap(param);
        return dailyTrainingMapper.deleteById(id);
    }

    /**
     * 查询日常培训列表
     *
     * @param dailyTrainingPO
     * @return
     */
    @Override
    public List<DailyTrainingPO> selectDailyTrainingList(DailyTrainingPO dailyTrainingPO) throws BaseException {
        /*QueryWrapper<DailyTrainingPO> queryWrapper = new QueryWrapper<>();
        dailyTrainingMapper.selectList();*/
        List<DailyTrainingPO> dailyTrainingPOList = dailyTrainingMapper.selectDailyTrainingListByConditions(dailyTrainingPO);
        return dailyTrainingPOList;
    }

    /**
     * 根据ID查询日常培训信息以及关联的培训课程与培训人员信息
     *
     * @param dailyId
     * @return
     */
    @Override
    public DailyTrainingPO selectDailyTrainingInfo(String dailyId) throws BaseException {
        QueryWrapper<DailyTrainingMemberPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DAILY_ID", dailyId);
        List<DailyTrainingMemberPO> memberPOS = dailyTrainingMemberMapper.selectList(queryWrapper);

        QueryWrapper<DailyTrainingClassPO> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("DAILY_ID", dailyId);
        List<DailyTrainingClassPO> classPOS = dailyTrainingClassMapper.selectList(queryWrapper1);
        if (!CollectionUtils.isEmpty(classPOS)) {
            for (DailyTrainingClassPO classPO : classPOS) {
                String members = classPO.getMembers();
                if (StringUtils.isNotBlank(members)) {
                    if (members.contains(",")) {
                        String[] memberArray = members.split(",");
                        List<String> memberList = Arrays.asList(memberArray);
                        classPO.setMemberList(memberList);
                    } else {
                        List<String> memberList = new ArrayList<>();
                        memberList.add(members);
                        classPO.setMemberList(memberList);
                    }
                }
                String classId = classPO.getClassId();
                QueryWrapper<PiisDocumentPO> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("object_id", classId);
                List<PiisDocumentPO> piisDocumentPOS1 = documentMapper.selectList(queryWrapper2);
                classPO.setPiisDocumentPOS(piisDocumentPOS1);
            }
        }

        DailyTrainingPO dailyTrainingPO = dailyTrainingMapper.selectById(dailyId);
        dailyTrainingPO.setMemberPOS(memberPOS);
        dailyTrainingPO.setClassPOS(classPOS);
        return dailyTrainingPO;
    }

    @Override
    public List<DailyTrainingPO> selectDailyTrainingByOrgId() throws BaseException {
        return dailyTrainingMapper.selectDailyTrainingByOrgId();
    }

    /**
     * 修改日常培训课程信息
     *
     * @param dailyTrainingPO
     */
    private void editTrainingClassInfo(DailyTrainingPO dailyTrainingPO) {
        List<DailyTrainingClassPO> classPOS = dailyTrainingPO.getClassPOS();
        if (!CollectionUtils.isEmpty(classPOS)) {
            for (DailyTrainingClassPO classPO : classPOS) {
                Integer operationType = classPO.getOperationType();
                if (null != operationType) {
                    switch (operationType) {
                        case INSERT: {
                            classPO.setDailyId(dailyTrainingPO.getDailyId());
                            String classId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
                            classPO.setClassId(classId);
                            List<String> memberList = classPO.getMemberList();
                            if (!CollectionUtils.isEmpty(memberList)){
                                String members = "";
                                String[] memberArray = new String[memberList.size()];
                                memberList.toArray(memberArray);
                                if (memberArray.length >= 1) {
                                    for (int i = 0; i < memberArray.length; i++) {
                                        members = members + memberArray[i];
                                        if (i < memberArray.length - 1) {
                                            members += ",";
                                        }
                                    }
                                }
                                classPO.setMembers(members);
                            }
                            BizUtils.setCreatedOperation(DailyTrainingClassPO.class, classPO);
                            BizUtils.setCreatedTimeOperation(DailyTrainingClassPO.class, classPO);
                            List<PiisDocumentPO> piisDocumentPOS = classPO.getPiisDocumentPOS();
                            for (PiisDocumentPO piisDocumentPO : piisDocumentPOS) {
                                piisDocumentPO.setObjectId(classId);
                                documentMapper.updateById(piisDocumentPO);
                            }
                            dailyTrainingClassMapper.insert(classPO);
                            break;
                        }
                        case UPDATE: {
                            String classId = classPO.getClassId();
                            QueryWrapper<PiisDocumentPO> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("object_id", classId);
                            List<PiisDocumentPO> piisDocumentPOS1 = documentMapper.selectList(queryWrapper);
                            for (PiisDocumentPO piisDocumentPO : piisDocumentPOS1) {
                                piisDocumentPO.setObjectId("");
                            }
                            List<PiisDocumentPO> piisDocumentPOS2 = classPO.getPiisDocumentPOS();
                            piisDocumentPOS2.forEach(piisDocumentPO -> {
                                piisDocumentPO.setObjectId(classId);
                                documentMapper.updateById(piisDocumentPO);
                            });

                            List<String> memberList = classPO.getMemberList();
                            if (!CollectionUtils.isEmpty(memberList)){
                                String members = "";
                                String[] memberArray = new String[memberList.size()];
                                memberList.toArray(memberArray);
                                if (memberArray.length >= 1) {
                                    for (int i = 0; i < memberArray.length; i++) {
                                        members = members + memberArray[i];
                                        if (i < memberArray.length - 1) {
                                            members += ",";
                                        }
                                    }
                                }
                                classPO.setMembers(members);
                            }
                            // 更新课程关联课件文件外键
                            BizUtils.setUpdatedOperation(DailyTrainingClassPO.class, classPO);
                            BizUtils.setUpdatedTimeOperation(DailyTrainingClassPO.class, classPO);
                            dailyTrainingClassMapper.updateById(classPO);
                            break;
                        }
                    }
                } else {
                    log.warn("DailyTrainingClass operationType is null! classPO = {}", classPO);
                }
            }
        }

    }

    /**
     * 修改培训人员信息
     *
     * @param
     */
    private void editTrainingMemberInfo(DailyTrainingPO dailyTrainingPO) {
        List<DailyTrainingMemberPO> memberPOS = dailyTrainingPO.getMemberPOS();
        if (!CollectionUtils.isEmpty(memberPOS)) {
            for (DailyTrainingMemberPO memberPO : memberPOS) {
                Integer operationType = memberPO.getOperationType();

                if (null != operationType) {
                    switch (operationType) {
                        case INSERT: {
                            memberPO.setDailyId(dailyTrainingPO.getDailyId());
                            BizUtils.setCreatedOperation(DailyTrainingMemberPO.class, memberPO);
                            BizUtils.setCreatedTimeOperation(DailyTrainingMemberPO.class, memberPO);
                            dailyTrainingMemberMapper.insert(memberPO);
                            break;
                        }
                        case UPDATE: {

                            BizUtils.setUpdatedOperation(DailyTrainingMemberPO.class, memberPO);
                            BizUtils.setUpdatedTimeOperation(DailyTrainingMemberPO.class, memberPO);
                            dailyTrainingMemberMapper.updateById(memberPO);
                            break;
                        }
                    }
                } else {
                    log.warn("DailyTrainingMember operationType is null! DailyTrainingMember = {}", memberPO);
                }
            }
        }
    }

    private void deleClassFile(List<DailyTrainingClassPO> classPOS) {
        if (!CollectionUtils.isEmpty(classPOS)) {
            for (DailyTrainingClassPO classPO : classPOS) {
                String classId = classPO.getClassId();
                QueryWrapper<PiisDocumentPO> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("object_id", classId);
                List<PiisDocumentPO> piisDocumentPOS = documentMapper.selectList(queryWrapper);
                piisDocumentPOS.forEach(piisDocumentPO -> {
                    piisDocumentPO.setObjectId("");
                });
            }
        }

    }

}
