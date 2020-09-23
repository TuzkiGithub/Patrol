package tech.piis.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.piis.common.constant.Constants;

/**
 * 通用配置
 * 
 * @author Kevin<EastascendWang@gmail.com>
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer
{
//    @Autowired
//    private RepeatSubmitInterceptor repeatSubmitInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        /** 本地文件上传路径 */
        registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/**").addResourceLocations("file:" + PIISConfig.getProfile() + "/");

        /** swagger配置 */
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 自定义拦截规则
     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry)
//    {
//        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");
//    }
}