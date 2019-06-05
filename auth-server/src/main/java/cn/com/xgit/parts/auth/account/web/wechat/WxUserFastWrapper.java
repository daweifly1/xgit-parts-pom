package cn.com.xgit.parts.auth.account.web.wechat;

import me.chanjar.weixin.mp.bean.result.WxMpUser;

public class WxUserFastWrapper
{
  private WxMpUser wxMpUser;
  private String userId;
  private String redirectUrl;
  
  public WxMpUser getWxMpUser()
  {
    return this.wxMpUser;
  }
  
  public void setWxMpUser(WxMpUser wxMpUser)
  {
    this.wxMpUser = wxMpUser;
  }
  
  public String getUserId()
  {
    return this.userId;
  }
  
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  public String getRedirectUrl()
  {
    return this.redirectUrl;
  }
  
  public void setRedirectUrl(String redirectUrl)
  {
    this.redirectUrl = redirectUrl;
  }
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer("WxUserFastWrapper{");
    sb.append("wxMpUser=").append(this.wxMpUser);
    sb.append(", userId='").append(this.userId).append('\'');
    sb.append(", redirectUrl='").append(this.redirectUrl).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
