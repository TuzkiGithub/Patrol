package tech.piis.modules.core.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.po.InspectionMobilizeAttendeePO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.domain.dto
 * User: Tuzki
 * Date: 2020/9/30
 * Time: 9:12
 * Description:动员人员文件DTO
 */

@Data
@Accessors(chain = true)
public class MobilizedAttendeeFileDTO {

    /**
     * 会议通知文件
     */
    private List<PiisDocumentPO> meetingFiles;

    /**
     * 动员人员信息及文件
     */
    private List<InspectionMobilizeAttendeePO> mobilizeAttendeeList;
}
