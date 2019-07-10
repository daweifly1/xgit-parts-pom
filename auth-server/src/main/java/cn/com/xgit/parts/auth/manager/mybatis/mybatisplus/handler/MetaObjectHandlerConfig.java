package cn.com.xgit.parts.auth.manager.mybatis.mybatisplus.handler;

import cn.com.xgit.gw.http.CommHttpParam;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@Slf4j
@Component
public class MetaObjectHandlerConfig implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Object createdTime = getFieldValByName("createdTime", metaObject);
        Object updatedTime = getFieldValByName("updatedTime", metaObject);
        if (createdTime == null)
            setFieldValByName("createdTime", new Date(), metaObject);//mybatis-plus版本2.0.9+
        if (updatedTime == null)
            setFieldValByName("updatedTime", new Date(), metaObject);//mybatis-plus版本2.0.9+

        Long curUserId = tryGetCurUserId();
        if (curUserId != null) {
            setFieldValByName("createdBy", curUserId, metaObject);
            setFieldValByName("updatedBy", curUserId, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object updatedTime = getFieldValByName("updatedTime", metaObject);
        if (updatedTime == null) {
            setFieldValByName("updatedTime", new Date(), metaObject);//mybatis-plus版本2.0.9+
        }
        Long curUserId = tryGetCurUserId();
        if (curUserId != null) {
            setFieldValByName("updatedBy", curUserId, metaObject);
        }
    }

    private Long tryGetCurUserId() {
        try {
            return CommHttpParam.getUserId(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }
}
