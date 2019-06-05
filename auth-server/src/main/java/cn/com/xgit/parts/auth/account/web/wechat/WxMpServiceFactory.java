package cn.com.xgit.parts.auth.account.web.wechat;

import cn.com.xgit.parts.auth.exception.AuthException;
import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import cn.com.xgit.parts.auth.account.dao.entity.ThirdpartySecretDO;
import cn.com.xgit.parts.auth.account.dao.mapper.ThirdpartySecretMapper;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class WxMpServiceFactory {
    private ConcurrentHashMap<String, WxMpService> serviceHolder = new ConcurrentHashMap();
    @Autowired
    private ThirdpartySecretMapper thirdpartySecretMapper;

    public WxMpService getService(String appId) {
        if (this.serviceHolder.get(appId) == null) {
            return buildNewService(appId);
        }
        return (WxMpService) this.serviceHolder.get(appId);
    }

    public WxMpInMemoryConfigStorage queryConfig(String appId) {
        ThirdpartySecretDO thirdpartySecretDO = this.thirdpartySecretMapper.itemByAppId(appId);
        if (thirdpartySecretDO == null) {
            return null;
        }
        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();

        config.setAppId(thirdpartySecretDO.getAppId());

        config.setSecret(thirdpartySecretDO.getAppSecret());

        config.setToken(thirdpartySecretDO.getToken());

        config.setAesKey(thirdpartySecretDO.getAesKey());
        return config;
    }

    private synchronized WxMpService buildNewService(String appId) {
        if (this.serviceHolder.get(appId) != null) {
            return (WxMpService) this.serviceHolder.get(appId);
        }
        WxMpInMemoryConfigStorage config = queryConfig(appId);
        if (config != null) {
            WxMpService service = new WxMpServiceImpl();
            service.setWxMpConfigStorage(config);
            this.serviceHolder.put(appId, service);
            return service;
        }
        throw new AuthException(ErrorCode.AppIdNotExist);
    }
}
