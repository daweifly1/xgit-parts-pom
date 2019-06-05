package cn.com.xgit.parts.auth.account.dao.mapper;

import cn.com.xgit.parts.auth.account.dao.entity.DepartmentDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public abstract interface DepartmentMapper
{
  public abstract List<DepartmentDO> queryList(@Param("deptId") String paramString1, @Param("spaceId") String paramString2);
  
  public abstract List<DepartmentDO> queryDeptList(DepartmentDO paramDepartmentDO);
  
  public abstract DepartmentDO selectById(String paramString);
  
  public abstract int insert(DepartmentDO paramDepartmentDO);
  
  public abstract int update(DepartmentDO paramDepartmentDO);
  
  public abstract List<DepartmentDO> selectDeptNotEmpty(DepartmentDO paramDepartmentDO);
  
  public abstract int removeByCode(DepartmentDO paramDepartmentDO);
  
  public abstract int checkName(DepartmentDO paramDepartmentDO);
  
  public abstract int deleteById(String paramString);
  
  public abstract String queryLastCode(@Param("parentId") String paramString1, @Param("spaceId") String paramString2);

    List<DepartmentDO> queryUserDeptMapByIds(@Param("ids") ArrayList<String> ids);

  List<DepartmentDO> queryListByParent(@Param("parentId") String parentId);
}
