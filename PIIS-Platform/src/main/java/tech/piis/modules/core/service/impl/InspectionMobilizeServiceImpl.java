package tech.piis.modules.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.piis.common.enums.FileEnum;
import tech.piis.common.enums.OperationEnum;
import tech.piis.common.utils.DateUtils;
import tech.piis.common.utils.IdUtils;
import tech.piis.common.utils.StringUtils;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.utils.file.FileUploadUtils;
import tech.piis.modules.core.domain.po.InspectionMobilizeAttendeePO;
import tech.piis.modules.core.domain.po.InspectionMobilizePO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.mapper.InspectionMobilizeAttendeeMapper;
import tech.piis.modules.core.mapper.InspectionMobilizeMapper;
import tech.piis.modules.core.mapper.PiisDocumentMapper;
import tech.piis.modules.core.service.IInspectionMobilizeService;

import java.util.ArrayList;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.*;

/**
 * 巡视动员 Service业务层处理
 *
 * @author Kevin
 * @date 2020-09-27
 */
@Slf4j
@Service
public class InspectionMobilizeServiceImpl implements IInspectionMobilizeService {
    @Autowired
    private InspectionMobilizeMapper inspectionMobilizeMapper;

    @Autowired
    private InspectionMobilizeAttendeeMapper inspectionMobilizeAttendeeMapper;

    /**
     * 有些原生的mybatis plus方法，还未来得及封装到service中
     */
    @Autowired
    private PiisDocumentMapper documentMapper;

    @Value("${piis.profile}")
    private String baseFileUrl;

    /**
     * 定义文件字典ID映射字段
     */
    private static final int MAPPING_MEETING_TOPIC_FILE = 1;
    private static final int MAPPING_TEACHING_FILE = 2;
    private static final int MAPPING_INVITATION_FILE = 3;
    private static final int MAPPING_SPEECH_DRAFT_FILE = 4;


    /**
     * 查询巡视动员信息
     *
     * @param inspectionMobilize
     * @return
     */
    @Override
    public InspectionMobilizePO findMobilize(InspectionMobilizePO inspectionMobilize) throws Exception {
        return inspectionMobilizeMapper.selectMobilizeList(inspectionMobilize);
    }

    /**
     * 新增巡视动员信息
     *
     * @param inspectionMobilize
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public int saveInspectionMobilize(InspectionMobilizePO inspectionMobilize) throws Exception {
        //新增巡视动员
        int row = insertMobilize(inspectionMobilize);
        //批量新增巡视动员人员
        insertMobilizeAttendeeBatch(inspectionMobilize.getInspectionMobilizeAttendeeList());
        //更新文件表
        updateFile(inspectionMobilize);
        return row;
    }


    /**
     * 新增巡视组动员信息表
     *
     * @param inspectionMobilize
     * @return
     */
    private int insertMobilize(InspectionMobilizePO inspectionMobilize) throws Exception {
        BizUtils.setCreatedOperation(InspectionMobilizePO.class, inspectionMobilize);
        //巡视动员主键生成
        String mobilizeId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
        inspectionMobilize.setMobilizeId(mobilizeId);
        //新增巡视动员信息
        int row = inspectionMobilizeMapper.insert(inspectionMobilize);
        List<InspectionMobilizeAttendeePO> mobilizeAttendeeList = inspectionMobilize.getInspectionMobilizeAttendeeList();
        //设置关联字段
        if (!CollectionUtils.isEmpty(mobilizeAttendeeList)) {
            mobilizeAttendeeList.forEach(inspectionMobilizeAttendee -> inspectionMobilizeAttendee.setMobilizeId(inspectionMobilize.getMobilizeId()));
        }
        return row;
    }

    /**
     * 批量新增巡视组组员信息
     *
     * @param inspectionMobilizeAttendeeList
     * @return
     */
    private int insertMobilizeAttendeeBatch(List<InspectionMobilizeAttendeePO> inspectionMobilizeAttendeeList) throws Exception {
        if (!CollectionUtils.isEmpty(inspectionMobilizeAttendeeList)) {
            for (InspectionMobilizeAttendeePO mobilizeAttendee : inspectionMobilizeAttendeeList) {
                BizUtils.setCreatedOperation(InspectionMobilizeAttendeePO.class, mobilizeAttendee);
            }
        }
        return inspectionMobilizeAttendeeMapper.saveMobilizeAttendeeBatch(inspectionMobilizeAttendeeList);
    }

    /**
     * 修改巡视动员信息
     *
     * @param inspectionMobilize
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public int updateInspectionMobilize(InspectionMobilizePO inspectionMobilize) throws Exception {
        //更新人员
        updateMobilizeAttendee(inspectionMobilize);
        //更新文件
        updateFile(inspectionMobilize);
        //修改巡视动员信息
        BizUtils.setUpdatedOperation(InspectionMobilizePO.class, inspectionMobilize);
        return inspectionMobilizeMapper.updateById(inspectionMobilize.setInspectionMobilizeAttendeeList(null));
    }

    /**
     * 更新动员人员
     *
     * @param inspectionMobilize
     * @return
     */
    private void updateMobilizeAttendee(InspectionMobilizePO inspectionMobilize) {
        List<InspectionMobilizeAttendeePO> mobilizeAttendeeList = inspectionMobilize.getInspectionMobilizeAttendeeList();
        if (!CollectionUtils.isEmpty(mobilizeAttendeeList)) {
            mobilizeAttendeeList.forEach(mobilizeAttendee -> {
                mobilizeAttendee.setMobilizeId(inspectionMobilize.getMobilizeId());
                Integer operationType = mobilizeAttendee.getOperationType();
                if (null != operationType) {
                    switch (operationType) {
                        case INSERT: {
                            BizUtils.setCreatedOperation(InspectionMobilizeAttendeePO.class, mobilizeAttendee);
                            inspectionMobilizeAttendeeMapper.insert(mobilizeAttendee);
                            break;
                        }
                        case UPDATE: {
                            BizUtils.setUpdatedOperation(InspectionMobilizeAttendeePO.class, mobilizeAttendee);
                            inspectionMobilizeAttendeeMapper.updateById(mobilizeAttendee);
                            break;
                        }
                        case DELETE: {
                            inspectionMobilizeAttendeeMapper.deleteById(mobilizeAttendee.getAttendeeId());
                            break;
                        }
                    }
                }
            });
        }
    }

    /**
     * 更新文件表
     *
     * @param mobilize
     */
    private void updateFile(InspectionMobilizePO mobilize) {
        List<PiisDocumentPO> meetingsFiles = mobilize.getDocuments();
        if (!CollectionUtils.isEmpty(meetingsFiles)) {
            meetingsFiles.forEach(meetingsFile -> {
                int operationType = meetingsFile.getOperationType();
                //根据文件操作字段更新/删除文件
                if (operationType == OperationEnum.INSERT.getCode()) {
                    //更新文件表
                    meetingsFile.setObjectId(mobilize.getMobilizeId())
                            .setFileDictId(FileEnum.MEETING_TOPIC_FILE.getCode());
                    BizUtils.setUpdatedOperation(PiisDocumentPO.class, meetingsFile);
                    documentMapper.updateById(meetingsFile);
                }

                if (operationType == OperationEnum.DELETE.getCode()) {
                    documentMapper.deleteById(meetingsFile.getPiisDocId());
                    String filePath = meetingsFile.getFilePath();
                    if (!StringUtils.isEmpty(filePath)) {
                        FileUploadUtils.deleteServerFile(filePath.replace(filePath, baseFileUrl));
                    }

                }
            });
        }

        List<InspectionMobilizeAttendeePO> mobilizeAttendeeList = mobilize.getInspectionMobilizeAttendeeList();
        if (!CollectionUtils.isEmpty(mobilizeAttendeeList)) {
            mobilizeAttendeeList.forEach(mobilizeAttendee -> {
                List<PiisDocumentPO> documents = mobilizeAttendee.getDocuments();
                if (!CollectionUtils.isEmpty(documents)) {
                    documents.forEach(document -> {
                        /**
                         * document对象中包含上传文件状态、文件名（人员ID、文件类型）
                         *
                         * fileName
                         *          part1 -> 人员ID
                         *          part2 -> 文件类型
                         *          part3 -> 文件名
                         */
                        Integer operationType = document.getOperationType();
                        String fileName = document.getFileName();
                        String filePath = document.getFilePath();
                        String temp[] = fileName.split("\\|");
                        //动员ID
                        String attendeeId = temp[0];
                        //获取文件类型
                        int dictIdMapping = Integer.parseInt(temp[1]);
                        //文件实际名称
                        String actualFileName = temp[2];
                        Long dictId = -1L;
                        switch (dictIdMapping) {
                            case MAPPING_TEACHING_FILE:
                                dictId = FileEnum.TEACHING_FILE.getCode();
                                break;
                            case MAPPING_INVITATION_FILE:
                                dictId = FileEnum.INVITATION_FILE.getCode();
                                break;
                            case MAPPING_SPEECH_DRAFT_FILE:
                                dictId = FileEnum.SPEECH_DRAFT_FILE.getCode();
                                break;
                        }

                        //根据文件操作字段更新/删除文件
                        if (operationType == OperationEnum.INSERT.getCode()) {

                            //更新文件表
                            document.setObjectId(String.valueOf(mobilizeAttendee.getMobilizeAttendeeId()))
                                    .setFileDictId(dictId)
                                    .setFileName(actualFileName);
                            BizUtils.setUpdatedOperation(PiisDocumentPO.class, document);
                            documentMapper.updateById(document);
                        }

                        if (operationType == OperationEnum.DELETE.getCode()) {
                            documentMapper.deleteById(document.getPiisDocId());
                            if (!StringUtils.isEmpty(filePath)) {
                                FileUploadUtils.deleteServerFile(filePath.replace(filePath, baseFileUrl));
                            }

                        }
                    });
                }
            });
        }

    }

    /**
     * 根据巡视动员ID查询文件列表
     *
     * @param mobilizedId
     * @return
     */
    @Override
    public List<PiisDocumentPO> getFileByMobilizeId(String mobilizedId) throws Exception {
        InspectionMobilizePO mobilize = inspectionMobilizeMapper.selectMobilizeList(new InspectionMobilizePO().setMobilizeId(mobilizedId));
        List<String> attendeeIds = new ArrayList<>();
        List<InspectionMobilizeAttendeePO> mobilizeAttendeeList = mobilize.getInspectionMobilizeAttendeeList();
        if (!CollectionUtils.isEmpty(mobilizeAttendeeList)) {
            mobilizeAttendeeList.forEach(var -> attendeeIds.add(String.valueOf(var.getMobilizeAttendeeId())));
        }
        return documentMapper.selectBatchIds(attendeeIds);
    }

}