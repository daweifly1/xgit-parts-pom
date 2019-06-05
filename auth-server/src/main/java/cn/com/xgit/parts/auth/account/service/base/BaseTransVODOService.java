package cn.com.xgit.parts.auth.account.service.base;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;

public class BaseTransVODOService<V, D>
{
  private Class<D> dClass;
  private Class<V> vClass;
  
  protected BaseTransVODOService(Class<V> vClass, Class<D> dClass)
  {
    this.vClass = vClass;
    this.dClass = dClass;
  }
  
  protected D getDO(V dataVO)
  {
    if (dataVO == null) {
      return null;
    }
    D dataDO;
    try
    {
      dataDO = this.dClass.newInstance();
    }
    catch (Exception e)
    {
      return null;
    }
    BeanUtils.copyProperties(dataVO, dataDO);
    return dataDO;
  }
  
  protected V getVO(D dataDO)
  {
    if (dataDO == null) {
      return null;
    }
    V dataVO;
    try
    {
      dataVO = this.vClass.newInstance();
    }
    catch (Exception e)
    {

      return null;
    }

    BeanUtils.copyProperties(dataDO, dataVO);
    return dataVO;
  }
  
  protected List<V> getVOList(List<D> doList)
  {
    List<V> voList = new ArrayList();
    if (doList == null) {
      return voList;
    }
    for (D dataDO : doList) {
      if (dataDO != null)
      {
        V viewVo = getVO(dataDO);
        voList.add(viewVo);
      }
    }
    return voList;
  }
}
