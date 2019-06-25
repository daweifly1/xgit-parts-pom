package cn.com.xgit.gw.module.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fp295 on 2018/5/15.
 */
@Controller
@SessionAttributes({"authorizationRequest"})
public class MvcController {

    @Autowired
    private JdbcClientDetailsService jdbcClientDetailsService;

    /**
     * 登出回调
     *
     * @param request
     * @param response
     */
    @RequestMapping("/backReferer")
    public void sendBack(HttpServletRequest request, HttpServletResponse response) {

        try {
            //sending back to client app
            String referer = request.getHeader("referer");
            if (referer != null) {
                int index = referer.indexOf("?");
                if (index != -1)
                    referer = referer.substring(0, index);
                response.sendRedirect(referer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 授权页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/oauth/confirm_access")
    public ModelAndView authorizePage(Map<String, Object> model) {
        // 获取用户名
        String userName = ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();
        model.put("userName", userName);
        return new ModelAndView("authorize", model);
    }

    /**
     * 主页，未从客户端跳转直接登陆会显示
     *
     * @param model
     * @return
     */
    @RequestMapping("/")
    public ModelAndView indexPage(Map<String, Object> model) {
        // 获取用户名
        String userName = ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();
        model.put("userName", userName);

        List<ClientDetails> ll = jdbcClientDetailsService.listClientDetails();
        List<Map<String, Object>> client = new ArrayList<>();

        for (int i = 0; i < ll.size(); i++) {
            ClientDetails cc = ll.get(i);
            Map<String, Object> dd = new HashMap<>();
            dd.put("name", "CODE模式授权给" + cc.getClientId());
            dd.put("webServerRedirectUri", " http://10.3.1.33:9000/oauth/authorize?response_type=code&client_id=" + cc.getClientId() + "&redirect_uri=" + cc.getRegisteredRedirectUri().toArray()[0]);
            client.add(dd);
            Map<String, Object> dd2 = new HashMap<>();
            dd2.put("name", "简化模式授权给" + cc.getClientId());
            dd2.put("webServerRedirectUri", " http://10.3.1.33:9000/oauth/authorize?response_type=token&client_id=" + cc.getClientId() + "&scope=all&redirect_uri=" + cc.getRegisteredRedirectUri().toArray()[0]);
            client.add(dd2);
        }
        model.put("client", client);
        return new ModelAndView("index", model);
    }
}
