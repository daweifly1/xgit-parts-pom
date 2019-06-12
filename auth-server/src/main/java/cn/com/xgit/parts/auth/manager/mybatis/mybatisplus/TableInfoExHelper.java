package cn.com.xgit.parts.auth.manager.mybatis.mybatisplus;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.TableInfoHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author f00lish
 * @version 2019-05-30
 * Created By IntelliJ IDEA.
 * Qun:530350843
 */
@Slf4j
public class TableInfoExHelper {

    private static final Map<String, TableInfo> TABLE_NAME_INFO_CACHE = new ConcurrentHashMap<>();

    public static void initTable(){
        List<TableInfo> tableInfos = TableInfoHelper.getTableInfos();
        for (TableInfo tableInfo : tableInfos){
            TABLE_NAME_INFO_CACHE.put(tableInfo.getTableName(),tableInfo);
        }
    }

    public static TableInfo getTableInfo(String tableName) {
        if (tableName != null) {
            return TABLE_NAME_INFO_CACHE.get(tableName);
        }
        return null;
    }
}
