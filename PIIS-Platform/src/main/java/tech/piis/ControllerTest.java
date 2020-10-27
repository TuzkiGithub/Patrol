package tech.piis;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.piis.ex_service.job.SyncData;
import tech.piis.framework.security.LoginBody;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.core.service.impl.InspectionPlanServiceImpl;
import tech.piis.modules.system.domain.SysDept;
import tech.piis.modules.system.domain.SysUser;
import tech.piis.modules.system.mapper.SysDeptMapper;
import tech.piis.modules.system.mapper.SysPostMapper;
import tech.piis.modules.system.mapper.SysRoleMapper;
import tech.piis.modules.system.mapper.SysUserDetailMapper;

import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis
 * User: Tuzki
 * Date: 2020/9/3
 * Time: 20:59
 * Description:
 */
@RequestMapping("test")
@RestController
public class ControllerTest {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysUserDetailMapper sysUserDetailMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private InspectionPlanServiceImpl planService;


    @Autowired
    private SyncData syncData;

    @GetMapping("auth")
    public String auth(LoginBody loginBody, HttpServletResponse httpServletResponse) throws Exception {
        CloseableHttpClient httpClient = null;
        CookieStore cookieStore = new BasicCookieStore();
        httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpPost httpPost = new HttpPost("http://localhost:8080/login");
        httpPost.addHeader("Content-Type", "application/json");
        StringEntity body = new StringEntity(JSON.toJSONString(loginBody), "utf-8");
        httpPost.setEntity(body);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String context = EntityUtils.toString(entity, HTTP.UTF_8);
        List<Cookie> cookies = cookieStore.getCookies();
        String token = null;
        System.out.println("=================== cookie start =======================");
        for (Cookie cookie : cookies) {
            System.out.print("name = " + cookie + ", value = " + cookie.getValue());
            System.out.println();
            if (cookie.getName().equals("token")) {
                token = cookie.getValue();
            }
        }
        System.out.println("=================== cookie end ======================");
        httpServletResponse.addCookie(new javax.servlet.http.Cookie("token", token));
        return "yes";
    }

    @GetMapping("dept")
    public String testDept() {
        SysDept dept = new SysDept();
        dept.setDeptId("100");
        dept.setLeaf(false);
        dept.setLeader("@@@");
        sysDeptMapper.updateDept(dept);
        return "0";
    }

    @GetMapping("sync/dept")
    public int syncDept() throws Exception {
        syncData.syncDept();
        return 0;
    }

    @GetMapping("sync/user")
    public int syncUser() throws URISyntaxException {
        syncData.syncUser();
        return 0;
    }

    @GetMapping("/userDeptPost")
    public AjaxResult selectUserDeptPost(SysUser user) {
        return AjaxResult.success(sysUserDetailMapper.selectUserDeptPost(user));
    }


    @GetMapping("transaction")
    public int testTransaction() throws Exception{
        planService.testTransaction();
        return 0;
    }
}
