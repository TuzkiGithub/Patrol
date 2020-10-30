package tech.piis.common.utils.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Package: cn.kj.eb.utils
 * UserVO: Tuzki
 * Date: 2020/4/7
 * Time: 10:20
 * Description:Http工具类
 */
@Slf4j
public class HttpClientUtils {

    /**
     * 发送POST请求，参数拼接在url上
     *
     * @param url  请求url
     * @param data 请求数据
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String doPostParamToUrl(String url, String data) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        String context = StringUtils.EMPTY;

        if (!StringUtils.isEmpty(data)) {
            StringEntity body = new StringEntity(data, "utf-8");
            httpPost.setEntity(body);
        }
        // 设置回调接口接收的消息头
        httpPost.addHeader("Content-Type", "application/json");
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            context = EntityUtils.toString(entity, HTTP.UTF_8);
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpPost.abort();
                httpClient.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        return context;
    }

    /**
     * 发送Post请求，参数存放body中
     *
     * @param url
     * @param mapData
     * @return
     */
    public static String doPostParamToBody(String url, Map<String, String> mapData) {
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httpPost = new HttpPost(url);
        try {
            // 设置提交方式
            httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
            //
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            if (mapData.size() != 0) {
                // 将mapdata中的key存在set集合中，通过迭代器取出所有的key，再获取每一个键对应的值
                Set keySet = mapData.keySet();
                Iterator it = keySet.iterator();
                while (it.hasNext()) {
                    String k = (String) it.next();// key
                    String v = mapData.get(k);// value
                    nameValuePairs.add(new BasicNameValuePair(k, v));
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // 执行http请求
            response = httpClient.execute(httpPost);
            // 获得http响应体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // 响应的结果
                return EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 发送Get请求
     *
     * @param url 请求地址加参数
     * @return
     */
    public static String doGet(String url) throws URISyntaxException {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();

        URI uri = new URI(url);
        HttpGet get = new HttpGet(uri);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(get);
            HttpEntity entity = null;
            if (null != response) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    entity = response.getEntity();
                    result = EntityUtils.toString(entity, "UTF-8").trim();
                }
                return null != result ? result : EntityUtils.toString(response.getEntity(), "UTF-8").trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 发送Get请求
     * 请求头添加token
     *
     * @param url 请求地址加参数
     * @return
     */
    public static String doGet(String url, String token) throws URISyntaxException {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();

        URI uri = new URI(url);
        HttpGet get = new HttpGet(uri);
        get.addHeader("Authorization", token);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(get);
            HttpEntity entity = null;
            if (null != response) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    entity = response.getEntity();
                    result = EntityUtils.toString(entity, "UTF-8").trim();
                }
                return null != result ? result : EntityUtils.toString(response.getEntity(), "UTF-8").trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}