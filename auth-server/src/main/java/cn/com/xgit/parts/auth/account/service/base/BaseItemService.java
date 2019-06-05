package cn.com.xgit.parts.auth.account.service.base;

import cn.com.xgit.parts.auth.account.dao.mapper.base.BaseItemMapper;
import cn.com.xgit.parts.auth.account.infra.ErrorCode;

import java.util.ArrayList;
import java.util.List;

public class BaseItemService<V, D>
  extends BaseTransVODOService<V, D>
{
  BaseItemMapper baseItemMapper;
  
  protected BaseItemService(Class<V> vClass, Class<D> dClass)
  {
    super(vClass, dClass);
  }
  
  protected void addMapper(BaseItemMapper baseItemMapper)
  {
    this.baseItemMapper = baseItemMapper;
  }
  
  public List<V> list(String no)
  {
    List<D> doList = this.baseItemMapper.list(no);
    List<V> voList = getVOList(doList);
    return voList;
  }
  
  public ErrorCode delete(String no)
  {
    int effectRow = this.baseItemMapper.delete(no);
    return effectRow > 0 ? ErrorCode.Success : ErrorCode.FailedToInsertRecord;
  }
  
  public ErrorCode insertBatch(List<V> vos)
  {
    if ((vos == null) || (vos.size() < 1)) {
      return ErrorCode.Success;
    }
    List<D> dos = new ArrayList();
    for (V v : vos)
    {
      D d = getDO(v);
      dos.add(d);
    }
    int effectRow = this.baseItemMapper.insertBatch(dos);
    return effectRow > 0 ? ErrorCode.Success : ErrorCode.FailedToInsertRecord;
  }
}
