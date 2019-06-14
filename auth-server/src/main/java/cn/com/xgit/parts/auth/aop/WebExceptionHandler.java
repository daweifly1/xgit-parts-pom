package cn.com.xgit.parts.auth.aop;


import cn.com.xgit.parts.auth.exception.AuthException;
import cn.com.xgit.parts.auth.exception.CommonException;
import cn.com.xgit.parts.auth.exception.code.ErrorCode;
import cn.com.xgit.parts.common.result.ResultMessage;
import cn.com.xgit.parts.common.util.fastjson.FastJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 异常处理
 */
@ControllerAdvice
@Slf4j
public class WebExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Object invoke(HttpServletRequest request, HttpServletResponse response, Exception e) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            if (e instanceof ClientAbortException) {
                log.warn("ClientAbortException url: {}, msg: {}", request.getRequestURI(), e.getMessage());
                // 浏览器已经关闭
                response.setContentType("text/html;charset=utf-8");
                out.write("发生错误，请联系开发解决");
                return null;
            }
            response.setContentType("application/json; charset=utf-8");
            this.exceptionLog(e, request);
            if (e instanceof AuthException) {
                ResultMessage view = ResultMessage.error(((AuthException) e).getCode(), e.getMessage());
                out.write(FastJsonUtil.toJSONString(view));
            } else if (e instanceof CommonException) {
                ResultMessage view = ResultMessage.error(((CommonException) e).getCode(), e.getMessage());
                out.write(FastJsonUtil.toJSONString(view));
            } else {
                ResultMessage view = ResultMessage.error(ErrorCode.Failure.getCode(), e.getMessage());
                out.write(FastJsonUtil.toJSONString(view));
            }
            out.flush();
            out.close();
        } catch (Exception ex) {
            this.exceptionLog(ex, request);
        } finally {
            IOUtils.closeQuietly(out);
            return null;
        }
    }

    /**
     * 异常日志
     */
    private void exceptionLog(Exception e, HttpServletRequest request) {
        try {
            String parameters = FastJsonUtil.toJSONString(request.getParameterMap());
            String uri = request.getRequestURI();
            // 异常堆栈信息
            // 异常堆栈信息
//        String fullStackTrace = org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace(e);
            log.error("系统异常,uri:{},parameters:{}", uri, parameters, e);
        } catch (Exception ex) {
            log.error("系统异常打印失败", ex);
        }

    }


//    @ExceptionHandler({AuthException.class})
//    @ResponseBody
//    public ResultMessage processExcption(NativeWebRequest request, AuthException e) {
//        log.error(e.getMessage(), e.getCode());
//        return  ResultMessage.error(e.getCode(), e.getMessage(), null);
//    }
//
//    @ExceptionHandler({Exception.class})
//    @ResponseBody
//    public ResultMessage processExcption(NativeWebRequest request, Exception e) {
//        this.log.error("Exception ", e);
//        ErrorCode code = ErrorCode.UnExceptedError;
//
//        return ResultMessage.error(code.getCode(), code.getDesc(), e.getMessage());
//    }
//
//    @ExceptionHandler({IllegalArgumentException.class})
//    @ResponseBody
//    public ResultMessage processIllegalArumentExcption(NativeWebRequest request, IllegalArgumentException e) {
//        this.log.error("IllegalArgumentException ", e);
//        ErrorCode code = ErrorCode.IllegalArument;
//
//        return  ResultMessage.error(code.getCode(), code.getDesc(), null);
//    }
//
//    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
//    @ResponseBody
//    public ResultMessage processSQLIntegrityConstraintViolationException(NativeWebRequest request, IllegalArgumentException e) {
//        this.log.error("SQLIntegrityConstraintViolationException 异常", e);
//        ErrorCode code = ErrorCode.SQLIntegrityConstraintViolation;
//        return  ResultMessage.error(code.getCode(), code.getDesc(), null);
//    }

}
