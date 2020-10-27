package tech.piis.ex_service.auth;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.piis.common.utils.http.HttpClientUtils;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.system.mapper.SysUserDetailMapper;

/**
 * Created with IntelliJ IDEA.
 * Package: cn.spring.ssm.auth
 * User: 25414
 * Date: 2020/2/4
 * Time: 9:49
 * Description:IAM认证类
 */
@RestController
@Slf4j
@RequestMapping("iam")
public class IAMAuth {

    /**
     * get code url
     **/
    @Value("${iam.code.url}")
    private String code_url;

    /**
     * get access_token url
     */
    @Value("${iam.token.url}")
    private String access_token_url;

    /**
     * get userBasicInfo url
     **/
    @Value("${iam.user.url}")
    private String userInfo_url;

    /**
     * 客户端ID
     */
    @Value("${iam.clientId}")
    private String clientId;

    /**
     * 表示客户端的secret，必选项
     */
    @Value("${iam.clientSecret}")
    private String clientSecret;

    /**
     * 获取code的回调地址
     **/
    @Value("${iam.redirect.url}")
    private String redirectUrl;

    /**
     * 返回类型，此时固定为code
     **/
    @Value("${iam.responseType}")
    private String response_type;

    /**
     * 应用授权作用域
     **/
    @Value("${iam.scope}")
    private String scope;

    /**
     * 表示客户端的当前状态，必选项，可以指定任意值，认证服务器会原封不动地返回这个值
     */
    @Value("${iam.state}")
    private String state;

    /**
     * 授权方式，为authorization_code 固定值，必选项
     */
    @Value("${iam.grantType}")
    private String grantType;

    /**
     * 连接符
     **/
    private static final String and = "&";
    private static final String con = "?";
    private static final String eq = "=";

    @Autowired
    private SysUserDetailMapper sysUserDetailMapper;


    /**
     * 获取code
     * @param code code
     * @param state 应用系统标识
     * @return
     * @throws Exception
     */
    @RequestMapping("auth")
    public AjaxResult getCode(@RequestParam(value = "code", required = false) String code, @RequestParam(value = "state", required = false) String state) throws Exception {
        if (StringUtils.isEmpty(code)) {
            String url = code_url +
                    con + "client_id" + eq + clientId +
                    and + "redirect_uri" + eq + redirectUrl +
                    and + "scope" + eq + scope +
                    and + "response_type" + eq + response_type +
                    and + "state" + eq + state;
            log.info("###IAM认证### Get code url = {}", url);
            HttpClientUtils.doGet(url);
            return AjaxResult.success("###IAM认证###获取code start");
        }
        log.info("###IAM认证### code = {}, state = {}", code, state);
        return getUserInfo(code, state);
    }


    /**
     * 根据应用信息获取AccessToken
     *
     * @return
     */
    private String getAccess_token(String code) throws Exception {
        log.info("###IAM认证 Get accessToken code = {}", code);
        String url = access_token_url +
                con + "client_id" + eq + clientId +
                and + "client_secret" + eq + clientSecret +
                and + "code" + eq + code +
                and + "grant_type" + eq + grantType +
                and + "redirect_uri" + eq + redirectUrl;
        String result = HttpClientUtils.doPostParamToUrl(url, null);
        String access_token = null;
        if (!StringUtils.isEmpty(result)) {
            JSONObject resultJson = JSONObject.parseObject(result);
            access_token = resultJson.getString("access_token");
        }
        log.info("###IAM认证 Get accessToken = {}", access_token);
        return access_token;
    }

    /**
     * 根据code获取用户基本信息
     *
     * @param code
     * @param state
     * @return
     * @throws Exception
     */
    private AjaxResult getUserInfo(String code, String state) throws Exception {
        log.info("###IAM认证 getUserInfo code = {}, state = {}", code, state);
        //获取Token
        String access_token = getAccess_token(code);
        if (StringUtils.isEmpty(access_token)) {
            return AjaxResult.error("token is null!");
        }

        String url = userInfo_url + con + "access_token" + eq + access_token;
        //获取用户基本信息
        log.info("###IAM认证 userInfo url = {}", url);
        String userInfo = HttpClientUtils.doPostParamToUrl(url, null);
        log.info("###IAM认证 userInfo = {}", userInfo);

        if (StringUtils.isEmpty(userInfo)) {
            return AjaxResult.error("userInfo is null!");
        }
        String userId;
        JSONObject resultJson = JSONObject.parseObject(userInfo);
        userId = resultJson.getString("username");
        log.info("###IAM认证 userId = {}", userId);
        return AjaxResult.success(sysUserDetailMapper.selectUserAllByUserId(userId));
    }


}
