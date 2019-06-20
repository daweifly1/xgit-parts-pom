package cn.com.xgit.gw.authorization.module.controller;

import cn.com.xgit.gw.authorization.module.CustomsSecurityProperties;
import cn.com.xgit.parts.common.result.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class MyErrorController implements ErrorController {
    private static final String PATH = "/error";

    @Autowired
    private CustomsSecurityProperties customsSecurityProperties;


    /**
     * 除Web页面外的错误处理，比如Json/XML等
     */
    @RequestMapping(value = PATH)
    @ResponseBody
    @ExceptionHandler(value = {Exception.class})
    public ResultMessage errorApiHandler(HttpServletRequest request, final Exception ex, final WebRequest req) {

        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        log.info(ex.getMessage() + "------------------" + ex.getStackTrace());
        int httpStatus = getStatus(request);
        switch (httpStatus) {
            case HttpServletResponse.SC_NOT_FOUND:
                return ResultMessage.error(httpStatus, "未知请求");
            case HttpServletResponse.SC_FORBIDDEN:
                return ResultMessage.error(httpStatus, "没有权限");
            default:
                break;
        }
        return ResultMessage.error(httpStatus, "内部错误");
    }

    private int getStatus(HttpServletRequest request) {
        Integer status = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (status != null) {
            return status;
        }

        return 500;
    }

    @RequestMapping(value = PATH, produces = "text/html")
    private String error(HttpServletRequest request, HttpServletResponse response) {
        int httpStatus = response.getStatus();
        switch (httpStatus) {
            case HttpServletResponse.SC_NOT_FOUND:
                if (StringUtils.isNotBlank(customsSecurityProperties.getRedirectErrorPage404())) {
                    return "redirect:" + customsSecurityProperties.getRedirectErrorPage404();
                } else {
                    return "404";
                }
            case HttpServletResponse.SC_FORBIDDEN:
                if (StringUtils.isNotBlank(customsSecurityProperties.getRedirectErrorPage403())) {
                    return "redirect:" + customsSecurityProperties.getRedirectErrorPage403();
                } else {
                    return "403";
                }
            default:
                break;
        }
        if (StringUtils.isNotBlank(customsSecurityProperties.getRedirectErrorPage500())) {
            return "redirect:" + customsSecurityProperties.getRedirectErrorPage500();
        } else {
            return "500";
        }
    }


    @Override
    public String getErrorPath() {
        return PATH;
    }
}
