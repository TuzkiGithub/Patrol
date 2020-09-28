package tech.piis.modules.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.piis.framework.config.PIISConfig;
import tech.piis.framework.utils.file.FileUploadUtils;
import tech.piis.modules.core.domain.po.InspectionMobilizeAttendeePO;
import tech.piis.modules.core.domain.po.InspectionMobilizePO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.mapper.InspectionMobilizeAttendeeMapper;
import tech.piis.modules.core.mapper.InspectionMobilizeMapper;
import tech.piis.modules.core.mapper.PiisDocumentMapper;
import tech.piis.modules.core.service.IInspectionMobilizeService;

import java.util.List;
import java.util.Objects;

/**
 * 巡视动员 Service业务层处理
 *
 * @author Kevin
 * @date 2020-09-27
 */
@Service
public class InspectionMobilizeServiceImpl implements IInspectionMobilizeService {
    @Autowired
    private InspectionMobilizeMapper inspectionMobilizeMapper;

    @Autowired
    private InspectionMobilizeAttendeeMapper inspectionMobilizeAttendeeMapper;

    @Autowired
    private PiisDocumentMapper documentMapper;


    /**
     * 查询巡视动员信息
     *
     * @param inspectionMobilize
     * @return
     */
    @Override
    public InspectionMobilizePO findMobilize(InspectionMobilizePO inspectionMobilize) throws Exception {
        return inspectionMobilizeMapper.selectById(inspectionMobilize.getPlanId());
    }

    /**
     * 新增巡视动员信息
     *
     * @param inspectionMobilize
     * @return
     * @throws Exception
     */
    @Override
    public int saveInspectionMobilize(InspectionMobilizePO inspectionMobilize, MultipartFile[] files) throws Exception {
        //新增巡视动员信息
        int row = inspectionMobilizeMapper.insert(inspectionMobilize.setInspectionMobilizeAttendeeList(null));
        List<InspectionMobilizeAttendeePO> mobilizeAttendeeList = inspectionMobilize.getInspectionMobilizeAttendeeList();
        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();

            /**
             * part1 -> 人员ID
             * part2 -> 文件类型
             * part3 -> 文件名
             */
            assert filename != null;
            String temp[] = filename.split("\\|");
            String attendeeId = temp[0];
            Long dictId = Long.parseLong(temp[1]);
            String currFileName = temp[2];

            mobilizeAttendeeList.forEach(mobilizeAttendee -> {
                if (Objects.equals(mobilizeAttendee.getAttendeeId(), attendeeId)) {
                    //新增人员
                    inspectionMobilizeAttendeeMapper.insert(mobilizeAttendee);
                    String MobilizeAttendeeId = mobilizeAttendee.getAttendeeId();

                    //插入文件表
                    PiisDocumentPO document = new PiisDocumentPO()
                            .setObjectId(MobilizeAttendeeId)
                            .setFileDictId(dictId)
                            .setFileName(currFileName)
                            .setFileSize(file.getSize())
                            .setFilePath(PIISConfig.getProfile() + filename);
                    documentMapper.insert(document);
                }
            });

        }
        return row;
    }


    /**
     * 修改巡视动员信息
     *
     * @param inspectionMobilize
     * @return
     * @throws Exception
     */
    @Override
    public int updateInspectionMobilize(InspectionMobilizePO inspectionMobilize, MultipartFile[] files) throws Exception {

        //更新人员

        //编辑文件
        editMobilizeFile(inspectionMobilize.getInspectionMobilizeAttendeeList(), files);
        //修改巡视动员信息
        inspectionMobilizeMapper.updateById(inspectionMobilize.setInspectionMobilizeAttendeeList(null));
        return 0;
    }

    /**
     * 编辑文件
     * @param inspectionMobilizeAttendeeList
     * @param files
     */
    private void editMobilizeFile(List<InspectionMobilizeAttendeePO> inspectionMobilizeAttendeeList, MultipartFile[] files) {
        //删除原有文件

        //新增文件
    }
}