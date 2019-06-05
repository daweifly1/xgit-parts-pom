package cn.com.xgit.parts.common.util;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BeanUtil {

    public static <S, T> T do2bo(S source, Class<T> targetClass) {
        if (null == source) {
            return null;
        } else {
            try {
                T result = targetClass.newInstance();
                BeanUtils.copyProperties(source, result);
                return result;
            } catch (Exception var4) {
                throw new IllegalArgumentException("对象copy失败，请检查相关module", var4);
            }
        }
    }

    public static <S, T> List<T> do2bo4List(List<S> source, Class<T> targetClass) {
        List<T> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(source)) {
            return result;
        } else {
            Iterator<S> var3 = source.iterator();
            while (var3.hasNext()) {
                S obj = var3.next();
                result.add(do2bo(obj, targetClass));
            }

            return result;
        }
    }
}
