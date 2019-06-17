package cn.com.xgit.gw.social.github;

import cn.com.xgit.gw.social.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * github社交登录后回调，根据回调判断是绑定还是登录
 */
@Controller
public class GitHubCallBackController {

    @Autowired
    private GitHubProperties gitHubProperties;

    private String CODE_URL = null;
    private String TOKEN_URL = null;
    private String USER_INFO_URL = null;

    @PostConstruct
    public void init() {
        CODE_URL = "https://github.com/login/oauth/authorize?client_id=" + gitHubProperties.getAppId() + "&state=STATE&redirect_uri=" + gitHubProperties.getCallBack();
        //获取token的url
        TOKEN_URL = "https://github.com/login/oauth/access_token?client_id=" + gitHubProperties.getAppId() + "&client_secret=" + gitHubProperties.getAppSecret() + "&code=CODE&redirect_uri=" + gitHubProperties.getCallBack();
        //获取用户信息的url
        USER_INFO_URL = "https://api.github.com/user?access_token=TOKEN";
    }

    @RequestMapping("/connect/githubPage")
    public String githubPage(Model model) {
        String url = "https://github.com/login/oauth/authorize?client_id=" + gitHubProperties.getAppId() + "&state=STATE";
        model.addAttribute("githubUrl", url);
        return "loginGithub";
    }


    @RequestMapping("/connect/github")
    public String callback(String code, String state) throws Exception {
        if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(state)) {
            //拿到我们的code,去请求token
            //发送一个请求到
//            String token_url = TOKEN_URL.replace("CALLBACK", gitHubProperties.getCallBack())
//                    .replace("CODE", code);

            String token_url = "https://github.com/login/oauth/access_token";
//           System.out.println("用户信息数据"+token_url);//这个里面有我们想要的用户信息数据
            Map<String, Object> map = new HashMap<>();
            map.put("client_id", gitHubProperties.getAppId());
            map.put("client_secret", gitHubProperties.getAppSecret());
            map.put("code", code);
            map.put("state", state);
            map.put("redirect_uri", gitHubProperties.getCallBack());
            String responseStr = HttpClientUtils.doPost(token_url, map);
            String token = HttpClientUtils.parseResponseEntity(responseStr).get("access_token");

            //根据token发送请求获取登录人的信息
            String userinfo_url = USER_INFO_URL.replace("TOKEN", token);
            responseStr = HttpClientUtils.doGet(userinfo_url);//json

            Map<String, String> responseMap = HttpClientUtils.parseResponseEntityJSON(responseStr);
            System.out.println("登录用户信息:" + responseMap);//responseMap里面保存着用户登录信息
            System.out.println("获取登录用户的用户名:" + responseMap.get("login"));
        }
        return "404";
    }
}
