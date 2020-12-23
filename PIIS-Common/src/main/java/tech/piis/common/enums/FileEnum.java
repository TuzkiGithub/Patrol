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
    PERSONAL_OTHER_FILE("个人事项", 47L),
    PIIS_INFORMATION_FILE("巡视巡察了解情况文件", 48L),
    SUPPORTING_MATERIALS_FILE("支撑材料目录", 49L),
    PROBLEM_DRAFT_FILE("问题底稿其他材料", 50L),
    LEGISLATION_REFORM_FILE("立行立改相关附件", 51L),
    IMPORTANT_SPORT_FILE("重要报告相关附件", 52L),
    APPROVAL_FILE("审批表", 53L),
    HANDOVER_FILE("交接单", 54L),
    COMMITTEE_OTHER_FILE("党委会相关附件", 55L),
    REPORT_APPROVAL_FILE("报请审批附件", 56L),
    TEMP_BRANCH_FILE("临时支部成立文件", 57L),
    LEARN_TRAIN_FILE("学习培训会议通知文件", 58L),
    GENERAL_SECRETARY_FILE("总书记重要讲话文件", 59L),
    AUTH_APPOINT_FILE("授权任职决定文件", 60L),
    MOBILIZATION_DEPLOYMENT_FILE("动员部署讲话文件", 61L),
    PIIS_PROJECT_FILE("巡视项目附件", 62L),
    PIIS_SITE_ARRANGEMENT_FILE("巡视现场安排文件", 63L),
    PIIS_WORK_RULE_FILE("巡视组工作制度文件", 64L),
    PIIS_NEGATIVE_TALK_FILE("巡视谈话负面清单文件", 65L),
    PIIS_TEMP_BRANCH_FILE("巡视组临时党支部成立文件", 66L),
    OPERATION_OTHER_FILE("工作人员手册其他文件", 67L),
    NOTICE_CONTENT_FILE("通报内容文件", 68L),
    INVITATION_OTHER_FILE("邀请函文件", 69L),
    PRINT_TOPIC_FILE("印发通知文件", 70L),
    SPOT_MATERIALS_FILE("进驻材料参会领导讲话文件", 71L),
    GROUP_SPEECH_FILE("组长讲话文件", 72L),
    SPOT_NEWS_FILE("进驻新闻通告", 73L),
    SPOT_OTHER_FILE("进驻材料其他文件", 74L),
    GROUP_FILE("巡视组任命文件", 75L),
    AGENDA_FILE("会议议程文件", 76L),
    SECRETARY_SPEECH_FILE("被巡视党组织党委书记表态讲话文件", 77L),
    ANNOUNCEMENT_INFO_FILE("公告信息文件", 78L),
    FORUM_OTHER_FILE("座谈会相关附件", 79L),
    PRESENT_OTHER_FILE("列席会议相关附件", 80L),
    REPORT_OTHER_FILE_ONE("报请审批报请党委审批巡视方案的签报批示页", 81L),
    REPORT_OTHER_FILE_TWO("报请审批报请党委审批的巡视方案", 82L),
    TEMP_OTHER_FILE_ONE("临时党支部审批巡视方案的签报批示页", 83L),
    TEMP_OTHER_FILE_TWO("临时党支部报请党委审批的巡视方案", 84L),
    MEETINGS_RESEARCH_FILE("会议研究文件", 85L),
    PATROL_REPORT_FILE("巡视报告文件", 86L),
    FEEDBACK_PLAN_FILE("反馈方案文件", 87L),
    FEEDBACK_MEETINGS_FILE("反馈会文件", 88L),
    RECTIFICATION_FILE_ONE("含被巡视巡察党组织在整改通报文件", 89L),
    RECTIFICATION_FILE_TWO("单位内部公开整改情况文件", 90L);

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
