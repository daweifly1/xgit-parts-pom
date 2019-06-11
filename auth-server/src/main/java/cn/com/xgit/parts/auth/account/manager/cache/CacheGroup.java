package cn.com.xgit.parts.auth.account.manager.cache;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * cache 分组
 */
@Slf4j
public class CacheGroup {

    public static final String GOODS = "goods";     // goods 分组

    public static final String USER = "user";     // user 分组

    public static final String ROLE = "role";     // user 分组


    public static Set<String> getCacheGroups() {
        Set<String> r = new HashSet<>();
        Field[] fields = CacheGroup.class.getFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getType().toString().endsWith("java.lang.String") && Modifier.isStatic(field.getModifiers())) {
                    r.add((String) field.get(CacheGroup.class));
                }
            }
        } catch (Exception e) {
            log.error("CacheGroup fatal error");
            System.exit(0);
        }
        return r;
    }

    public static void main(String[] args) {
        System.out.println(getCacheGroups());
    }
}
