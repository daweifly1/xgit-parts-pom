package cn.com.xgit.parts.auth.module.account.service;

import cn.com.xgit.parts.auth.common.base.SuperMapper;
import cn.com.xgit.parts.auth.common.base.SuperService;
import cn.com.xgit.parts.auth.module.account.entity.SysAccount;
import cn.com.xgit.parts.auth.module.account.entity.SysAccountSocial;
import cn.com.xgit.parts.auth.module.account.mapper.SysAccountSocialMapper;
import cn.com.xgit.parts.auth.module.account.param.SysUserLoginInfoVO;
import cn.com.xgit.parts.common.result.ResultMessage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

/**
 * SysAccountSocial 后台接口实现类
 */
@Slf4j
@Service
public class SysAccountSocialService extends SuperService<SuperMapper<SysAccountSocial>, SysAccountSocial> {

    @Autowired
    private SysAccountService sysAccountService;

    @Autowired
    private SysAccountRoleService sysAccountRoleService;

    @Autowired
    private SysAccountSocialMapper sysAccountSocialMapper;

    public ResultMessage<String> bindSocialAccount(SysAccountSocial sysAccountSocial) {

        SysAccountSocial t = new SysAccountSocial();
        t.setType(sysAccountSocial.getType());
        t.setSocialAccount(sysAccountSocial.getSocialAccount());
        List<SysAccountSocial> ll = super.list(new QueryWrapper<SysAccountSocial>(t));
        if (CollectionUtils.isNotEmpty(ll)) {
            if (null != ll.get(0) && null != ll.get(0).getAccountId() && ll.get(0).getAccountId().intValue() == sysAccountSocial.getAccountId()) {
                return ResultMessage.success("账号已经绑定成功");
            }
            return ResultMessage.error("账号已经被其他账号绑定");
        }
        if (super.insertByVO(sysAccountSocial)) {
            return ResultMessage.success();
        }
        return ResultMessage.error("绑定失败");
    }

    public ResultMessage<SysUserLoginInfoVO> queryAccountBySocail(SysAccountSocial sysAccountSocial) {
        List<SysAccountSocial> ll = super.list(new QueryWrapper<SysAccountSocial>(sysAccountSocial));
        if (CollectionUtils.isEmpty(ll)) {
            return ResultMessage.error("账号未绑定");
        }
        if (null != ll.get(0) && null != ll.get(0).getAccountId()) {
            SysAccount accountDO = sysAccountService.getById(ll.get(0).getAccountId());
            if (null != accountDO) {
                SysUserLoginInfoVO r = new SysUserLoginInfoVO();
                r.setId(accountDO.getId());
                r.setUsername(accountDO.getUsername());
                r.setName(accountDO.getName());
                List<Long> roleIds = sysAccountRoleService.querRoleIdsByUserId(null, r.getId());
                r.setRoleIds(new HashSet<>(roleIds));
                return ResultMessage.success(r);
            }
        }
        return ResultMessage.error("查询账号信息失败");
    }
}
