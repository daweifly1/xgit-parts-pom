package cn.com.xgit.parts.auth.feign.fallback;

import cn.com.xgit.parts.auth.feign.AuthClient;
import cn.com.xgit.parts.auth.module.account.param.UserRegistVO;
import cn.com.xgit.parts.auth.module.account.vo.SysAccountVO;
import cn.com.xgit.parts.common.result.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AuthClientFallBack implements AuthClient {
    @Override
    public ResultMessage<List<String>> getAuthCodes(String userId, String selfUserId) {
        log.warn("com.xgit.bj.auth.feign.AuthClient.getAuthCodes 方法熔断，userId：{}，", userId);
        return ResultMessage.success(null);
    }

    @Override
    public ResultMessage<Boolean> checkAuthCodes(String userId, String url) {
        log.warn("com.xgit.bj.auth.feign.AuthClient.checkAuthCodes 方法熔断，userId：{}，url：{}", userId, url);
        return ResultMessage.success(false);
    }

    @Override
    public ResultMessage<SysAccountVO> querySysAccountVO(String userId) {
        log.warn("com.xgit.bj.auth.feign.AuthClient.querySysAccountVO 方法熔断，userId：{}", userId);
        return ResultMessage.success(null);
    }

    @Override
    public ResultMessage<String> addUser(String userId, UserRegistVO userRegistVO) {
        log.warn("com.xgit.bj.auth.feign.AuthClient.addUser 方法熔断，userId：{}，userRegistVO：{}", userId, userRegistVO);
        return ResultMessage.success(null);
    }

    @Override
    public ResultMessage<SysAccountVO> queryAccountByLoginName(String loginName) {
        log.warn("com.xgit.bj.auth.feign.AuthClient.queryAccountByLoginName 方法熔断，loginName：{}", loginName);
        return ResultMessage.success(null);
    }
}
