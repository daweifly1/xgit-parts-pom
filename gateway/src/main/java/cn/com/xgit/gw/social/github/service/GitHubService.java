package cn.com.xgit.gw.social.github.service;

import cn.com.xgit.gw.social.HttpClientUtils;
import cn.com.xgit.gw.social.github.GitHubProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class GitHubService {

    @Autowired
    private GitHubProperties gitHubProperties;

    public Map<String, String> queryGitHubLoginInfo(String code, String state) {
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(state)) {
            return null;
        }
        try {
            String token_url = "https://github.com/login/oauth/access_token";
            Map<String, Object> map = new HashMap<>();
            map.put("client_id", gitHubProperties.getAppId());
            map.put("client_secret", gitHubProperties.getAppSecret());
            map.put("code", code);
            String responseStr = HttpClientUtils.doPost(token_url, map);
            String token = HttpClientUtils.parseResponseEntity(responseStr).get("access_token");
            //根据token发送请求获取登录人的信息
            String userinfo_url = "https://api.github.com/user?access_token=" + token;
            responseStr = HttpClientUtils.doGet(userinfo_url);//json
            Map<String, String> responseMap = HttpClientUtils.parseResponseEntityJSON(responseStr);
            return responseMap;
        } catch (Exception e) {
            log.warn("", e);
        }
        return null;
    }
}
