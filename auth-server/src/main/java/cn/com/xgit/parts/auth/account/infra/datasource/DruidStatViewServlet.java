package cn.com.xgit.parts.auth.account.infra.datasource;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = {"/druid/*"}, initParams = {@javax.servlet.annotation.WebInitParam(name = "loginUsername", value = "admin"), @javax.servlet.annotation.WebInitParam(name = "loginPassword", value = "123456"), @javax.servlet.annotation.WebInitParam(name = "resetEnable", value = "false")})
public class DruidStatViewServlet
        extends StatViewServlet {
}
