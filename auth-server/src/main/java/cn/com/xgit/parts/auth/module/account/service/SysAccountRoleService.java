package cn.com.xgit.parts.auth.module.account.service;

import cn.com.xgit.parts.auth.module.account.entity.SysAccountRole;
import cn.com.xgit.parts.auth.module.base.SuperMapper;
import cn.com.xgit.parts.auth.module.base.SuperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * SysAccountRole 系统密码
 */
@Slf4j
@Service
public class SysAccountRoleService extends SuperService<SuperMapper<SysAccountRole>, SysAccountRole> {

}
