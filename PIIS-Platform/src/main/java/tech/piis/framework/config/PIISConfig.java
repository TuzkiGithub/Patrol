package tech.piis.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 * 
 * @author Kevin<EastascendWang@gmail.com>
 */
@Component
@ConfigurationProperties(prefix = "piis")
public class PIISConfig
{
    /** 项目名称 */
    private String name;

    /** 版本 */
    private String version;

    /** 版权年份 */
    private String copyright;

    /** 上传路径 */
    private static String profile;

    /** 获取地址开关 */
    private static boolean ipAddress;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getCopyrightYear()
    {
        return copyright;
    }

    public void setCopyrightYear(String copyright)
    {
        this.copyright = copyright;
    }

    public static String getProfile()
    {
        return profile;
    }

    public void setProfile(String profile)
    {
        PIISConfig.profile = profile;
    }

    public static boolean isIpAddress()
    {
        return ipAddress;
    }

    public void setIpAddress(boolean ipAddress)
    {
        PIISConfig.ipAddress = ipAddress;
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath()
    {
        return getProfile() + "/avatar";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath()
    {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath()
    {
        return getProfile();
    }

    /**
     * 获取人员履历照片上传路径
     */
    public static String getPhotoPath()
    {
        return getProfile() + "/photo";
    }
}