package tech.piis.common.enums;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.common.enums
 * User: Tuzki
 * Date: 2020/9/25
 * Time: 10:44
 * Description:文件枚举字典类
 */
public enum FileEnum {
    WORK_PREPARED_OTHER_FILE("工作准备相关附件", 29L),
    MEETING_TOPIC_FILE("会议通知文件", 30L),
    TEACHING_FILE("授课课件", 31L),
    INVITATION_FILE("邀请函", 32L),
    SPEECH_DRAFT_FILE("演讲稿", 33L),
    SPECIAL_REPORT_FILE("专题报告相关附件", 34L),
    CONSULT_INFO_FILE("查阅资料相关附件", 35L),
    CONSULT_DETAIL_FILE("查阅资料详情相关附件", 36L),
    ORG_OTHER_FILE("组务会相关附件", 37L),
    BRANCH_OTHER_FILE("支部会相关附件", 38L),
    ATTENDEE_OTHER_FILE("参会情况相关附件", 39L),
    SINKING_OTHER_FILE("下沉了解相关附件", 40L),
    SINKING_RESULT_FILE("下沉了解结果文件", 41L),
    INVESTIGATION_OTHER_FILE("调研走访相关附件", 42L),
    VISIT_SEAL_FILE("来访盖章版文件", 43L),
    VISIT_OTHER_FILE("来访材料", 44L),
    CALL_SEAL_FILE("来电盖章版文件", 45L),
    CALL_OTHER_FILE("来电材料", 46L),
    PERSONAL_OTHER_FILE("个人事项", 47L);


    private String name;

    private Long code;

    FileEnum(String name, Long code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }
}
