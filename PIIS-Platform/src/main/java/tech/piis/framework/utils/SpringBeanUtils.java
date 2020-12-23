package tech.piis.framework.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import tech.piis.modules.core.service.impl.*;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.framework.utils
 * User: Tuzki
 * Date: 2020/11/30
 * Time: 14:00
 * Description:
 */
@Component
public class SpringBeanUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * 根据代办任务名称获取spring容器中bean
     *
     * @param todoName 代办任务名称
     * @return
     */
    public Object getBeanByCustom(String todoName) {
        if (StringUtils.isEmpty(todoName)) {
            return null;
        }
        String formName = todoName.substring(0, todoName.indexOf("]"));
        formName = formName.substring(1);
        Class clazz = null;
        switch (formName) {
            case "巡视动员会":
                clazz = InspectionMobilizationServiceImpl.class;
                break;
            case "公告信息":
                clazz = InspectionAnnouncementInfoServiceImpl.class;
                break;
            case "问卷测评":
//                clazz = InspectionMobilizationServiceImpl.class;
                break;
            case "听取汇报":
                clazz = InspectionSpecialReportServiceImpl.class;
                break;
            case "个别谈话":
//                clazz = InspectionMobilizationServiceImpl.class;
                break;
            case "查阅资料":
                clazz = InspectionConsultInfoServiceImpl.class;
                break;
            case "列席会议":
                clazz = InspectionPresentMeetingServiceImpl.class;
                break;
            case "座谈会":
                clazz = InspectionForumServiceImpl.class;
                break;
            case "信访管理-来电":
                clazz = InspectionCallVisitServiceImpl.class;
                break;
            case "信访管理-来访":
                clazz = InspectionVisitServiceImpl.class;
                break;
            case "走访调研":
                clazz = InspectionInvestigationVisitServiceImpl.class;
                break;
            case "组务会":
                clazz = InspectionOrganizationMeetingsServiceImpl.class;
                break;
            case "临时党支部会":
                clazz = InspectionOrganizationMeetingsServiceImpl.class;
                break;
            case "下沉了解":
                clazz = InspectionSinkingUnderstandingServiceImpl.class;
                break;
            case "抽查个人事项报告":
                clazz = InspectionCheckPersonMattersServiceImpl.class;
                break;
            case "重要情况报告":
                clazz = InspectionImportantReportServiceImpl.class;
                break;
            case "立行立改":
                clazz = InspectionLegislationReformServiceImpl.class;
                break;
            case "作风纪律后评估":
//                clazz = InspectionMobilizationServiceImpl.class;
                break;
            case "问题底稿":
                clazz = InspectionProblemDraftServiceImpl.class;
                break;
            case "会议研究":
                clazz = InspectionMeetingsResearchServiceImpl.class;
                break;
            case "巡视报告":
                clazz = InspectionPatrolReportServiceImpl.class;
                break;
            case "反馈意见":
                clazz = InspectionClueTransferServiceImpl.class;
                break;
            case "反馈方案":
//                clazz = InspectionProblemDraftServiceImpl.class;
                break;
            case "反馈会":
                clazz = InspectionFeedbackMeetingsServiceImpl.class;
                break;
            case "线索移交":
                clazz = InspectionClueTransferServiceImpl.class;
                break;
            case "整改公开情况":
                clazz = InspectionRectificationServiceImpl.class;
                break;

        }
        assert clazz != null;
        return applicationContext.getBean(clazz);
    }

    /**
     * Set the ApplicationContext that this object runs in.
     * Normally this call will be used to initialize the object.
     * <p>Invoked after population of normal bean properties but before an init callback such
     * as {@link InitializingBean#afterPropertiesSet()}
     *
     * @throws BeansException if thrown by application context methods
     * @see BeanInitializationException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringBeanUtils.applicationContext == null) {
            SpringBeanUtils.applicationContext = applicationContext;
        }
    }
}
