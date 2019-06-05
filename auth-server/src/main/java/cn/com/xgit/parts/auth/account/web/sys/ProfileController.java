package cn.com.xgit.parts.auth.account.web.sys;

import cn.com.xgit.parts.auth.account.facade.sys.UserAuthFacade;
import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import cn.com.xgit.parts.auth.account.service.sys.SysAccountService;
import com.github.pagehelper.PageInfo;
import cn.com.xgit.parts.auth.VO.LockVO;
import cn.com.xgit.parts.auth.module.account.vo.SysAccountVO;
import cn.com.xgit.parts.auth.account.web.BasicController;
import com.xgit.bj.core.Ref;
import com.xgit.bj.core.rsp.ResultMessage;
import com.xgit.bj.core.rsp.PageCommonVO;
import com.xgit.bj.core.rsp.SearchCommonVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping({"/profile"})
public class ProfileController extends BasicController {

    @Autowired
    private SysAccountService sysAccountService;

    @Autowired
    UserAuthFacade userAuthFacade;


    @RequestMapping(value = {"/getLogin"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    @ApiOperation("查询登录用户信息")
    public ResultMessage<SysAccountVO> getLoginUser(@RequestHeader("x-user-id") String userId) {
        return ResultMessage(userAuthFacade.queryLoginUser(userId));
    }

    @RequestMapping(value = {"/updateUserInfo"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ApiOperation("修改用户信息（用户管理）")
    public ResultMessage<ErrorCode> updateUserInfo(@RequestBody SysAccountVO profileVO, @RequestHeader("x-user-id") String userId) {
        log.info("{} 修改用户 {}", userId, profileVO);
        if (StringUtils.isBlank(profileVO.getUserId())) {
            return ResultMessage(ErrorCode.IllegalArument);
        }
        Ref<String> userIdRef = new Ref("");
        userIdRef.set(profileVO.getUserId());
        return ResultMessage(userAuthFacade.save(profileVO, userIdRef));
//        return ResultMessage(this.profileService.update(profileVO));
    }


    @RequestMapping(method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ApiOperation("添加用户")
    public ResultMessage<String> add(@RequestBody SysAccountVO profileVO, @RequestHeader("x-user-id") String userId) {
        log.info("{} 添加用户 {}", userId, profileVO);
        Ref<String> userIdRef = new Ref("");
        ErrorCode err = this.userAuthFacade.save(profileVO, userIdRef);
        return ResultMessage(err, userIdRef.get());
    }

    @RequestMapping(value = {"/detail"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    @ApiOperation("查询用户列表详细信息")
    public ResultMessage<SysAccountVO> detail(@RequestParam("userId") String userId) {
        return ResultMessage(userAuthFacade.queryLoginUser(userId));
    }

    @RequestMapping(value = {"/userInfo"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    @ApiOperation("查询个人信息")
    public ResultMessage<SysAccountVO> queryUserInfo(@RequestHeader("x-user-id") String userId) {
        return ResultMessage(userAuthFacade.queryLoginUser(userId));
    }

    @RequestMapping(value = {"/list"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ApiOperation("查询用户列表")
    public ResultMessage<PageInfo<SysAccountVO>> list(@RequestBody SearchCommonVO<SysAccountVO> condition, @RequestHeader("x-user-id") String userId) {
        PageCommonVO<SysAccountVO> pageCommonVO = sysAccountService.list(condition);
        return ResultMessage(pageCommonVO.getPageInfo());
    }

    @RequestMapping(value = {"/remove"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ApiOperation("批量删除用户")
    public ResultMessage removeUsers(@RequestBody List<String> userIds, @RequestHeader("x-user-id") String userId) {
        if (userIds.contains(userId)) {
            return ResultMessage(ErrorCode.CannotRemoveYouself);
        }
        return ResultMessage(sysAccountService.batchDelete(userIds));
    }

    @RequestMapping(value = {"/update"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ApiOperation("修改个人信息（个人中心）")
    public ResultMessage<ErrorCode> update(@RequestBody SysAccountVO profileVO, @RequestHeader("x-user-id") String userId) {
        profileVO.setUserId(userId);
        Ref<String> userIdRef = new Ref("");
        return ResultMessage(userAuthFacade.save(profileVO, userIdRef));
    }


    @RequestMapping(value = {"/updateUserByAccount"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ApiOperation("根据登录名修改用户信息")
    public ResultMessage updateUserByAccount(@RequestBody SysAccountVO profileVO) {
        if (null == profileVO || StringUtils.isBlank(profileVO.getUserId())) {
            return actionErrorResult("参数错误");
        }
        Ref<String> userIdRef = new Ref("");
        return ResultMessage(userAuthFacade.save(profileVO, userIdRef));
    }

    @RequestMapping(value = {"/updateLock"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ApiOperation("用户禁用与启用")
    public ResultMessage<ErrorCode> lock(@RequestBody LockVO lockVO) {
        return ResultMessage(sysAccountService.updateLock(lockVO));
    }
}
