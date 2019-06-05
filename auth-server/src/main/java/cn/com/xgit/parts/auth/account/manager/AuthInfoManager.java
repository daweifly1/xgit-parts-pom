package cn.com.xgit.parts.auth.account.manager;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class AuthInfoManager
{
  @Autowired
  private StringRedisTemplate redisTemplate;
  
  public void savAuthKaptcha(String authId, String text)
  {
    this.redisTemplate.opsForValue().set(authId, text, 60L, TimeUnit.SECONDS);
  }
  
  public String getVerifyCode(String authId)
  {
    String verifyCode = (String)this.redisTemplate.opsForValue().get(authId);
    this.redisTemplate.delete(authId);
    return verifyCode;
  }
  
  public String getRedisCode(String authId)
  {
    return (String)this.redisTemplate.opsForValue().get(authId);
  }
}
