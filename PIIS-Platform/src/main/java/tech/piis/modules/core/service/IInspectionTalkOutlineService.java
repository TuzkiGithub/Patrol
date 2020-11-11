package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.domain.po.InspectionTalkOutlinePO;
import java.util.List;

/**
 * 谈话提纲Service接口
 *
 * @author Tuzki
 * @date 2020-11-04
 */
public interface IInspectionTalkOutlineService {
    /**
     * 查询谈话提纲列表
     * @param inspectionTalkOutline
     * @return
     * @throws BaseException
     */
    List<InspectionTalkOutlinePO> selectInspectionTalkOutlineList(InspectionTalkOutlinePO inspectionTalkOutline) throws BaseException;

    /**
     * 根据ID修改谈话提纲
     * @param inspectionTalkOutline
     * @return
     * @throws BaseException
     */
    int update(InspectionTalkOutlinePO inspectionTalkOutline) throws BaseException;

}
