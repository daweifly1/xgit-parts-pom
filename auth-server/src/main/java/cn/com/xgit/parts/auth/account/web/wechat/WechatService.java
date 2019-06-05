package cn.com.xgit.parts.auth.account.web.wechat;

import cn.com.xgit.parts.auth.VO.ProfileVO;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

public abstract interface WechatService
{
  public abstract String queryUserIdByWechat(WxMpUser paramWxMpUser);
  
  public abstract ProfileVO generateProfile(String paramString, WxMpUser paramWxMpUser);
}
