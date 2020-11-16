package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.utils.BizUtils;
import tech.piis.modules.core.domain.po.InspectionTalkOutlinePO;
import tech.piis.modules.core.mapper.InspectionTalkOutlineMapper;
import tech.piis.modules.core.service.IInspectionTalkOutlineService;

import java.util.List;

import static tech.piis.common.constant.OperationConstants.*;

/**
 * 谈话提纲Service业务层处理
 *
 * @author Tuzki
 * @date 2020-11-04
 */
@Transactional
@Service
public class InspectionTalkOutlineServiceImpl implements IInspectionTalkOutlineService {
    @Autowired
    private InspectionTalkOutlineMapper inspectionTalkOutlineMapper;

    /**
     * 查询谈话提纲列表
     *
     * @param inspectionTalkOutline
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionTalkOutlinePO> selectInspectionTalkOutlineList(InspectionTalkOutlinePO inspectionTalkOutline) throws BaseException {
        return inspectionTalkOutlineMapper.selectTalkOutlineList(inspectionTalkOutline.getPlanId());
    }

    /**
     * 根据ID修改谈话提纲
     *
     * @param inspectionTalkOutline
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionTalkOutlinePO inspectionTalkOutline) throws BaseException {
        int row = -1;
        Integer operationType = inspectionTalkOutline.getOperationType();
        switch (operationType) {
            case INSERT:
                BizUtils.setCreatedOperation(InspectionTalkOutlinePO.class, inspectionTalkOutline);
                List<InspectionTalkOutlinePO> questionList = inspectionTalkOutline.getQuestionList();
                row = inspectionTalkOutlineMapper.insert(inspectionTalkOutline.setTalkClassificationId(-1L).setQuestionList(null));
                if (!CollectionUtils.isEmpty(questionList)) {
                    questionList.forEach(question -> {
                        BizUtils.setCreatedOperation(InspectionTalkOutlinePO.class, question);
                        question.setPlanId(inspectionTalkOutline.getPlanId());
                        question.setTalkClassificationId(inspectionTalkOutline.getTalkOutlineId());
                        inspectionTalkOutlineMapper.insert(question.setQuestionList(null));
                    });
                }
                break;
            case UPDATE:
                //先删除后新增
                QueryWrapper<InspectionTalkOutlinePO> delQueryWrapper = new QueryWrapper<>();
                delQueryWrapper.eq("TALK_CLASSIFICATION_ID", inspectionTalkOutline.getTalkOutlineId());
                inspectionTalkOutlineMapper.delete(delQueryWrapper);

                List<InspectionTalkOutlinePO> questions = inspectionTalkOutline.getQuestionList();
                if (!CollectionUtils.isEmpty(questions)) {
                    questions.forEach(question -> {
                        BizUtils.setCreatedOperation(InspectionTalkOutlinePO.class, question);
                        question.setPlanId(inspectionTalkOutline.getPlanId());
                        question.setTalkClassificationId(inspectionTalkOutline.getTalkOutlineId());
                        inspectionTalkOutlineMapper.insert(question);
                    });
                }
                BizUtils.setUpdatedOperation(InspectionTalkOutlinePO.class, inspectionTalkOutline);
                row = inspectionTalkOutlineMapper.updateById(inspectionTalkOutline.setQuestionList(null).setTalkQuestion(null));
                break;
            case DELETE:
                inspectionTalkOutlineMapper.deleteById(inspectionTalkOutline.getTalkOutlineId());
                QueryWrapper<InspectionTalkOutlinePO> delQuery = new QueryWrapper<>();
                delQuery.eq("TALK_CLASSIFICATION_ID", inspectionTalkOutline.getTalkOutlineId());
                row = inspectionTalkOutlineMapper.delete(delQuery);
                break;
        }
        return row;
    }

}
