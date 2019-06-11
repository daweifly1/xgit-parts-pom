package cn.com.xgit.parts.auth.feign.fallback;

import cn.com.xgit.parts.auth.feign.AuthClient;
import cn.com.xgit.parts.auth.module.account.param.UserRegistVO;
import cn.com.xgit.parts.auth.module.account.vo.SysAccountVO;
import cn.com.xgit.parts.auth.module.menu.param.SysAuthsParam;
import cn.com.xgit.parts.common.result.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AuthClientFallBack implements AuthClient {

    @Override
    public ResultMessage<List<Long>> getAuthIds(SysAuthsParam sysAuthsParam) {
        log.warn("getAuthIds 方法熔断，userId：{}，", sysAuthsParam);
        return ResultMessage.success(null);
    }

    @Override
    public ResultMessage<Boolean> checkAuthCodes(String userId, String url) {
        log.warn("checkAuthCodes 方法熔断，userId：{}，url：{}", userId, url);
        return ResultMessage.success(false);
    }

    @Override
    public ResultMessage<String> addUser(UserRegistVO userRegistVO) {
        log.warn("addUser 方法熔断，userRegistVO：{}", userRegistVO);
        return ResultMessage.success(null);
    }

    @Override
    public ResultMessage<SysAccountVO> queryAccountByLoginName(String loginName) {
        log.warn("com.xgit.bj.auth.feign.AuthClient.queryAccountByLoginName 方法熔断，loginName：{}", loginName);
        return ResultMessage.success(null);
    }
}
