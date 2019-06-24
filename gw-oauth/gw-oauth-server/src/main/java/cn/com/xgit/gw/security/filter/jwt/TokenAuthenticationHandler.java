package cn.com.xgit.gw.security.filter.jwt;

import cn.com.xgit.gw.api.beans.CommonUserDetails;
import cn.com.xgit.gw.module.CustomsSecurityProperties;
import cn.com.xgit.gw.http.CookieUtil;
import cn.com.xgit.parts.common.util.fastjson.FastJsonUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt token相关操作
 */
@Slf4j
@Component
public class TokenAuthenticationHandler implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private CustomsSecurityProperties securityProperties;

    private static final String CLAIM_KEY_SUBJECT = "subject";

    public TokenAuthenticationHandler() {
    }

    public CommonUserDetails getSubjectFromToken(String token) {
        CommonUserDetails subject;
        try {
            final Claims claims = getClaimsFromToken(token);
            String sub = claims.get(CLAIM_KEY_SUBJECT).toString();
            subject = FastJsonUtil.parse(sub, CommonUserDetails.class);
        } catch (Exception e) {
            subject = null;
        }
        return subject;
    }


    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(securityProperties.getClaimKeySecret()).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + securityProperties.getJwtExpiration() * 1000L);
    }

    public String generateToken(CommonUserDetails subject) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put(securityProperties.getClaimKeyCreated(), new Date());
        String sub = FastJsonUtil.toJSONString(subject);
        claims.put(CLAIM_KEY_SUBJECT, sub);
        return doGenerateToken(claims);
    }


    /**
     * 刷新令牌
     *
     * @param token 原令牌
     * @return 新令牌
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put("created", new Date());
            refreshedToken = doGenerateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    String doGenerateToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, securityProperties.getClaimKeySecret()).compact();
    }

    /**
     * 认证 返回令牌 cookie过了过期时间一半时候刷新令牌
     *
     * @param resp
     * @param token
     */
    public void doRefreshToken(HttpServletResponse resp, String token, boolean init) {
        Claims claims = getClaimsFromToken(token);
        if (null != claims && null != claims.get(CLAIM_KEY_SUBJECT)) {
            String sub = claims.get(CLAIM_KEY_SUBJECT).toString();
            CommonUserDetails subject = FastJsonUtil.parse(sub, CommonUserDetails.class);
            if (null != subject) {
                SecurityContextHolder.getContext().setAuthentication(new JWTAuthenticationToken(subject));
            }
            long expiration = claims.getExpiration().getTime();
            long date = System.currentTimeMillis() + (securityProperties.getJwtExpiration() * 1000 >> 1);

            //距离过期时间还有一半时候刷新token
            if (date > expiration) {
                token = refreshToken(token);
                CookieUtil.setCookie(resp, JWTConsts.HEADER_STRING, JWTConsts.TOKEN_PREFIX + token, securityProperties.getJwtExpiration());
//                ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//                HttpSession session = attr.getRequest().getSession(true);
//                session.setAttribute(JWTConsts.HEADER_STRING, token);
            }
            if (init) {
                CookieUtil.setCookie(resp, JWTConsts.HEADER_STRING, JWTConsts.TOKEN_PREFIX + token, securityProperties.getJwtExpiration());
            }
            resp.setHeader(JWTConsts.HEADER_STRING, JWTConsts.TOKEN_PREFIX + token);
        }
    }

    /**
     * 登陆成功后操作（若登录定制化较多可以使用此方法）
     *
     * @param commonUserDetails
     * @param resp
     */
    public void saveAfterLogin(CommonUserDetails commonUserDetails, HttpServletResponse resp) {
        log.debug("ww");
        SecurityContextHolder.getContext().setAuthentication(new JWTAuthenticationToken(commonUserDetails));
        String token = generateToken(commonUserDetails);
        resp.setHeader(JWTConsts.HEADER_STRING, JWTConsts.TOKEN_PREFIX + token);
        CookieUtil.setCookie(resp, JWTConsts.HEADER_STRING, JWTConsts.TOKEN_PREFIX + token, securityProperties.getJwtExpiration());

    }

    /**
     * 退出登录时候需要清理
     *
     * @param response
     * @param request
     */
    public void rmAfterLoginOut(HttpServletResponse response, HttpServletRequest request) {
        CookieUtil.delCookie(request, response, JWTConsts.HEADER_STRING);
        response.setHeader(JWTConsts.HEADER_STRING, null);
    }
}
