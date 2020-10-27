package tech.piis.ex_service.job;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tech.piis.common.constant.ResultEnum;
import tech.piis.common.core.lang.UUID;
import tech.piis.common.utils.DateUtils;
import tech.piis.common.utils.http.HttpClientUtils;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.utils.SecurityUtils;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.common.domain.DeptExternal;
import tech.piis.modules.common.domain.DeptResponse;
import tech.piis.modules.common.domain.UserExternal;
import tech.piis.modules.common.domain.UserResponse;
import tech.piis.modules.system.domain.*;
import tech.piis.modules.system.mapper.*;

import java.net.URISyntaxException;
import java.util.*;

import static tech.piis.common.constant.HttpStatus.SUCCESS;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.ex_service.job
 * User: Tuzki
 * Date: 2020/10/8
 * Time: 16:32
 * Description:同步部门、岗位、员工数据
 */
@Slf4j
@Component("SyncData")
@Transactional
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

    @Autowired
    private SysPostHistoryMapper postHistoryMapper;

    @Value("${data.dept.url}")
    private String GET_DEPT_URL;

    @Value("${data.user.url}")
    private String GET_USER_URL;

    /**
     * 从数据港同步部门数据
     */
    public AjaxResult syncDept() throws Exception {

        String syncDeptUrl = GET_DEPT_URL + DateUtils.dateTimeNow("yyyyMMdd");
        log.info("================================同步部门增量数据START================================ get dept url = {}", syncDeptUrl);
        DeptResponse deptResponse = JSONObject.parseObject(HttpClientUtils.doGet(GET_DEPT_URL), DeptResponse.class);
        if (null != deptResponse) {
            if (deptResponse.getCode() == SUCCESS) {
                syncDept(deptResponse.getData());
            } else {
                log.error("同步部门增量数据失败！ code = {}, msg = {}", deptResponse.getCode(), deptResponse.getMessage());
                return new AjaxResult(ResultEnum.EXTERNAL_FAILED.getCode(), ResultEnum.EXTERNAL_FAILED.getMsg());
            }
        } else {
            log.error("同步部门增量数据失败！ deptResponse is null");
            return new AjaxResult(ResultEnum.EXTERNAL_FAILED.getCode(), ResultEnum.EXTERNAL_FAILED.getMsg());
        }
        log.info("================================同步部门增量数据END================================");
        return new AjaxResult(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg());
    }


    /**
     * 从数据港同步用户数据
     */
    public AjaxResult syncUser() throws URISyntaxException {
        String syncUserUrl = GET_USER_URL + DateUtils.dateTimeNow("yyyyMMdd");
        log.info("================================同步用户增量数据START================================ ger user url = {}", syncUserUrl);
        UserResponse userResponse = JSONObject.parseObject(HttpClientUtils.doGet(GET_USER_URL), UserResponse.class);
        if (null != userResponse) {
            if (userResponse.getCode() == SUCCESS) {
                syncUser(userResponse.getData());
            } else {
                log.error("同步用户增量数据失败！ code = {}, msg = {}", userResponse.getCode(), userResponse.getMessage());
                return new AjaxResult(ResultEnum.EXTERNAL_FAILED.getCode(), ResultEnum.EXTERNAL_FAILED.getMsg());
            }
        } else {
            log.error("同步用户增量数据失败！ userResponse is null!");
            return new AjaxResult(ResultEnum.EXTERNAL_FAILED.getCode(), ResultEnum.EXTERNAL_FAILED.getMsg());
        }
        log.info("================================同步用户增量数据END================================");
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
            //历史岗位表
            List<SysPostHistory> postHistories = new ArrayList<>(userExternalList.size());

            //存放临时岗位名称
            List<String> tempPosts = new ArrayList<>();
            userExternalList.forEach(userExternal -> {
                SysUser user = new SysUser();
                String userId = userExternal.getGrpEmpUnifId();
                user.setUserId(userId);
                user.setUserName(userExternal.getEmpNm());
                user.setEmail(userExternal.getEmpEml());
                user.setPhonenumber(userExternal.getMoblNum());
                user.setPassword(SecurityUtils.encryptPassword(user.getUserId()));
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

                //判断数据库中是否存在当前岗位，null不存在
                SysPost temp = postMapper.checkPostNameUnique(currPostn);

                //判断结果中是否存在当前岗位，true不存在
                boolean flag = true;
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
                } else {
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

                SysPostHistory postHistory = new SysPostHistory();
                postHistory.setUserId(userId)
                        .setPostName(currPostn)
                        .setCreatedTime(DateUtils.getNowDate());
                userPost.setUserId(userId);
                userPost.setPostId(postId);
                userPosts.add(userPost);

                QueryWrapper<SysPostHistory> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id", userId);
                queryWrapper.eq("post_name", currPostn);
                if (CollectionUtils.isEmpty(postHistoryMapper.selectList(queryWrapper))) {
                    postHistories.add(postHistory);
                }
                users.add(user);
                userDepts.add(userDept);
            });

            if (!CollectionUtils.isEmpty(users)) {
                userMapper.insertBatch(users);
            }
            if (!CollectionUtils.isEmpty(posts)) {
                postMapper.insertPostBatch(posts);
            }
            if (!CollectionUtils.isEmpty(postHistories)) {
                postHistoryMapper.insertPostHistoryBatch(postHistories);
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
                //设置祖先节点
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
