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
    SPEECH_DRAFT_FILE("演讲稿", 33L);


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
