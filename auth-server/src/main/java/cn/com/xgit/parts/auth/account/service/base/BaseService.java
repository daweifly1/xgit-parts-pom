package cn.com.xgit.parts.auth.account.service.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cn.com.xgit.parts.auth.account.dao.mapper.base.BaseMapper;
import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import com.xgit.bj.core.rsp.PageCommonVO;
import com.xgit.bj.core.rsp.SearchCommonVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class BaseService<V, D> extends BaseTransVODOService<V, D> {
    BaseMapper<V, D> baseMapper;

    protected BaseService(Class<V> vClass, Class<D> dClass) {
        super(vClass, dClass);
    }

    protected void addMapper(BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    protected ErrorCode checkParam(V v) {
        if (v == null) {
            return ErrorCode.IllegalArument;
        }
        return ErrorCode.Success;
    }

    public PageCommonVO list(SearchCommonVO<V> condition) {
        PageCommonVO pageCommonVO = new PageCommonVO();
        if (StringUtils.isNotBlank(condition.getOrderBy())) {
            PageHelper.orderBy(genColum(condition.getOrderBy()));
        } else {
            PageHelper.orderBy("id ");
        }
        PageHelper.startPage(condition.getPageNum().intValue(), condition.getPageSize().intValue());
        List<D> doList = this.baseMapper.list(condition.getFilters());
        List<V> voList = getVOList(doList);
        pageCommonVO.setPageInfo(new PageInfo(doList));
        pageCommonVO.setPageInfoList(voList);
        return pageCommonVO;
    }

    private static String genColum(String str) {
        if (str.trim().equalsIgnoreCase("ID")) {
            return str;
        }
        return str.replaceAll("[A-Z]", "_$0").toUpperCase();
    }


    @Transactional
    public ErrorCode insert(V v) {
        ErrorCode ret = checkParam(v);
        if (ret != ErrorCode.Success) {
            return ret;
        }
        D d = getDO(v);

        int effectRow = this.baseMapper.insert(d);
        return effectRow > 0 ? ErrorCode.Success : ErrorCode.FailedToInsertRecord;
    }

    @Transactional
    public ErrorCode update(V v) {
        ErrorCode ret = checkParam(v);
        if (ret != ErrorCode.Success) {
            return ret;
        }
        D d = getDO(v);

        int effectRow = this.baseMapper.update(d);
        return effectRow > 0 ? ErrorCode.Success : ErrorCode.FailedToUpdateRecord;
    }

    public V item(String no) {
        try {
            D d = this.baseMapper.item(no);
            return (V) getVO(d);
        } catch (Exception e) {
        }
        return null;
    }

    public V item(Integer id) {
        try {
            D d = this.baseMapper.item(id);
            return (V) getVO(d);
        } catch (Exception e) {
        }
        return null;
    }
}
