package cn.com.xgit.parts.auth.account.service;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import cn.com.xgit.parts.auth.VO.AuthInfoVO;
import cn.com.xgit.parts.auth.account.manager.AuthInfoManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class AuthService {
    @Autowired
    DefaultKaptcha defaultKaptcha;
    @Autowired
    GuidService guidService;
    @Autowired
    AuthInfoManager authInfoManager;
    @Value("${login.verifyCode.default.flag:close}")
    private String verifyCodeFlag;

    @Value("${test-env:false}")
    private boolean testEnv;

    public AuthInfoVO createAuthInfo() {
        AuthInfoVO authInfoVO = new AuthInfoVO();
        authInfoVO.setAuthId(guidService.genTextId());
        if (testEnv || "open".equals(this.verifyCodeFlag.trim())) {
            authInfoVO.setVerifyCode("1234");
        } else {
            authInfoVO.setVerifyCode(this.defaultKaptcha.createText());
        }
        this.authInfoManager.savAuthKaptcha(authInfoVO.getAuthId(), authInfoVO.getVerifyCode());

        return authInfoVO;
    }

    public BufferedImage createVerifyImg(String authId) {
        String text = this.authInfoManager.getRedisCode(authId);
        if (StringUtils.isBlank(text)) {
            if (testEnv) {
                text = "1234";
            } else {
                text = this.defaultKaptcha.createText();
            }
            this.authInfoManager.savAuthKaptcha(authId, text);
        }
        return this.defaultKaptcha.createImage(text);
    }
}
