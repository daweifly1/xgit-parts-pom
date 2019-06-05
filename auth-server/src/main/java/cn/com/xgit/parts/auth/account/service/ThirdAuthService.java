package cn.com.xgit.parts.auth.account.service;

import cn.com.xgit.parts.auth.exception.AuthException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.com.xgit.parts.auth.account.dao.entity.ThirdpartyOauthDO;
import cn.com.xgit.parts.auth.account.dao.mapper.ThirdpartyOauthMapper;
import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ThirdAuthService {
    @Autowired
    private ThirdpartyOauthMapper thirdpartyOauthMapper;

    public boolean isUserIdAlreadyBind(String userId, String type) {
        List<ThirdpartyOauthDO> list = this.thirdpartyOauthMapper.listBindedByUserId(userId);
        int typeInt = transBindType2Int(type);
        if ((list != null) && (!list.isEmpty())) {
            for (ThirdpartyOauthDO oauthDO : list) {
                if (typeInt == oauthDO.getBindType().intValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isThirdIdAlreadyBind(String thirdId) {
        List<ThirdpartyOauthDO> list = this.thirdpartyOauthMapper.listBindedByThirdpartyId(thirdId);
        if ((list != null) && (!list.isEmpty())) {
            return true;
        }
        return false;
    }

    public void bindAccount(String appId, String userId, String thirdId, String type, Object metaInfo) {
        ThirdpartyOauthDO oauthDO = new ThirdpartyOauthDO();
        oauthDO.setAppId(appId);
        oauthDO.setUserId(userId);
        oauthDO.setThirdpartyId(thirdId);
        oauthDO.setBindType(Integer.valueOf(transBindType2Int(type)));
        oauthDO.setBindTime(new Date());
        oauthDO.setBindStatus(Integer.valueOf(1));
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            oauthDO.setMetaData(objectMapper.writeValueAsString(metaInfo));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        this.thirdpartyOauthMapper.insert(oauthDO);
    }

    private int transBindType2Int(String type) {
        if ("wechat".equals(type)) {
            return 1;
        }
        throw new AuthException(ErrorCode.ThirdAuthTypeError);
    }
}
