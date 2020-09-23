package tech.piis.common.exception.user;

import tech.piis.common.exception.BaseException;

/**
 * 用户信息异常类
 * 
 * @author Kevin<EastascendWang@gmail.com>
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
