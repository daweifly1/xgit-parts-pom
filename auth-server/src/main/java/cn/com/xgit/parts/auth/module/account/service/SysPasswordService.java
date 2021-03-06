package cn.com.xgit.parts.auth.module.account.service;

import cn.com.xgit.parts.auth.module.account.entity.SysPassword;
import cn.com.xgit.parts.auth.common.base.SuperMapper;
import cn.com.xgit.parts.auth.common.base.SuperService;
import cn.com.xgit.parts.auth.module.account.mapper.SysAccountMapper;
import cn.com.xgit.parts.auth.module.account.mapper.SysPasswordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SysPassword 系统密码
 */
@Slf4j
@Service
public class SysPasswordService extends SuperService<SuperMapper<SysPassword>, SysPassword> {

    @Autowired
    private SysPasswordMapper sysPasswordMapper;
}
