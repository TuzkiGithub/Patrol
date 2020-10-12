package tech.piis.auth;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.piis.common.utils.http.HttpUtils;

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
    private String code_url;

    /**
     * get access_token url
     */
    private String access_token_url;

    /**
     * get userBasicInfo url
     **/
    private String userInfo_url;

    /**
     * get userDetail url
     **/
    private String userDetail_url;

    /**
     * 企业CorpId
     **/
    private String appId;

    /**
     * 应用的凭证密钥
     */
    private String corpsecret;

    /**
     * 获取code的回调地址
     **/
    private String redirect_url;

    /**
     * 返回类型，此时固定为code
     **/
    private String response_type;

    /**
     * 应用授权作用域
     **/
    private String scope;

    /**
     * 应用Id
     **/
    private String agentId;

    /**
     * 终端使用此参数判断是否需要带上身份信息
     **/
    private String wechat_redirect;

    /**
     * 连接符
     **/
    private static final String and = "&";
    private static final String con = "?";
    private static final String xp = "#";


    /**
     * 拼接url获取code
     *
     * @throws Exception
     */
    @RequestMapping("code")
    public void getCode() throws Exception {
        String url = code_url +
                con + appId +
                and + redirect_url +
                and + scope +
                and + agentId +
                and + response_type +
                xp + wechat_redirect;
        log.info("######Get code url = {}", url);
//        HttpUtils.sendGet(url,null);
    }


    /**
     * 根据code和access_token获取用户基本信息
     *
     * @param code
     * @param state
     * @return
     * @throws Exception
     */
    @RequestMapping("user")
    public void getUserInfo(@RequestParam("code") String code, @RequestParam("state") String state) throws Exception {
        log.info("######Auth getUserInfo code = {}, state = {}", code, state);

        String access_token = getAccess_token(appId, corpsecret);
        if (!StringUtils.isEmpty(access_token)) {
            String url = userInfo_url +
                    con + code +
                    and + access_token;

            //获取用户基本信息
            log.info("######Get userInfo url = {}", url);
//            String userInfo = HttpUtils.doGet(url);
            String userInfo = "as";
            log.info("######Get userInfo = {}", userInfo);

            String userId = null;
            if (null != userInfo) {
                JSONObject resultJson = JSONObject.parseObject(userInfo);
                userId = null == resultJson.get("userid") ? null : resultJson.getString("userid");
                /**
                 * TODO
                 * 根据userId 查询账户表，若存在跳转首页，否则跳转错误页面？查询用户详细信息并注册新用户
                 */

            } else {
                log.error("######Get userInfo false!!!");
            }
        }


    }


    /**
     * 根据应用信息获取AccessToken
     *
     * @param appId
     * @param corpsecret
     * @return
     */
    private String getAccess_token(String appId, String corpsecret) throws Exception {
        log.info("######Get accessToken appId = {}, corpsecret = {}", appId, corpsecret);
        String url = new StringBuilder(access_token_url)
                .append(con).append(appId)
                .append(and).append(corpsecret).toString();
//        String result = HttpUtils.doGet(url);
        String result = "111";
        String access_token = null;
        if (!StringUtils.isEmpty(result)) {
            JSONObject resultJson = JSONObject.parseObject(result);
            access_token = resultJson.getString("access_token");
        }
        return access_token;
    }

}
