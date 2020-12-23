package tech.piis.framework.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tech.piis.common.utils.ServletUtils;
import tech.piis.framework.annotation.DataPermission;
import tech.piis.framework.security.LoginUser;
import tech.piis.framework.security.service.TokenService;
import tech.piis.modules.core.domain.po.PIBaseApprovalEntityPO;
import tech.piis.modules.core.domain.po.PIBaseEntity;
import tech.piis.modules.core.domain.vo.PlanConditionVO;
import tech.piis.modules.core.service.IInspectionPlanService;
import tech.piis.modules.system.domain.SysUser;
import tech.piis.modules.system.service.ISysRoleService;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Autowired
    private ISysRoleService roleService;

    @Value("${piis.unFilterRoleIdStr}")
    private String unFilterRoleIdStr;

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

    private void handleDataScope(final JoinPoint joinPoint) {
        // 获得注解
        DataPermission controllerDataScope = getAnnotationLog(joinPoint);
        if (null != controllerDataScope) {
            // 获取当前的用户
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            SysUser user = loginUser.getUser();
            //某些角色无需数据过滤
            List<Integer> roleIdList = roleService.selectRoleListByUserId(user.getUserId());
            if (!CollectionUtils.isEmpty(roleIdList)) {
                String[] unFilterRoleIdArr = unFilterRoleIdStr.split(",");
                List<String> unFilterRoleIdList = Arrays.asList(unFilterRoleIdArr);
                for (Integer roleId : roleIdList) {
                    if (unFilterRoleIdList.contains(String.valueOf(roleId))) {
                        return;
                    }
                }
            }

            //查询用户参与过的巡视情况
            List<PlanConditionVO> planConditionList = planService.selectPiisConditionByUserId(user.getUserId());
            if (!CollectionUtils.isEmpty(planConditionList)) {
                Set<String> planIdDataScope = null;
                Set<String> groupIdDataScope = null;
                if (!CollectionUtils.isEmpty(roleIdList)) {
                    planIdDataScope = new HashSet<>(roleIdList.size());
                    groupIdDataScope = new HashSet<>(roleIdList.size());
                    for (PlanConditionVO planConditionVO : planConditionList) {
                        planIdDataScope.add(planConditionVO.getPlanId());
                        groupIdDataScope.add(planConditionVO.getGroupId());
                    }
                }
                PIBaseEntity piBaseEntity;
                PIBaseApprovalEntityPO piBaseApprovalEntityPO;
                Object[] args = joinPoint.getArgs();
                if (null != args && args.length != 0) {
                    for (int i = 0; i < joinPoint.getArgs().length; i++) {
                        if (joinPoint.getArgs()[i] instanceof PIBaseEntity) {
                            piBaseEntity = (PIBaseEntity) joinPoint.getArgs()[i];
                            piBaseEntity.setPlanIdDataScope(planIdDataScope)
                                    .setGroupIdDataScope(groupIdDataScope);
                        }
                        if (joinPoint.getArgs()[i] instanceof PIBaseApprovalEntityPO) {
                            piBaseApprovalEntityPO = (PIBaseApprovalEntityPO) joinPoint.getArgs()[i];
                            piBaseApprovalEntityPO.setPlanIdDataScope(planIdDataScope)
                                    .setGroupIdDataScope(groupIdDataScope);
                        }
                    }
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
