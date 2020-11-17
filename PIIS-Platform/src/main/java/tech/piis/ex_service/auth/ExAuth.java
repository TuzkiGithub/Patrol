package tech.piis.ex_service.auth;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.piis.common.exception.BaseException;
import tech.piis.common.utils.http.HttpClientUtils;
import tech.piis.framework.redis.RedisCache;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.system.service.ISysUserService;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.ex_service.auth
 * User: Tuzki
 * Date: 2020/11/5
 * Time: 8:58
 * Description:E信认证类
 */
@RestController
@Slf4j
public class ExAuth {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;
    /**
     * get code url
     **/
    @Value("${ex.code.url}")
    private String code_url;

    /**
     * 获取code的回调地址
     **/
    @Value("${ex.redirect.url}")
    private String redirect_uri;

    /**
     * get access_token url
     */
    @Value("${ex.token.url}")
    private String access_token_url;

    /**
     * get userBasicInfo url
     **/
    @Value("${ex.user.url}")
    private String userInfo_url;

    /**
     * 返回类型，此时固定为code
     **/
    @Value("${ex.responseType}")
    private String response_type;

    /**
     * 应用授权作用域
     **/
    @Value("${ex.scope}")
    private String scope;

    /**
     * 企业的CorpID
     */
    @Value("${ex.appId}")
    private String appId;

    /**
     * 应用Id
     **/
    @Value("${ex.agentId}")
    private String agentId;

    /**
     * 终端使用此参数判断是否需要带上身份信息
     **/
    @Value("${ex.weChat}")
    private String weChat_redirect;

    /**
     * 企业CorpId
     **/
    @Value("${ex.corpId}")
    private String corpId;

    /**
     * 应用的凭证密钥
     */
    @Value("${ex.corpSecret}")
    private String corpSecret;

    /**
     * 连接符
     **/
    private static final String and = "&";
    private static final String con = "?";
    private static final String eq = "=";


    @RequestMapping("oauthCode")
    public void getCode() {
        String url = code_url +
                con + "appid" + eq + appId +
                and + "redirect_uri" + eq + redirect_uri +
                and + "response_type" + eq + response_type +
                and + "scope" + eq + scope +
                and + "agentId" + eq + agentId + weChat_redirect;
        log.info("###EX认证 Get code url = {}", url);
        try {
            HttpClientUtils.doGet(url);
        } catch (URISyntaxException e) {
            log.error("###EX认证 获取Code接口失败!");
            e.printStackTrace();
        }
    }

    @RequestMapping("oauthLogin")
    public AjaxResult getUser(String code) {
        log.info("###EX认证 code = {}", code);
        //获取code
        if (StringUtils.isEmpty(code)) {
            return AjaxResult.error("code is null!");
        }
        //获取accessToken
        String token = getAccessToken();
        if (StringUtils.isEmpty(token)) {
            return AjaxResult.error("token is null!");
        }
        log.info("###EX认证 token = {}", token);
        //获取userId
        String userId = getUserId(code, token);
        if (StringUtils.isEmpty(userId)) {
            return AjaxResult.error("userId is null!");
        }
        log.info("###EX认证 userId = {}", userId);
        //获取用户信息
        return AjaxResult.success(userService.selectUserById(userId));
    }


    /**
     * 获取token
     * 先从redis中获取，失效则调接口
     *
     * @return
     */
    private String getAccessToken() {
        String token = null;
        try {
            token = redisCache.getCacheObject("EX_TOKEN");
        } catch (Exception e) {
            log.info("###EX认证 缓存中获取Token失败！");
            throw new BaseException("###EX认证 缓存中获取Token失败！");
        }
        if (!StringUtils.isEmpty(token)) {
            return token;
        } else {
            return getAccessTokenByHttp();
        }
    }

    private String getAccessTokenByHttp() {
        Map<String, String> tokenParamMap = new HashMap<>();
        tokenParamMap.put("corpid", corpId);
        tokenParamMap.put("corpsecret", corpSecret);

        log.info("###EX 认证 getAccessToken url = {}, params = {}", access_token_url, tokenParamMap);
        String result = HttpClientUtils.doPostParamToBody(access_token_url, tokenParamMap);
        log.info("###EX 认证 getAccessToken result = {}", result);

        String token = null;
        if (!StringUtils.isEmpty(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.getInteger("errcode") == 0) {
                token = jsonObject.getString("access_token");
                /**
                 * 待优化，缓存失败不应影响正常业务流程
                 */
                redisCache.setCacheObject("EX_TOKEN", token, 5, TimeUnit.MINUTES);
            }
        }
        return token;
    }

    /**
     * 获取userId
     *
     * @param code
     * @param token
     * @return
     */
    private String getUserId(String code, String token) {
        Map<String, String> urlParamMap = new HashMap<>();
        urlParamMap.put("code", code);
        urlParamMap.put("access_token", token);
        log.info("###EX认证 get UserInfo userUrl = {}, params = {}", userInfo_url, urlParamMap);
        String result = HttpClientUtils.doPostParamToBody(userInfo_url, urlParamMap);
        log.info("###EX认证 get UserInfo result = {}", result);

        String userId = null;
        if (!StringUtils.isEmpty(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.getInteger("errcode") == 0) {
                userId = jsonObject.getString("UserId");
            }
        }
        return userId;
    }
}
