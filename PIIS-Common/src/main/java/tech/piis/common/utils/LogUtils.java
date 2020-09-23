package tech.piis.common.utils;

/**
 * 处理并记录日志文件
 * 
 * @author Kevin<EastascendWang@gmail.com>
 */
public class LogUtils
{
    public static String getBlock(Object msg)
    {
        if (msg == null)
        {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }
}
