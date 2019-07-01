package cn.com.xgit.parts.feigin;

import cn.com.xgit.parts.feigin.fallback.GenIdClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "${snowflake-server:snowflake-server}", fallback = GenIdClientFallBack.class)
public interface GenIdClient {

    /**
     * 获取id
     *
     * @return
     */
    @RequestMapping(value = {"/genId"}, method = {RequestMethod.GET})
    Long genId();
}
