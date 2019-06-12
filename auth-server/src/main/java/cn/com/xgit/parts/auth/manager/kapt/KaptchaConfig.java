package cn.com.xgit.parts.auth.manager.kapt;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class KaptchaConfig
{
  @Bean
  public DefaultKaptcha getDefaultKaptcha()
  {
    DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
    Properties properties = new Properties();
    properties.setProperty("kaptcha.border", "no");
    properties.setProperty("kaptcha.border.color", "105,179,90");
    properties.setProperty("kaptcha.textproducer.font.color", "19,0,171");
    properties.setProperty("kaptcha.image.width", "82");
    properties.setProperty("kaptcha.image.height", "36");
    properties.setProperty("kaptcha.textproducer.font.size", "28");
    properties.setProperty("kaptcha.session.key", "code");
    properties.setProperty("kaptcha.textproducer.char.length", "4");
    properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
    
    properties.setProperty("kaptcha.textproducer.char.string", "abcde2345678gfynknpsx");
    properties.setProperty("kaptcha.textproducer.char.space", "3");
    properties.setProperty("kaptcha.background.clear.from", "white");
    properties.setProperty("kaptcha.background.clear.to", "white");
    properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
    properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.WaterRipple");
    
    Config config = new Config(properties);
    defaultKaptcha.setConfig(config);
    
    return defaultKaptcha;
  }
}
