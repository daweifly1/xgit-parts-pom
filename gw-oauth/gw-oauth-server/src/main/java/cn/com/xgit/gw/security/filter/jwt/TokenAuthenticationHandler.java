package cn.com.xgit.gw.security.filter.jwt;

import cn.com.xgit.gw.api.beans.CommonUserDetails;
import cn.com.xgit.gw.http.CookieUtil;
import cn.com.xgit.gw.http.exceptions.GwException;
import cn.com.xgit.gw.module.CustomsSecurityProperties;
import cn.com.xgit.parts.common.util.fastjson.FastJsonUtil;
import cn.com.xgit.parts.common.util.security.DESUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TokenAuthenticationHandler implements Serializable {
    private static final long serialVersionUID = 1L;
    @Autowired
    private CustomsSecurityProperties securityProperties;
    private static final String CLAIM_KEY_SUBJECT = "subject";

    public CommonUserDetails getSubjectFromToken(final String token) {
        CommonUserDetails subject;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            final String sub = claims.get("subject").toString();
            subject = (CommonUserDetails) FastJsonUtil.parse(sub, CommonUserDetails.class);
        } catch (Exception e) {
            subject = null;
        }
        return subject;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            //此处对称解密
            token = this.deCode(token);
            claims = Jwts.parser().setSigningKey(this.securityProperties.getClaimKeySecret()).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    String doGenerateToken(final Map<String, Object> claims) {
        String token = Jwts.builder().setClaims(claims).setExpiration(this.generateExpirationDate()).signWith(SignatureAlgorithm.HS512, this.securityProperties.getClaimKeySecret()).compact();
        //此处对称加密
        token = this.enCode(token);
        return token;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + this.securityProperties.getJwtExpiration() * 1000L);
    }

    public String generateToken(final CommonUserDetails subject) {
        final Map<String, Object> claims = new HashMap<String, Object>();
        claims.put(this.securityProperties.getClaimKeyCreated(), new Date());
        final String sub = FastJsonUtil.toJSONString(subject);
        claims.put("subject", sub);
        return this.doGenerateToken(claims);
    }

    public String refreshToken(final String token) {
        String refreshedToken;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            claims.put("created", new Date());
            refreshedToken = this.doGenerateToken((Map<String, Object>) claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public void doRefreshToken(final HttpServletResponse resp, String token, final boolean init) {
        final Claims claims = this.getClaimsFromToken(token);
        if (null != claims && null != claims.get("subject")) {
            final String sub = claims.get("subject").toString();
            final CommonUserDetails subject = FastJsonUtil.parse(sub, CommonUserDetails.class);
            final long expiration = claims.getExpiration().getTime();
            final long date = System.currentTimeMillis() + (this.securityProperties.getJwtExpiration() * 1000L >> 1);
            if (System.currentTimeMillis() > expiration) {
                return;
            }
            if (null != subject) {
                SecurityContextHolder.getContext().setAuthentication(new JWTAuthenticationToken(subject));
            }
            if (date > expiration) {
                token = this.refreshToken(token);
                CookieUtil.setCookie(resp, JWTConsts.HEADER_STRING, JWTConsts.TOKEN_PREFIX + token, (long) this.securityProperties.getJwtExpiration());
            }
            if (init) {
                CookieUtil.setCookie(resp, JWTConsts.HEADER_STRING, JWTConsts.TOKEN_PREFIX + token, (long) this.securityProperties.getJwtExpiration());
            }
            resp.setHeader(JWTConsts.HEADER_STRING, JWTConsts.TOKEN_PREFIX + token);
        }
    }

    public void saveAfterLogin(final CommonUserDetails commonUserDetails, final HttpServletResponse resp) {
        log.debug("ww");
        SecurityContextHolder.getContext().setAuthentication(new JWTAuthenticationToken(commonUserDetails));
        final String token = this.generateToken(commonUserDetails);
        resp.setHeader(JWTConsts.HEADER_STRING, JWTConsts.TOKEN_PREFIX + token);
        CookieUtil.setCookie(resp, JWTConsts.HEADER_STRING, JWTConsts.TOKEN_PREFIX + token, (long) this.securityProperties.getJwtExpiration());
    }

    public void rmAfterLoginOut(final HttpServletResponse response, final HttpServletRequest request) {
        CookieUtil.delCookie(request, response, JWTConsts.HEADER_STRING);
        response.setHeader(JWTConsts.HEADER_STRING, (String) null);
    }

    private String enCode(final String s) {
        final String seed = this.securityProperties.getAesSeed();
        if (StringUtils.isBlank(seed) || StringUtils.isBlank(s)) {
            return s;
        }
        try {
            final String r = DESUtils.encode(s, seed);
            return r;
        } catch (Exception e) {
            log.error("", e);
            throw new GwException("对称加密异常");
        }
    }

    private String deCode(final String s) {
        final String seed = this.securityProperties.getAesSeed();
        if (StringUtils.isBlank(seed) || StringUtils.isBlank(s)) {
            return s;
        }
        try {
            final String r = DESUtils.decode(s, seed);
            return r;
        } catch (Exception e) {
            log.error("", e);
            throw new GwException("对称解密异常");
        }
    }

}
