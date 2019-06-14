package cn.com.xgit.parts.auth.feign.fallback;

import cn.com.xgit.parts.auth.feign.AuthClient;
import cn.com.xgit.parts.auth.module.account.param.UserRegistVO;
import cn.com.xgit.parts.auth.module.account.vo.SysAccountVO;
import cn.com.xgit.parts.auth.module.menu.param.SysAuthsParam;
import cn.com.xgit.parts.auth.module.role.param.AuthRolePlatformParam;
import cn.com.xgit.parts.common.result.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class AuthClientFallBack implements AuthClient {

    @Override
    public ResultMessage<List<Long>> getAuthIds(SysAuthsParam sysAuthsParam) {
        log.warn("getAuthIds 方法熔断，userId：{}，", sysAuthsParam);
        return ResultMessage.success(null);
    }

    @Override
    public ResultMessage<Set<String>> queryUrlsByRoleIds(AuthRolePlatformParam param) {
        log.warn("queryUrlsByRoleIds 方法熔断，param：{}，", param);
        return ResultMessage.success(null);
    }


    @Override
    public ResultMessage<SysAccountVO> queryAccountByLoginName(String loginName, Long platformId) {
        log.warn("queryAccountByLoginName 方法熔断，loginName：{}，platformId:{}", loginName, platformId);
        return ResultMessage.success(null);
    }

    @Override
    public ResultMessage<String> addUser(UserRegistVO userRegistVO) {
        log.warn("addUser 方法熔断，userRegistVO：{}", userRegistVO);
        return ResultMessage.success(null);
    }

}
