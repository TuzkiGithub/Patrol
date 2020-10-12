package tech.piis.framework.job;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tech.piis.common.constant.ResultEnum;
import tech.piis.common.core.lang.UUID;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.common.domain.DeptExternal;
import tech.piis.modules.common.domain.DeptResponse;
import tech.piis.modules.common.domain.UserExternal;
import tech.piis.modules.common.domain.UserResponse;
import tech.piis.modules.system.domain.*;
import tech.piis.modules.system.mapper.*;

import java.util.*;

import static tech.piis.common.constant.HttpStatus.SUCCESS;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.framework.job
 * User: Tuzki
 * Date: 2020/10/8
 * Time: 16:32
 * Description:同步部门、岗位、员工数据
 */
@Component
public class SyncData {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysUserDeptMapper userDeptMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    /**
     * 从数据港同步部门数据
     */
    @Transactional
    public AjaxResult syncDept() {
        String deptData = "{\n" +
                "  \"code\": 200,\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1804000000\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"大数据部\",\n" +
                "      \"orgFullNm\": \"大数据部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1804000000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1805000000\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"战略规划部\",\n" +
                "      \"orgFullNm\": \"战略规划部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1805000000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1806000000\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"人力资源部\",\n" +
                "      \"orgFullNm\": \"人力资源部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1806000000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1807000000\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"综合管理部\",\n" +
                "      \"orgFullNm\": \"综合管理部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1807000000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1808000000\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"财务管理部\",\n" +
                "      \"orgFullNm\": \"财务管理部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1808000000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1809000000\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"党群工作部\",\n" +
                "      \"orgFullNm\": \"党群工作部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1809000000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1810000000\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"风险管理部\",\n" +
                "      \"orgFullNm\": \"风险管理部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1810000000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1899000000\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"外包项目组\",\n" +
                "      \"orgFullNm\": \"外包项目组\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1899000000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1800000000\",\n" +
                "      \"upGrpOrgId\": \"0000000000\",\n" +
                "      \"orgShtNm\": \"光大科技\",\n" +
                "      \"orgFullNm\": \"光大科技有限公司\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1800000001\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"光大云缴费\",\n" +
                "      \"orgFullNm\": \"光大云缴费科技有限公司\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1800000001\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1801000001\",\n" +
                "      \"upGrpOrgId\": \"1800000001\",\n" +
                "      \"orgShtNm\": \"综合管理部\",\n" +
                "      \"orgFullNm\": \"综合管理部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1800000001|1801000001\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1802000001\",\n" +
                "      \"upGrpOrgId\": \"1800000001\",\n" +
                "      \"orgShtNm\": \"市场拓展部\",\n" +
                "      \"orgFullNm\": \"市场拓展部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1800000001|1802000001\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1803000001\",\n" +
                "      \"upGrpOrgId\": \"1800000001\",\n" +
                "      \"orgShtNm\": \"投资及机构管理部\",\n" +
                "      \"orgFullNm\": \"投资及机构管理部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1800000001|1803000001\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1804000001\",\n" +
                "      \"upGrpOrgId\": \"1800000001\",\n" +
                "      \"orgShtNm\": \"产品与平台支持部\",\n" +
                "      \"orgFullNm\": \"产品与平台支持部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1800000001|1804000001\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1801000000\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"总经理室\",\n" +
                "      \"orgFullNm\": \"总经理室\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1801000000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1802000000\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"产品研发部\",\n" +
                "      \"orgFullNm\": \"产品研发部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1802000000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpOrgId\": \"1803000000\",\n" +
                "      \"upGrpOrgId\": \"1800000000\",\n" +
                "      \"orgShtNm\": \"智能云计算部\",\n" +
                "      \"orgFullNm\": \"智能云计算部\",\n" +
                "      \"orgStatCd\": \"1\",\n" +
                "      \"orgIdHrcyStr\": \"0000000000|1800000000|1803000000\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";
        DeptResponse deptResponse = JSONObject.parseObject(deptData, DeptResponse.class);
        if (deptResponse.getCode() == SUCCESS) {
            syncDept(deptResponse.getData());
        } else {
            return new AjaxResult(ResultEnum.EXTERNAL_FAILED.getCode(), ResultEnum.EXTERNAL_FAILED.getMsg());
        }
        return new AjaxResult(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg());
    }


    /**
     * 从数据港同步用户数据
     */
    public AjaxResult syncUser() {
        StringBuilder userData = new StringBuilder("");
        String userData1 = "{\n" +
                "  \"code\": 200,\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"grpEmpUnifId\": \"18000110\",\n" +
                "      \"empNm\": \"吴**\",\n" +
                "      \"genderCd\": \"1\",\n" +
                "      \"workTel\": null,\n" +
                "      \"moblNum\": \"***\",\n" +
                "      \"belgGrpDeptId\": \"1806000000\",\n" +
                "      \"dutyStatCd\": \"1\",\n" +
                "      \"empEml\": \"***\",\n" +
                "      \"currPostn\": \"员工\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpEmpUnifId\": \"18000167\",\n" +
                "      \"empNm\": \"王**\",\n" +
                "      \"genderCd\": \"2\",\n" +
                "      \"workTel\": null,\n" +
                "      \"moblNum\": \"***\",\n" +
                "      \"belgGrpDeptId\": \"1806000000\",\n" +
                "      \"dutyStatCd\": \"1\",\n" +
                "      \"empEml\": \"***\",\n" +
                "      \"currPostn\": \"员工\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpEmpUnifId\": \"18000181\",\n" +
                "      \"empNm\": \"周**\",\n" +
                "      \"genderCd\": \"2\",\n" +
                "      \"workTel\": null,\n" +
                "      \"moblNum\": \"***\",\n" +
                "      \"belgGrpDeptId\": \"1806000000\",\n" +
                "      \"dutyStatCd\": \"1\",\n" +
                "      \"empEml\": \"***\",\n" +
                "      \"currPostn\": \"经理\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpEmpUnifId\": \"18000103\",\n" +
                "      \"empNm\": \"赵**\",\n" +
                "      \"genderCd\": \"2\",\n" +
                "      \"workTel\": null,\n" +
                "      \"moblNum\": \"***\",\n" +
                "      \"belgGrpDeptId\": \"1806000000\",\n" +
                "      \"dutyStatCd\": \"1\",\n" +
                "      \"empEml\": \"***\",\n" +
                "      \"currPostn\": \"部门总经理\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpEmpUnifId\": \"18000364\",\n" +
                "      \"empNm\": \"陈*\",\n" +
                "      \"genderCd\": \"2\",\n" +
                "      \"workTel\": null,\n" +
                "      \"moblNum\": \"***\",\n" +
                "      \"belgGrpDeptId\": \"1806000000\",\n" +
                "      \"dutyStatCd\": \"1\",\n" +
                "      \"empEml\": \"***\",\n" +
                "      \"currPostn\": \"员工\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"grpEmpUnifId\": \"18000252\",\n" +
                "      \"empNm\": \"李*\",\n" +
                "      \"genderCd\": \"1\",\n" +
                "      \"workTel\": null,\n" +
                "      \"moblNum\": \"***\",\n" +
                "      \"belgGrpDeptId\": \"1806000000\",\n" +
                "      \"dutyStatCd\": \"1\",\n" +
                "      \"empEml\": \"***\",\n" +
                "      \"currPostn\": \"经理\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";
        UserResponse userResponse = JSONObject.parseObject(userData1, UserResponse.class);
        if (userResponse.getCode() == SUCCESS) {
            syncUser(userResponse.getData());
        } else {
            return new AjaxResult(ResultEnum.EXTERNAL_FAILED.getCode(), ResultEnum.EXTERNAL_FAILED.getMsg());
        }
        return new AjaxResult(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg());
    }

    /**
     * 同步用户数据
     *
     * @param userExternalList
     */
    public void syncUser(List<UserExternal> userExternalList) {
        /** 示例数据
         * {
         *       "grpEmpUnifId": "18000110",
         *       "empNm": "吴**",
         *       "genderCd": "1",
         *       "workTel": null,
         *       "moblNum": "***",
         *       "belgGrpDeptId": "1806000000",
         *       "dutyStatCd": "1",
         *       "empEml": "***",
         *       "currPostn": "员工"
         *     }
         */
        if (!CollectionUtils.isEmpty(userExternalList)) {
            //用户表
            List<SysUser> users = new ArrayList<>(userExternalList.size());
            //用户-部门关系表
            List<SysUserDept> userDepts = new ArrayList<>(userExternalList.size());
            //岗位表
            List<SysPost> posts = new ArrayList<>(userExternalList.size());
            //用户-岗位关系表
            List<SysUserPost> userPosts = new ArrayList<>(userExternalList.size());

            List<String> tempPosts = new ArrayList<>();
            userExternalList.forEach(userExternal -> {
                SysUser user = new SysUser();
                String userId = userExternal.getGrpEmpUnifId();
                user.setUserId(userId);
                user.setUserName(userExternal.getEmpNm());
                user.setEmail(userExternal.getEmpEml());
                user.setPhonenumber(userExternal.getMoblNum());
                String gender = userExternal.getGenderCd();
                if (!StringUtils.isEmpty(gender)) {
                    switch (gender) {
                        case "1":
                            gender = "0";
                            break;
                        case "2":
                            gender = "1";
                            break;
                        default:
                            gender = "2";
                            break;
                    }
                    user.setSex(gender);
                }
                BizUtils.setCreatedOperation(SysUser.class, user);
                BizUtils.setUpdatedOperation(SysUser.class, user);

                SysUserDept userDept = new SysUserDept();
                userDept.setUserId(userId);
                userDept.setDeptId(userExternal.getBelgGrpDeptId());
                BizUtils.setCreatedOperation(SysUserDept.class, userDept);
                BizUtils.setUpdatedOperation(SysUserDept.class, userDept);

                SysPost post = new SysPost();
                SysUserPost userPost = new SysUserPost();
                String currPostn = userExternal.getCurrPostn();
                SysPost temp = postMapper.checkPostNameUnique(currPostn);
                boolean flag = true;//list中是否存在相同岗位
                if (!CollectionUtils.isEmpty(tempPosts)) {
                    for (String tempPost : tempPosts) {
                        if (Objects.equals(tempPost, currPostn)) {
                            flag = false;
                            break;
                        }
                    }
                }

                tempPosts.add(currPostn);
                String postId = null;
                if (null != temp) {
                    postId = temp.getPostId();
                }else {
                    postId = UUID.fastUUID().toString().substring(6) + System.currentTimeMillis();
                }

                if (null == temp && flag) {
                    post.setPostId(postId);
                    post.setPostName(currPostn);
                    post.setStatus("0");
                    posts.add(post);
                }
                BizUtils.setCreatedOperation(SysPost.class, post);
                BizUtils.setUpdatedOperation(SysPost.class, post);
                BizUtils.setCreatedOperation(SysUserPost.class, userPost);
                BizUtils.setUpdatedOperation(SysUserPost.class, userPost);

                userPost.setUserId(userId);
                userPost.setPostId(postId);
                userPosts.add(userPost);
                users.add(user);
                userDepts.add(userDept);
            });

            if (!CollectionUtils.isEmpty(users)) {
                userMapper.insertBatch(users);
            }
            if (!CollectionUtils.isEmpty(posts)) {
                postMapper.insertPostBatch(posts);
            }
            //先删除原有的用户部门关系
            if (!CollectionUtils.isEmpty(userDepts)) {
                userDeptMapper.delUserDeptBatch(userDepts);
                userDeptMapper.batchUserDept(userDepts);
            }
            //先删除原有的用户岗位关系
            if (!CollectionUtils.isEmpty(userPosts)) {
                userPostMapper.delUserPostBatch(userPosts);
                userPostMapper.batchUserPost(userPosts);
            }
        }

    }

    /**
     * 同步部门数据
     */
    public void syncDept(List<DeptExternal> deptExternalList) {
        /** 示例数据
         * {
         *       "grpOrgId": "1804000000",
         *       "upGrpOrgId": "1800000000",
         *       "orgShtNm": "大数据部",
         *       "orgFullNm": "大数据部",
         *       "orgStatCd": "1",
         *       "orgIdHrcyStr": "0000000000|1800000000|1804000000"
         *     },
         */
        if (!CollectionUtils.isEmpty(deptExternalList)) {
            List<SysDept> deptList = new ArrayList<>(deptExternalList.size());
            deptExternalList.forEach(deptExternal -> {
                SysDept dept = new SysDept();
                dept.setDeptId(deptExternal.getGrpOrgId());
                dept.setParentId(deptExternal.getUpGrpOrgId());
                dept.setDeptName(deptExternal.getOrgFullNm());
                String status = deptExternal.getOrgStatCd();
                if ("1".equals(status) || "5".equals(status)) {
                    dept.setStatus("0");
                } else {
                    dept.setStatus("1");
                }
                String ancestors = deptExternal.getOrgIdHrcyStr();
                StringBuilder newAncestors = new StringBuilder();
                ancestors = ancestors.replace("|", ",").replace(deptExternal.getGrpOrgId(), "");
                String[] parentIds = ancestors.split(",");
                Collections.reverse(Arrays.asList(parentIds));
                for (String parentId : parentIds) {
                    newAncestors.append(parentId).append(",");
                }
                newAncestors = new StringBuilder(newAncestors.substring(0, newAncestors.lastIndexOf(",")));
                dept.setAncestors(newAncestors.toString());
                dept.setLeaf(deptMapper.hasChildByDeptId(dept.getDeptId()) <= 0);
                BizUtils.setCreatedOperation(SysDept.class, dept);
                BizUtils.setUpdatedOperation(SysDept.class, dept);
                deptList.add(dept);
            });
            deptMapper.insertDeptBatch(deptList);
        }
    }
}
