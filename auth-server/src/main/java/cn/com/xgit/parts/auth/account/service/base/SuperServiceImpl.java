package cn.com.xgit.parts.auth.account.service.base;

import cn.com.xgit.parts.common.base.entity.BaseEntity;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author f00lish
 * @version 2018/4/27
 * Created By IntelliJ IDEA.
 * Qun:530350843
 */
@Slf4j
public class SuperServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    public boolean insertByVO(Object objectVO) {
        //BeanUtils 拷贝的时候会set ID为0
        Long id = ((BaseEntity) objectVO).getId();
        if (id != null && id == 0)
            ((BaseEntity) objectVO).setId(null);
        T t = this.getNewInstance(objectVO);
        boolean flag = super.save(t);
        ((BaseEntity) objectVO).setId(((BaseEntity) t).getId());
        return flag;
    }

    public boolean updateByVO(Object objectVO) {
        T t = this.getNewInstance(objectVO);
        return super.updateById(t);
    }

    public List<Object> selectListByVO(Wrapper<Object> wrapper) {
        QueryWrapper entityWrapper = (QueryWrapper) wrapper;
        Object objectVO = wrapper.getEntity();
        T t = this.getNewInstance(objectVO);
        entityWrapper.setEntity(t);
        return this.list(entityWrapper);
    }

    public IPage<Object> selectPageByVO(IPage<T> page, Wrapper<Object> wrapper) {
        QueryWrapper entityWrapper = (QueryWrapper) wrapper;
        Object objectVO = wrapper.getEntity();
        T t = this.getNewInstance(objectVO);
        entityWrapper.setEntity(t);
        return this.page(page, entityWrapper);
    }

    /**
     * 获取数据库实体
     *
     * @param objectVO
     * @return
     */
    private T getNewInstance(Object objectVO) throws IllegalAccessException, InstantiationException {
        T t = (T) this.getMyClass().newInstance();
        //当实体拷贝实体时需要忽略current字段  拷贝VO不需要
        BeanUtils.copyProperties(objectVO, t);
        return t;
    }

    //得到泛型类T
    private Class getMyClass() {
        log.debug("getMyClass:{}", this.getClass());
        //返回表示此 Class 所表示的实体类的 直接父类 的 Type。注意，是直接父类
        Type type = getClass().getGenericSuperclass();
        // 判断 是否泛型
        if (type instanceof ParameterizedType) {
            // 返回表示此类型实际类型参数的Type对象的数组.
            // 当有多个泛型类时，数组的长度就不是1了
            Type[] ptype = ((ParameterizedType) type).getActualTypeArguments();
            for (Type cls : ptype) {
                if (!(cls instanceof ParameterizedType))
                    return (Class) cls;
            }
        }
        return Object.class;//若没有给定泛型，则返回Object类
    }
}
