package tech.piis.auth;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.piis.framework.utils.HttpUtils;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.system.mapper.SysUserDetailMapper;

import java.util.HashMap;
import java.util.Map;

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
@RequestMapping("auth")
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


    @Autowired
    private SysUserDetailMapper sysUserDetailMapper;

    /**
     * 拼接url获取code
     *
     * @throws Exception
     */
    @RequestMapping("auth")
    public void getCode() throws Exception {
        String url = code_url +
                and + redirectUrl +
                and + scope +
                and + clientId +
                and + response_type +
                and + state;
        log.info("######Get code url = {}", url);
        HttpUtils.get(url);
    }


    /**
     * 根据应用信息获取AccessToken
     *
     * @return
     */
    private String getAccess_token(String code) throws Exception {
        log.info("######Get accessToken code = {}", code);
        String url = access_token_url +
                con + clientId +
                and + clientSecret +
                and + code +
                and + grantType +
                and + redirectUrl;
        String result = HttpUtils.get(url);
        String access_token = null;
        if (!StringUtils.isEmpty(result)) {
            JSONObject resultJson = JSONObject.parseObject(result);
            access_token = resultJson.getString("access_token");
        }
        log.info("######Get accessToken = {}", access_token);
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
    @RequestMapping("user")
    public AjaxResult getUserInfo(@RequestParam("code") String code, @RequestParam("state") String state) throws Exception {
        log.info("######Auth getUserInfo code = {}, state = {}", code, state);
        //获取Token
        String access_token = getAccess_token(code);
        if (StringUtils.isEmpty(access_token)) {
            return AjaxResult.error("token is null!");
        }

        String url = userInfo_url +
                con + code +
                and + access_token;
        //获取用户基本信息
        log.info("######Get userInfo url = {}", url);
        Map<String, String> params = new HashMap<>();
        params.put("access_token", access_token);
        String userInfo = HttpUtils.postParamToBody(url, params);
        log.info("######Get userInfo = {}", userInfo);

        if (StringUtils.isEmpty(userInfo)) {
            return AjaxResult.error("userInfo is null!");
        }
        String userId;
        JSONObject resultJson = JSONObject.parseObject(userInfo);
        userId = resultJson.getString("username");
        log.info("######Get userId = {}", userId);
        return AjaxResult.success(sysUserDetailMapper.selectUserAllByUserId(userId));
    }


}
