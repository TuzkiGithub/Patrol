package tech.piis.framework.web.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import tech.piis.common.constant.HttpStatus;
import tech.piis.common.utils.DateUtils;
import tech.piis.common.utils.StringUtils;
import tech.piis.common.utils.sql.SqlUtil;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.PageDomain;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.framework.web.page.TableSupport;
import tech.piis.modules.core.domain.vo.UserBriefVO;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * web层通用数据处理
 */
public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * controller初始化方法
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置默认分页
     */
    public void initPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (null == pageNum || null == pageSize) {
            pageDomain.setPageNum(0);
            pageDomain.setPageSize(10);
        }
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows, String msg) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error(msg);
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows, int code, String msg) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error(code, msg);
    }


    /**
     * 将人员拼接字符串转为List
     *
     * @param userId
     * @param userName
     */
    protected List<UserBriefVO> paramsCovert2List(String userId, String userName) {
        List<UserBriefVO> result = new ArrayList<>();
        if (!org.springframework.util.StringUtils.isEmpty(userId) && !org.springframework.util.StringUtils.isEmpty(userName)) {
            String[] userIdsArr = userId.split(",");
            String[] userNamesArr = userName.split(",");
            if (userNamesArr.length != 0 && userIdsArr.length != 0) {
                int length = userNamesArr.length;
                for (int i = 0; i < length; i++) {
                    UserBriefVO userBriefVO = new UserBriefVO();
                    userBriefVO.setUserName(userNamesArr[i]);
                    userBriefVO.setUserId(userIdsArr[i]);
                    result.add(userBriefVO);
                }
            }
        }
        return result;
    }

    /**
     * 将人员对象数组转为字符串  以，分割
     *
     * @param data
     */
    protected List<String> paramsCovert2String(List<UserBriefVO> data) {
        List<String> result = new ArrayList<>();
        StringBuilder userId = new StringBuilder();
        StringBuilder userName = new StringBuilder();
        if (!CollectionUtils.isEmpty(data)) {
            data.forEach(var -> {
                userId.append(var.getUserId()).append(",");
                userName.append(var.getUserName()).append(",");
            });
        }
        String userIdStr = userId.toString().substring(0, userId.toString().lastIndexOf(","));
        String userNameStr = userName.toString().substring(0, userName.toString().lastIndexOf(","));
        result.add(userIdStr);
        result.add(userNameStr);
        return result;
    }

}
