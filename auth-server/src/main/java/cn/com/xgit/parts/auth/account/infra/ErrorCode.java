package cn.com.xgit.parts.auth.account.infra;

public enum ErrorCode implements com.xgit.bj.core.ErrorCode {
    Success(0, "操作成功"),
    Failure(1, "操作失败"),
    NeedLogin(2, "用户没有登录"),
    UnExceptedError(3, "未知的错误发生"),
    IllegalArument(4, "参数错误"),
    SQLIntegrityConstraintViolation(5, "违反完整性约束"),
    NoAuthorization(6, "没有权限执行此操作"),
    CheckLoginFailure(7, "用户不存在或者密码错误"),
    UserNameExists(8, "用户帐号已存在"),
    RoleNameExists(9, "角色名已存在"),
    RoleIsUsed(10, "角色已经被使用"),
    UserLocked(11000, "用户被锁定"),
    UserNeedValidateCode(11, "由于密码录入错误过多，请输入验证码"),
    OldPwdNotRight(12, "原密码不正确"),
    PwdAlreadSet(13, "该用户已经设置密码"),
    SqlSyntaxError(14, "数据库执行异常"),
    FailedToInsertRecord(15, "插入数据记录失败"),
    FailedToRemoveRecord(16, "删除数据记录失败"),
    FailedToUpdateRecord(17, "更新数据记录失败"),
    FailedToRetreiveRecord(18, "获取数据记录失败"),
    FailedToVerifyCode(19, "验证码错误"),
    FailedToCacheUserDate(20, "缓存用户信息失败"),
    FailedToCacheAuthCode(21, "缓存用户权限失败"),
    FailedToRenewLeaseSession(22, "更新SESSION失败"),
    DeptNameExist(23, "名称已被使用"),
    TemplateIsNull(24, "模板信息不完整"),
    NoPermissionForThisSite(25, "没有权限登录该终端"),
    FailedToGetSession(26, "获取缓存信息失败"),
    FailedToNewCode(27, "组织机构编号生成失败"),
    CannotEditSystemData(28, "系统初始化数据无法编辑"),
    CannotRemoveYouself(29, "不能删除用户自己"),
    TemplateInUse(32, "权限模板被使用中"),
    AdminCannotRemove(33, "系统管理员不可以删除"),
    MobileExistError(34, "手机号码已存在"),
    FailedDeleteMenu(35, "菜单已被应用，无法删除"),
    FailedDeleteFixedMenu(36, "系统菜单不允许删除"),
    YourErrorCodeGoesHere(1000, "你的失败码请在后面定义"),
    AppAddFail(1101, "应用创建失败"), AppNotExist(1102, "应用不存在"),
    OAuthAppIdNotNull(1101, "app_id参数不能为空"), OAuthRedirectUrlNotNull(1102, "redirect_url参数不能为空"),
    OAuthResponseTypeNotNull(1103, "response_type参数不能为空"),
    OAuthAppSecretNotNUll(1104, "app_secret参数不能为空"), OAuthGrantTypeNotNull(1105, "grant_type参数不能为空"),
    OAuthAppIdInvalid(1104, "无效的app_id"), OAuthRedirectUrlInvalid(1105, "无效的redirect_url"),
    OAuthResponseTypeInvalid(1106, "无效的response_type"),
    OAuthAppSecretInvalid(1104, "无效的app_secret"), OAuthGrantTypeInvalid(1104, "无效的grant_type"),
    OAuthCodeInvalid(1104, "无效的授权码"), OAuthRefreshTokenInvalid(1104, "无效的刷新令牌"),
    OAuthGenAuthCodeFail(1107, "生成授权码失败"), OAuthAddAuthRecordFail(1108, "生成授权记录失败"),
    OAuthGenToeknFail(1108, "生成授权码失败"), ThirdAuthTypeError(1201, "授权类型错误"),
    AppIdNotExist(1202, "该appId不存在"), userIdAlreadyBind(1203, "该用户账号已绑定"),
    thirdIdAlreadyBind(1204, "该第三方账户已绑定");

    private String desc;
    private int code;

    private ErrorCode(int code, String desc) {
        this.desc = desc;
        this.code = code;
    }

    public String getDesc() {
        return this.desc;
    }

    public int getCode() {
        return this.code;
    }
}
