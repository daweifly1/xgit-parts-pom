package com.louis.spring.oauth.client;

import com.louis.spring.oauth.client.fastjson.FastJsonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class DemoController {


    @RequestMapping("/connect/github")
    @ResponseBody
    public Map callback(String code, String state, Model model, HttpServletResponse response) {
        Map<String, String> m = queryGitHubLoginInfo(code, state);
        return m;
    }


    @RequestMapping("/connect/refreshToken")
    @ResponseBody
    public Map refreshToken(String refreshToken, HttpServletResponse response) throws Exception { ;
        String token_url = "http://10.3.1.33:9000/oauth/token";
        Map<String, Object> map = new HashMap<>();
        map.put("client_id", "client");
        map.put("client_secret", "secret");
        map.put("grant_type", "refresh_token");
        map.put("refresh_token", refreshToken);
        map.put("redirect_uri", "http://10.3.1.33:9001/connect/github");
        String responseStr = HttpClientUtils.doPost(token_url, map);
        Map<String, Object> m = FastJsonUtil.parseMap(responseStr);
        return m;
    }


    public Map<String, String> queryGitHubLoginInfo(String code, String state) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        try {
            String token_url = "http://10.3.1.33:9000/oauth/token";
            Map<String, Object> map = new HashMap<>();
            map.put("client_id", "client");
            map.put("client_secret", "secret");
            map.put("grant_type", "authorization_code");
            map.put("code", code);
            map.put("redirect_uri", "http://10.3.1.33:9001/connect/github");
            map.put("scope", "all");

            String responseStr = HttpClientUtils.doPost(token_url, map);
            System.out.println("  " + responseStr);
            Map<String, Object> m = FastJsonUtil.parseMap(responseStr);
            if (null != m.get("access_token")) {
                String userinfo_url = "http://10.3.1.33:9000/api/user?access_token=" + m.get("access_token");
                responseStr = HttpClientUtils.doGet(userinfo_url);//json
                Map<String, String> responseMap = HttpClientUtils.parseResponseEntityJSON(responseStr);
                return responseMap;
            }

        } catch (Exception e) {
        }
        return null;
    }
}
