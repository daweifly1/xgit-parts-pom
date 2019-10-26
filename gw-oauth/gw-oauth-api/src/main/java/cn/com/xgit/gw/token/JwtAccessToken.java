//package cn.com.xgit.gw.token;
//
//
//import cn.com.xgit.gw.api.CommonUserDetails;
//import cn.com.xgit.parts.common.util.fastjson.FastJsonUtil;
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//
//import java.util.Map;
//
///**
// * 自定义JwtAccessToken转换器
// */
//public class JwtAccessToken extends JwtAccessTokenConverter {
//    public static String USER_INFO = "user_info";
//
//    /**
//     * 生成token
//     *
//     * @param accessToken
//     * @param authentication
//     * @return
//     */
//    @Override
//    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//        DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken);
//
//        // 设置额外用户信息
//        CommonUserDetails user = (CommonUserDetails) authentication.getPrincipal();
//
//        // 将用户信息添加到token额外信息中
//        defaultOAuth2AccessToken.getAdditionalInformation().put(USER_INFO, user);
//
//        return super.enhance(defaultOAuth2AccessToken, authentication);
//    }
//
//    /**
//     * 解析token
//     *
//     * @param value
//     * @param map
//     * @return
//     */
//    @Override
//    public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map) {
//        OAuth2AccessToken oauth2AccessToken = super.extractAccessToken(value, map);
//        convertData(oauth2AccessToken, oauth2AccessToken.getAdditionalInformation());
//        return oauth2AccessToken;
//    }
//
//    private void convertData(OAuth2AccessToken accessToken, Map<String, ?> map) {
//        accessToken.getAdditionalInformation().put(USER_INFO, convertUserData(map.get(USER_INFO)));
//
//    }
//
//    private CommonUserDetails convertUserData(Object map) {
//        String json = FastJsonUtil.toJSONString(map);
//        CommonUserDetails user = FastJsonUtil.parse(json, CommonUserDetails.class);
//        return user;
//    }
//}
