package cn.com.xgit.parts.auth.feign.fallback;

import cn.com.xgit.parts.auth.feign.AuthClient;
import cn.com.xgit.parts.auth.module.account.param.SysUserLoginInfoVO;
import cn.com.xgit.parts.auth.module.account.param.UserRegistVO;
import cn.com.xgit.parts.auth.module.account.vo.SysAccountVO;
import cn.com.xgit.parts.auth.module.menu.param.SysAuthsParam;
import cn.com.xgit.parts.auth.module.role.param.AuthRolePlatformParam;
import cn.com.xgit.parts.common.result.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class AuthClientFallBack implements AuthClient {

    @Override
    public ResultMessage<List<Long>> getAuthIds(SysAuthsParam sysAuthsParam) {
        log.warn("getAuthIds 方法熔断，userId：{}，", sysAuthsParam);
        return ResultMessage.error("服务异常，请稍后重试");
    }

    @Override
    public ResultMessage<Set<String>> queryUrlsByRoleIds(AuthRolePlatformParam param) {
        log.warn("queryUrlsByRoleIds 方法熔断，param：{}，", param);
        return ResultMessage.error("服务异常，请稍后重试");
    }

    @Override
    public ResultMessage<String> bindSocialAccount(Long accountId, String socialAccount, String type) {
        log.warn("bindSocialAccount 方法熔断，accountId：{}，socialAccount：{}", accountId, socialAccount);
        return ResultMessage.error("服务异常，请稍后重试");
    }

    @Override
    public ResultMessage<SysUserLoginInfoVO> queryAccountBySocail(String socialAccount, String type) {
        log.warn("queryAccountBySocail 方法熔断，socialAccount：{}", socialAccount);
        return ResultMessage.error("服务异常，请稍后重试");
    }


    @Override
    public ResultMessage<Map<Long, SysAccountVO>> map(@NotEmpty ArrayList<Long> ids) {
        log.warn("map 方法熔断，ids：{}", ids);
        return ResultMessage.error("服务异常，请稍后重试");
    }

    @Override
    public ResultMessage<SysAccountVO> queryAccountByLoginName(String loginName, Long platformId) {
        log.warn("queryAccountByLoginName 方法熔断，loginName：{}，platformId:{}", loginName, platformId);
        return ResultMessage.error("服务异常，请稍后重试");
    }

    @Override
    public ResultMessage<SysUserLoginInfoVO> queryAccountByUserNameOrMobi(String account, Long platformId, Boolean dynamicPsw) {
        log.warn("queryAccountByUserName 方法熔断，username：{}，platformId:{}", account, platformId);
        return ResultMessage.error("服务异常，请稍后重试");
    }

    @Override
    public ResultMessage<String> addUser(UserRegistVO userRegistVO) {
        log.warn("addUser 方法熔断，userRegistVO：{}", userRegistVO);
        return ResultMessage.error("服务异常，请稍后重试");
    }

}
