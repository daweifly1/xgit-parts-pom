package cn.com.xgit.parts.auth.aop;


import cn.com.xgit.parts.auth.exception.AuthException;
import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import com.xgit.bj.common.util.fastjson.FastJsonUtil;
import com.xgit.bj.core.rsp.ResultMessage;
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
            out = response.getWriter();
            if (e instanceof ClientAbortException) {
                log.warn("ClientAbortException url: {}, msg: {}", request.getRequestURI(), e.getMessage());
                // 浏览器已经关闭
                response.setContentType("text/html;charset=utf-8");
                out.write("发生错误，请联系开发解决");
            }
            this.exceptionLog(e, request);
            if (e instanceof AuthException) {
                ResultMessage view = new ResultMessage(((AuthException) e).getCode(), e.getMessage());
                response.setContentType("text/html;charset=utf-8");
                out.write(FastJsonUtil.toJSONString(view));
            }

//        // ajax请求
//        View view = View.wrapError("发生错误，请联系开发解决");
//        view.put("errorMsg", fullStackTrace);
            ResultMessage view = new ResultMessage(ErrorCode.Failure, e.getMessage());
            response.setContentType("text/html;charset=utf-8");
            out.write(FastJsonUtil.toJSONString(view));

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
        String parameters = FastJsonUtil.toJSONString(request.getParameterMap());
        String uri = request.getRequestURI();
        // 异常堆栈信息
        // 异常堆栈信息
//        String fullStackTrace = org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace(e);
        log.error("系统异常,uri:{},parameters:{}", uri, parameters, e);
    }

}
