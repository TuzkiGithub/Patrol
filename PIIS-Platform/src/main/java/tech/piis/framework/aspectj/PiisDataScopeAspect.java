package tech.piis.framework.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.piis.common.utils.ServletUtils;
import tech.piis.framework.annotation.DataPermission;
import tech.piis.framework.security.LoginUser;
import tech.piis.framework.security.service.TokenService;
import tech.piis.modules.core.domain.po.PIBaseEntity;
import tech.piis.modules.core.domain.vo.PlanConditionVO;
import tech.piis.modules.core.service.IInspectionPlanService;
import tech.piis.modules.system.domain.SysUser;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.framework.aspectj
 * User: Tuzki
 * Date: 2020/12/1
 * Time: 18:34
 * Description:数据过滤处理切面
 */
@Aspect
@Component
public class PiisDataScopeAspect {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IInspectionPlanService planService;

    /*
     * 配置织入点
     */
    @Pointcut("@annotation(tech.piis.framework.annotation.DataPermission)")
    public void dataScopePointCut() {
    }

    @Before("dataScopePointCut()")
    public void doBefore(JoinPoint point) {
        handleDataScope(point);
    }

    protected void handleDataScope(final JoinPoint joinPoint) {
        // 获得注解
        DataPermission controllerDataScope = getAnnotationLog(joinPoint);
        if (null != controllerDataScope) {
            // 获取当前的用户
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            SysUser user = loginUser.getUser();
            //查询用户参与过的巡视情况
            List<PlanConditionVO> planConditionList = planService.selectPiisConditionByUserId(user.getUserId());
            PIBaseEntity baseEntity;
            Object[] args = joinPoint.getArgs();
            if (null != args && args.length != 0) {
                for (int i = 0; i < joinPoint.getArgs().length; i++) {
                }
            }

        }

    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private DataPermission getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(DataPermission.class);
        }
        return null;
    }
}
