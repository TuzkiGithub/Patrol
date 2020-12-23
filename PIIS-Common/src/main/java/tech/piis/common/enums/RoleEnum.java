package tech.piis.common.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户状态
 *
 * @author Tuzki
 */
public enum RoleEnum {
    ADMIN(1L, "管理员"),
    COMMON(2L, "普通用户"),
    INSPECTION_LEADER(3L, "集团巡视领导小组"),
    INSPECTION_DEPART_LEADER(4L, "处室领导"),
    INSPECTION_SYS(5L, "巡视办操作员"),
    INSPECTION_BUSS(6L, "巡视办普通人员"),
    CIRCULATION_DEPARTMENT(7L, "传阅部门"),
    SYS_MANAGEMENT(8L, "系统管理员"),
    INSPECTION_GROUP_LEADER(9L, "巡视组组长"),
    INSPECTION_GROUP_MEMBER(10L, "巡视组成员"),
    PATROL_LEADER(11L, "巡察组领导小组"),
    PATROL_SYS(12L, "巡察办操作员"),
    PATROL_BUSS(13L, "巡察办普通人员"),
    PATROL_GROUP_LEADER(14L, "巡察组组长"),
    PATROL_GROUP_MEMBER(15L, "巡察组成员");

    private Long roleId;
    private String roleName;

    /**
     * 巡视办角色
     */
    private static List<Long> inspectionRoleList = new ArrayList<>();

    /**
     * 巡视组角色
     */
    private static List<Long> inspectionGroupRoleList = new ArrayList<>();

    /**
     * 巡察办角色
     */
    private static List<Long> patrolRoleList = new ArrayList<>();

    /**
     * 巡察组角色
     */
    private static List<Long> patrolGroupRoleList = new ArrayList<>();

    static {
        inspectionRoleList.add(RoleEnum.INSPECTION_LEADER.getRoleId());
        inspectionRoleList.add(RoleEnum.INSPECTION_DEPART_LEADER.getRoleId());
        inspectionRoleList.add(RoleEnum.INSPECTION_SYS.getRoleId());
        inspectionRoleList.add(RoleEnum.INSPECTION_BUSS.getRoleId());
        inspectionRoleList.add(RoleEnum.CIRCULATION_DEPARTMENT.getRoleId());
        inspectionRoleList.add(RoleEnum.SYS_MANAGEMENT.getRoleId());

        inspectionGroupRoleList.add(RoleEnum.INSPECTION_GROUP_LEADER.getRoleId());
        inspectionGroupRoleList.add(RoleEnum.INSPECTION_GROUP_MEMBER.getRoleId());

        patrolRoleList.add(RoleEnum.PATROL_LEADER.getRoleId());
        patrolRoleList.add(RoleEnum.PATROL_SYS.getRoleId());
        patrolRoleList.add(RoleEnum.PATROL_BUSS.getRoleId());

        patrolGroupRoleList.add(RoleEnum.PATROL_GROUP_LEADER.getRoleId());
        patrolGroupRoleList.add(RoleEnum.PATROL_GROUP_MEMBER.getRoleId());
    }


    RoleEnum(Long roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public static List<Long> getInspectionRoleList() {
        return inspectionRoleList;
    }

    public static void setInspectionRoleList(List<Long> inspectionRoleList) {
        RoleEnum.inspectionRoleList = inspectionRoleList;
    }

    public static List<Long> getInspectionGroupRoleList() {
        return inspectionGroupRoleList;
    }

    public static void setInspectionGroupRoleList(List<Long> inspectionGroupRoleList) {
        RoleEnum.inspectionGroupRoleList = inspectionGroupRoleList;
    }

    public static List<Long> getPatrolRoleList() {
        return patrolRoleList;
    }

    public static void setPatrolRoleList(List<Long> patrolRoleList) {
        RoleEnum.patrolRoleList = patrolRoleList;
    }

    public static List<Long> getPatrolGroupRoleList() {
        return patrolGroupRoleList;
    }

    public static void setPatrolGroupRoleList(List<Long> patrolGroupRoleList) {
        RoleEnum.patrolGroupRoleList = patrolGroupRoleList;
    }
}
