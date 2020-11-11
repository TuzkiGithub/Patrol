package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.INSERT;

import  tech.piis.modules.core.mapper.InspectionQuotationMapper;
import  tech.piis.modules.core.domain.po.InspectionQuotationPO;
import  tech.piis.modules.core.service.IInspectionQuotationService;

/**
 * 语录Service业务层处理
 *
 * @author Tuzki
 * @date 2020-11-06
 */
@Transactional
@Service
public class InspectionQuotationServiceImpl implements IInspectionQuotationService {
    @Autowired
    private InspectionQuotationMapper inspectionQuotationMapper;


    /**
     * 查询语录列表
     * @param inspectionQuotation
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionQuotationPO> selectInspectionQuotationList(InspectionQuotationPO inspectionQuotation) throws BaseException {
        return inspectionQuotationMapper.selectList(null);
    }

    /**
     * 新增语录
     * @param inspectionQuotation
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionQuotationPO inspectionQuotation) throws BaseException {
        return inspectionQuotationMapper.insert(inspectionQuotation);
    }

    /**
     * 根据ID修改语录
     * @param inspectionQuotation
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionQuotationPO inspectionQuotation) throws BaseException {
        return inspectionQuotationMapper.updateById(inspectionQuotation);
    }

    /**
     * 根据ID批量删除语录
     * @param quotationIds 语录编号
     *
     * @return
     */
    @Override
    public int deleteByInspectionQuotationIds(Long[]quotationIds) {
        List<Long> list = Arrays.asList(quotationIds);
        return inspectionQuotationMapper.deleteBatchIds(list);
    }

}
