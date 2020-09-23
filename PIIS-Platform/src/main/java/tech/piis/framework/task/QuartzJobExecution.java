package tech.piis.framework.task;

import org.quartz.JobExecutionContext;
import tech.piis.modules.monitor.domain.SysJob;

/**
 * 定时任务处理（允许并发执行）
 * 
 * @author Kevin<EastascendWang@gmail.com>
 *
 */
public class QuartzJobExecution extends AbstractQuartzJob
{
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception
    {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
