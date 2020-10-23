package tech.piis.framework.aspectj;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.piis.common.constant.ResultEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.web.domain.AjaxResult;

/**
 * Created with IntelliJ IDEA.
 * Package: cn.zz.web.exception
 * User: 25414
 * Date: 2019/9/3
 * Time: 10:59
 * Description:
 */
@Slf4j
//@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    /**
     * 全局异常捕捉处理
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public AjaxResult errorHandler(Exception ex) throws BaseException {
        log.error("###ControllerAdvice### error message:{}", ex.getMessage());
        for (StackTraceElement stackTraceElement : ex.getStackTrace()) {
            log.error("###ControllerAdvice### {}", stackTraceElement.toString());
        }
        return AjaxResult.error(ResultEnum.EXCEPTION.getCode(), ex.getMessage());
    }

}
