package tech.piis.modules.core.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.framework.web
 * User: Tuzki
 * Date: 2020/11/27
 * Time: 13:47
 * Description:更新业务表审批状态事件处理器TEST
 */
@Component
public class WorkFlowEventHandler implements ApplicationListener<WorkFlowEvent> {
    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(WorkFlowEvent event) {
        System.out.println("WorkFlowEventHandler Handler..");
        if (event.getSource() instanceof WorkFlowEventHandler) {
            System.out.println("do somethings");
        }
    }
}
