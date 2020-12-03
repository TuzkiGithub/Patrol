package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionNotificationPO;
import java.util.List;

/**
 * 情况通报 Service接口
 *
 * @author Tuzki
 * @date 2020-11-23
 */
public interface IInspectionNotificationService {

    /**
     * 查询情况通报 列表
     * @param inspectionNotification
     * @return
     * @throws BaseException
     */
    List<InspectionNotificationPO> selectInspectionNotificationList(InspectionNotificationPO inspectionNotification) throws BaseException;

    /**
    * 新增情况通报 
    * @param inspectionNotification
    * @return
    * @throws BaseException
    */
    int save(InspectionNotificationPO inspectionNotification) throws BaseException;

    /**
     * 根据ID修改情况通报 
     * @param inspectionNotification
     * @return
     * @throws BaseException
     */
    int update(InspectionNotificationPO inspectionNotification) throws BaseException;

    /**
     * 根据ID批量删除情况通报 
     * @param notificationIds 情况通报 编号
     *
     * @return
     */
    int deleteByInspectionNotificationIds(String[] notificationIds);

    /**
     * 统计总数
     * @return
     */
    int count();
}
