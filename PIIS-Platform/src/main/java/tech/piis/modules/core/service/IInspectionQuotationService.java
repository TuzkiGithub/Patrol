package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.domain.po.InspectionQuotationPO;
import java.util.List;

/**
 * 语录Service接口
 *
 * @author Tuzki
 * @date 2020-11-06
 */
public interface IInspectionQuotationService {

    /**
     * 查询语录列表
     *
     * @param inspectionQuotation
     * @return
     * @throws BaseException
     */
    List<InspectionQuotationPO> selectInspectionQuotationList(InspectionQuotationPO inspectionQuotation) throws BaseException;

    /**
     * 新增语录
     *
     * @param inspectionQuotation
     * @return
     * @throws BaseException
     */
    int save(InspectionQuotationPO inspectionQuotation) throws BaseException;

    /**
     * 根据ID修改语录
     *
     * @param inspectionQuotation
     * @return
     * @throws BaseException
     */
    int update(InspectionQuotationPO inspectionQuotation) throws BaseException;

    /**
     * 根据ID批量删除语录
     *
     * @param quotationIds 语录编号
     * @return
     */
    int deleteByInspectionQuotationIds(Long[] quotationIds);

}
