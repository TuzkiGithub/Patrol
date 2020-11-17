package tech.piis.modules.managment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.piis.common.constant.ManagmentConstants;
import tech.piis.common.enums.FileEnum;
import tech.piis.common.utils.DateUtils;
import tech.piis.common.utils.IdUtils;
import tech.piis.common.utils.StringUtils;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.utils.SecurityUtils;
import tech.piis.framework.utils.file.FileUploadUtils;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.mapper.PiisDocumentMapper;
import tech.piis.modules.managment.domain.DailyTrainingClassPO;
import tech.piis.modules.managment.domain.DailyTrainingMemberPO;
import tech.piis.modules.managment.domain.DailyTrainingPO;
import tech.piis.modules.managment.mapper.DailyTrainingClassMapper;
import tech.piis.modules.managment.mapper.DailyTrainingMapper;
import tech.piis.modules.managment.mapper.DailyTrainingMemberMapper;
import tech.piis.modules.managment.service.IDailyTrainingService;
import tech.piis.modules.system.domain.SysDept;
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
    private PiisDocumentMapper piisDocumentMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;

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
    public int saveDailyTraining(DailyTrainingPO dailyTrainingPO) {

        String dailyId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
        dailyTrainingPO.setDailyId(dailyId);

        List<DailyTrainingMemberPO> memberPOS = dailyTrainingPO.getMemberPOS();
        List<DailyTrainingClassPO> classPOS = dailyTrainingPO.getClassPOS();

        memberPOS.forEach(dailyTrainingMemberPO -> {
            dailyTrainingMemberPO.setDailyId(dailyId);
            BizUtils.setCreatedOperation(DailyTrainingMemberPO.class, dailyTrainingMemberPO);
            BizUtils.setCreatedTimeOperation(DailyTrainingMemberPO.class, dailyTrainingMemberPO);
            dailyTrainingMemberMapper.insert(dailyTrainingMemberPO);
        });

        for (DailyTrainingClassPO classPO : classPOS) {
            classPO.setDailyId(dailyId);
            BizUtils.setCreatedOperation(DailyTrainingClassPO.class, classPO);
            BizUtils.setCreatedTimeOperation(DailyTrainingClassPO.class, classPO);
            List<PiisDocumentPO> piisDocumentPOS = classPO.getPiisDocumentPOS();
            String docuId = getDocusId(piisDocumentPOS);
            classPO.setPiisDocuId(docuId);
            dailyTrainingClassMapper.insert(classPO);
        }

        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 发起人编号
        dailyTrainingPO.setInitiatorId(user.getUserId());
        // 发起人名称
        dailyTrainingPO.setInitiator(user.getUserName());
        return dailyTrainingMapper.insert(dailyTrainingPO);
    }

    private String getDocusId(List<PiisDocumentPO> piisDocumentPOS) {
        String piisDocuId = "";
        if (piisDocumentPOS.size() > 1) {
            for (PiisDocumentPO piisDocumentPO: piisDocumentPOS){
                piisDocuId = piisDocuId +piisDocumentPO.getPiisDocId()+",";
            }
            String jsonString = piisDocuId.replaceAll(",$", "");
            return jsonString;
        }else {
            for (PiisDocumentPO piisDocumentPO:piisDocumentPOS) {
                piisDocuId = piisDocumentPO.getPiisDocId()+"";
            }
            return piisDocuId;
        }

    }

    private String getParentId(String orgId) {
        SysDept dept = sysDeptMapper.selectDeptById(orgId);
        String parentId = dept.getParentId();
        if (!ManagmentConstants.FIRST_BRANCH_UNION_ID.contains(parentId)) {
            parentId = getParentId(parentId);
        }
        return parentId;
    }

    /**
     * 修改日常培训
     *
     * @param dailyTrainingPO
     * @return
     */
    @Override
    public int updateDailyTraining(DailyTrainingPO dailyTrainingPO) {
        List<DailyTrainingMemberPO> memberPOS = dailyTrainingPO.getMemberPOS();
        if (!CollectionUtils.isEmpty(memberPOS)) {
            editTrainingMemberInfo(memberPOS);
        }
        List<DailyTrainingClassPO> classPOS = dailyTrainingPO.getClassPOS();
        if (!CollectionUtils.isEmpty(classPOS)) {
            editTrainingClassInfo(classPOS);
        }
        return dailyTrainingMapper.updateById(dailyTrainingPO);
    }

    /**
     * 删除日常培训
     *
     * @param id
     * @return
     */
    @Override
    public int delDailyTrainingById(String id) {
        // 由日常培训编号 关联所有相关培训课程
        QueryWrapper<DailyTrainingClassPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DAILY_ID", id);
        List<DailyTrainingClassPO> classPOS = dailyTrainingClassMapper.selectList(queryWrapper);

        for (DailyTrainingClassPO classPO : classPOS) {
            // 根据培训课程 关联相关课件ID
            String piisDocumentId = classPO.getPiisDocuId();
            if (piisDocumentId.contains(",")){
                String[] split = piisDocumentId.split(",");
                List<String> list = Arrays.asList(split);
                List<PiisDocumentPO> piisDocumentPOS = documentMapper.selectBatchIds(list);
                for (PiisDocumentPO document:piisDocumentPOS) {
                    //删除服务器上文件以及文件表数据
//                    documentMapper.deleteById(document.getPiisDocId());
                    String filePath = document.getFilePath();
                    if (!StringUtils.isEmpty(filePath)) {
                        FileUploadUtils.deleteServerFile(filePath.replace(serverAddr + "/upload", baseFileUrl));
                    }
                }
                documentMapper.deleteBatchIds(list);
            }else {
                PiisDocumentPO piisDocumentPO = documentMapper.selectById(piisDocumentId);
                String filePath = piisDocumentPO.getFilePath();
                if (!StringUtils.isEmpty(filePath)) {
                    FileUploadUtils.deleteServerFile(filePath.replace(serverAddr + "/upload", baseFileUrl));
                }
                documentMapper.deleteById(piisDocumentId);
            }

        }

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
    public List<DailyTrainingPO> selectDailyTrainingList(DailyTrainingPO dailyTrainingPO) {
       /* QueryWrapper<DailyTrainingPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(dailyTrainingPO.getInitiator()), "INITIATOR", dailyTrainingPO.getInitiator());
        queryWrapper.eq(StringUtils.isNotBlank(dailyTrainingPO.getTrainingName()), "TRAINING_NAME", dailyTrainingPO.getTrainingName());
        queryWrapper.eq(StringUtils.isNotEmpty(String.valueOf(dailyTrainingPO.getTrainingYear())) && !String.valueOf(dailyTrainingPO.getTrainingYear()).equals("null"), "TRAINING_YEAR", dailyTrainingPO.getTrainingYear());*/
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
    public DailyTrainingPO selectDailyTrainingInfo(String dailyId) {
        QueryWrapper<DailyTrainingMemberPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DAILY_ID", dailyId);
        List<DailyTrainingMemberPO> memberPOS = dailyTrainingMemberMapper.selectList(queryWrapper);

        QueryWrapper<DailyTrainingClassPO> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("DAILY_ID", dailyId);
        List<DailyTrainingClassPO> classPOS = dailyTrainingClassMapper.selectList(queryWrapper1);

        for (DailyTrainingClassPO classPO: classPOS) {
            List<PiisDocumentPO> piisDocumentPOS = new ArrayList<>();
            String piisDocuId = classPO.getPiisDocuId();

            if (piisDocuId.contains(",")){
                String[] split = piisDocuId.split(",");
                List<String> list = Arrays.asList(split);
                piisDocumentPOS = piisDocumentMapper.selectBatchIds(list);

            }else {
                PiisDocumentPO piisDocumentPO = piisDocumentMapper.selectById(piisDocuId);
                piisDocumentPOS.add(piisDocumentPO);
            }
            classPO.setPiisDocumentPOS(piisDocumentPOS);
        }
        DailyTrainingPO dailyTrainingPO = dailyTrainingMapper.selectById(dailyId);
        dailyTrainingPO.setMemberPOS(memberPOS);
        dailyTrainingPO.setClassPOS(classPOS);
        return dailyTrainingPO;
    }

    @Override
    public List<DailyTrainingPO> selectDailyTrainingByOrgId() {
        return dailyTrainingMapper.selectDailyTrainingByOrgId();
    }

    /**
     * 修改日常培训信息
     *
     * @param classPOS
     */
    private void editTrainingClassInfo(List<DailyTrainingClassPO> classPOS) {
        if (!CollectionUtils.isEmpty(classPOS)) {
            for (DailyTrainingClassPO classPO : classPOS) {
                Integer operationType = classPO.getOperationType();
                if (null != operationType) {
                    switch (operationType) {
                        case INSERT: {
                            BizUtils.setCreatedOperation(DailyTrainingClassPO.class, classPO);
                            BizUtils.setCreatedTimeOperation(DailyTrainingClassPO.class, classPO);
                            /*List<PiisDocumentPO> piisDocumentPOS = classPO.getPiisDocumentPOS();
                            String docuId = getDocusId(piisDocumentPOS);
                            classPO.setPiisDocuId(docuId);*/
                            dailyTrainingClassMapper.insert(classPO);
                            break;
                        }
                        case UPDATE: {
                            BizUtils.setUpdatedOperation(DailyTrainingClassPO.class, classPO);
                            BizUtils.setUpdatedTimeOperation(DailyTrainingClassPO.class, classPO);
                            dailyTrainingClassMapper.updateById(classPO);
                            break;
                        }
                        case DELETE: {
                           /* List<PiisDocumentPO> piisDocumentPOS = classPO.getPiisDocumentPOS();
                            for (PiisDocumentPO document:piisDocumentPOS) {
                                //删除服务器上文件以及文件表数据
                                documentMapper.deleteById(document.getPiisDocId());
                                String filePath = document.getFilePath();
                                if (!StringUtils.isEmpty(filePath)) {
                                    FileUploadUtils.deleteServerFile(filePath.replace(serverAddr + "/upload", baseFileUrl));
                                }
                            }*/
                            dailyTrainingClassMapper.deleteById(classPO.getClassId());
                            break;
                        }
                    }
                } else {
                    log.warn("DailyTrainingClass operationType is null! group = {}", classPO);
                }
                updateFile(classPO);
            }
        }

    }

    private void updateFile(DailyTrainingClassPO classPO) {
        List<PiisDocumentPO> piisDocumentPOS = classPO.getPiisDocumentPOS();
        if (!CollectionUtils.isEmpty(piisDocumentPOS)) {
            piisDocumentPOS.forEach(document -> {
                Integer operationType = document.getOperationType();
                if (null != operationType) {
                    switch (operationType) {
                        case INSERT: {
                            /*//将业务字段赋予文件表
                            document.setFileDictId(FileEnum.WORK_PREPARED_OTHER_FILE.getCode())
                                    .setObjectId(planId);*/
                            if (classPO.getPiisDocuId() != null){
                                String piisDocuId = classPO.getPiisDocuId()+","+document.getPiisDocId();
                                classPO.setPiisDocuId(piisDocuId);
                            }
                            classPO.setPiisDocuId(String.valueOf(document.getPiisDocId()));
                            dailyTrainingClassMapper.updateById(classPO);
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
                            classPO.setPiisDocuId(null);
                            dailyTrainingClassMapper.updateById(classPO);
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
     * 修改培训人员信息
     *
     * @param memberPOS
     */
    private void editTrainingMemberInfo(List<DailyTrainingMemberPO> memberPOS) {
        if (!CollectionUtils.isEmpty(memberPOS)) {
            for (DailyTrainingMemberPO memberPO : memberPOS) {
                Integer operationType = memberPO.getOperationType();

                if (null != operationType) {
                    switch (operationType) {
                        case INSERT: {
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
                        case DELETE: {
                            dailyTrainingMemberMapper.deleteById(memberPO.getMemberId());
                            break;
                        }
                    }
                } else {
                    log.warn("DailyTrainingMember operationType is null! group = {}", memberPO);
                }
            }
        }
    }


}
