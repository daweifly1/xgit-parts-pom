package cn.com.xgit.gw.module.controller;

import cn.com.xgit.gw.CustomsSecurityProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MyErrorController implements ErrorController {
    private static final String PATH = "/error";

    @Autowired
    private CustomsSecurityProperties customsSecurityProperties;

    @RequestMapping(value = PATH)
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
