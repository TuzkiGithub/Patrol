package tech.piis.modules.core.event;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.event
 * User: Tuzki
 * Date: 2020/11/30
 * Time: 8:28
 * Description:审批中处理事件
 */
@Slf4j
public class WorkFlowEvent extends ApplicationEvent {

    /**
     * 0 查询
     * 1 新增
     * 2 更新
     * 3 删除
     */
    @TableField(exist = false)
    private Integer eventType;

    /**
     * 业务数据
     */
    private Object data;

    /**
     * 业务表单ID
     */
    private String bizId;

    /**
     * 是否继续审批
     * 0无需继续审批 1：需要上级继续审批
     */
    private Integer continueApprovalFlag;

    /**
     * 是否同意状态
     * 0：不同意
     * 1：同意
     *
     */
    private Integer agreeFlag;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public WorkFlowEvent(Object source) {
        super(source);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public Integer getContinueApprovalFlag() {
        return continueApprovalFlag;
    }

    public void setContinueApprovalFlag(Integer continueApprovalFlag) {
        this.continueApprovalFlag = continueApprovalFlag;
    }

    public Integer getAgreeFlag() {
        return agreeFlag;
    }

    public void setAgreeFlag(Integer agreeFlag) {
        this.agreeFlag = agreeFlag;
    }
}
