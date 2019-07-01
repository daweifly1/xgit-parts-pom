package cn.com.xgit.parts.feigin.fallback;

import cn.com.xgit.parts.feigin.GenIdClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GenIdClientFallBack implements GenIdClient {


    @Override
    public Long genId() {
        log.error("genId 服务异常");
        return null;
    }
}
